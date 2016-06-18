package rmi;
import java.lang.reflect.Method;
import java.lang.*;

/** RMI exceptions. */
public class RMIException extends Exception
{
    /**
     *
     */
    private static final long serialVersionUID = 8123146547897L;

    /** Creates an <code>RMIException</code> with the given message string. */
    public RMIException(String message) {
        super(message);
    }

    /** Creates an <code>RMIException</code> with a message string and the given
        cause. */
    public RMIException(String message, Throwable cause) {
        super(message, cause);
    }

    /** Creates an <code>RMIException</code> from the given cause. */
    public RMIException(Throwable cause) {
        super(cause);
    }

    @SuppressWarnings("rawtypes")
    public static boolean inferRMI(Method method) {
        Class[] ecs = method.getExceptionTypes();
        for(Class e : ecs) {
            if(e.equals(RMIException.class)) {
                return true;
            }
        }
        return false;
    }
    @SuppressWarnings("rawtypes")
    public static boolean isRemoteInterface(Class c) {
        if (!c.isInterface()){
            return false;
        }

        Class[] superInterfaces = c.getInterfaces();

            for (Class cc : superInterfaces) {
                if (!isRemoteInterface(cc))
                    return false;
            }

        Method[] mds = c.getDeclaredMethods();
        for(Method m : mds) {
            if(!inferRMI(m)) {
                return false;
            }
        }
        return true;
    }
}
