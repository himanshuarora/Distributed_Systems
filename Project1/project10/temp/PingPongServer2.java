
import rmi.*;
class PingPongServer2 implements PongInterface1 {
    private String res = "JMRULES";
    public String ping(int i) throws RMIException {
        String res = "Pong" + String.valueOf(i);
        return res;
    }

    public String pg(PongInterface i) throws RMIException {
        System.out.println(i.hashCode());
        return i.ping(10)+res;
    }

}
