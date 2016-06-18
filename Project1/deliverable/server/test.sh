echo "########### Stopping and removing server container... ###########"

docker stop server
docker rm server

echo "########### Removing  server image... ###########"

docker rmi project2/server:1.0


#create image for the shared volume, string.txt gets moved to /data
echo "########### Building images for registery ... ###########"

docker build -t project2/server:1.0 server/
#spin a container for shared data

echo "########### Running images for registery ... ###########"

docker run -itd --net=host --volumes-from dataVol --name server project2/server:1.0 /bin/bash -c "source startjava.sh"
