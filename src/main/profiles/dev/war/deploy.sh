#! /bin/bash

ECHO=`which echo`
DATE=`which date`
TODAY=`$DATE +%Y%m%d`

echo "deploy start"

backfile="/app/ctis/war/curation_api.war.backup.$TODAY"

if [ -f "$backfile" ]
then
	cd /app/ctis/war
	rm -rf META-INF WEB-INF
	unzip curation_api.war

	cd /app/ctis/webroot/curation_api.war
	rm -rf META-INF WEB-INF
	cd /app/ctis/war
	cp -R META-INF WEB-INF /app/ctis/webroot/curation_api.war

	echo "deploy end"

else
	echo "#############  $backfile not found!   ##################"
	echo "############# You must run backup_src.sh first!!!   ##############"
	echo "deploy failed!!!"
fi

exit
