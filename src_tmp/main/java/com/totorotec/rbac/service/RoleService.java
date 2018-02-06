package com.totorotec.rbac.service;

import com.totorotec.rbac.entity.Role;

public interface RoleService {
    Role createRole(Role role);
    Role updateRole(Role role);
    Role deleteRole(Role role);
    Role getRole(String uuid);
}
