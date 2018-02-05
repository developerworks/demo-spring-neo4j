package com.example.demospringneo4j;

import org.neo4j.ogm.annotation.Id;
import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Relationship;

import java.util.Set;

@NodeEntity
public class User {
    @Id
    private String uuid;

    private String name;

    /**
     * 一个用户可能有多个角色
     */
    @Relationship(type = "HAS_ROLE")
    Set<Role> roles;

    public User() {
    }

    public User(String uuid, String name) {
        this.uuid = uuid;
        this.name = name;
    }

    public String getUuid() {
        return uuid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
