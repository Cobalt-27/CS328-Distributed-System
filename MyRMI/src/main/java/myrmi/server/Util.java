package myrmi.server;

import myrmi.Remote;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;

public class Util {

    public static Remote createStub(RemoteObjectRef ref) {
        try {
            // ref.interfaceName is the interface name of the remote object
            String interfaceName = ref.getInterfaceName();
            Class<?> interfaceClass = Class.forName(interfaceName);
            InvocationHandler handler = new StubInvocationHandler(ref);
            return (Remote) Proxy.newProxyInstance(
                    interfaceClass.getClassLoader(),
                    new Class<?>[]{interfaceClass},
                    handler);
        } catch (ClassNotFoundException e) {
            // Handle the exception here
            e.printStackTrace();
            return null; // Or throw a custom exception, log the error, etc.
        }
    }


}
