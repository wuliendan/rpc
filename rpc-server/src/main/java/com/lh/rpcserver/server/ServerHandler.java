package com.lh.rpcserver.server;

import com.lh.rpccore.entity.RPCRequest;
import com.lh.rpccore.entity.RPCResponse;
import com.lh.rpccore.entity.RPCResponseCodeEnum;
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
public class ServerHandler implements Runnable {

    public static final Map<String, Class<?>> SERVICEREGISTRY = new HashMap<>();

    /**
     * 客户端发来的 socket.
     */
    private final Socket socketClient;

    private final RPCResponse rpcResponse = new RPCResponse();

    public ServerHandler(final Socket socketClient) {
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
    @Override
    public void run() {
        ObjectInputStream objectInputStream = null;
        ObjectOutputStream objectOutputStream = null;

        try {
            //定义输出流
            OutputStream outputStream = socketClient.getOutputStream();

            //1.建立好连接后，从socket中获取输入流，并建立缓冲区进行读取
            InputStream inputStream = socketClient.getInputStream();

            objectInputStream = new ObjectInputStream(inputStream);
            objectOutputStream = new ObjectOutputStream(outputStream);

            //2.获取请求数据，强转参数类型
            Object param = objectInputStream.readObject();
            RPCRequest rpcRequest = null;
            if (!(param instanceof RPCRequest)) {
                //将结果通过 socket 输出到 client 端
                rpcResponse.setCode(RPCResponseCodeEnum.FAIL.getCode());
                rpcResponse.setMessage(RPCResponseCodeEnum.FAIL.getMessage());
                objectOutputStream.writeObject(rpcResponse);
                objectOutputStream.flush();
                return;
            } else {
                rpcRequest = (RPCRequest) param;
            }

            //3.查找并执行服务方法
            log.info("要执行的类型为：" + rpcRequest.getClassName());
            Class<?> service = SERVICEREGISTRY.get(rpcRequest.getClassName());
            if (service != null) {
                Method method = service.getMethod(rpcRequest.getMethodName(), rpcRequest.getParamTypes());
                Object result = method.invoke(service.newInstance(), rpcRequest.getParams());
                //4.得到结果并返回
                rpcResponse.setRequestId(rpcRequest.getRequestId());
                rpcResponse.setObj(result);
                rpcResponse.setCode(RPCResponseCodeEnum.SUCCESS.getCode());
                rpcResponse.setMessage(RPCResponseCodeEnum.SUCCESS.getMessage());
            }

            //将结果通过 socket 输出到 client 端
            objectOutputStream.writeObject(rpcResponse);
            objectOutputStream.flush();
            outputStream.close();
            inputStream.close();
            socketClient.close();
        } catch (IOException | ClassNotFoundException | NoSuchMethodException | IllegalAccessException | InvocationTargetException | InstantiationException e) {
            e.printStackTrace();
        } finally {
            closeOpenSource(objectInputStream, objectOutputStream);
        }
    }

    private void closeOpenSource(final ObjectInputStream objectInputStream, final ObjectOutputStream objectOutputStream) {
        if (objectInputStream != null) {
            try {
                objectInputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        if (objectOutputStream != null) {
            try {
                objectOutputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        if (socketClient != null) {
            try {
                socketClient.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 注册服务.
     *
     * @param server key
     * @param imp value
     */
    public static void registerServer(final Class<?> server, final Class<?> imp) {
        SERVICEREGISTRY.put(server.getName(), imp);
    }
}
