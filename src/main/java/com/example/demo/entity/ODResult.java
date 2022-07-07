package com.example.demo.entity;

import lombok.Data;

@Data
public class ODResult {
    private Integer id;

    private Integer category_id;

    private String category;

    private Double[] bbox;

    private Double score;

}
