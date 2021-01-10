package com.lh.rpcserver.server;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

@Slf4j
public class Server {

    private ServerSocket serverSocket;

    /**
     * 端口号.
     */
    private final int serverPort;

    public Server(final int serverPort) {
        this.serverPort = serverPort;
    }

    /**
     * 1.服务注册.
     *
     * @param serviceInterface service接口
     * @param impClass 实现类
     * @return server
     */
    public Server register(final Class<?> serviceInterface, final Class<?> impClass) {
        ServerHandler.registerServer(serviceInterface, impClass);
        return this;
    }

    /**
     * 2.服务启动发布（启动）.
     */
    public void start() {
        log.info("服务启动====");

        // 创建一个线程池
        ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(5, 10, 200, TimeUnit.SECONDS,
                new ArrayBlockingQueue<Runnable>(10));

        //3.通过循环不断接收请求
        while (true) {
            try {
                serverSocket = new ServerSocket(serverPort);

                // 监听客户端的请求
                Socket socket = serverSocket.accept();
                //4、每一个socket交给一个ServerHandler处理，这里的target就是真正的业务类
                //处理客户端的请求
                threadPoolExecutor.execute(new ServerHandler(socket));
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (serverSocket != null) {
                    try {
                        serverSocket.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }
}
