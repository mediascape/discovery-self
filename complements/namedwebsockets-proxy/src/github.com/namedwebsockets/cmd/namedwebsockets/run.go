package main

import (
	"log"
	"os"
	"github.com/namedwebsockets/namedwebsockets"

	//"github.com/notificator"
)

//var notify *notificator.Notificator
var sock *namedwebsockets.NamedWebSocket

func main() {
	hostname, err := os.Hostname()

	if err != nil {
		log.Printf("Could not determine device hostname: %v\n", err)
		return
	}

	service := namedwebsockets.NewNamedWebSocketService(hostname, 2000)

	// Start mDNS/DNS-SD discovery service
	//go service.StartNewDiscoveryServer()

	// Start HTTP/WebSocket endpoint server (blocking call) 
	go service.StartHTTPServer()   
	//service.StartHTTPServer() 

	namedwebsockets.GetNamedWebSockets(service, sock, "/network/Mediascape")
	if sock == nil {
		sock = namedwebsockets.NewNamedWebSocket(service, "Mediascape", true, 2000)
		namedwebsockets.SetNamedWebSockets(service, sock, "/network/Mediascape")
	}

	service.StartNewDiscoveryServer()
}
