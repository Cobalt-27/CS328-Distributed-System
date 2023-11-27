package myrmi.server;

import myrmi.exception.RemoteException;
import myrmi.network.InvokeMessage;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.net.InetSocketAddress;
import java.net.Socket;

public class StubInvocationHandler implements InvocationHandler, Serializable {
    private String host;
    private int port;
    private int objectKey;

    public StubInvocationHandler(String host, int port, int objectKey) {
        this.host = host;
        this.port = port;
        this.objectKey = objectKey;
        System.out.printf("Stub created to %s:%d, object key = %d\n", host, port, objectKey);
    }

    public StubInvocationHandler(RemoteObjectRef ref) {
        this(ref.getHost(), ref.getPort(), ref.getObjectKey());
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args)
            throws RemoteException, IOException, ClassNotFoundException, Throwable {
        // Open a socket to the skeleton
        try (Socket socket = new Socket()) {
            socket.connect(new InetSocketAddress(host, port));

            InvokeMessage invokeMessage = new InvokeMessage(objectKey, method.getName(), method.getParameterTypes(),
                    args);

            try (ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
                    ObjectInputStream in = new ObjectInputStream(socket.getInputStream())) {

                out.writeObject(invokeMessage);
                out.flush();

                Object result = in.readObject();

                if (result instanceof RemoteException) {
                    throw (RemoteException) result;
                }
                if (result instanceof ClassNotFoundException) {
                    throw (ClassNotFoundException) result;
                }
                if (result instanceof Throwable) {
                    throw (Throwable) result;
                }

                return result;
            } catch (IOException e) {
                throw e;
            }
        } catch (IOException e) {
            throw e;
        }
    }

}
