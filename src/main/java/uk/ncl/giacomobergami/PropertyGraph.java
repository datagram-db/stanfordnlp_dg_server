package uk.ncl.giacomobergami;

import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.util.Date;
import java.util.HashMap;

public class PropertyGraph {

    private final JSONArray graphId;

    private final JSONArray tx_val;
    private HashMap<Integer, Vertex> vertices;
    private HashMap<Pair<Integer, Integer>, Vertex> edges;
    private HashMap<Pair<Integer,Integer>, Integer> ecounter;
    int edgeCount = 0;
    private int maxInt;

    public GradoopGraph.Vertex asGradoopVertex(Vertex localV) {
        return new GradoopGraph.Vertex(localV.id,
                localV.labels.toJSONString(),
                localV.properties.toJSONString(),
                graphId.toJSONString(),
                tx_val.toJSONString(),
                tx_val.toJSONString()
                );
    }

    public GradoopGraph.Edge asGradoopEdge(int src, int dst) {
        var cp = new ImmutablePair<>(src, dst);
        var localV = edges.get(cp);
        var edgeId = ecounter.get(cp);
        if (localV == null) return null;
        return new GradoopGraph.Edge(edgeId,
                src,
                dst,
                localV.labels.toJSONString(),
                localV.properties.toJSONString(),
                graphId.toJSONString(),
                tx_val.toJSONString(),
                tx_val.toJSONString()
                );
    }

    public GradoopGraph asGradoopGraph() {
        var g = new GradoopGraph();
        for (var v : vertices.values())
            g.addVertex(asGradoopVertex(v));
        for (var cp : edges.keySet())
            g.addEdge(asGradoopEdge(cp.getKey(), cp.getValue()));
        return g;
    }

    public int maxVertexId() {
        return maxInt;
    }

    public PropertyGraph(int graphId, Date start, Date end) {
        this.graphId = new JSONArray();
        ecounter = new HashMap<>();
        this.graphId.add(graphId);
        tx_val = new JSONArray();
        tx_val.add(start.toString());
        tx_val.add(end.toString());
        vertices = new HashMap<>();
        edges = new HashMap<>();
    }

    public Vertex newVertex(int index) {
        var newAlloc = new Vertex(index);
        maxInt = Math.max(maxInt, index);
        var result = vertices.putIfAbsent(index, newAlloc);
        return result == null ? newAlloc : result;
    }

    public Vertex newEdge(int srcId, int targetId) {
        var cp = new ImmutablePair<Integer, Integer>(srcId, targetId);
        ecounter.put(cp, edgeCount);
        var newAlloc = new Vertex(edgeCount++);
        var result = edges.putIfAbsent(cp, newAlloc);
        return result == null ? newAlloc : result;
    }

    public static class Vertex {
        long id;
        JSONObject properties;
        JSONArray  labels;

        public Vertex(long id) {
            this.id = id;
            properties = new JSONObject();
            labels = new JSONArray();
        }

        public void update(String specification, Object common) {
            properties.put(specification, common);
        }

        public void addLabel(String tag) {
            labels.add(tag);
        }
    }

}
