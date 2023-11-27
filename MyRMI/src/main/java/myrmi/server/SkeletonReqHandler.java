package myrmi.server;

import myrmi.Remote;
import myrmi.network.InvokeMessage;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.Socket;

public class SkeletonReqHandler extends Thread {
    private Socket socket;
    private Remote obj;
    private int objectKey;

    public SkeletonReqHandler(Socket socket, Remote remoteObj, int objectKey) {
        this.socket = socket;
        this.obj = remoteObj;
        this.objectKey = objectKey;
    }

    @Override
    public void run() {
        /*
         * TODO: implement method here
         * You need to:
         * 1. handle requests from stub, receive invocation arguments, deserialization
         * 2. get result by calling the real object, and handle different cases
         * (non-void method, void method, method throws exception, exception in
         * invocation process)
         * Hint: you can use an int to represent the cases: -1 invocation error, 0
         * exception thrown, 1 void method, 2 non-void method
         *
         */
        try (ObjectInputStream in = new ObjectInputStream(socket.getInputStream());
                ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream())) {

            // Read the InvokeMessage from the client
            InvokeMessage invokeMessage = (InvokeMessage) in.readObject();

            if (invokeMessage.getObjectKey() != objectKey) {
                throw new IllegalArgumentException("Object key mismatch");
            }

            try {
                // Attempt to find and invoke the method on the remote object
                Method method = obj.getClass().getMethod(invokeMessage.getMethodName(),
                        invokeMessage.getParameterTypes());
                Object result = method.invoke(obj, invokeMessage.getArgs());
                out.writeObject(result);
            } catch (Exception e) {
                out.writeObject(e);
            }
            out.flush();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                socket.close();
            } catch (IOException e) {
                // Log and handle the exception as appropriate for your application
                e.printStackTrace();
            }
        }

    }
}
