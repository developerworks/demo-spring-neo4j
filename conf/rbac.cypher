// 创建角色(管理员,运维,普通用户)
CREATE (:Role {name: "Operator"}),(:Role {name: "Admin"}),(:Role {name: "User"});

// 创建资源节点
CREATE (resource:Resource {path: "/", name: "根目录"})
  RETURN resource;


// 普通用户在资源 / 上有, READ, CREATE权限
MATCH (role:Role {name: "User"}), (resource:Resource {path: "/"})
  CREATE (role)-[op:READ {created_at: timestamp()}]->(resource);

MATCH (role:Role {name: "User"}), (resource:Resource {path: "/"})
  CREATE (role)-[op:CREATE {created_at: timestamp()}]->(resource);


// 查询一个用户在某个资源山的操作权限列表
MATCH (u:User {name: "测试用户"})-[r:HAS_ROLE]->(role:Role {name: "User"})-[op]->(resource)
  WHERE u.roleId = role.uuid
  RETURN u.name,op.name, resource.path;

// 权限查询
MATCH (u:User)
        -[:HAS_ROLE]->(r:Role {name: "User"})
        -[op]->(resource:Resource {path: "/"})
RETURN u.uuid as user_id,
       r.name as role_name,
       resource.path as resource_path,
       type(op) as operation;
