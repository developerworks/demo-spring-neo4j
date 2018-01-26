package com.example.demospringneo4j.embedded;

import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.procedure.Context;
import org.neo4j.procedure.Name;
import org.neo4j.procedure.Procedure;

import java.util.stream.Stream;

public class ProcedureExample {
    @Context
    private GraphDatabaseService db;

    @Procedure
    public Stream<DenseNode> findDenseNodes(@Name("threshold") long threshold) {
        return db.getAllNodes()
            .stream()
            .filter(
            (node) -> node.getDegree() > threshold
        ).map(DenseNode::new);
    }

}
