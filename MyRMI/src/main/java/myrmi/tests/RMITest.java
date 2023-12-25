package myrmi.tests;

import java.util.Scanner;

import myrmi.Remote;
import myrmi.server.*;
import myrmi.registry.*;
import myrmi.exception.*;

public class RMITest {

    // Internal Remote Interface 1
    public interface RemoteInterface1 extends Remote {
        String getMessage() throws RemoteException;
    }

    // Internal Remote Object 1
    public static class RemoteObject1 extends UnicastRemoteObject implements RemoteInterface1 {
        public RemoteObject1() throws RemoteException {
            super();
        }

        @Override
        public String getMessage() {
            return "Hello from Object 1";
        }
    }

    // Internal Remote Interface 2
    public interface RemoteInterface2 extends Remote {
        String getResponse() throws RemoteException;
    }

    // Internal Remote Object 2
    public static class RemoteObject2 extends UnicastRemoteObject implements RemoteInterface2 {
        public RemoteObject2() throws RemoteException {
            super();
        }

        @Override
        public String getResponse() {
            return "Hello from Object 2";
        }
    }

    // Internal Remote Interface for Counter Service
    public interface CounterService extends Remote {
        void increment() throws RemoteException;

        void decrement() throws RemoteException;

        int getCount() throws RemoteException;
    }

    // Internal Remote Object for Counter Service
    public static class RemoteCounterService extends UnicastRemoteObject implements CounterService {
        private int count;

        public RemoteCounterService() throws RemoteException {
            super();
        }

        @Override
        public void increment() {
            count++;
        }

        @Override
        public void decrement() {
            count--;
        }

        @Override
        public int getCount() {
            return count;
        }
    }

    // Test for Simple Remote Invocation
    private static void testSimpleRemoteInvocation(Registry registry) throws Exception {

        RemoteInterface1 clientStub = (RemoteInterface1) registry.lookup("Object1");
        String response = clientStub.getMessage();
        if ("Hello from Object 1".equals(response)) {
            System.out.println("Test Simple Invocation PASS");
        } else {
            System.out.println("Test Simple Invocation FAILED: Received - " + response);
        }
    }

    // // Test for Rebinding
    // private static void testRebinding(Registry registry) throws Exception {
    // RemoteInterface1 newRemoteObj = new RemoteObject1();
    // Remote newStub = UnicastRemoteObject.exportObject(newRemoteObj,
    // Registry.SERVER_ADDRESS, 0);
    // registry.rebind("Object1", newStub);

    // RemoteInterface1 clientStub = (RemoteInterface1) registry.lookup("Object1");
    // String response = clientStub.getMessage();
    // if ("Hello from Object 1".equals(response)) {
    // System.out.println("Test Rebinding PASS");
    // } else {
    // System.out.println("Test Rebinding FAILED: Received - " + response);
    // }
    // }

    // // Test for Unbinding
    // private static void testUnbinding(Registry registry) throws Exception {
    // registry.unbind("Object1");
    // try {
    // RemoteInterface1 clientStub = (RemoteInterface1) registry.lookup("Object1");
    // System.out.println("Test Unbinding FAILED: Lookup should have failed");
    // } catch (Exception e) {
    // System.out.println("Test Unbinding PASS");
    // }
    // }

    private static void testAnotherSimpleRemoteInvocation(Registry registry) throws Exception {

        RemoteInterface2 stub = (RemoteInterface2) registry.lookup("Object2");
        if ("Hello from Object 2".equals(stub.getResponse())) {
            System.out.println("Test Object 2 PASS");
        } else {
            System.out.println("Test Object 2 FAILED: Incorrect response");
        }
    }

    private static void testCounterService(Registry registry) throws Exception {

        CounterService stub = (CounterService) registry.lookup("CounterService");

        // Performing multiple calls to change state
        stub.increment();
        stub.increment();
        stub.decrement();

        if (stub.getCount() == 1) {
            System.out.println("Test Counter Service PASS");
        } else {
            System.out.println("Test Counter Service FAILED: Incorrect count");
        }
    }

    private static void waitForStop() {
        // Scanner scanner = new Scanner(System.in);
        // scanner.nextLine();
        // scanner.close();
        try {
            while (true) {

                Thread.sleep(100); // Sleep for 1 second

            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private static void setupObjects(Registry registry) throws Exception {
        RemoteInterface1 remoteObj = new RemoteObject1();
        Remote stub = UnicastRemoteObject.exportObject(remoteObj, Registry.SERVER_ADDRESS, 8001);
        registry.bind("Object1", stub);

        RemoteInterface2 remoteObj2 = new RemoteObject2();
        Remote stub2 = UnicastRemoteObject.exportObject(remoteObj2, Registry.SERVER_ADDRESS, 8002);
        registry.bind("Object2", stub2);

        CounterService counterService = new RemoteCounterService();
        Remote counterStub = UnicastRemoteObject.exportObject(counterService, Registry.SERVER_ADDRESS, 8003);
        registry.bind("CounterService", counterStub);
    }

    public static void main(String[] args) {
        try {
            // int port=0;

            // Initialize registry
            int role = -1;
            if (args.length > 0) {
                role = Integer.parseInt(args[0]);
            }
            int roleRegistry = 0;
            int roleServer = 1;
            int roleClient = 2;
            Registry registry;
            if (role == roleRegistry) {
                System.out.println("Starting registry");
                registry = LocateRegistry.createRegistry();
                waitForStop();
                System.exit(0);
            } else if (role == roleServer) {
                Thread.sleep(1000);
                System.out.println("Starting server");
                registry = LocateRegistry.getRegistry();
                setupObjects(registry);
                waitForStop();
                System.exit(0);
            } else if (role == roleClient) {
                System.out.println("Starting client");
                Thread.sleep(2000);
                registry = LocateRegistry.getRegistry();

                testSimpleRemoteInvocation(registry);
                Thread.sleep(1000); // Sleep between tests

                // Test Remote Object 2
                testAnotherSimpleRemoteInvocation(registry);
                Thread.sleep(1000);

                // testRebinding(registry);
                // Thread.sleep(1000); // Sleep between tests

                // testUnbinding(registry);
                // Thread.sleep(1000); // Sleep between tests

                // Test Counter Service
                testCounterService(registry);
                // Exit the program
                System.exit(0);
            } else {
                System.out.println("Invalid role");
                System.exit(0);
            }
        } catch (NumberFormatException e) {
            System.err.println("Role must be an integer");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
