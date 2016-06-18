package rmi;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.HashMap;

public interface RMIinterface {
//    public <T> Class<T> lookupClass(String key) throws RMIException;
    public InetSocketAddress lookupIP(String key) throws RMIException ;
    public <T> void bind(String key,InetSocketAddress add) throws RMIException;
}
