package myrmi.server;

import myrmi.Remote;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;

public class Skeleton extends Thread {
    static final int BACKLOG = 5;
    private Remote remoteObj;

    private String host;
    private int port;
    private int objectKey;

    public int getPort() {
        return port;
    }

    public Skeleton(Remote remoteObj, RemoteObjectRef ref) {
        this(remoteObj, ref.getHost(), ref.getPort(), ref.getObjectKey());
    }

    public Skeleton(Remote remoteObj, String host, int port, int objectKey) {
        super();
        this.remoteObj = remoteObj;
        this.host = host;
        this.port = port;
        this.objectKey = objectKey;
        this.setDaemon(false);
    }

    @Override
    public void run() {
        /*TODO: implement method here
         * You need to:
         * 1. create a server socket to listen for incoming connections
         * 2. use a handler thread to process each request (use SkeletonReqHandler)
         *  */

        try (ServerSocket serverSocket = new ServerSocket(port, BACKLOG, InetAddress.getByName(host))) {
            port=serverSocket.getLocalPort();
            System.out.println("Skeleton running on port " + port);

            while (!Thread.interrupted()) {
                try {
                    // Accept an incoming connection
                    Socket clientSocket = serverSocket.accept();

                    // Create a handler for the request and start it in a new thread
                    SkeletonReqHandler handler = new SkeletonReqHandler(clientSocket, remoteObj, objectKey);
                    new Thread(handler).start();
                } catch (SocketException e) {
                    System.out.println("Server Socket closed");
                } catch (IOException e) {
                    System.out.println("I/O error occurred when waiting for a connection");
                }
            }
        } catch (IOException e) {
            System.out.println("Could not listen on port " + port);
        }
        throw new NotImplementedException();

    }
}
