package uk.ncl.giacomobergami;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Date;
import java.util.Iterator;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

public class FileReading {
    public static void main(String args[]) throws IOException {
        if (args.length >= 2) {
            String command = args[0];
            String file = args[1];
            if (command.equals("file")) {
                writeFile(file);
            } else {
                Files.list(Path.of(file)).forEach(x -> {
                    try {
                        FileReading.writeFile(x);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                });
            }
        }

    }

    private static void writeFile(String file) throws IOException {
        writeFile(Paths.get(file));
    }

    private static void writeFile(Path p) throws IOException {
        StanfordGraph.reset();
        PropertyGraph.reset();
        AtomicInteger ai = new AtomicInteger(0);
        {
            BufferedWriter bw = new BufferedWriter(new FileWriter(p.toString() +".gsm.txt"));
            Date startDate = new Date();
            Date endDate = new Date(Long.MAX_VALUE);
            var ls = Files.readAllLines(p);
            for (int i = 0; i<ls.size(); i++) {
                if ((i % 100 )==0)
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
