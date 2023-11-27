package myrmi.tests;

import myrmi.SimpleRemoteInterface;
import myrmi.server.RemoteObjectRef;
import myrmi.server.UnicastRemoteObject;
import myrmi.server.Util;

public class RMITest {

    public static void main(String[] args) {
        try {
            // Simulate server side
            String host = "localhost";
            int port = 1099;
            SimpleRemoteInterface remoteObj = new SimpleRemoteObject();
            UnicastRemoteObject.exportObject(remoteObj, host, port);

            // Simulate client side
            RemoteObjectRef ref = new RemoteObjectRef(host, port, remoteObj.hashCode(), SimpleRemoteInterface.class.getName());
            SimpleRemoteInterface stub = (SimpleRemoteInterface) Util.createStub(ref);
            System.out.println(stub.sayHello());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
