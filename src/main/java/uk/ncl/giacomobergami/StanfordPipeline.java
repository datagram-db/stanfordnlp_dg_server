package uk.ncl.giacomobergami;

import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;

import java.util.Properties;

public class StanfordPipeline {

    public final static StanfordPipeline self = new StanfordPipeline();
    private StanfordCoreNLP pipeline;
    private StanfordPipeline() {
        // creates a StanfordCoreNLP object, with POS tagging, lemmatization, NER, parsing, and coreference resolution
        Properties props = new Properties();
        props.put("annotators", "tokenize, ssplit, pos, lemma, ner, parse, dcoref");
        pipeline = new StanfordCoreNLP(props);
    }

    public static Annotation annotate(String annotation) {
        Annotation toret = new Annotation(annotation);
        self.pipeline.annotate(toret);
        return toret;
    }

}
