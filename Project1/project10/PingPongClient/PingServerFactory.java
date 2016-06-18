package PingPongClient;
import PongServer.*;
import rmi.*;
public interface PingServerFactory {
    public PongInterface makePingServer(int portNumber) throws RMIException;
    public PongInterface makePingServer() throws RMIException;
}
