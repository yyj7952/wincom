#!/bin/sh

. ./env.sh 

for count in 1 2 3 4 5; do
    echo "Thread Dump : $count"
    for i in `ps -ef | grep java | grep "SERVER=$SERVER_NAME " | awk '{print $2}'`;do
	echo "+jstack -l $i"
        jstack -l $i >> $SERVER_NAME"_"$DATE.dmp
	echo "sleep 1 sec"
	sleep 1
    done
    echo "done"
    sleep 3
done
	
