package com.example.demospringneo4j.embedded;

import lombok.extern.slf4j.Slf4j;
import org.neo4j.graphdb.GraphDatabaseService;

@Slf4j
public class Util {

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

    public static String getUserIdentity(final int id) {
        return "user" + id + "@neo4j.org";
    }
}
