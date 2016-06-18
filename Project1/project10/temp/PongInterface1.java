import rmi.*;
public interface PongInterface1 {
    public String ping(int i) throws RMIException;
    public String pg(PongInterface i) throws RMIException;
}
