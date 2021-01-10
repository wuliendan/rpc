package com.lh.rpctest;

import com.lh.rpcclient.proxy.SocketClientProxy;
import com.lh.rpccore.entity.Student;
import com.lh.rpcserver.server.Server;
import com.lh.rpcserver.service.StudentService;
import com.lh.rpcserver.service.impl.StudentServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@Slf4j
public class RpcTestApplication {

    /**
     * 测试主类.
     *
     * @param args 参数
     */
    public static void main(final String[] args) {
        new Thread(() -> {
            Server server = new Server(12000);
            server.register(StudentService.class, StudentServiceImpl.class);
            server.start();
        }).start();

        SocketClientProxy proxy = new SocketClientProxy();
        StudentService studentService = (StudentService) proxy.getProxy(StudentService.class);
        Student student = studentService.getInfo();
        log.info("接收服务端返回的消息：" + student.getName());
    }

}
