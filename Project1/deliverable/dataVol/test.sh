echo "########### Stopping and removing dataVol container... ###########"

docker stop dataVol
docker rm dataVol

echo "########### Removing  dataVol image... ###########"

docker rmi project2/dataVol:1.0


#create image for the shared volume, string.txt gets moved to /data
echo "########### Building images for registery ... ###########"

docker build -t project2/datavol:1.0 dataVol/
#spin a container for shared data

echo "########### Running images for registery ... ###########"

docker run -itd --net=host --name dataVol project2/datavol:1.0 /bin/bash
