package com.example.demospringneo4j.rbac.service;

import com.example.tmp.Role;

public interface RoleService {
    Role createRole(Role role);
    Role updateRole(Role role);
    Role deleteRole(Role role);
    Role getRole(String uuid);
}
