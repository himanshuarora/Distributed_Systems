package PingPongClient;
import RemoteInterface.*;
import rmi.*;
import java.net.*;

public class PingPongClient {
    public static void main(String args[]) {

        System.out.println("Looking for remote object of PingServerFactory in RMIregistry");
        PingServerFactory pf = Stub.lookup(PingServerFactory.class,"PingServerFactory",new InetSocketAddress(7001));
        System.out.println("Lookup Successful");
        try {
            System.out.println("Calling makePingServer method to create remote object for PingServer");
            PongInterface p = (PongInterface)pf.makePingServer();
            PongInterface p1 = (PongInterface)pf.makePingServer(15151);
            try {
                int fail = 0;
                int no_tests = 4;
                for (int i = 0; i < no_tests; i++) {
                    System.out.format("Pinging server %d\n",i);
                    String res  = p.ping(i);
                    System.out.format("Received from server %s\n",res);
                    String curr = "Pong " + String.valueOf(i);
                    if (res.equals(curr) != true) {
                        fail++;
                    }
                }
                System.out.format("%d Tests Completed, %d Tests Failed\n",no_tests,fail);
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
}
