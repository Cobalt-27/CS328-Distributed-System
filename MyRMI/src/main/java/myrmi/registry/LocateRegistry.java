package myrmi.registry;

import myrmi.Remote;
import myrmi.exception.RemoteException;
import myrmi.server.RemoteObjectRef;
import myrmi.server.StubInvocationHandler;
import myrmi.server.Util;
import java.lang.reflect.Proxy;

public class LocateRegistry {
    public static Registry getRegistry() {
        return getRegistry(Registry.ADDRESS, Registry.REGISTRY_PORT);
    }

    /**
     * returns a stub of remote registry
     */
    public static Registry getRegistry(String host, int port) {
        if (port <= 0) {
            port = Registry.REGISTRY_PORT;
        }
        if (host == null || host.length() == 0) {
            try {
                host = java.net.InetAddress.getLocalHost().getHostAddress();
            } catch (Exception e) {
                host = "";
            }
        }
        Remote stub = (Remote) Util.createStub(new RemoteObjectRef(host, port, 0, Registry.class.getName()));

        return (Registry) stub;
    }

    public static Registry createRegistry() throws RemoteException {
        return createRegistry(Registry.REGISTRY_PORT);
    }

    /**
     * create a registry locally
     * but we still need to wrap around the lookup() method
     */
    public static Registry createRegistry(int port) throws RemoteException {
        //TODO: Notice here the registry can only bind to 127.0.0.1, can you extend that?
        if (port == 0) {
            port = Registry.REGISTRY_PORT;
        }
        return new RegistryImpl(port);
    }
}
