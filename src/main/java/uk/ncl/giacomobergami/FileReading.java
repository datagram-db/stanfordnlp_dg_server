package uk.ncl.giacomobergami;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Date;
import java.util.Iterator;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

public class FileReading {
    public static void main(String args[]) throws IOException {
        StanfordGraph.reset();
        PropertyGraph.reset();
        AtomicInteger ai = new AtomicInteger(0);
        if (args.length >= 1) {
            BufferedWriter bw = new BufferedWriter(new FileWriter(args[0]+".gsm.txt"));
            Date startDate = new Date();
            Date endDate = new Date(Long.MAX_VALUE);
            var ls = Files.readAllLines(Paths.get(args[0]));
            for (int i = 0; i<ls.size(); i++) {
                System.out.println(i+"/"+ls.size());
                StringBuilder sb = new StringBuilder();
                StanfordGraph
                        .parse(ls.get(i), startDate, endDate)
                        .asYAMLObjectCollection(sb);
                bw.write(sb.toString());
                if (i != (ls.size()-1)) {
                    bw.write("~~\n");
                }
            }
            bw.flush();
            bw.close();
        }
    }
}
