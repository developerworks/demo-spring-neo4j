package com.example.demospringneo4j.rbac.service;

import org.springframework.stereotype.Service;
import com.example.demospringneo4j.rbac.entity.Resource;

@Service
public interface ResourceService {
    Resource createResource(Resource resource);
}
