package PongServer;
import RemoteInterface.*;
import rmi.*;
public class PingPongServer implements PongInterface {
    public String ping(int i) throws RMIException {
        String res = "Pong " + String.valueOf(i);
        return res;
    }
}
