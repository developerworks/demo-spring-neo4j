package com.example.demospringneo4j;

import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Relationship;
import org.springframework.data.annotation.Id;

import java.util.Set;

@NodeEntity
public class Role {

    @Id
    private String uuid;

    /**
     * 一个角色有多个权限
     */
    @Relationship(type = "HAS_PERMISSION", direction = Relationship.OUTGOING)
    private Set<Permission> permission;
}
