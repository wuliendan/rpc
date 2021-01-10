package com.lh.rpcclient.client;

import com.lh.rpccore.entity.RPCRequest;
import com.lh.rpccore.entity.RPCResponse;
import com.lh.rpccore.exception.RequestTimeoutException;
import com.lh.rpccore.serializer.KryoSerializer;
import com.lh.rpccore.util.Object2Array;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectOutputStream;
import java.io.ObjectInputStream;
import java.io.OutputStream;
import java.net.Socket;

public class SocketClient {
    private Socket socket;

    public SocketClient() {
    }

    /**
     * 使用 JDK 自带的序列化与反序列化.
     *
     * @param rpcRequest request
     * @param ip ip地址
     * @param port port端口
     * @return return
     * @throws IOException e
     */
    public Object invoke(final RPCRequest rpcRequest, final String ip, final int port) throws IOException {
        InputStream inputStream = socket.getInputStream();
        OutputStream outputStream = socket.getOutputStream();
        ObjectOutputStream objectOutputStream;
        ObjectInputStream objectInputStream;
        RPCResponse rpcResponse = new RPCResponse();

        try {
            //1.创建socket客户端，根据指定地址选择远程服务提供者
            socket = new Socket(ip, port);
            //2.将远程服务调用所需的接口类，方法名，参数列表等编码后可以发送给服务端提供者
            objectOutputStream = new ObjectOutputStream(outputStream);
            objectOutputStream.writeObject(rpcRequest);
            objectOutputStream.flush();
            //3.同步阻塞等待服务器返回应答，获取应答后返回
            objectInputStream = new ObjectInputStream(inputStream);
            Object result = objectInputStream.readObject();
            if (result instanceof RPCResponse) {
                rpcResponse = (RPCResponse) result;
            } else {
                throw new RequestTimeoutException("返回参数不正确");
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } finally {
            outputStream.close();
            inputStream.close();
            socket.close();
        }

        return rpcResponse.getObj();
    }

    /**
     * 使用 Kryo 自带的序列化与反序列化.
     * @param rpcRequest 接收信息
     * @param ip ip地址
     * @param port 端口
     * @return object
     * @throws IOException e
     */
    public Object invokeForKryo(final RPCRequest rpcRequest, final String ip, final int port) throws IOException {
        socket = new Socket(ip, port);
        RPCResponse rpcResponse = new RPCResponse();
        KryoSerializer kryoSerializer = new KryoSerializer();

        try (InputStream inputStream = socket.getInputStream();
             OutputStream outputStream = socket.getOutputStream()) {
            byte[] bytes = kryoSerializer.serialize(inputStream);
            Object result = Object2Array.byteArrayToObject(bytes);
            if (result instanceof RPCResponse) {
                rpcResponse = (RPCResponse) result;
            } else {
                throw new RequestTimeoutException("返回参数不正确");
            }
        } finally {
            socket.close();
        }

        return rpcResponse.getObj();
    }
}
