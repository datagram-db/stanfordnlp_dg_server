package uk.ncl.giacomobergami;

import edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.ling.IndexedWord;
import edu.stanford.nlp.semgraph.SemanticGraph;
import edu.stanford.nlp.semgraph.SemanticGraphCoreAnnotations;
import edu.stanford.nlp.semgraph.SemanticGraphEdge;
import edu.stanford.nlp.util.CoreMap;
import org.apache.commons.lang3.tuple.Pair;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

public class StanfordGraph {

    private static AtomicInteger currentGraphId = new AtomicInteger(0);
    public static PropertyGraph parse(String text, Date start, Date end) {
        PropertyGraph graph = new PropertyGraph(currentGraphId.getAndIncrement(), start, end);
        text = text.toLowerCase();
        List<CoreMap> sentences = StanfordPipeline.annotate(text).get(CoreAnnotations.SentencesAnnotation.class);
        for(CoreMap sentence: sentences) {
            visit(graph, sentence.get(SemanticGraphCoreAnnotations.CollapsedCCProcessedDependenciesAnnotation.class));
            int prevMaxVertex = graph.maxVertexId();
        }
        return graph;
    }

    private static void visit(PropertyGraph graph, SemanticGraph semanticGraph) {
        Set<Integer> visitedVertices = new HashSet<>();
        {
            Collection<IndexedWord> rootNodes = semanticGraph.getRoots();
            for (IndexedWord w : rootNodes) {
                vertex(graph, w, semanticGraph, visitedVertices, true);
            }
        }
    }

    private static void vertex(PropertyGraph graph, IndexedWord v, SemanticGraph semanticGraph, Set<Integer> visitedVertices, boolean isRoot) {
        String value = v.value();
        String tag = v.tag();
        String stemmed = v.lemma();
        String nonWord = v.originalText();
        int index = v.get(CoreAnnotations.IndexAnnotation.class);
        visitedVertices.add(index);

        boolean hasWord = (!value.equals(tag));
        var vertexIndex = graph.newVertex(index);


        if (tag.equals("DT")) {
            tag = "det";
        } else if (tag.equals("EX")) {
            tag = "∃";
        } else if (tag.equals("JJ")) {
        } else if (tag.equals("JJR") || tag.equals("RBR")) {
            tag = "cmp";
        } else if (tag.equals("JJS") || tag.equals("RBS")) {
            tag = "aggregation";
        } else if (tag.equals("NNS")) {
            tag = "noun";
            vertexIndex.update("specification","common");
            vertexIndex.update("number","plural");
        } else if (tag.equals("NN")) {
            tag = "noun";
            vertexIndex.update("specification","common");
            vertexIndex.update("number","singular");
        } else if (tag.equals("NNP")) {
            tag = "noun";
            vertexIndex.update("specification","proper");
            vertexIndex.update("number","singular");
        } else if (tag.equals("NNPS")) {
            tag = "noun";
            vertexIndex.update("specification","proper");
            vertexIndex.update("number","plural");
        } else if (tag.equals("WDT")) {
            tag = "var";
        }
        Pair<String, String> cp = QuerySemantics.extended_semantics.resolve(tag, hasWord ? value : nonWord);
        tag = cp.getKey();
        value = cp.getValue();
        vertexIndex.addLabel(tag);
        if (isRoot) vertexIndex.addLabel("root");
        vertexIndex.update("value",value);
        if (!value.equals(stemmed)) vertexIndex.update("lemma",stemmed);
        vertexIndex.update("pos", index);

        for (SemanticGraphEdge e :
                semanticGraph.outgoingEdgeList(v)) {
            edge(graph, index, e, semanticGraph, visitedVertices);
        }
    }

    private static void edge(PropertyGraph graph, int srcId, SemanticGraphEdge edge, SemanticGraph semanticGraph, Set<Integer> visitedVertices) {
        Integer targetId = edge.getTarget().get(CoreAnnotations.IndexAnnotation.class);
        //Checking if it has a case
        String role = GetEdgeType.getInstance().apply(edge).toString();

        //Getting Specific
        String shortName = edge.getRelation().getShortName();
        String[] ar = shortName.split(":");
        String specific = null;
        if (ar.length==2) {
            specific = ar[1];
        } else specific = edge.getRelation().getSpecific();

        var edgeIndex = graph.newEdge(srcId, targetId);
        edgeIndex.addLabel(role);
        if (specific != null)
            edgeIndex.update("specification",specific);

        if (!visitedVertices.contains(targetId)) {
            vertex(graph, edge.getTarget(), semanticGraph, visitedVertices, false);
        }
    }
}
