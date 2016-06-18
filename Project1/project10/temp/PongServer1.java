import rmi.*;
import java.net.*;
public class PongServer1 {
    public static void main(String args[]) {
        try {
        PongInterface p = new PingPongServer();
        Skeleton s = new Skeleton(PongInterface.class,p,new InetSocketAddress(7000));
        System.out.println("Created two Skeleton objects");
        //Stub.bind(new InetSocketAddress(7001),"pongInterface1", new InetSocketAddress(7000));
        //Stub.bind(new InetSocketAddress(7001),"pongInterface2", new InetSocketAddress(7002));
        s.start();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
}
