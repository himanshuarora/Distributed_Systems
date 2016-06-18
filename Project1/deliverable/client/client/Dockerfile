#Client
FROM java
#RUN javac /rmi/*.java
COPY PingPongClient /PingPongClient
COPY RemoteInterface /RemoteInterface
#RUN javac /RemoteInterface/*.java
#RUN javac /PingPongClient/*.java
EXPOSE "7000"
COPY startjava.sh /startjava.sh
