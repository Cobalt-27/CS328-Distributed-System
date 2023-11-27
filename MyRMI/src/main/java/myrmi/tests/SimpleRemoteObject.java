package myrmi.tests;

import myrmi.exception.RemoteException;
import myrmi.server.UnicastRemoteObject;

public class SimpleRemoteObject extends UnicastRemoteObject implements SimpleRemoteInterface {
    public SimpleRemoteObject() throws RemoteException {
        super();
    }

    @Override
    public String sayHello() {
        return "Hello from the remote object!";
    }
}
