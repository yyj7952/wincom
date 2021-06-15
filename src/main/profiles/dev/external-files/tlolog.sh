#!/bin/bash

MV="/bin/mv"
CP="/bin/cp"
TAR="/bin/tar"
RM="/bin/rm"

##############setting####################
#Log file path
BASE_DIR=/data/logs/ctis/tlolog
#Log file extension
FILE_EXT=log
#TLOLOG directory
TLOLOG_DIR=/data/TLOLOG

#hostname
SNAME=`hostname`
#Today -1(YYYYMM)
YYYYMM=`date +%Y%m --date '-1 days'`
#Today -1(MMDDYYYY)
MMDDYYYY=`date +%m%d%Y --date '-1 days'`
##########################################

MOVE_DIR=${BASE_DIR}"/"${YYYYMM}
FIND_FILE_DIR="./*"${MMDDYYYY}"."${FILE_EXT}
TAR_FILENAME="TLO_"${SNAME}"_"${MMDDYYYY}".tar"

echo "### MAKE TAR[${TAR_FILENAME}] ####"
cd ${MOVE_DIR}
echo "...MOVE DIR...${MOVE_DIR}"
$TAR "cvf" ${TAR_FILENAME} ${FIND_FILE_DIR}
echo "############ MAKE END ############"

sleep 1
echo ""
echo "############ MOVE FILE ##########"
echo "...${TAR_FILENAME} --> ${TLOLOG_DIR}/"
$MV ${TAR_FILENAME} ${TLOLOG_DIR}"/"
sleep 1
cd ${TLOLOG_DIR}
echo "...MOVE DIR...${TLOLOG_DIR}"
$TAR "xvf" ${TAR_FILENAME}
echo "...TAR xvf...SUCCESS"
echo "############ MOVE END ##########"

sleep 1
echo ""
echo "############ RM TAR ##########"
$RM ${TAR_FILENAME} 
echo "...rm ${TAR_FILENAME} SUCCESS"
echo "############ RM END ##########"


