package com.example.oneinkedoneproject.domain;

public enum Grade {
    ROLE_BASIC(1, "Basic"),
    ROLE_SILVER(2, "Silver"),
    ROLE_GOLD(3, "Gold");

    private final int id;
    private final String name;

    Grade(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public String getRoleName() {
        return "ROLE_" + name.toUpperCase(); // 스프링 시큐리티에 사용될 이름
    }
}
