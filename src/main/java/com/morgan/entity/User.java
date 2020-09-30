package com.morgan.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class User implements Serializable {

    private static final long serialVerisionUID = 1L;
    private int uid;
    private String userName;
    private String password;
    private int salary;
}
