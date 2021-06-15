#! /bin/bash
findProcess=`jps |grep UserWatchTempdb`

echo $findProcess

if [ -z "$findProcess" ]
then
echo "findTempdb is blank"
nohup java -Dtempdb.config.path=/app/nosql/tempdb_consumer.conf -cp /app/nosql/tempdb-0.1.0-jar-with-dependencies.jar lguplus.kafkaConsumer.UserWatchTempdb &
nohup java -Dtempdb.config.path=/app/nosql/tempdb_consumer.conf -cp /app/nosql/tempdb-0.1.0-jar-with-dependencies.jar lguplus.kafkaConsumer.UserWatchTempdbHistory &
else
echo "findTempdb is not blank"
fi

exit 0
