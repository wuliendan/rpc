package com.lh.rpcclient.client;

import com.lh.rpccore.entity.Request;
import com.lh.rpccore.entity.Response;

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
     * @param request request
     * @param ip ip地址
     * @param port port端口
     * @return return
     * @throws IOException e
     */
    public Object invoke(final Request request, final String ip, final int port) throws IOException {
        socket = new Socket(ip, port);
        InputStream inputStream = socket.getInputStream();
        OutputStream outputStream = socket.getOutputStream();
        ObjectOutputStream objectOutputStream;
        ObjectInputStream objectInputStream;
        Response response = null;

        try {
            objectOutputStream = new ObjectOutputStream(outputStream);
            objectOutputStream.writeObject(request);
            objectOutputStream.flush();
            objectInputStream = new ObjectInputStream(inputStream);
            Object result = objectInputStream.readObject();
            if (result instanceof Response) {
                response = (Response) result;
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

        assert response != null;
        return response.getObj();
    }
}
