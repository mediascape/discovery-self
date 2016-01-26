#UPnPREST

Basic Android RESTful agent for getting UPnP services.

 * Very thin and lightweight.
 * Standard information.
 * Completely RESTful, the right way to build apps.
 * Insclusion of Restlet and Cling libraries.

## Navigation
[Goals][] | [Requirements][] | [Instalation][] | [Simple Routing][] | [Example][]| [Lisence][]

### Goals
[Top][]

The project is not a typical android project because it is a service that inits when the OS finish booting. It is a system that works in background and that respond to the user when it receive a request by the invocation of a URI.
The main idea of this project was to create a UPnP server that was accessible by javascript. For this aim we decided to use a RESTFul server and agent concept. For the creation of the RESTful server we have use Restlet library. This library permitted us the possibility of building a server with not great effort and little code.
The UPnP server permit us to control the devices connected to the wifi and the services that those devices offer to the rest of devices. For developing the UPnP service has been use Cling library.
 
### Requirements
[Top][]

1.- Android 4.4 "KitKat" or higher

### Instalation
[Top][]

Instalation in Android:
	Before attempting a manual installation of apps using the .apk files or source code, you must first allow your phone to install from “Unknown Sources” (i.e. non-Market apps).

	To do this, navigate to Menu -> Settings -> Applications/Security -> check the box marked “Unknown Sources“.

	* By Source Code:
		 - Step 1: Connect Android device to PC via USB cable.
		 - Step 2: Download and extract source code.
		 - Step 3: Open Eclipse.
		 - Step 4: File -> Import -> General -> Existing Projects into Workspace -> Next.
		 - Step 5: Select the path to the project and check "Copy projects into workspace".
		 - Step 6: Click Finish.
		 - Step 7: Finally, build and run the project in your device.
	* By Apk:
		 - Step 1: Connect Android device to PC via USB cable.
		 - Step 2: Copy .apk file that can you find in the bin folder to attached device's storage.
		 - Step 3: Turn off USB storage and disconnect it from PC.
		 - Step 4: Open FileManager app and click on the copied .apk file.It will ask you whether to install this app or not. Click Yes or OK.

### Simple Routing
[Top][]

The routing URI is divided in two part, routed part and remaining part.

```php
    http://localhost:8182/androidAgent?getDevices
    http://localhost:8182/androidAgent?getDeviceInfo=1
```

The routed part is allwais the same:

```php
    http://localhost:8182/androidAgent
```

The remaining part will determinate the user's request. This is the part that comes after the `?` 
symbol. For example, in the code that follows this words, you can find an URI example for getting
de devices connected in the wifi that have UPnP.

```php
    http://localhost:8182/androidAgent?getDevices
```

### Example
[Top][]

We have made an application that consume REST content using AJAX's GET and POST methods. In the following example we can see the result obtained from the different requests.

```html
<div>
    	<h1>Old GET request</h1>

	<div>http://localhost:8182/discoveragent/upnp?devices</div>

	<div>{"devices":[{"deviceName":"TP-LINK Windows Media Connect Compatible (TL-WR1043ND) 001"},{"deviceName":"Rockchip Media Renderer 1.0"},{"deviceName":"fbox FboxTV MediaRenderer 1"},{"deviceName":"fbox FboxTV MediaRenderer 1"},{"deviceName":"TP-LINK TL-WR1043ND 1.0"},{"deviceName":"rockchip GNaP v1"},{"deviceName":"rockchip fbox v1"}]}</div>

	<div>http://localhost:8182/discoveragent/upnp?devicedetails&device=4</div>

	<div>{"details":[{"number":"1","FriendlyName":"Fbox MediaRenderer : 192.168.1.107","description":"MediaRenderer on Android","manufacturer":"Fbox MediaRenderer : 192.168.1.107","name":"Fbox MediaRenderer : 192.168.1.107"}],"device":"fbox FboxTV MediaRenderer 1"}</div>

	<div>http://localhost:8182/discoveragent/upnp?services&device=4</div>

	<div>{"services":[{"type":"AVTransport","serviceId":"urn:upnp-org:serviceId:AVTransport","name":"(RemoteService) ServiceId: urn:upnp-org:serviceId:AVTransport"},{"type":"RenderingControl","serviceId":"urn:upnp-org:serviceId:RenderingControl","name":"(RemoteService) ServiceId: urn:upnp-org:serviceId:RenderingControl"},{"type":"ConnectionManager","serviceId":"urn:upnp-org:serviceId:ConnectionManager","name":"(RemoteService) ServiceId: urn:upnp-org:serviceId:ConnectionManager"}]}</div>

	<div>http://localhost:8182/discoveragent/upnp?actions&device=4&service=RenderingControl</div>

	<div>{"service":"RenderingControl","actions":[{"actionName":"ListPresets"},{"actionName":"GetLoudness"},{"actionName":"SetMute"},{"actionName":"SetVolume"},{"actionName":"GetVolume"},{"actionName":"SetLoudness"},{"actionName":"GetVolumeDB"},{"actionName":"SetVolumeDB"},{"actionName":"GetVolumeDBRange"},{"actionName":"GetMute"},{"actionName":"SelectPreset"}]}</div>

	<div>http://localhost:8182/discoveragent/upnp?parameters&device=4&service=RenderingControl&action=SetVolume</div>

	<div>{"action":"SetVolume","parameters":[{"type":"(UnsignedIntegerFourBytesDatatype)","argument":"InstanceID"},{"type":"(StringDatatype)","argument":"Channel"},{"type":"(UnsignedIntegerTwoBytesDatatype)","argument":"DesiredVolume"}]}</div>
	
	<h1>New GET request</h1>

	<div>http://localhost:8182/discoveragent/upnp/devices</div>

	<div>{"devices":[{"deviceName":"TP-LINK Windows Media Connect Compatible (TL-WR1043ND) 001"},{"deviceName":"Rockchip Media Renderer 1.0"},{"deviceName":"fbox FboxTV MediaRenderer 1"},{"deviceName":"fbox FboxTV MediaRenderer 1"},{"deviceName":"TP-LINK TL-WR1043ND 1.0"},{"deviceName":"rockchip GNaP v1"},{"deviceName":"rockchip fbox v1"}]}</div>

	<div>http://localhost:8182/discoveragent/upnp/devicedetails&device=4</div>

	<div>{"details":[{"number":"1","FriendlyName":"Fbox MediaRenderer : 192.168.1.107","description":"MediaRenderer on Android","manufacturer":"Fbox MediaRenderer : 192.168.1.107","name":"Fbox MediaRenderer : 192.168.1.107"}],"device":"fbox FboxTV MediaRenderer 1"}</div>

	<div>http://localhost:8182/discoveragent/upnp/services?device=4</div>

	<div>{"services":[{"type":"AVTransport","serviceId":"urn:upnp-org:serviceId:AVTransport","name":"(RemoteService) ServiceId: urn:upnp-org:serviceId:AVTransport"},{"type":"RenderingControl","serviceId":"urn:upnp-org:serviceId:RenderingControl","name":"(RemoteService) ServiceId: urn:upnp-org:serviceId:RenderingControl"},{"type":"ConnectionManager","serviceId":"urn:upnp-org:serviceId:ConnectionManager","name":"(RemoteService) ServiceId: urn:upnp-org:serviceId:ConnectionManager"}]}</div>

	<div>http://localhost:8182/discoveragent/upnp/actions?device=4&service=RenderingControl</div>

	<div>{"service":"RenderingControl","actions":[{"actionName":"ListPresets"},{"actionName":"GetLoudness"},{"actionName":"SetMute"},{"actionName":"SetVolume"},{"actionName":"GetVolume"},{"actionName":"SetLoudness"},{"actionName":"GetVolumeDB"},{"actionName":"SetVolumeDB"},{"actionName":"GetVolumeDBRange"},{"actionName":"GetMute"},{"actionName":"SelectPreset"}]}</div>

	<div>http://localhost:8182/discoveragent/upnp/parameters?device=4&service=RenderingControl&action=SetVolume</div>

	<div>{"action":"SetVolume","parameters":[{"type":"(UnsignedIntegerFourBytesDatatype)","argument":"InstanceID"},{"type":"(StringDatatype)","argument":"Channel"},{"type":"(UnsignedIntegerTwoBytesDatatype)","argument":"DesiredVolume"}]}</div>

	<h1>Old POST request</h1>

	<div>http://localhost:8182/discoveragent/upnp?devices</div>

	<div>{"devices":[{"deviceName":"TP-LINK Windows Media Connect Compatible (TL-WR1043ND) 001"},{"deviceName":"Rockchip Media Renderer 1.0"},{"deviceName":"fbox FboxTV MediaRenderer 1"},{"deviceName":"fbox FboxTV MediaRenderer 1"},{"deviceName":"TP-LINK TL-WR1043ND 1.0"},{"deviceName":"rockchip GNaP v1"},{"deviceName":"rockchip fbox v1"}]}</div>

	<div>http://localhost:8182/discoveragent/upnp?devicedetails</div>

	<div>{"details":[{"number":"1","FriendlyName":"Fbox MediaRenderer : 192.168.1.107","description":"MediaRenderer on Android","manufacturer":"Fbox MediaRenderer : 192.168.1.107","name":"Fbox MediaRenderer : 192.168.1.107"}],"device":"fbox FboxTV MediaRenderer 1"}</div>

	<div>http://localhost:8182/discoveragent/upnp?services</div>

	<div>{"device":"fbox FboxTV MediaRenderer 1","services":[{"type":"AVTransport","serviceId":"urn:upnp-org:serviceId:AVTransport","name":"(RemoteService) ServiceId: urn:upnp-org:serviceId:AVTransport"},{"type":"RenderingControl","serviceId":"urn:upnp-org:serviceId:RenderingControl","name":"(RemoteService) ServiceId: urn:upnp-org:serviceId:RenderingControl"},{"type":"ConnectionManager","serviceId":"urn:upnp-org:serviceId:ConnectionManager","name":"(RemoteService) ServiceId: urn:upnp-org:serviceId:ConnectionManager"}]}</div>

	<div>http://localhost:8182/discoveragent/upnp?actions</div>

	<div>{"service":"RenderingControl","actions":[{"actionName":"ListPresets"},{"actionName":"GetLoudness"},{"actionName":"SetMute"},{"actionName":"SetVolume"},{"actionName":"GetVolume"},{"actionName":"SetLoudness"},{"actionName":"GetVolumeDB"},{"actionName":"SetVolumeDB"},{"actionName":"GetVolumeDBRange"},{"actionName":"GetMute"},{"actionName":"SelectPreset"}]}</div>

	<div>http://localhost:8182/discoveragent/upnp?parameters</div>

	<div>{"action":"RenderingControl","parameters":[{"type":"(UnsignedIntegerFourBytesDatatype)","argument":"InstanceID"},{"type":"(StringDatatype)","argument":"Channel"},{"type":"(UnsignedIntegerTwoBytesDatatype)","argument":"DesiredVolume"}]}</div>
	
	<h1>New POST request<h1>

	<div>http://localhost:8182/discoveragent/upnp/devices</div>
	<br>
	<div>{"devices":[{"deviceName":"TP-LINK Windows Media Connect Compatible (TL-WR1043ND) 001"},{"deviceName":"Rockchip Media Renderer 1.0"},{"deviceName":"fbox FboxTV MediaRenderer 1"},{"deviceName":"fbox FboxTV MediaRenderer 1"},{"deviceName":"TP-LINK TL-WR1043ND 1.0"},{"deviceName":"rockchip GNaP v1"},{"deviceName":"rockchip fbox v1"}]}</div>

	<div>http://localhost:8182/discoveragent/upnp/devicedetails</div>

	<div>{"details":[{"number":"1","FriendlyName":"Fbox MediaRenderer : 192.168.1.107","description":"MediaRenderer on Android","manufacturer":"Fbox MediaRenderer : 192.168.1.107","name":"Fbox MediaRenderer : 192.168.1.107"}],"device":"fbox FboxTV MediaRenderer 1"}</div>

	<div>http://localhost:8182/discoveragent/upnp/services</div>

	<div>{"device":"fbox FboxTV MediaRenderer 1","services":[{"type":"AVTransport","serviceId":"urn:upnp-org:serviceId:AVTransport","name":"(RemoteService) ServiceId: urn:upnp-org:serviceId:AVTransport"},{"type":"RenderingControl","serviceId":"urn:upnp-org:serviceId:RenderingControl","name":"(RemoteService) ServiceId: urn:upnp-org:serviceId:RenderingControl"},{"type":"ConnectionManager","serviceId":"urn:upnp-org:serviceId:ConnectionManager","name":"(RemoteService) ServiceId: urn:upnp-org:serviceId:ConnectionManager"}]}</div>

	<div>http://localhost:8182/discoveragent/upnp/actions</div>

	<div>{"service":"RenderingControl","actions":[{"actionName":"ListPresets"},{"actionName":"GetLoudness"},{"actionName":"SetMute"},{"actionName":"SetVolume"},{"actionName":"GetVolume"},{"actionName":"SetLoudness"},{"actionName":"GetVolumeDB"},{"actionName":"SetVolumeDB"},{"actionName":"GetVolumeDBRange"},{"actionName":"GetMute"},{"actionName":"SelectPreset"}]}</div>

	<div>http://localhost:8182/discoveragent/upnp/parameters</div>

	<div>{"action":"RenderingControl","parameters":[{"type":"(UnsignedIntegerFourBytesDatatype)","argument":"InstanceID"},{"type":"(StringDatatype)","argument":"Channel"},{"type":"(UnsignedIntegerTwoBytesDatatype)","argumen:"DesiredVolume"}]}</div>
</div>
```
### Lisence
[Top][]

[Top]: #navigation
[Goals]: #goals
[Requirements]: #requirements
[Instalation]: #instalation
[Simple Routing]: #simple-routing
[Example]: #example
[Lisence]: #lisence
