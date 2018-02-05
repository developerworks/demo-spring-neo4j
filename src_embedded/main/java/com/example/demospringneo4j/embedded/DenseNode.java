package com.example.demospringneo4j.embedded;

import org.neo4j.graphdb.Node;

/**
 * 密集节点: 关系多于一个指定数量的节点
 */
public class DenseNode {
    public long nodeId;
    public long degree;

    public DenseNode(Node node) {
        this.nodeId = node.getId();
        this.degree = node.getDegree();
    }
}
