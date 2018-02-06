package com.totorotec.rbac.service;

import org.springframework.stereotype.Service;
import com.totorotec.rbac.entity.Resource;

@Service
public interface ResourceService {
    Resource createResource(Resource resource);
}
