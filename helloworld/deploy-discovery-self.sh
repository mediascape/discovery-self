#!/bin/bash

src=API/mediascape/Discovery
discovery_lib=API/mediascape/lib
mediascape_lib=API/lib
helloworld=helloworld

cd /tmp/

#Reset color
Color_Off='\033[0m'       # Text Reset

# Regular Colors
Black='\033[0;30m'        # Blackls 
Red='\033[0;31m'          # Red
Green='\033[0;32m'        # Green
Yellow='\033[0;33m'       # Yellow
Blue='\033[0;34m'         # Blue
Purple='\033[0;35m'       # Purple
Cyan='\033[0;36m'         # Cyan
White='\033[0;37m'        # White

user=$1
FILE=./discovery-self

if [ ! -d $FILE  ];
then
	git clone https://github.com/mediascape/discovery-self.git
	cd ./discovery-self
	git config core.sparsecheckout true
	git read-tree -m -u HEAD
else 
	echo "${Blue}Checking for updates...${Color_Off}"
	cd ./discovery-self
	git pull
fi

if [ -d /var/www/html ];
then
	sudo cp $helloworld /var/www/html/discovery-self -r
	sudo cp $src/* /var/www/html/discovery-self/js/mediascape -r
	sudo cp $mediascape_lib/* /var/www/html/discovery-self/js/lib -r
	sudo cp $discovery_lib/* /var/www/html/discovery-self/js/mediascape/lib -r
else
	sudo cp $helloworld /var/www/discovery-self -r
	sudo cp $src/* /var/www/discovery-self/js/mediascape -r
	sudo cp $mediascape_lib/* /var/www/discovery-self/js/lib -r
	sudo cp $discovery_lib/* /var/www/discovery-self/js/mediascape/lib -r
fi

##
#In case that we prefer the use of node
#Install node libraries and start node execution
#sudo npm install
#sudo node index.js
##

if [ -d /var/www/discovery-self ];
then
	echo "${Yellow}discovery-self installed in /var/www/${Color_Off}"
	echo "${Yellow}Demo url: http://localhost/discovery-self${Color_Off}"
else
	echo "${Red}Error during the installation${Color_Off}"
fi

