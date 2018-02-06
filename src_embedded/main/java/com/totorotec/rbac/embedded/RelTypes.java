package com.totorotec.rbac.embedded;

import org.neo4j.graphdb.RelationshipType;

/**
 * 关系类型
 */
public enum RelTypes implements RelationshipType {
    USERS_REFERENCE, USER, KNOWS
}