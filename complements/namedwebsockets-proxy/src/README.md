#Named Web Sockets for Device Association

The work that me we present in this package is based in [Rich Tibbett's](https://github.com/richtr) [Named Web Sockets](https://github.com/namedwebsockets). We have modified peers and proxy files so that the Named Web Sockets proxies could generate linux and android Notification as soon as a messeage containing html url is recived.

## Navigation
[Goals][] | [Requirements][] | [Instalation][] | [Run Named Web Sockets][]  | [Generate Named Web Sockets Binary][] 

## Goals
[Top][]

A web page or application can create a new Network Web Socket by choosing a channel name (any alphanumeric name) via any of the available Network Web Socket interfaces. When other peers join the same channel name then they will join all other peers in the same Network Web Socket broadcast network and send each other an association url. Once the url is received by a Network Web Socket Proxy, it sends linux and android notifications to the OS that house the proxy. 

## Requirements
[Top][]

This repository contains an implementation of a Network Web Socket Proxy, written in Go, required to use Network Web Sockets.

https://golang.org/doc/install

If you wanna create binnaries for multiple OSs of the Named Web Sockets you also need Go cross compilation support.

https://github.com/davecheney/golang-crosscompile

## Instalation
[Top][]

### Install hg (mercurial):


	sudo apt-get install mercurial

### Install go:


Download go1.2.2.linux-amd64.tar.gz from https://golang.org/dl/:

	sudo tar -C /usr/local -xzf go$VERSION.$OS-$ARCH.tar.gz

We have used go1.2.2 because in some foros people say that is were it work. In our case:

	sudo tar -C /usr/local -xzf go1.2.2.linux-amd64.tar.gz

Add /usr/local/go/bin to the PATH environment variable. You can do this by adding this line to your /etc/profile (for a system-wide installation) or $HOME/.profile:

	export PATH=$PATH:/usr/local/go/bin

if you installed Go into your home directory you should add the following commands:

Set GOPATH:

	export GOROOT=$HOME/go

Set PATH:

	export PATH=$PATH:$GOROOT/bin

Go to the user folder: cd ~

### Install Named Web Sockets:

Then you have to download the full discovery-self package and set discovery-self/complements/namedwebsockets-proxy as GOPATH:

export GOPATH=path to the download discovery-self/complements/namedwebsockets-proxy

For example, if you download into your home directory:

$ cd

$ git clone https://github.com/mediascape/discovery-self

export GOPATH=/home/user/discovery-self/complements/namedwebsockets-proxy

## Run Named Web Sockets:
[Top][]

Go to /home/user/discovery-self/complements/namedwebsockets-proxy/src/github.com/namedwebsockets/cmd/namedwebsockets and execute the following:

$ go run run.go

## Generate Named Web Sockets Binary:
[Top][]

Go to /home/user/discovery-self/complements/namedwebsockets-proxy/src/github.com/namedwebsockets/cmd/namedwebsockets and execute the following:

$ go build

[Top]: #navigation
[Goals]: #goals
[Requirements]: #requirements
[Instalation]: #instalation
[Run Named Web Sockets]: #run-named-web-sockets
[Generate Named Web Sockets Binary]: #generate-named-web-sockets-binary
