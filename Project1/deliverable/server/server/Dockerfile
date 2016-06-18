#Server
FROM java
#RUN javac /rmi/*.java
COPY PongServer /PongServer
COPY RemoteInterface /RemoteInterface
COPY PingServerFactory /PingServerFactory
COPY startjava.sh /startjava.sh
#RUN javac /PongServer/*.java
EXPOSE "7000"
