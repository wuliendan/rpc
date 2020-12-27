package com.lh.rpcserver.service.impl;

import com.lh.rpccore.entity.Student;
import com.lh.rpcserver.service.StudentService;

public class StudentServiceImpl implements StudentService {
    /**
     * 获取学生信息.
     *
     * @return
     */
    @Override
    public Student getInfo() {
        Student student = new Student();
        student.setId("123456");
        student.setAge(10);
        student.setName("XDE");
        return student;
    }

    /**
     * 打印信息.
     *
     * @param student 学生信息.
     */
    @Override
    public void print(final Student student) {

    }
}
