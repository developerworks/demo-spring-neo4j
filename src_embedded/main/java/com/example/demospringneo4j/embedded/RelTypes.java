package com.example.demospringneo4j.embedded;

import org.neo4j.graphdb.RelationshipType;

/**
 * 关系类型
 */
public enum RelTypes implements RelationshipType {
    USERS_REFERENCE, USER, KNOWS
}