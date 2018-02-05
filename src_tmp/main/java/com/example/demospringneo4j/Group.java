package com.example.demospringneo4j;

import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Relationship;

import java.util.Set;

@NodeEntity
public class Group extends AbstractEntity {

    private String name;

    /**
     * 组也可以有角色, 那么组中的用户自动获得该组所具有的权限集合
     */
    @Relationship(type = "HAS_ROLE")
    private Set<Role> roles;

    public Group() {
    }

    public Group(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
