# Demos #

In this folder you will find the demostrations of WP3 for association and discovery and WP4 discovery for agent context.

* WP3Demo: 
  *   Shows the way that the association and discovery libraries have to be used.

* WP3&4Demo: 
  * Shows the way that the discovery for agent context have to be use over WP3 discovery API.
 
## Deployment and Run ##

### Deployment ###

 You will find a deployment file for each of the demos to could install in a linux platform. It is very easy to install both demos. You just have to download the deployment files and execute like: 

For WP3Demo:
```
    sh ./deployWP3Demo.sh
```
 For WP3&4Demo:
```
    sh ./deployWP3\&4Demo.sh
```
 The deployment will be done into the /var/www/ folder.

### run ###

 After been deployed the users can access to the content in the URLs:
 
 For WP3Demo:
```
    http://localhost/WP3Demo/
```
 For WP3&4Demo:
```
    http://localhost/WP3&4Demo/
```

# WP3Demo Example #

The aim of this web page is to show the way to consume the Discovery API developed in Mediascape project. You will you find in this example:

- The way to consume the calls to the agents.
- One example of each call implemented. Some of the examples are ad-hoc to the devices, services and actions we have in vicomtech.

This is the code that can be found in the webpage: http://150.241.250.4:7443/WP3Demo

## API ##

You will find the WP3 Discovery and Association API code in:

https://github.com/mediascape/WP3/tree/master/API/WP3mediascape
        
## Complements ##

For a better use of this example, it is very important to open the web page in a device. This device must have installed UPnPRESTFUL service and a namedwebsockets that you will find in:

UPnPRESTFUL service

https://github.com/mediascape/WP3/tree/master/Tests/complements/Android%20Service

namedwebsockets 

https://github.com/mediascape/WP3/tree/master/Tests/complements/NamedWebSockets%20Proxy%20Servers
