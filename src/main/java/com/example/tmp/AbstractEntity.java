package com.example.tmp;

import org.springframework.data.annotation.Id;

public abstract class AbstractEntity {
    @Id
    public String uuid;

    public String getUuid() {
        return uuid;
    }
}
