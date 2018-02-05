package com.example.demospringneo4j.embedded;

import org.neo4j.graphdb.*;
import org.neo4j.graphdb.factory.GraphDatabaseFactory;
import org.neo4j.io.fs.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;

/**
 * https://github.com/neo4j/neo4j-documentation/blob/3.3/embedded-examples/src/main/java/org/neo4j/examples/EmbeddedNeo4j.java
 */
public class EmbeddedNeo4j {
    private static final Logger logger = LoggerFactory.getLogger(EmbeddedNeo4j.class);

    private static final File databaseDirectory = new File("neo4j-embedded-databases/embedded-neo4j.db");
    public String greeting;
    GraphDatabaseService graphDb;
    Node firstNode;
    Node secondNode;
    Relationship relationship;

    private static enum RelTypes implements RelationshipType {
        KNOWS
    }

    public static void main(final String[] args) throws IOException {
        EmbeddedNeo4j hello = new EmbeddedNeo4j();
        hello.createDb();
        hello.removeData();
        hello.shutDown();
    }

    /**
     * 创建数据库
     *
     * @throws IOException
     */
    void createDb() throws IOException {
        // 递归删除数据库目录
        FileUtils.deleteRecursively(databaseDirectory);
        // 创建数据库
        graphDb = new GraphDatabaseFactory().newEmbeddedDatabase(databaseDirectory);
        registerShutdownHook(graphDb);
        // 启动事务
        try (Transaction tx = graphDb.beginTx()) {
            // 创建两个节点
            firstNode = graphDb.createNode();
            secondNode = graphDb.createNode();
            firstNode.setProperty("message", "hello,");
            secondNode.setProperty("message", "world!");
            // 建立关系
            relationship = firstNode.createRelationshipTo(secondNode, RelTypes.KNOWS);
            // 设置关系属性
            relationship.setProperty("created_at", System.currentTimeMillis() / 1000L);
            logger.info(firstNode.getProperty("message").toString());
            logger.info(relationship.getProperty("created_at").toString());
            logger.info(secondNode.getProperty("message").toString());
            greeting =
                firstNode.getProperty("message").toString()
                    + relationship.getProperty("created_at").toString()
                    + secondNode.getProperty("message").toString();
            tx.success();
        }
    }

    /**
     * 删除数据
     */
    void removeData() {
        try (Transaction tx = graphDb.beginTx()) {
            // START SNIPPET: removingData
            // let's remove the data
            firstNode.getSingleRelationship(RelTypes.KNOWS, Direction.OUTGOING).delete();
            firstNode.delete();
            secondNode.delete();
            // END SNIPPET: removingData
            tx.success();
        }
    }

    void shutDown() {
        System.out.println("Shutting down database ...");
        graphDb.shutdown();
    }

    /**
     * 关闭数据库钩子
     *
     * @param graphDb
     */
    private static void registerShutdownHook(final GraphDatabaseService graphDb) {
        Runtime.getRuntime().addShutdownHook(new Thread() {
            @Override
            public void run() {
                graphDb.shutdown();
            }
        });
    }
}
