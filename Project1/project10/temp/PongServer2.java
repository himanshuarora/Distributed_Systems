import rmi.*;
import java.net.*;
public class PongServer2 {
    public static void main(String args[]) {
        try {
        PongInterface1 p = new PingPongServer2();
        Skeleton s = new Skeleton(PongInterface1.class,p,new InetSocketAddress(7100));
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
