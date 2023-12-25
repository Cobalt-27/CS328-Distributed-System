package myrmi.registry;

import myrmi.Remote;
import myrmi.exception.AlreadyBoundException;
import myrmi.exception.RemoteException;
import myrmi.exception.NotBoundException;

import java.util.HashMap;

public interface Registry extends Remote {
    static String ADDRESS = System.getenv("RMI_REGISTRY_ADDRESS") != null ? System.getenv("RMI_REGISTRY_ADDRESS") : "localhost";
    static String SERVER_ADDRESS = System.getenv("RMI_SERVER_ADDRESS") != null ? System.getenv("RMI_SERVER_ADDRESS") : "localhost";
    int REGISTRY_PORT = 11099;
    HashMap<String, Remote> bindings = new HashMap<>();
    //returns the proxy
    public Remote lookup(String name) throws RemoteException, NotBoundException;

    public void bind(String name, Remote obj) throws RemoteException, AlreadyBoundException;

    public void unbind(String name) throws RemoteException, NotBoundException;

    public void rebind(String name, Remote obj) throws RemoteException;

    public String[] list() throws RemoteException;


}
