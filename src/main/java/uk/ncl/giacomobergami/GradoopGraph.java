package uk.ncl.giacomobergami;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;

import java.io.IOException;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

public class GradoopGraph {
    private final static CsvSchema schema = CsvSchema.emptySchema()
            .withColumnSeparator(';'); // instead of comma
    private final static CsvMapper CSV_MAPPER = new CsvMapper();
    private final static XmlMapper xmlMapper = new XmlMapper();

    List<Vertex> vertexList;
    List<Edge> edgeList;

    public GradoopGraph() {
        vertexList = new ArrayList<>();
        edgeList = new ArrayList<>();
    }

    public static class GradoopXMLOut {
        public String vertexFile;
        public String edgeFile;

        public GradoopXMLOut(String vertexFile, String edgeFile) {
            this.vertexFile = vertexFile;
            this.edgeFile = edgeFile;
        }
    }

    public String edgeList()  {
        try (StringWriter strW = new StringWriter()) {
            var w = CSV_MAPPER.writerWithSchemaFor(Edge.class).writeValues(strW);
            edgeList.forEach( x -> {
                try {
                    w.write(x);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
            w.close();
            var s = strW.toString();
            return s;
        } catch (IOException e) {
            e.printStackTrace();
            return "";
        }
    }


    public String vertexList()  {
        try (StringWriter strW = new StringWriter()) {
            var w = CSV_MAPPER.writerWithSchemaFor(Vertex.class).writeValues(strW);
            vertexList.forEach( x -> {
                try {
                    w.write(x);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
            w.close();
            var s = strW.toString();
            return s;
        } catch (IOException e) {
            e.printStackTrace();
            return "";
        }
    }

    public void addVertex(Vertex v) {
        vertexList.add(v);
    }

    public void addEdge(Edge e) {
        edgeList.add(e);
    }

    public String asXMLGradoopResponse() {
        try {
            return xmlMapper.writeValueAsString(new GradoopXMLOut(vertexList(), edgeList()));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return "";
        }
    }

//    @JsonPropertyOrder({ "id", "srcId", "trgId", "label", "properties", "graphs", "tx", "val"})
    public static class Edge extends Vertex {
        long srcId;
        long trgId;

        public Edge(long id, long srcId, long trgId, String label, String properties, String graphs, String tx, String val) {
            super(id, label, properties, graphs, tx, val);
            this.srcId = srcId;
            this.trgId = trgId;
        }
    }

//    @JsonPropertyOrder({ "id", "label", "properties", "graphs", "tx", "val"})
    public static class Vertex {
        public long id;
        public String label;
        public String properties;
    public String graphs;
    public String tx;
    public String val;

        public Vertex(long id, String label, String properties, String graphs, String tx, String val) {
            this.id = id;
            this.label = label;
            this.properties = properties;
            this.graphs = graphs;
            this.tx = tx;
            this.val = val;
        }
    }
}
