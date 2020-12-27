package com.lh.rpccore.entity;

import lombok.Data;

import java.io.Serializable;

@Data
public class Student implements Serializable {
    private String id;

    private String name;

    private int age;

    private String school;
}
