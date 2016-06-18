echo "Running startjava.sh from shell"
javac /rmi/*.java
javac /PingServerFactory/*.java
javac /PongServer/*.java
java PingServerFactory.PingServerFactoryRun
