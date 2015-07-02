package namedwebsockets

import (
	"time"

	"github.com/gorilla/websocket"
	
	"log"
	"strings"
	"github.com/notificator"

	"net/http"
	"container/list"
	"fmt"

	"runtime"
)

var notify *notificator.Notificator

var peerNotifiedList list.List

type PeerConnection struct {
	// Unique identifier for this peer connection
	id int

	// WebSocket connection object
	ws *websocket.Conn
}

type Message struct {
	// The source peer connection of the message
	source int

	// The destination peer connection id target
	target int

	// The message payload
	payload string

	// Whether this message originated from a ProxyConnection object
	fromProxy bool
}

func NewPeerConnection(id int, socket *websocket.Conn) *PeerConnection {
	peerConn := &PeerConnection{
		id: id,
		ws: socket,
	}

	return peerConn
}

// Send a message to the target websocket connection
func (conn *PeerConnection) send(payload string) {
	conn.ws.SetWriteDeadline(time.Now().Add(writeWait))
	conn.ws.WriteMessage(websocket.TextMessage, []byte(payload))
}

// readConnectionPump pumps messages from an individual websocket connection to the dispatcher
func (peer *PeerConnection) readConnectionPump(sock *NamedWebSocket) {
	defer func() {
		peer.removeConnection(sock)
	}()
	peer.ws.SetReadLimit(maxMessageSize)
	peer.ws.SetReadDeadline(time.Now().Add(pongWait))
	peer.ws.SetPongHandler(func(string) error { peer.ws.SetReadDeadline(time.Now().Add(pongWait)); return nil })
	for {
		opCode, message, err := peer.ws.ReadMessage()
		if err != nil || opCode != websocket.TextMessage {
			break
		}

		//log.Printf("Received Peer Message: %s",message)

		wsBroadcast := &Message{
			source:    peer.id,
			target:    0, // target all connections
			payload:   string(message),
			fromProxy: false,
		}
		var notiRecived bool = false

		for e := peerNotifiedList.Front(); e != nil; e=e.Next() {
			if(string(message)==e.Value.(string)){
				notiRecived = true
			}
		}

		//create notifications with the url for linux and android
		if(strings.Contains(string(message),"http://")&&!notiRecived){

			peerNotifiedList.PushBack(string(message))

			notify = notificator.New(notificator.Options{
				DefaultIcon: "Mediascape.png",
				AppName:     "Mediascape",
			})

			if(string(runtime.GOOS)=="linux"&&string(runtime.GOARCH)!="arm"){
				notify.Push("Mediascape", string(message), "./Mediascape.png")
			}else if(string(runtime.GOOS)=="linux"&&string(runtime.GOARCH)=="arm"){
				completUrl := fmt.Sprintf("http://localhost:8182/discoveragent/notification?url=%s", string(message))
				response, err := http.Get(completUrl)
				if err != nil {
					// handle error
					log.Printf("Error: %s",err)
				}else{
					defer response.Body.Close()
				}
			}
		}
		sock.broadcastBuffer <- wsBroadcast
	}
}

// writeConnectionPump keeps an individual websocket connection alive
func (peer *PeerConnection) writeConnectionPump(sock *NamedWebSocket) {
	ticker := time.NewTicker(pingPeriod)
	defer func() {
		ticker.Stop()
		peer.removeConnection(sock)
	}()
	for {
		select {
		case <-ticker.C:
			peer.ws.SetWriteDeadline(time.Now().Add(writeWait))
			peer.ws.WriteMessage(websocket.PingMessage, []byte{})
		}
	}
}

// Set up a new NamedWebSocket connection instance
func (peer *PeerConnection) addConnection(sock *NamedWebSocket) {
	// Add this websocket instance to Named WebSocket broadcast list
	sock.peers = append(sock.peers, peer)

	// Inform all control connections that we now own this peer connection
	for _, control := range sock.controllers {
		// don't notify controller if its id matches the peer's id
		if control.id != peer.id {
			control.send("connect", control.id, peer.id, "")
		}
	}

	// Inform all proxy connections that we now own this peer connection
	for _, proxy := range sock.proxies {
		if proxy.writeable {
			proxy.send("connect", proxy.id, peer.id, "")
		}
	}

	// Start connection read/write pumps
	go peer.writeConnectionPump(sock)
	go peer.readConnectionPump(sock)
}

// Tear down an existing NamedWebSocket connection instance
func (peer *PeerConnection) removeConnection(sock *NamedWebSocket) {
	for i, conn := range sock.peers {
		if conn.id == peer.id {
			sock.peers = append(sock.peers[:i], sock.peers[i+1:]...)
			break
		}
	}

	// Find associated control connection and close also
	for _, control := range sock.controllers {
		if control.id == peer.id {
			control.removeConnection(sock)
			break
		}
	}

	// Inform all control connections that we no longer own this peer connection
	for _, control := range sock.controllers {
		// don't notify controller if its id matches the peer's id
		if control.id != peer.id {
			control.send("disconnect", control.id, peer.id, "")
		}
	}

	// Inform all proxy connections that we no longer own this peer connection
	for _, proxy := range sock.proxies {
		if proxy.writeable {
			proxy.send("disconnect", proxy.id, peer.id, "")
		}
	}

	peer.ws.Close()
}
