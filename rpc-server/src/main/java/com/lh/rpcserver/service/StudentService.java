package com.lh.rpcserver.service;

import com.lh.rpccore.entity.Student;

public interface StudentService {

    /**
     * 获取学生信息.
     * @return student
     */
    Student getInfo();

    /**
     * 打印信息.
     * @param student 学生信息.
     */
    void print(Student student);
}
