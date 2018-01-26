## 源代码

本文所有的例子都是参考的: 

https://github.com/neo4j/neo4j-documentation/tree/3.3/embedded-examples

文本的源代码如下:

```
git clone https://github.com/developerworks/demo-spring-neo4j.git
```

## 注册存储过程和函数

在嵌入式模式下, APOC提供的存储过程和函数需要在启动 GraphDatabaseService 实例后注册.

```
/**
 * 注册用户定义函数
 *
 * @param db
 * @param procedures
 * @throws KernelException
 */
public static void registerFunction(GraphDatabaseService db, Class<?>... procedures) throws KernelException {
    Procedures proceduresService = ((GraphDatabaseAPI) db).getDependencyResolver().resolveDependency(Procedures.class);
    for (Class<?> procedure : procedures) {
        log.info("=== register function: {}", procedure.toString());
        proceduresService.registerFunction(procedure);
    }
}
```

## 让嵌入式模式的Neo4j开启监听端口

目的是让GUI管理工具(比如: Neo4j browser)可以连接到嵌入式模式下的Neo4j实例, 方便在开发过程中查看数据.

```
public static void main(final String[] args) throws Exception {
    // 获取用户输入
    boolean trueForLocal = waitForUserInput("Would you like to start a local shell instance or enable neo4j to accept remote connections [l/r]? ")
        .equalsIgnoreCase("l");
    // 本地还是远程?
    if (trueForLocal) {
        startLocalShell();
    } else {
        startRemoteShell();
    }
    shutdown();
}
private static void startLocalShell() throws Exception {
    // 创建 BoltConnector 连接器实例
    BoltConnector bolt = new BoltConnector("0");
    // 通过 newEmbeddedDatabaseBuilder 构建 GraphDatabaseService 实例
    graphDb = new GraphDatabaseFactory()
        .newEmbeddedDatabaseBuilder(DATABASE_DIRECTORY)
        // 启用连接器
        .setConfig(bolt.enabled, "true")
        // 协议类型, 支持BOLT,BOLT
        .setConfig(bolt.type, "BOLT")
        // 监听地组织
        .setConfig(bolt.listen_address, "0.0.0.0:7687")
        // 禁用加密
        .setConfig(bolt.encryption_level, BoltConnector.EncryptionLevel.OPTIONAL.toString())
        // 启用远程SHELL
        .setConfig(ShellSettings.remote_shell_enabled, Settings.TRUE)
        .newGraphDatabase();
        
    // RETURN apoc.date.format(1516898575, "ms", "yyyy-MM-dd hh:mm:ss");
    // RETURN apoc.date.currentTimestamp();
    registerFunction(graphDb, apoc.date.Date.class);
    // 虚拟机关闭钩子
    Util.registerShutdownHook(graphDb);
    // 创建好友关系图
    createFriendsRelationshipGraph();
    // 创建 ShellServer
    ShellServer shellServer = new GraphDatabaseShellServer((GraphDatabaseAPI) graphDb);
    // 启动客户端会话
    ShellLobby.newClient(shellServer).grabPrompt();
    // 关闭服务器
    shellServer.shutdown();
}
```