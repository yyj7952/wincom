#!/bin/bash

##############Setting###################
SH_PATH=`pwd`
CALL_IP=([0]=123.140.17.240)
CALL_PORT=([0]=8180 [1]=8280)
URL=/mjdview
########################################

IP_CNT=${#CALL_IP[@]}
PORT_CNT=${#CALL_PORT[@]}

##############################################################################################
fnPressKey()
{
   if [ $1 == 1]; then
        SRT_DISP="[ 1  or  ALL ]"
   else
        SRT_DISP="[ 1 ~ $1  or  ALL ]"
   fi

   echo
   echo "Invalid input. Enter a value of $SRT_DISP"
   echo "Press ENTER to Continue ..."
   read a
}

while true
do

	if [ "$IP_CNT" -gt 1 ] 
	then 
	  echo -n "[Non-Stop mode]Enter the server number[1 ~ ${IP_CNT}] (Enter the 'ALL' - Switch to All Server): "
	else
	  echo -n "[Non-Stop mode]Enter the server number[1] (Enter the 'ALL' - Switch to All Server): "
	fi
	read SERVER_NUM



	if [ "$SERVER_NUM" == "ALL" ]
	then
		break
	elif [[ $SERVER_NUM =~ ^-?[0-9]+$  ]]
        then
                if [ $SERVER_NUM -ge 1 -a  $SERVER_NUM -le ${IP_CNT}  ]
                then
                        break
                else
                        fnPressKey $IP_CNT
                fi
        else
                if [ ${#SERVER_NUM} != 0 ]; then
                        fnPressKey $IP_CNT
                fi
	fi
done

##############################################################################################


##############Setting###################
HEAD_MODE="accept-mode:Non-Stop"
HEAD_API="accept-api:ALL"
########################################
 

echo "####### SERVER : ${IP_CNT} EA #######"
echo ""

if [ "$SERVER_NUM" == "ALL" ]
then

  i=0
  x=1
  while [ "$i" -lt "$IP_CNT" ]
  do
    y=0
    while [ "$y" -lt "$PORT_CNT" ]
    do
      echo "####### SERVER : ${CALL_IP[$i]}:${CALL_PORT[$y]}#######"
      echo ""
      echo "####### Y : Non-Stop status, N : Normal status #######"

      curl -H $HEAD_MODE -H $HEAD_API "http://${CALL_IP[$i]}:${CALL_PORT[$y]}${URL}"
      echo ''
      echo '--------------------------------------------------------------------------'
      echo ''
      sleep 3

      echo ""
      let "y = y + 1"
    done
    let "i = i + 1"
    let "x = x + 1"
  done

else

  let "REAL_NUM = SERVER_NUM - 1"
  if [ ! -d ${CALL_IP[$REAL_NUM]} ]

  then
    y=0
    while [ "$y" -lt "$PORT_CNT" ]
    do
      echo "####### SERVER : ${CALL_IP[$REAL_NUM]}:${CALL_PORT[$y]}#######"
      echo ""
      echo "####### Y : Non-Stop status, N : Normal status #######"

      curl -H $HEAD_MODE -H $HEAD_API "http://${CALL_IP[$REAL_NUM]}:${CALL_PORT[$y]}${URL}"
      echo ''
      echo '--------------------------------------------------------------------------'
      echo ''
      sleep 3

      echo ""
      let "y = y + 1"
    done
  else

    echo "####### SERVER NOT FOUND #######"

  fi

  echo ""

fi
echo "####### END #######"
