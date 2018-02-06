package com.totorotec.rbac.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import com.totorotec.rbac.entity.Role;
import com.totorotec.rbac.service.RoleService;

@RestController("/com/example/rbac")
public class RbacController {

    @Autowired
    private RoleService roleService;

    @GetMapping("/role/{id}")
    public Role getById(@PathVariable String id) {
        return roleService.getRole(id);
    }
}
