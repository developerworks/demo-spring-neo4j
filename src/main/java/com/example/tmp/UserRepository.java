package com.example.tmp;

import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import com.example.tmp.User;

@RepositoryRestResource
public interface UserRepository extends Neo4jRepository<User, String> {
    User findByUuid(String uuid);
}
