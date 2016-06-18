cd dataVol/
./test.sh
cd ..
cd rmireg
./test.sh
cd ..
cd server
./test.sh
cd ..
cd client
./test.sh
sleep 10
docker logs client
