import rmi.*;
public interface PongInterface {
    public String ping(int i) throws RMIException;
    public String pg(PongInterface i) throws RMIException;
}
