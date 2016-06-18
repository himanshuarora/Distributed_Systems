
import rmi.*;
class PingPongServer implements PongInterface {
    public String ping(int i) throws RMIException {
        String res = "Pong" + String.valueOf(i);
        return res;
    }

    public String pg(PongInterface i) throws RMIException {
        return i.ping(10);
    }

}
