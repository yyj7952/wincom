#! /bin/bash

ECHO=`which echo`
DATE=`which date`
TODAY=`$DATE +%Y%m%d`

echo "backup start"

cd /app/ctis/war
rm curation_api.war.backup.$TODAY
tar cvzf curation_api.war.backup.$TODAY /app/ctis/webroot1/curation_api.war

echo "backup end"
exit

