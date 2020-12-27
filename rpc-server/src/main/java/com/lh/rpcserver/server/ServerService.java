package com.lh.rpcserver.server;

import com.lh.rpccore.entity.Request;
import com.lh.rpccore.entity.Response;
import lombok.extern.slf4j.Slf4j;

import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

/**
 * 接收客户端连接类.
 */
@Slf4j
public class ServerService implements Runnable {

    private final Socket socketClient;

    private final Map<String, Class<?>> serviceRegistry = new HashMap<>();

    private final Response response = new Response();

    public ServerService(final Socket socketClient) {
        this.socketClient = socketClient;
    }

    /**
     * When an object implementing interface <code>Runnable</code> is used
     * to create a thread, starting the thread causes the object's
     * <code>run</code> method to be called in that separately executing
     * thread.
     * The general contract of the method <code>run</code> is that it may
     * take any action whatsoever.
     *
     * @see Thread#run()
     */
    @SuppressWarnings("checkstyle:SingleSpaceSeparator")
    @Override
    public void run() {
        try {
            OutputStream outputStream = socketClient.getOutputStream();

            //1.建立好连接后，从socket中获取输入流，并建立缓冲区进行读取
            InputStream inputStream = socketClient.getInputStream();

            ObjectInputStream objectInputStream = new ObjectInputStream(inputStream);
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(outputStream);

            //2.获取请求数据，强转参数类型
            Object param = objectInputStream.readObject();
            Request request = null;
            if (!(param instanceof Request)) {
                response.setMessage("参数错误");
                objectOutputStream.writeObject(response);
                objectOutputStream.flush();
                return;
            } else {
                request = (Request) param;
            }

            //3.查找并执行服务方法
            log.info("要执行的类型为：" + request.getClassName());
            Class<?> service = serviceRegistry.get(request.getClassName());
            if (service != null) {
                Method method = service.getMethod(request.getMethodName(), request.getParamTypes());
                Object result = method.invoke(service.newInstance(), request.getParams());
                //4.得到结果并返回
                response.setObj(result);
            }
            objectOutputStream.writeObject(response);
            objectOutputStream.flush();
            outputStream.close();
            inputStream.close();
            socketClient.close();
        } catch (IOException | ClassNotFoundException | NoSuchMethodException | IllegalAccessException | InvocationTargetException | InstantiationException e) {
            e.printStackTrace();
        }
    }

    /**
     * register server.
     *
     * @param server key
     * @param imp value
     */
    public void registerServer(final Class server, final Class imp) {
        this.serviceRegistry.put(server.getName(), imp);
    }
}
