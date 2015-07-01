# Discovery-self API

## Navigation
[Overview][] | [Architecture][] | [API][] | [JS Discovery API for Capabilities Discovery Agents][] | [JS Discovery API for Networking Discovery Agents][] | [Examples][]

## Overview
[Top][]

This library build awareness about the device features and the available devices ready to be connected for each application instance. To achieve it, this *Discovery* library introspects the features of the device and detect the reachable devices through the possible network interfaces and communication protocols.

This document summarizes the *Discovery* API to access the implemented JS library information. The target of this document is to reflect the status of design and development of the *Discovery* library and provide a list of available functions.

New features are being implemented.

The solutions include two major possibilities:
* **Web-Broser Agents**- W3C specification based Javascript functions gaining the abilities of the web browser to know and operate its underlying HW and SW stack
* **Native Agents** executed publishing the introspection results in a uniform format

---

## Architecture
[Top][]

A major concern is the heterogeneity of the devices where agents must perform their activity. However, from the point of view of the developer, there must be a common interface to these agents, independent of the underlying functionality, technology or pipeline.

This Discovery library fosters Web-browser based agents. First, because native agents developed on top of specific OS SDK and HW API must be implemented and built for each kind of target device and OS, requiring continuous upgrade as the OS updates. Second, most of Web-browser agents are based on W3C standards or drafts, widening the suitable devices and increasing the future applicability.

The MediaScape JavaScript Discovery API hides the complexity of the Discover Agents HTTP interfaces. This way, the developer does not need to take care of HTTP interactions, and just needs to execute Javascript calls.

**_The Discovery API interfaces underlying Agents, native or web-browser based. In order to create a uniform JavaScript interface, it has been defined a common REST web services for local native agents. The results are formatted as JSON. This architecture is depicted in the next figure._**

![alt text](https://github.com/mediascape/discovery-self/blob/master/API/DiscoveryAgentAPI.png "Discovery Architecture based on Discovery Agents")

The different agents for different **_target_** technologies will be operable through a pattern of URLs as follows:
 ```
 http://localhost:<port>/discoveryagent/<target>/<function>
 ```

This way, all the agents installed on a device will have a common URL, where each agent would meet a different target, but all sharing a common function dataset.

The result of calling functions is a JSON response with this format
 ```
 {functionName:[{itemName1:{itemValue1}, itemName2:{itemValue2}, … }]}
 ```

For example the next URLs:
```html
	http://localhost:8182/discoveragent/bluetooth/presence
	http://localhost:8182/discoveragent/bluetooth/extra
	http://localhost:8182/discoveragent/upnp/presence
	http://localhost:8182/discoveragent/upnp/devices
	http://localhost:8182/discoveragent/upnp/services
	http://localhost:8182/discoveragent/upnp/actions
	http://localhost:8182/discoveragent/upnp/parameters
	http://localhost:8182/discoveragent/upnp/extra
```

This way different native implemented discovery agents have a standard interface. Thus, the Discovery API can operate with them on an agnostic way from the inner development language or architecture.

---

## API
[Top][]

In order to preserve the smoothness of the Web application, it is mandatory to perform asynchronous requests to the discovery agents to avoid blocking the application runtime waiting for the discovery responses. Promises overcomes these issues by providing uniform patterns for the callbacks of asynchronous operations.

Thus, the pattern for the different **_target_** discovery technologies will be:
 ```javascript
 mediascape.discovery.<function>("<target>",[parameters]).then(cbOk(returnedJSON),cbErr(returnedJSON));
 ```

And the result is a JavaScript object with this format:
 ```
 {"functionName":[{"itemName1":{"itemValue1"}, "itemName2":{"itemValue2"}, … }]}
 ```
* IsPresent
 ```javascript
 mediascape.discovery.isPresent().then(cb(presenceJSON),cb2(errorJSON));
 ```
 
* Extra
 ```javascript
 mediascape.discovery.getExtra().then(cb(extraJSON),cb2(errorJSON));
 ```

This **_general function_** returns all the discovered information about the device, service or asset in a human-understandable way that could be relevant for the user. To this end, it performs the calls to all the available Discovery Agents a produces an **_integrated result_**.

> For the correct invocation of the services it is necessary to understand the steps that the API follows to get the information:
> * If the *Discovery* library gets response to calls to underlying native agents (developed for Android at this moment), it collect the requested information
> * Otherwise the web browser-based agents are requested

---

### JS Discovery API for Capabilities Discovery Agents
[Top][]

Bringing the need of getting awareness of the features of a device, this Discovery library collects the capabilities supplied by each appliance and create a common inventory where the available capabilities for each device are published. Thus, the list of available capabilities is stored through the *MediaScape Shared Context API* to get persistence and share the results.

The first thing that the Discovery API must do is to check for the agent presence. Once it is verified, the Discovery API can publish the agent availability.

For each **_target_** discovery technology, two functions are available:

* Presence
 ```javascript
 mediascape.discovery.isPresent("<target>").then(cb(presenceJSON),cb2(errorJSON));
 ```

This function provides a boolean result to get awareness of the agent availability. For native *Discovery* Agents It performs a basic REST call and depending on the HTTP response (success 2xx or error 4xx/5xx), whether the agent is installed, deployed and running or not. This way all the agents installed on a device can be first checked for existence then probed for more detailed information.

> For the correct invocation of the services it is necessary to understand the steps that the API follows to get the information:
> * If the web browser is able to provide directly the requested information the *Discovery* library returns it
> * Otherwise the *Discovery* library calls to underlying native agents (developed for Android at this moment) to collect the requested information


* Extra
 ```javascript
 mediascape.discovery.getExtra("<target>").then(cb(extraJSON),cb2(errorJSON));
 ```

This function returns additional information about the device, service or asset in a human-understandable way that could be relevant for the user.

> For the correct invocation of the services it is necessary to understand the steps that the API follows to get the information:
> * If the web browser is able to provide directly the requested information the *Discovery* library returns it
> * Otherwise the *Discovery* library calls to underlying native agents (developed for Android at this moment) to collect the requested information

---

#### Discovery of Device Capabilities
[Top][]

This section collects the **_current_** available information available through the *Discovery* API.

**Screen**
* IsPresent
This function provide the information about the user’s device screen availability. This information is based in the screen object of javascript.
* GetExtra
This function provide the information about the visitor's device screen. This information is based in the screen object of javascript.
 
**Geolocation**
* IsPresent
This function provide information about user’s device geolocation sensor availability. This information is based in the Geolocation API of javascript.
* GetExtra
This function provide user’s location using the positioning capabilities of their device. This information is based in the Geolocation API of javascript.
 
**Orientation**
* IsPresent
This function provide information about user’s device orientation sensor availability.
* GetExtra
This function provide user’s device orientation using the DeviceOrientation event implemented in javascript.

**Camera**
* IsPresent
This function provide information about user’s device camera availability. This information is based in the Media Capture and Streams API of javascript.
* GetExtra
This function provide information about user’s device camera numbers. This information is based in the Media Capture and Streams API of javascript.
 
**Vibration**
* IsPresent
This function provide information about user’s device vibration sensor availability. This information is based in the Vibration API of javascript.

**Battery**
* IsPresent
This function provide information about user’s device battery sensor availability. This information is based in the BatteryStatus API of javascript.
* GetExtra
This function provide information about user’s device battery status. This information is based in the BatteryStatus API of javascript.

**UserProximity**
* IsPresent
This function provide information about user’s device proximity sensor availability. This information is based in the UserProximityEvent event of javascript.
* GetExtra
This function provide information about user’s device proximity sensor. This information is based in the UserProximityEvent event of javascript.

**DeviceProximity**
* IsPresent
This function provide information about user’s device proximity sensor availability. This information is based in the DeviceProximityEvent event of javascript.
* GetExtra
This function provide information about user’s device proximity sensor availability. This information is based in the deviceProximityEvent event of javascript.

**Language**
* IsPresent
This function provide information about user’s device browser language adquisition availability. This information is based in the Navigator object of javascript.
* GetExtra
This function provide information about user’s device browser language. This information is based in the Navigator object of javascript.

**DeviceType**
* IsPresent
This function provide information about user’s device type adquisition availability. This information is based in the Navigator object of javascript.
* GetExtra
This function provide information about user’s device type. This information is based in the Navigator object of javascript.

**Connection**
* IsPresent
This function provide information about user’s connection type adquisition availability. This information is based in the Navigator object of javascript.
* GetExtra
This function provide information about user’s device connection type. This information is based in the Navigator object of javascript.

---

### JS Discovery API for Networking Discovery Agents
[Top][]

Bringing the need of getting awareness of the reachable assets through a device by means of its network interface or supported communication protocols, this Discovery library detect the devices, services and media sources reachable by each appliance and create a common inventory where the available assets for each device are published. Thus, the list of available assets is stored through the *MediaScape Shared Context API* to get persistence and share the results.

Because most of the Discovery protocols for networks exploit Multicast and UDP connections not available yet through the Web Browser, it is necessary to employ Native Agents to overcome interoperable connectivity.

The complete set of JavaScript functions defined for each Networking Discovery Agent are listed below.

* IsPresent
This function provides availability status of the agent. For native *Discovery* Agents It performs a basic REST call and depending on the HTTP response (success 2xx or error 4xx/5xx), whether the agent is installed, deployed and running or not. This way all the agents installed on a device can be first checked for existence then probed for more detailed information.

* GetProfile
This function provides information of the level of compliance with the standard interface or protocol or skills of the agent.

* GetDevices
This function launches the discovery messages and collects the reachable devices through a specific interface or protocol.

* GetServices
This function launches the discovery messages and collects the reachable services from the previously detected devices through a specific interface or protocol.

* GetAssets
This function launches the discovery messages and collects the reachable assets, or media resources, from the previously detected devices through a specific interface or protocol coming from internal and external devices.

* GetExtra
This function returns additional information about the device, service or asset in a human-understandable way that could be relevant for the user.

* GetActions
This function provides a set of controls that can be operated over the services or assets controlling media contents such as video/audio.

* GetParameters
This function provides the list of parameters that must be included to operate over the services or assets.

* connectNWS
This function has been created just for namedwebsocket, it creates the connection to the sockets and start the connection and disconnection listeners.

---

#### Discovery of Device Networking
[Top][]

At this moment to check the valid proposed architecture for native implemented agents a basic Android RESTful agent for getting UPnP and Bluetooth services has been developed:

Very thin and lightweight.
Standard information.
Completely RESTful, the right way to build apps.
Inclusion of Restlet and Cling libraries.

This section collects the **_current_** reachable assets through the *Discovery* API.

**Bluetooth**

This module provide information about user’s device bluetooth interface availability and the detected bluetooth devices in the surrounding area.

**UPnP Discovery Agent**

This module provide information about user’s device UPnP protocol availability and the detected UPnP services, assets and operations hosted on devices in the same network.

**NamedWebSockets**

This module provide information about device's connected to a local named web socket.

---

**Example of calls:**

The examples that follows show the use of Discovery API. In the first example we can see the discovery of parameters for the action "SetVolume" of the service "RenderingControl" defined in the 4th device of the UPnP devices list.

However, in the second example we can see the discovery of device capabilities, in this case,the presence of the bluetooth agent.

```html
	mediascape.discovery.getParameters("upnp",4, "RenderingControl","SetVolume").then(function(data) {console.log('Parameters Ok'); console.log(data);}, function(data){console.log('Parameters Error');});
			
	mediascape.discovery.isPresent("bluetooth").then(function(data){console.log('Bluetooth Presence Ok');console.log(data);}, function(data){console.log("Bluetooth Presence Error");});

	mediascape.discovery.getDevices("namedwebsockets").then(function(data){console.log('NamedWebSockets Presence Ok');console.log(data);}, function(data){console.log("Devices Error");});
```

---

## Examples

### Code Example

You can find the implementation of an example for the use of the code in the web:

https://github.com/mediascape/discovery-self/tree/master/helloworld

### Use Example

You can view this example working in the URL:

http://150.241.250.4:7443/WP3Demo


[Top]: #navigation
[Overview]: #overview
[Architecture]: #architecture
[API]: #api
[JS Discovery API for Capabilities Discovery Agents]: #js-discovery-api-for-capabilities-discovery-agents
[JS Discovery API for Networking Discovery Agents]: #js-discovery-api-for-networking-discovery-agents
[Examples]: #examples
