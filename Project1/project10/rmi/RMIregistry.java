package rmi;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.HashMap;

   class NativeRMI  implements RMIinterface{
//      private HashMap < String, Class > lClass = new HashMap < String, Class > ();
      private HashMap < String, InetSocketAddress > lIP = new HashMap < String, InetSocketAddress > ();


//      public <T> Class<T> lookupClass(String key) throws RMIException {
//            if (lClass.containsKey(key) != false) {
//                System.out.format("LookUp Successful for %s",key);
//                return lClass.get(key);
//            }
//            throw new RMIException("From lookClass");
//      }

      public InetSocketAddress lookupIP(String key) throws RMIException {
            if (lIP.containsKey(key) != false) {
                System.out.format("LookUp Successful for %s",key);
                return lIP.get(key);
            }
            throw new RMIException("From LookupIP");
      }
      public <T> void bind(String key,InetSocketAddress add) { // Address is of remote Server
            lIP.put(key,add);
//            lClass.put(key,c);
           System.out.format("Bound Successful for %s",key);
      }
   }



public class RMIregistry {
   public static void main(String agrs[]) throws RMIException{
       RMIinterface n = new NativeRMI();
       Skeleton s = new Skeleton(RMIinterface.class, n, new InetSocketAddress(7001));
       try {
           System.out.println("Starting RMI registry");
           s.start();
       }
       catch (Exception e) {
            throw new RMIException(e);
       }
   }
}
