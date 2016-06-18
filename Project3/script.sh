#!/bin/bash

# List of arguments 
# 1) <optional> Input text file
# 2) <optional> Number of slaves
N=4
input=$1

if [ $# -eq 0 ]
    then input="big.txt"
fi

#[ $# -eq 0 ] && { echo "USAGE: ./run.sh <relative-path-to-input-file>"; exit 1; }

cp -f $input hadoop-master/files/input.txt

#### Cleanup ###########
i=1
rm -f hadoop-master/files/slaves
while [ $i -le $N ]
do
    echo slave$i >> hadoop-master/files/slaves
	docker rm -f slave$i
	i=$(( $i + 1 ))
done
docker rm -f master
docker network rm my-network
docker rmi -f hadoop-slave
docker rmi -f hadoop-master

##### Create network #######
docker network create -d bridge my-network

##### Build images #######
docker build -t hadoop-master ./hadoop-master/
docker build -t hadoop-slave ./hadoop-slave/

###### Run the containers ########
docker run -itd --net=my-network --name master -h master hadoop-master bash
i=1
while [ $i -le $N ]
do
    docker run -itd --net=my-network --name slave$i -h slave$i hadoop-slave bash
	i=$(( $i + 1 ))
done

###### Start the nodes ########
docker exec -it master /root/start.sh
sleep 10
i=1
while [ $i -le $N ]
do
    docker exec -it slave$i /root/start-slave.sh
	i=$(( $i + 1 ))
done

###### Run the program ########
sleep 30
docker exec -it master bin/bash /root/bigram.sh
