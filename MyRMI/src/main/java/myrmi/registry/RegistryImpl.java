package myrmi.registry;

import myrmi.Remote;
import myrmi.exception.AlreadyBoundException;
import myrmi.exception.NotBoundException;
import myrmi.exception.RemoteException;
import myrmi.server.Skeleton;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.net.InetAddress;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Set;

public class RegistryImpl implements Registry {
    private final HashMap<String, Remote> bindings = new HashMap<>();

    /**
     * Construct a new RegistryImpl
     * and create a skeleton on given port
     **/
    public RegistryImpl(int port) throws RemoteException {
        Skeleton skeleton = new Skeleton(this, Registry.ADDRESS, port, 0);
        skeleton.start();
    }


    @Override
    public Remote lookup(String name) throws RemoteException, NotBoundException {
        System.out.printf("RegistryImpl: lookup(%s)\n", name);
        Remote obj = bindings.get(name);
        if (obj == null) {
            throw new NotBoundException("No object bound with name: " + name);
        }
        return obj;
    }
    

    @Override
    public void bind(String name, Remote obj) throws RemoteException, AlreadyBoundException {
        System.out.printf("RegistryImpl: bind(%s)\n", name);
        if (bindings.containsKey(name)) {
            throw new AlreadyBoundException("Object already bound with name: " + name);
        }
        bindings.put(name, obj);
    }
    

    @Override
    public void unbind(String name) throws RemoteException, NotBoundException {
        System.out.printf("RegistryImpl: unbind(%s)\n", name);
        if (!bindings.containsKey(name)) {
            throw new NotBoundException("No object bound with name: " + name);
        }
        bindings.remove(name);
    }
    

    @Override
    public void rebind(String name, Remote obj) throws RemoteException {
        System.out.printf("RegistryImpl: rebind(%s)\n", name);
        bindings.put(name, obj);
    }
    

    @Override
    public String[] list() throws RemoteException {
        Set<String> keys = bindings.keySet();
        return keys.toArray(new String[0]);
    }
    

    public static void main(String args[]) {
        final int regPort = (args.length >= 1) ? Integer.parseInt(args[0])
                : Registry.REGISTRY_PORT;
        RegistryImpl registry;
        try {
            registry = new RegistryImpl(regPort);
        } catch (RemoteException e) {
            System.exit(1);
        }

        System.out.printf("RMI Registry is listening on port %d\n", regPort);

    }
}
