package com.totorotec.rbac.component;

import com.totorotec.rbac.entity.Resource;
import com.totorotec.rbac.entity.Role;
import com.totorotec.rbac.entity.User;
import com.totorotec.rbac.repository.ResourceRepository;
import com.totorotec.rbac.repository.RoleRepository;
import com.totorotec.rbac.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.spi.BackendIdConverter;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.Optional;

@Slf4j
@Component
public class CustomBackendIdConverter implements BackendIdConverter {

    @Autowired
    ResourceRepository resourceRepository;

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    UserRepository userRepository;

    /**
     * 把从客户端获取到的业务ID转换为Neo4j的内部ID
     *
     * @param id
     * @param entityType
     * @return
     */
    @Override
    public Serializable fromRequestId(String id, Class<?> entityType) {
        if (entityType.equals(Resource.class)) {
            Optional<Resource> resource = resourceRepository.findByUuid(id);
            if (!resource.isPresent()) {
                return -1;
            } else {
                return resource.get().getId();
            }
        } else if (entityType.equals(Role.class)) {
            Optional<Role> resource = roleRepository.findByUuid(id);
            if (!resource.isPresent()) {
                return -1;
            } else {
                return resource.get().getId();
            }
        }
        else if (entityType.equals(User.class)) {
            Optional<User> resource = userRepository.findByUuid(id);
            if (!resource.isPresent()) {
                return -1;
            } else {
                return resource.get().getId();
            }
        }
        else {
            throw new IllegalArgumentException("Unrecognized class " + entityType);
        }
    }

    /**
     * 把内部ID转换为请求UUID
     *
     * @param id
     * @param entityType
     * @return
     */
    @Override
    public String toRequestId(Serializable id, Class<?> entityType) {
        if (entityType.equals(Resource.class)) {
            Optional<Resource> resource = resourceRepository.findById(Long.parseLong(id.toString()));
            if (!resource.isPresent()) {
                return null;
            } else {
                return resource.get().getUuid();
            }
        }
        else if (entityType.equals(Role.class)) {
            Optional<Role> resource = roleRepository.findById(Long.parseLong(id.toString()));
            if (!resource.isPresent()) {
                return null;
            } else {
                return resource.get().getUuid();
            }
        }
        else if (entityType.equals(User.class)) {
            Optional<User> resource = userRepository.findById(Long.parseLong(id.toString()));
            if (!resource.isPresent()) {
                return null;
            } else {
                return resource.get().getUuid();
            }
        }
        else {
            throw new IllegalArgumentException("Unrecognized class " + entityType);
        }
    }

    /**
     * 每个实体都需要添加if分支
     * @param delimiter
     * @return
     */
    @Override
    public boolean supports(Class<?> delimiter) {
        if (delimiter.equals(Resource.class)) {
            return true;
        }
        if (delimiter.equals(Role.class)) {
            return true;
        }
        if (delimiter.equals(User.class)) {
            return true;
        }
        return false;
    }
}