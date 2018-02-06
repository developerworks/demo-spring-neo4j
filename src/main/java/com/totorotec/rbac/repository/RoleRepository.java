package com.totorotec.rbac.repository;

import com.totorotec.rbac.entity.Role;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.Optional;

@RepositoryRestResource(path = "/roles")
public interface RoleRepository extends Neo4jRepository<Role, Long> {
    Optional<Role> findByUuid(String id);
}
