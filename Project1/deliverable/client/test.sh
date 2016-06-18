echo "########### Stopping and removing rmiReg container... ###########"

docker stop client
docker rm client

echo "########### Removing  rmireg image... ###########"

docker rmi project2/client:1.0


#create image for the shared volume, string.txt gets moved to /data
echo "########### Building images for registery ... ###########"

docker build -t project2/client:1.0 client/
#spin a container for shared data

echo "########### Running images for registery ... ###########"

docker run -itd --net=host --volumes-from dataVol --name client project2/client:1.0 /bin/bash -c "source startjava.sh"
