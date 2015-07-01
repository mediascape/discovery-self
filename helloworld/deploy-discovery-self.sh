#!/bin/bash

WP3_src=WP3/API/WP3mediascape
WP3_lib=WP3/API/lib
WP3Demo=WP3/Tests/helloworld/WP3Demo
cd /tmp/

#Reset color
Color_Off='\033[0m'       # Text Reset

# Regular Colors
Black='\033[0;30m'        # Black
Red='\033[0;31m'          # Red
Green='\033[0;32m'        # Green
Yellow='\033[0;33m'       # Yellow
Blue='\033[0;34m'         # Blue
Purple='\033[0;35m'       # Purple
Cyan='\033[0;36m'         # Cyan
White='\033[0;37m'        # White

user=$1
FILE=./WP3
stty_orig=`stty -g` # save original terminal setting.
echo ${exclude};
echo "${Cyan}github user:"
read user
echo "githbub password:"
stty -echo          # turn-off echoing.
read password         # read the password
stty $stty_orig     # restore terminal setting.

if [ ! -d $FILE  ];
then
	git clone https://$user:$password@github.com/mediascape/WP3.git
	cd ./WP3
	git config core.sparsecheckout true
	#echo API/ > .git/info/sparse-checkout
	#echo Demos/ > .git/info/sparse-checkout
	git read-tree -m -u HEAD
	cd .. 
else 
	echo "${Blue}Checking for updates...${Color_Off}"
	cd ./WP3
	git pull
	cd .. 
fi

cp $WP3Demo /var/www/ -r
cp $WP3_src/* /var/www/WP3Demo/js/mediascape -r
cp $WP3_lib/* /var/www/WP3Demo/js/mediascape/lib -r

##
#In case that we prefer the use of node
#Install node libraries and start node execution
#sudo npm install
#sudo node index.js
##

if [ -d /var/www/WP3Demo ];
then
	echo "${Yellow}WP3Demo installed in /var/www/${Color_Off}"
	echo "${Yellow}Demo url: http://localhost/WP3Demo${Color_Off}"
else
	echo "${Red}Error during the installation${Color_Off}"
fi

