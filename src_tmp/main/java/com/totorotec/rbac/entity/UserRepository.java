package com.totorotec.rbac.entity;

import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource
public interface UserRepository extends Neo4jRepository<User, String> {
    User findByUuid(String uuid);
}
