package uk.ncl.giacomobergami;

import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

public class Main {
    public static void main(String[] args) throws IOException {
        int port = 9999;
        if (args.length > 0)
            port = Integer.valueOf(args[0]);
        HttpServer server = HttpServer.create(new InetSocketAddress(port), 0);

        /*
        curl -X POST -F "name=What if I want to write something more convoluted, then?" -F "p=This is a test, continuing the former" localhost:9999/stanfordnlp
         */
        server.createContext("/stanfordnlp", new MultiRequests());
        ThreadPoolExecutor threadPoolExecutor = (ThreadPoolExecutor) Executors.newFixedThreadPool(1);
        server.setExecutor(threadPoolExecutor); // creates a default executor
        server.start();
    }
}