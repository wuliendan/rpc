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
     * @param RPCRequest request
     * @param ip ip地址
     * @param port port端口
     * @return return
     * @throws IOException e
     */
    public Object invoke(final RPCRequest RPCRequest, final String ip, final int port) throws IOException {
        socket = new Socket(ip, port);
        InputStream inputStream = socket.getInputStream();
        OutputStream outputStream = socket.getOutputStream();
        ObjectOutputStream objectOutputStream;
        ObjectInputStream objectInputStream;
        RPCResponse RPCResponse = null;

        try {
            objectOutputStream = new ObjectOutputStream(outputStream);
            objectOutputStream.writeObject(RPCRequest);
            objectOutputStream.flush();
            objectInputStream = new ObjectInputStream(inputStream);
            Object result = objectInputStream.readObject();
            if (result instanceof RPCResponse) {
                RPCResponse = (RPCResponse) result;
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

        assert RPCResponse != null;
        return RPCResponse.getObj();
    }
}
