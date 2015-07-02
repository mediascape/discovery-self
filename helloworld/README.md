# Helloworld #

In this folder you will find the demostrations of discovery.

## Navigation
[Goals][] | [Deployment][] | [Run][] | [API][] | [complements][]

### Goals

The aim of this web page is to show the way to consume the Discovery API developed in Mediascape project. You will you find in this example:

- The way to consume the calls to the agents.
- One example of each call implemented. Some of the examples are ad-hoc to the devices, services and actions we have in vicomtech.

This is the code that can be found in the webpage: http://150.241.250.4:7443/WP3Demo

### Dependencies

A prior condition is the deployment of a HTTP server (Apache).

### Deployment

You will find a deployment file for each of the demos to could install in a linux platform. It is very easy to install both demos. You just have to download the deployment files and execute like: 

For discovery-self deployment:
```
    sh ./deploy.sh
```

The deployment will be done into the /var/www/html/ or /var/www/ folder.

### Run

After been deployed the users can access to the content in the URLs:
 
 For discovery-self run:
```
    http://localhost/discovery-self/
```


#### API

You will find the Discovery API code in:

https://github.com/mediascape/discovery-self/tree/master/API/
        
#### Complements

For a better use of this example, it is very important to open the web page in a device. This device must have installed UPnPRESTFUL service and a namedwebsockets that you will find in:

##### Native REST Agent

https://github.com/mediascape/discovery-self/tree/master/complements/discovery-agent-REST

##### Namedwebsockets Proxy

https://github.com/mediascape/discovery-self/tree/master/complements/namedwebsockets-proxy


[Top]: #navigation
[Goals]: #goals
[Deployment]: #deployment
[Run]: #run
[API]: #api
[complements]: #complements
