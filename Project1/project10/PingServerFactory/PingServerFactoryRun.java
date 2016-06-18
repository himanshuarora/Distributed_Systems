package PingServerFactory;
import PongServer.*;
import rmi.*;
import java.net.*;

public class PingServerFactoryRun {
    public static void main(String args[]) {
        try {
            PingServerFactory p = new PingServerFactoryImpl();
            Skeleton s = new Skeleton(PingServerFactory.class,p,new InetSocketAddress(20000));
            Stub.bind(new InetSocketAddress(7001),"PingServerFactory",new InetSocketAddress(20000));
            s.start();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
}

