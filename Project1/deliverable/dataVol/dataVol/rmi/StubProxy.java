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

class StubProxy implements InvocationHandler, Serializable {

    private InetSocketAddress remoteAddress;
    private String className;


    public StubProxy(InetSocketAddress address, String cName) {
        this.remoteAddress = address;
        this.className = cName;
    }

    /**
     *
     */
    private static final long serialVersionUID = -713214545986L;

    @SuppressWarnings("resource")
    private Object remoteInvoke(Object proxy, Method method, Object[] args) throws Throwable {

        Socket socket   = new Socket();
        Integer res     = null;
        Object obj      = null;

        try {
            socket = new Socket();
            socket.connect(remoteAddress);

            ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
            out.flush();

            ObjectInputStream in = new ObjectInputStream(socket.getInputStream());

            out.writeObject(method.getDeclaringClass().getName());
            out.writeObject(method.getName());
            out.writeObject(method.getParameterTypes());
            out.writeObject(args);
            out.flush();

            res     = (Integer) in.readObject();
            obj     = in.readObject();

            socket.close();
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

    private Boolean equals(Object proxy, Method method, Object[] args) {
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

    @Override
    public String toString() {
        return "ClassName:" + className + " HostName: " + remoteAddress.getHostString() + " Port: " + String.valueOf(remoteAddress.getPort());
    }

    private Object localInvoke(Object proxy, Method method, Object[] args) throws Throwable{

        if(method.getName().equals("toString")) {
            return toString();
        }

        // return true if two proxies were created for the same skeleton
        if(method.getName().equals("equals"))  {
            return equals(proxy, method, args);
        }

        StubProxy handler = (StubProxy) Proxy.getInvocationHandler(proxy);

        if(method.getName().equals("hashCode")) {
            return handler.remoteAddress.hashCode() + proxy.getClass().hashCode();
        }

        return method.invoke(handler, args);
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        try {
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


