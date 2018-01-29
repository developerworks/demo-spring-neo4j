
### 4.1.4 启动和停止

启动

```
GraphDatabaseService graphDb = new GraphDatabaseFactory().newEmbeddedDatabase( databaseDirectory );
registerShutdownHook( graphDb );
```

停止

```
graphDb.shutdown();
```

要让数据库正确的关闭, 需要注册一个JVM钩子:

```
/**
 * JVM关闭的钩子
 *
 * 1. 程序正常退出
 * 2. 使用System.exit()
 * 3. 终端使用Ctrl+C触发的中断
 * 4. 系统关闭
 * 5. OutOfMemory宕机
 * 6. 使用Kill pid命令干掉进程（注：在使用kill -9 pid时，是不会被调用的）
 *
 * @param db
 */
public static void registerShutdownHook(final GraphDatabaseService db) {
    Runtime.getRuntime().addShutdownHook(new Thread(() -> {
        log.info("Shutting down database...");
        db.shutdown();
    }));
}
```

#### 4.1.4.1 使用配置文件启动

```
GraphDatabaseService graphDb = new GraphDatabaseFactory()
    .newEmbeddedDatabaseBuilder("/tmp/graph.db")
    .loadPropertiesFromFile( /tmp/neo4j.conf" )
    .newGraphDatabase();
```

配置设置也可以以编程的方式完成

```
GraphDatabaseService graphDb = new GraphDatabaseFactory()
    .newEmbeddedDatabaseBuilder( testDirectory.graphDbDir() )
    .setConfig( GraphDatabaseSettings.pagecache_memory, "512M" )
    .setConfig( GraphDatabaseSettings.string_block_size, "60" )
    .setConfig( GraphDatabaseSettings.array_block_size, "300" )
    .newGraphDatabase();
```

#### 4.1.4.1 启动只读实例

```
graphDb = new GraphDatabaseFactory().newEmbeddedDatabaseBuilder( dir )
        .setConfig( GraphDatabaseSettings.read_only, "true" )
        .newGraphDatabase();
```


### 4.14 通过Bolt协议访问嵌入式Neo4j实例

Neo4j Browser 和 Neo4j Driver 使用 Bolt 数据库协议和 Neo4j 服务器通信. 默认嵌入式的Neo4j不会暴露 Bolt 连接器.

首先要在项目中添加依赖

```xml
<dependency>
    <groupId>org.neo4j</groupId>
    <artifactId>neo4j-bolt</artifactId>
    <version>3.3.1</version>
</dependency>
```

配合

```
// 内部类的用法废弃了
// GraphDatabaseSettings.BoltConnector bolt = GraphDatabaseSettings.boltConnector( "0" );
 
BoltConnector bolt = new BoltConnector("0");
GraphDatabaseService graphDb = new GraphDatabaseFactory()
    .newEmbeddedDatabaseBuilder( DB_PATH )
    .setConfig( bolt.type, "BOLT" )
    .setConfig( bolt.enabled, "true" )
    .setConfig( bolt.address, "0.0.0.0:7687" )
    .newGraphDatabase();
```

> 上面的代码实例化 Neo4j 实例. `graphDb` 建议不要直接使用. 
> 建议使用 Neo4j Driver连接到Neo4j, 这样不管是嵌入式的还是独立模式的都不需要修改代码.


### 4.15 终止一个运行中的事务

### 4.16. 在Java中执行 Cypher 查询

添加一些数据

```
GraphDatabaseService db = new GraphDatabaseFactory().newEmbeddedDatabase( databaseDirectory );

try (Transaction tx = db.beginTx())
{
    Node myNode = db.createNode();
    myNode.setProperty( "name", "my node" );
    tx.success();
}
```

执行查询:

```
String rows = "";
try (Transaction tx = graphDb.beginTx(); Result result = graphDb.execute("")) {
    while (result.hasNext()) {
        Map<String, Object> row = result.next();
        for (Map.Entry<String, Object> column : row.entrySet()) {
            rows += column.getKey() + ": " + column.getValue() + "; ";
        }
    }
}
// OUTPUT: n.name: my node; n: Node[0];
```

### 4.17 查询参数

https://neo4j.com/docs/java-reference/3.3/tutorials-java-embedded/#tutorials-cypher-java

节点ID

```
Map<String, Object> params = new HashMap<>();
params.put( "id", 0 );
String query = "MATCH (n) WHERE id(n) = $id RETURN n.name";
Result result = db.execute( query, params );
```

节点对象

```
Map<String, Object> params = new HashMap<>();
params.put( "node", andreasNode );
String query = "MATCH (n:Person) WHERE n = $node RETURN n.name";
Result result = db.execute( query, params );
```

多节点ID

```
Map<String, Object> params = new HashMap<>();
params.put( "ids", Arrays.asList( 0, 1, 2 ) );
String query = "MATCH (n) WHERE id(n) IN $ids RETURN n.name";
Result result = db.execute( query, params );
```

字符串字面量

```
Map<String, Object> params = new HashMap<>();
params.put( "name", "Johan" );
String query = "MATCH (n:Person) WHERE n.name = $name RETURN n";
Result result = db.execute( query, params );
```

索引值

```
Map<String, Object> params = new HashMap<>();
params.put( "value", "Michaela" );
String query = "START n=node:people(name = $value) RETURN n";
Result result = db.execute( query, params );
```

索引查询

```
Map<String, Object> params = new HashMap<>();
params.put( "query", "name:Andreas" );
String query = "START n=node:people($query) RETURN n";
Result result = db.execute( query, params );
```