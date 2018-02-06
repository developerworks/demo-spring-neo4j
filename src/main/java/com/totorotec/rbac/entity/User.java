package com.totorotec.rbac.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.neo4j.ogm.annotation.GeneratedValue;
import org.neo4j.ogm.annotation.Id;
import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Property;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@NodeEntity
@Slf4j
public class User {
    @Id
    @GeneratedValue
    private Long id;

    @Property(name = "uuid")
    private String uuid;

    @Property(name = "name")
    private String name;

//    /**
//     * 一个用户可能有多个角色
//     */
//    @Relationship(type = "HAS_ROLE")
//    Set<Role> roles;

}
