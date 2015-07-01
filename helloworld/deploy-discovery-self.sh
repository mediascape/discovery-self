#!/bin/bash

src=API/mediascape/Discovery
lib=API/mediascape/lib
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
#	git clone https://$user:$password@github.com/mediascape/discovery-self.git
	git clone https://github.com/mediascape/discovery-self.git
	cd ./discovery-self
	git config core.sparsecheckout true
	#echo API/ > .git/info/sparse-checkout
	#echo Demos/ > .git/info/sparse-checkout
	git read-tree -m -u HEAD
else 
	echo "${Blue}Checking for updates...${Color_Off}"
	cd ./discovery-self
	git pull
fi

sudo cp $helloworld /var/www/discovery-self -r
sudo cp $src/* /var/www/discovery-self/js/mediascape -r
sudo cp $lib/* /var/www/discovery-self/js/mediascape/lib -r

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

