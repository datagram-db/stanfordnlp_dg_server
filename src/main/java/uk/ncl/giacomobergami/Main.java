package uk.ncl.giacomobergami;

import com.esotericsoftware.yamlbeans.YamlReader;
import com.sun.net.httpserver.HttpServer;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.util.LinkedHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

public class Main {
    public static void main(String[] args) throws IOException {
        int port = 9998;
        String host = "localhost";
        if (new File("config.yaml").exists() && new File("config.yaml").isFile()) {
            YamlReader reader = new YamlReader(new FileReader("config.yaml"));
            Object object = reader.read();
            if (object instanceof LinkedHashMap<?,?>) {
                var map = (LinkedHashMap<String,String>)object;
                port = Integer.valueOf(map.getOrDefault("stanford_nlp_port", "9998"));
                host = map.getOrDefault("stanford_nlp_host", "localhost");
            }
        }
        HttpServer server = HttpServer.create(new InetSocketAddress(InetAddress.getByName(host), port), 0);
        /*
        curl -X POST -F "name=What if I want to write something more convoluted, then?" -F "p=This is a test, continuing the former" localhost:9999/stanfordnlp
         */
        server.createContext("/stanfordnlp", new GraphRequest());
        server.createContext("/sutime", new TimeRequest());
        ThreadPoolExecutor threadPoolExecutor = (ThreadPoolExecutor) Executors.newFixedThreadPool(1);
        server.setExecutor(threadPoolExecutor); // creates a default executor
        server.start();
    }
}