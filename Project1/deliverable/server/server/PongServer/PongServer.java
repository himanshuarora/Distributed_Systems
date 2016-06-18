package PongServer;
import rmi.*;
import java.net.*;
import RemoteInterface.*;
public class PongServer {
    public static void main(String args[]) {
        try {
        PongInterface p = new PingPongServer();
        Skeleton s = new Skeleton(PongInterface.class,p,new InetSocketAddress(7000));
        Stub.bind(new InetSocketAddress(7001),"pongInterface", new InetSocketAddress(7000));
        s.start();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
}
