package com.lh.rpcclient.proxy;

import com.lh.rpcclient.client.SocketClient;
import com.lh.rpccore.entity.RPCRequest;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

public class SocketClientProxy {

    private final SocketClient socketClient = new SocketClient();

    /**
     * 代理对象调用方法都会转发为InvocationHandler 接口的invoke函数调用.
     *
     * @param <T> T
     * @param clazz clazz
     * @return return
     */
    public <T> Object getProxy(final Class<T> clazz) {
        return Proxy.newProxyInstance(clazz.getClassLoader(),
                new Class<?>[]{clazz}, new InvocationHandler() {
                    @Override
                    public Object invoke(final Object proxy, final Method method, final Object[] args) throws Throwable {
                        RPCRequest RPCRequest = new RPCRequest();
                        RPCRequest.setClassName(method.getDeclaringClass().getName());
                        RPCRequest.setMethodName(method.getName());
                        RPCRequest.setParamTypes(method.getParameterTypes());
                        RPCRequest.setParams(args);

                        return socketClient.invoke(RPCRequest, "127.0.0.1", 12000);
                    }
                });
    }
}
