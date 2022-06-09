package com.example.demo.demo.shiro;

import lombok.Data;

import java.io.Serializable;

/**
 * @author Peter Hai
 */
@Data
public class AccountProfile implements Serializable {
    private Integer id;

    private String name;

    private String avatar;

    private Integer status;
}

