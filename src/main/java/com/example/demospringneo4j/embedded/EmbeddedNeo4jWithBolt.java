package com.example.demospringneo4j.embedded;

import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.factory.GraphDatabaseFactory;
import org.neo4j.io.fs.FileUtils;
import org.neo4j.kernel.configuration.BoltConnector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;

public class EmbeddedNeo4jWithBolt {
    private static final Logger logger = LoggerFactory.getLogger(EmbeddedNeo4jWithBolt.class);

    private static final File path = new File("neo4j-embedded-databases/embedded-neo4j-with-bolt.db");

    public static void main(final String[] args) throws IOException {
        logger.info("Staring database...");
        FileUtils.deleteRecursively(path);
        BoltConnector boltConnector = new BoltConnector("0");
        GraphDatabaseService graphDb = new GraphDatabaseFactory()
            .newEmbeddedDatabaseBuilder(path)
            .setConfig(boltConnector.type, "BOLT")
            .setConfig(boltConnector.enabled, "true")
            .setConfig(boltConnector.listen_address, "localhost:7687")
            .newGraphDatabase();

        graphDb.shutdown();
    }
}
