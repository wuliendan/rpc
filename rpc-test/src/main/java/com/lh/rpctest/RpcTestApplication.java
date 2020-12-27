package com.lh.rpctest;

import com.lh.rpcclient.proxy.SocketClientProxy;
import com.lh.rpccore.entity.Student;
import com.lh.rpcserver.server.Server;
import com.lh.rpcserver.service.StudentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.IOException;

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
            try {
                Server server = new Server(12000);
                server.start();
            } catch (IOException e) {
                e.printStackTrace();
                log.error(e.getMessage());
            }
        }).start();

        SocketClientProxy proxy = new SocketClientProxy();
        StudentService studentService = (StudentService) proxy.getProxy(StudentService.class);
        Student student = studentService.getInfo();
        log.info(student.getName());
    }

}
