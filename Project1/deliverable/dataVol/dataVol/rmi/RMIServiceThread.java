package rmi;

import java.lang.reflect.Method;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.InvocationTargetException;


public class RMIServiceThread<T> extends Thread {
        private Socket clientSocket;
		private T server;
		private Class<T> interfaceClassObject;
        private Skeleton<T> skeleton;

        public RMIServiceThread(Class<T> c, T server, Socket clientSocket,Skeleton<T> s) {
            this.clientSocket = clientSocket;
			this.server = server;
			this.interfaceClassObject = c;
            this.skeleton = s;
        }

        private Boolean validInterface(Class<T> c,String mClassName) {
            Class[] superInterfaces = c.getInterfaces();

            if (mClassName.equals(c.getName()))
                return true;
            for (Class cc : superInterfaces) {
                if (mClassName.equals(cc.getName()))
                    return true;
            }
            return false;
        }
        /**
         * Handle remote client request.
         */
        private synchronized void waitForClient() {
            ObjectOutputStream out = null;

            try {
                out = new ObjectOutputStream(clientSocket.getOutputStream());
                out.flush();
                ObjectInputStream in = new ObjectInputStream(clientSocket.getInputStream());

                // prepare for method invoke
                String interfaceName = (String) in.readObject();
                String methodName             = (String) in.readObject();
                @SuppressWarnings("rawtypes")
                Class[] parameterTypesList  = (Class[]) in.readObject();
                Object[] parameterValues           = (Object[]) in.readObject();

                // Method Check
                if (!validInterface(interfaceClassObject,interfaceName)){
                    out.writeObject(Skeleton.ERR_RESULT);
                    out.writeObject(new RMIException("Interface method mismatch"));
                    clientSocket.close();
                    return;
                }

                // invoke method on server
                Method method = interfaceClassObject.getMethod(methodName, parameterTypesList);
                Object result = method.invoke(server, parameterValues);

                // write the result back to client
                out.writeObject(Skeleton.RESULT_OK);
                out.writeObject(result);

                clientSocket.close();
            } catch (InvocationTargetException e) {
                try {
                    out.writeObject(Skeleton.ERR_RESULT);
                    out.writeObject(e.getCause());
                } catch (IOException e1) {
                    e1.printStackTrace();
                }

            } catch (Exception e) {
                try {
                    skeleton.service_error(new RMIException("ServiceError"));
                    out.writeObject(Skeleton.ERR_RESULT);
                    out.writeObject(e);
                } catch (IOException e1) {
                    // ignore
                }
            } finally {
                // TODO
                try {
                    if(clientSocket != null && !clientSocket.isClosed()) {
                        clientSocket.close();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        @Override
        public synchronized void run() {
            waitForClient();
        }
    }
