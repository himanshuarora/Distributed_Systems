package RemoteInterface;
import rmi.*;
public interface PongInterface {
    public String ping(int i) throws RMIException;
}
