package myrmi.tests;

import myrmi.Remote;
import myrmi.exception.RemoteException;

public interface SimpleRemoteInterface extends Remote {
    String sayHello() throws RemoteException;
}
