import rmi.*;
import java.net.*;

public class PingPongClient {
    public static void main(String args[]) {

        //PongInterface p = Stub.lookup(PongInterface.class,"pongInterface1",new InetSocketAddress(7001));
        PongInterface p = Stub.create(PongInterface.class,new InetSocketAddress(7000));
        //PongInterface p1 = Stub.lookup(PongInterface.class,"pongInterface2",new InetSocketAddress(7001));
        PongInterface1 p1 = Stub.create(PongInterface1.class,new InetSocketAddress(7100));
        try {
            int fail = 0;
            int no_tests = 4;
            for (int i = 0; i < no_tests; i++) {
                String res  = p.ping(i);
                String curr = "Pong" + String.valueOf(i);
                if (res.equals(curr) != true) {
                    fail++;
                }
            }
            System.out.format("%d Tests Completed, %d Tests Failed\n",no_tests,fail);
            System.out.println(p1.pg(p));
            System.out.println(p.hashCode());
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
}
