package myrmi.server;

import myrmi.Remote;
import myrmi.server.RemoteObjectRef;
import myrmi.exception.RemoteException;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

public class UnicastRemoteObject implements Remote, java.io.Serializable {
    int port;

    protected UnicastRemoteObject() throws RemoteException {
        this(0);
    }

    protected UnicastRemoteObject(int port) throws RemoteException {
        this.port = port;
        exportObject(this, port);
    }

    public static Remote exportObject(Remote obj, int port) throws RemoteException {
        return exportObject(obj, "localhost", port);
    }

    /**
     * 1. create a skeleton of the given object ''obj'' and bind with the address ''host:port''
     * 2. return a stub of the object ( Util.createStub() )
     **/
    public static Remote exportObject(Remote obj, String host, int port) throws RemoteException {
        // TODO: finish here
        int objectKey = obj.hashCode();

        // Create a RemoteObjectRef for this object
        RemoteObjectRef ref = new RemoteObjectRef(host, port, objectKey, obj.getClass().getName());

        // Start the skeleton in a new thread
        Skeleton skeleton = new Skeleton(obj, ref);
        skeleton.start();

        // Create and return a stub for the remote object
        return Util.createStub(ref);
    }
}
