package myrmi.tests;


import myrmi.server.*;
import myrmi.registry.*;
import myrmi.Remote;



public class RMITest {

    public static void main(String[] args) {
        try {
            // Simulate server side
            Registry serverRegistry = LocateRegistry.createRegistry(0);
            SimpleRemoteInterface remoteObj = new SimpleRemoteObject();
            Remote serverStub1=UnicastRemoteObject.exportObject(remoteObj,0);
            serverRegistry.bind("test", serverStub1);

            // Sleep for a second
            Thread.sleep(1000);

            // Simulate client side
            
            Registry clientRegistry = LocateRegistry.getRegistry();
            SimpleRemoteInterface stub = (SimpleRemoteInterface) clientRegistry.lookup("test");
            System.out.println(stub.sayHello());
            

            // Exit the program
            System.exit(0);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
