package com.example.demospringneo4j.embedded;

import lombok.extern.slf4j.Slf4j;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.factory.GraphDatabaseFactory;
import org.neo4j.io.fs.FileUtils;
import org.neo4j.logging.Log;
import org.neo4j.logging.LogProvider;
import org.neo4j.logging.NullLog;
import org.neo4j.logging.slf4j.Slf4jLog;
import org.neo4j.logging.slf4j.Slf4jLogProvider;

import java.io.File;
import java.io.IOException;

@Slf4j
public class EmbeddedNeo4jWithCustomLogging {

    private static final File databaseDirectory = new File("neo4j-embedded-databases/embedded-neo4j-with-custom-logging.db");
    private static GraphDatabaseService graphDb;

    /**
     * 自定义日志
     */
    private static class MyCustomLogProvider implements LogProvider {
        public MyCustomLogProvider(Object output) {
        }

        @Override
        public Log getLog(Class loggingClass) {
            return NullLog.getInstance();
        }

        @Override
        public Log getLog(String context) {
            return NullLog.getInstance();
        }
    }

    public static void main(String[] args) throws IOException {
        log.info("Clean database directory...");
        FileUtils.deleteRecursively(databaseDirectory);
        // 自定义日志
//        Object output = new Object();
//        LogProvider logProvider = new MyCustomLogProvider(output);
        graphDb = new GraphDatabaseFactory().setUserLogProvider(new Slf4jLogProvider()).newEmbeddedDatabase(databaseDirectory);
        shutdown();
    }

    private static void shutdown() {
        graphDb.shutdown();
    }
}
