package PingPongClient;
import rmi.*;
import java.net.*;
import PongServer.*;

public class PingPongClient {
    public static void main(String args[]) {

        PingServerFactory pf = Stub.lookup(PingServerFactory.class,"PingServerFactory",new InetSocketAddress(7001));
        try {
            PongInterface p = pf.makePingServer();
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
                System.out.format("%d Tests Completed, %d Tests Failed",no_tests,fail);
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        //PongInterface p = Stub.lookup(PongInterface.class,"pongInterface",new InetSocketAddress(7001));
        //try {
        //    int fail = 0;
        //    int no_tests = 4;
        //    for (int i = 0; i < no_tests; i++) {
        //        String res  = p.ping(i);
        //        String curr = "Pong" + String.valueOf(i);
        //        if (res.equals(curr) != true) {
        //            fail++;
        //        }
        //    }
        //    System.out.format("%d Tests Completed, %d Tests Failed",no_tests,fail);
        //}
        //catch (Exception e) {
        //    e.printStackTrace();
        //}
    }
}
