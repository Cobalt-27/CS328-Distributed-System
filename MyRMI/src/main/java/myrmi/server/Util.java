package myrmi.server;

import myrmi.Remote;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;

public class Util {

    public static Remote createStub(RemoteObjectRef ref) {
        //TODO: finish here, instantiate an StubInvocationHandler for ref and then return a stub
        InvocationHandler handler = new StubInvocationHandler(ref);
        return (Remote) Proxy.newProxyInstance(
                Remote.class.getClassLoader(),
                new Class<?>[]{Remote.class},
                handler);
    }


}
