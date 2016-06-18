package rmi;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.UnknownHostException;

// Invocationhandler for Proxy Object, Does the Marshalling of method parameters and send them on Network to remote Object.
// Also returns the output of the RMI Call
class StubProxy implements InvocationHandler, Serializable {

    // remote Address of Skeleton
    private InetSocketAddress remoteAddress;
    // remote interface className
    private String className;

    // Constructor
    public StubProxy(InetSocketAddress address, String cName) {
        this.remoteAddress = address;
        this.className = cName;
    }

    /**
     * SerialVersionUID
     */
    private static final long serialVersionUID = -713214545986L;

    // It takes proxy object, method and arguments and return the output of RMI call
    // It creates a network connection with Skeleton, passes the function name and returns the result
    @SuppressWarnings("resource")
    private Object remoteInvoke(Object proxy, Method method, Object[] args) throws Throwable {

        Socket socket   = new Socket();
        Integer res     = null;
        Object obj      = null;

        // Opening connection with Skeleton
        try {
            socket = new Socket();
            socket.connect(remoteAddress);

            ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
            out.flush();

            ObjectInputStream in = new ObjectInputStream(socket.getInputStream());

            // Sending ClassName, methods and argument list
            out.writeObject(method.getDeclaringClass().getName());
            out.writeObject(method.getName());
            out.writeObject(method.getParameterTypes());
            out.writeObject(args);
            out.flush();

            // Reading the object back
            res     = (Integer) in.readObject();
            obj     = in.readObject();

            // Closing the socket
            socket.close();
            // Catching the Exception
        } catch (Exception e) {
            throw new RMIException(e);
        } finally {
            if(socket != null && !socket.isClosed()) {
                socket.close();
            }
        }

        if(res == Skeleton.ERR_RESULT) {
            throw (Throwable) obj;
        }

        return obj;
    }

    // Equals method
    private Boolean equals(Object proxy, Method method, Object[] args) {
        // Verified attributes of equal methods
        if(args.length != 1) {
            return Boolean.FALSE;
        }

        Object obj = args[0];
        if(obj == null) {
            return Boolean.FALSE;
        }

        if(!proxy.getClass().equals(obj.getClass())) {
            return Boolean.FALSE;
        }

        InvocationHandler handler = Proxy.getInvocationHandler(obj);
        if(!(handler instanceof StubProxy)) {
            return Boolean.FALSE;
        }

        if(!Proxy.isProxyClass(obj.getClass())) {
            return Boolean.FALSE;
        }

        if(!remoteAddress.equals(((StubProxy) handler).remoteAddress)) {
            return Boolean.FALSE;
        }

        return Boolean.TRUE;
    }

    // toString Method
    @Override
    public String toString() {
        return "ClassName:" + className + " HostName: " + remoteAddress.getHostString() + " Port: " + String.valueOf(remoteAddress.getPort());
    }

    // Performs a localInvoke on methods equals or toString or hashCode
    private Object localInvoke(Object proxy, Method method, Object[] args) throws Throwable{

        // Calls toString method
        if(method.getName().equals("toString")) {
            return toString();
        }

        // return true if two proxies were created for the same skeleton
        if(method.getName().equals("equals"))  {
            return equals(proxy, method, args);
        }

        StubProxy handler = (StubProxy) Proxy.getInvocationHandler(proxy);

        // Calls hashCode method
        if(method.getName().equals("hashCode")) {
            return handler.remoteAddress.hashCode() + proxy.getClass().hashCode();
        }

        return method.invoke(handler, args);
    }

    // invoke is called on any method call to proxy object
    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        try {
            // if remoteCall then does a remoteInvoke else local invoke
            if(RMIException.inferRMI(method)) {
                return remoteInvoke(proxy, method, args);
            } else {
                return localInvoke(proxy, method, args);
            }
        } catch (Exception e) {
            throw e;
        }
    }

}


