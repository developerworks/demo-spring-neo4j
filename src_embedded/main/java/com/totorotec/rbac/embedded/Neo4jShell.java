package com.totorotec.rbac.embedded;

import lombok.extern.slf4j.Slf4j;
import org.neo4j.graphdb.*;
import org.neo4j.graphdb.factory.GraphDatabaseFactory;
import org.neo4j.graphdb.factory.GraphDatabaseSettings;
import org.neo4j.graphdb.index.Index;
import org.neo4j.kernel.api.exceptions.KernelException;
import org.neo4j.kernel.configuration.BoltConnector;
import org.neo4j.kernel.configuration.Settings;
import org.neo4j.kernel.impl.proc.Procedures;
import org.neo4j.kernel.internal.GraphDatabaseAPI;
import org.neo4j.shell.ShellLobby;
import org.neo4j.shell.ShellServer;
import org.neo4j.shell.ShellSettings;
import org.neo4j.shell.kernel.GraphDatabaseShellServer;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

@Slf4j
public class Neo4jShell {
    // 数据库目录
    private static final File DATABASE_DIRECTORY = new File("neo4j-embedded-databases/embedded-neo4j-shell.db");
    // 属性名称
    private static final String PROPERTY_USERNAME = "username";
    // 数据库实例
    private static GraphDatabaseService graphDb;

    private static final FileSystem fs = FileSystems.getDefault();

    /**
     * 启动
     *
     * @param args
     * @throws Exception
     */
    public static void main(final String[] args) throws Exception {
        // 获取用户输入
        boolean trueForLocal = true;
//        boolean trueForLocal = waitForUserInput("Would you like to start a local shell instance or enable neo4j to accept remote connections [l/r]? ")
//            .equalsIgnoreCase("l");

        // 本地还是远程?
        if (trueForLocal) {
            startLocalShell();
        } else {
            startRemoteShell();
        }
        shutdown();
    }

    /**
     * 创建好友关系图
     */
    private static void createFriendsRelationshipGraph() {
        try (Transaction tx = graphDb.beginTx()) {
            log.info("Creating totorotec graph ...");
            Random random = new Random();
            // 用户引用节点
            Node usersReferenceNode = graphDb.createNode();
            // 获取或创建一个名称为 references 的索引
            Index<Node> references = graphDb.index().forNodes("references");
            usersReferenceNode.setProperty("reference", "users");
            // 把节点添加到索引
            references.add(usersReferenceNode, "reference", "users");

            // 创建100个用户节点
            List<Node> users = new ArrayList<>();
            for (int id = 0; id < 100; id++) {
                // CREATE (:User {id: $id})
                Node userNode = createUser(Util.getUserIdentity(id));
                // CREATE (usersReferenceNode)-[:USER]->(userNode)
                usersReferenceNode.createRelationshipTo(userNode, RelTypes.USER);

                if (id > 10) {
                    // 产生一个0..5的随机数
                    int numberOfFriends = random.nextInt(5);
                    Set<Node> knows = new HashSet<>();
                    for (int i = 0; i < numberOfFriends; i++) {
                        // 从用户节点列表中随机获取一个用户
                        Node friend = users.get(random.nextInt(users.size()));
                        // 添加到集合中
                        if (knows.add(friend)) {
                            // CREATE (userNode)-[:KNOWS]->(friend)
                            userNode.createRelationshipTo(friend, RelTypes.KNOWS);
                        }
                    }
                }
                users.add(userNode);
            }
            tx.success();
        }
    }

    /**
     * 启动本地 SHELL
     *
     * @throws Exception
     */
    private static void startLocalShell1() throws Exception {
        // 通过工程方法创建嵌入式数据库实例
        // 如果 DATABASE_DIRECTORY 存在, 使用现有的数据, 否则创建新的数据库
        // 一个数据库目录只能由一个数据库实例启动, 多个实例不能指向同一个数据库目录

        graphDb = new GraphDatabaseFactory().newEmbeddedDatabase(DATABASE_DIRECTORY);
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

    /**
     * http://neo4j.com/docs/java-reference/3.4-preview/tutorials-java-embedded/#tutorials-java-embedded-bolt
     *
     * @throws Exception
     */
    private static void startLocalShell() throws Exception {
        // 创建 BoltConnector 连接器实例
        BoltConnector bolt = new BoltConnector("0");
        // 通过 newEmbeddedDatabaseBuilder 构建 GraphDatabaseService 实例
        Path path = Paths.get("conf/neo4j.conf");
        log.info("Configuration file: {}", path.toAbsolutePath());
//        Path pluginPath = Paths.get("plugins");
//        log.info("Plugins directory: {}", pluginPath.toAbsolutePath());

        graphDb = new GraphDatabaseFactory()
            .newEmbeddedDatabaseBuilder(DATABASE_DIRECTORY)
            // 配置
            .setConfig(GraphDatabaseSettings.pagecache_memory, "512M")
            .setConfig(GraphDatabaseSettings.string_block_size, "60")
            .setConfig(GraphDatabaseSettings.array_block_size, "300")
            .setConfig(GraphDatabaseSettings.store_internal_log_level, "DEBUG")
//            .setConfig(GraphDatabaseSettings.plugin_dir, pluginPath.toAbsolutePath().toString())
//            .setConfig(GraphDatabaseSettings.procedure_unrestricted, "*")
//            .setConfig(GraphDatabaseSettings.procedure_whitelist, "apoc.*")
            // 通过配置文件对上面的默认设置进行覆盖
            .loadPropertiesFromFile(path.toAbsolutePath().toString())
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
//            .setConfig("com.graphaware.runtime.enabled", "true")
            .newGraphDatabase();

        registerFunction(graphDb, apoc.date.Date.class);
//        registerFunction(graphDb, ga.uuid.NodeUuidFunctions.class);
//        registerFunction(graphDb, ga.uuid.RelationshipUuidFunctions.class);
        // RETURN apoc.date.format(1516898575, "ms", "yyyy-MM-dd hh:mm:ss");
        // RETURN apoc.date.currentTimestamp();
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

    /**
     * 启动远程 SHELL
     *
     * @throws Exception
     */
    private static void startRemoteShell() throws Exception {
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
            .setConfig(bolt.encryption_level, BoltConnector.EncryptionLevel.DISABLED.toString())
            // 启用远程SHELL
            .setConfig(ShellSettings.remote_shell_enabled, Settings.TRUE)
            .newGraphDatabase();
        // 虚拟机关闭钩子
        Util.registerShutdownHook(graphDb);
        // 创建好友关系图
        createFriendsRelationshipGraph();
        // 等待用户输入
        waitForUserInput("Remote shell enabled, connect to it by executing\n"
            + "the shell-client script in a separate terminal."
            + "The script is located in the bin directory.\n"
            + "\nWhen you're done playing around, just press [Enter] "
            + "in this terminal ");
    }

    /**
     * 获取用户终端输入
     *
     * @param textToSystemOut
     * @return
     * @throws Exception
     */
    private static String waitForUserInput(final String textToSystemOut) throws Exception {
        log.info(textToSystemOut);
        // 从标准输入读取一行UTF8字符串
        return new BufferedReader(new InputStreamReader(System.in, StandardCharsets.UTF_8)).readLine();
    }

    /**
     * 删除启动数据库时创建的节点和关系
     */
    private static void deleteExampleNodeSpace() {
        try (Transaction tx = graphDb.beginTx()) {
            log.info("Deleting totorotec graph ...");
            // 获取索引, 有则返回索引, 无则创建索引
            Node usersReferenceNode = graphDb.index().forNodes("references").get("reference", "users").getSingle();
            if (usersReferenceNode != null) {
                for (Relationship relationship : usersReferenceNode.getRelationships(RelTypes.USER, Direction.OUTGOING)) {
                    Node user = relationship.getEndNode();
                    for (Relationship knowsRelationship : user.getRelationships(RelTypes.KNOWS)) {
                        knowsRelationship.delete();
                    }
                    user.delete();
                    relationship.delete();
                }
                usersReferenceNode.getSingleRelationship(RelTypes.USERS_REFERENCE, Direction.INCOMING).delete();
                usersReferenceNode.delete();
            }
            tx.success();
        }
    }

    /**
     * 关闭数据库
     */
    private static void shutdownGraphDb() {
        log.info("Shutting down database...");
        graphDb.shutdown();
        graphDb = null;

    }

    /**
     * 清理并关闭
     */
    private static void shutdown() {
        if (graphDb != null) {
            deleteExampleNodeSpace();
            shutdownGraphDb();
        }
    }

    /**
     * 创建一个用户节点
     *
     * @param username
     * @return
     */
    private static Node createUser(final String username) {
        Label label = Label.label("User");
        Node node = graphDb.createNode(label);
        node.setProperty(PROPERTY_USERNAME, username);
        return node;
    }

    /**
     * 注册存储过程
     * <p>
     * https://github.com/neo4j-contrib/neo4j-apoc-procedures/blob/3.0/src/test/java/apoc/util/TestUtil.java#L89
     * https://stackoverflow.com/questions/43266933/loading-a-plugin-into-an-embedded-version-of-neo4j-database
     *
     * @param db
     * @param procedures
     * @throws KernelException
     */
    public static void registerProcedure(GraphDatabaseService db, Class<?>... procedures) throws KernelException {
        Procedures proceduresService = ((GraphDatabaseAPI) db).getDependencyResolver().resolveDependency(Procedures.class);
        for (Class<?> procedure : procedures) {
            log.info("=== register procedure: {}", procedure.toString());
            proceduresService.registerProcedure(procedure);
        }
    }

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
}
