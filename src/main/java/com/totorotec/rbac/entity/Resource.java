package com.totorotec.rbac.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.neo4j.ogm.annotation.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@NodeEntity
@Slf4j
public class Resource {

    @Id
    @GeneratedValue
    private Long id;

//    @Id
//    @GeneratedValue(strategy = UuidStrategy.class)
//    @Property(name = "uuid")
//    @Convert(UuidStringConverter.class)
//    private UUID uuid;

    @Property(name = "uuid")
    private String uuid;

    @Property(name = "name")
    private String name;

    @Property(name = "path")
    private String path;

    @PostLoad
    public void postLoad() {
        log.info("callback post loaded");
    }

}
