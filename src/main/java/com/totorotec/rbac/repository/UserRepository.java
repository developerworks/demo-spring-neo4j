package com.totorotec.rbac.repository;

import com.totorotec.rbac.entity.User;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.Optional;

@RepositoryRestResource(path = "/users")
public interface UserRepository extends Neo4jRepository<User, Long> {
    Optional<User> findByUuid(String id);
}
