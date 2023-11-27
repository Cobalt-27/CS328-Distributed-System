package myrmi.server;

import myrmi.Remote;
import myrmi.server.RemoteObjectRef;
import myrmi.exception.RemoteException;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

public class UnicastRemoteObject implements Remote, java.io.Serializable {
    int port;

    public static Remote exportObject(Remote obj, int port) throws RemoteException {
        return exportObject(obj,
                new RemoteObjectRef("localhost", port, obj.hashCode(), obj.getClass().getInterfaces()[0].getName()));
    }

    /**
     * 1. create a skeleton of the given object ''obj'' and bind with the address ''host:port''
     * 2. return a stub of the object ( Util.createStub() )
     **/
    public static Remote exportObject(Remote obj, RemoteObjectRef ref) throws RemoteException {
        // TODO: finish here

        // Start the skeleton in a new thread
        Skeleton skeleton = new Skeleton(obj, ref);
        skeleton.start();
        
        ref.setPort(skeleton.getPort());

        // Sleep for 1 second
        try{
            Thread.sleep(1000);
        }catch(InterruptedException e){
            System.out.println("Interrupted");
        }
        
        ref.setPort(skeleton.getPort());
        // Create and return a stub for the remote object
        return Util.createStub(ref);
    }
}
