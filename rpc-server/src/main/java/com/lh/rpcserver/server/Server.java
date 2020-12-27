package com.lh.rpcserver.server;

import com.lh.rpcserver.service.StudentService;
import com.lh.rpcserver.service.impl.StudentServiceImpl;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class Server {

    private ServerSocket serverSocket;

    private int serverPort;

    public Server(final int serverPort) throws IOException {
        this.serverPort = serverPort;
        serverSocket = new ServerSocket(this.serverPort);
    }

    /**
     * server start.
     */
    public void start() {
        ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(5, 10, 200, TimeUnit.SECONDS,
                new ArrayBlockingQueue<Runnable>(10));
        while (true) {
            try {
                Socket socket = serverSocket.accept();
                ServerService serverService = new ServerService(socket);
                serverService.registerServer(StudentService.class, StudentServiceImpl.class);
                threadPoolExecutor.execute(serverService);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
