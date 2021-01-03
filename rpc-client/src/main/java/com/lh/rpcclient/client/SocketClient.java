package com.lh.rpcclient.client;

import com.lh.rpccore.entity.RPCRequest;
import com.lh.rpccore.entity.RPCResponse;

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
     * 发送请求.
     *
     * @param rpcRequest request
     * @param ip ip地址
     * @param port port端口
     * @return return
     * @throws IOException e
     */
    public Object invoke(final RPCRequest rpcRequest, final String ip, final int port) throws IOException {
        socket = new Socket(ip, port);
        InputStream inputStream = socket.getInputStream();
        OutputStream outputStream = socket.getOutputStream();
        ObjectOutputStream objectOutputStream;
        ObjectInputStream objectInputStream;
        RPCResponse rpcResponse = null;

        try {
            objectOutputStream = new ObjectOutputStream(outputStream);
            objectOutputStream.writeObject(rpcRequest);
            objectOutputStream.flush();
            objectInputStream = new ObjectInputStream(inputStream);
            Object result = objectInputStream.readObject();
            if (result instanceof RPCResponse) {
                rpcResponse = (RPCResponse) result;
            } else {
                throw new RuntimeException("返回参数不正确");
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } finally {
            outputStream.close();
            inputStream.close();
            socket.close();
        }

        assert rpcResponse != null;
        return rpcResponse.getObj();
    }
}
