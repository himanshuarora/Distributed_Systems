package PingServerFactory;
import PongServer.*;
import rmi.*;
import java.net.*;
import RemoteInterface.*;


public class PingServerFactoryImpl implements PingServerFactory {
    public PongInterface makePingServer() throws RMIException {
        return makePingServer(10278);
    }

    public PongInterface makePingServer(int portNumber) throws RMIException {
        try {
            PongInterface p = new PingPongServer();
            Skeleton s = new Skeleton(PongInterface.class,p,new InetSocketAddress(portNumber));
            System.out.println("Start the PongServer");
            s.start();
            System.out.println("Started the PongServer");
            PongInterface pp = Stub.create(PongInterface.class,s);
            System.out.println(pp.ping(1));
            return pp;
        }
        catch (Exception e) {
             e.printStackTrace();
        }
        return null;
    }
}
