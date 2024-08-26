# stanfordnlp_dg_server

This project provides an HTTP server returning an enriched dependency graph returned by Stanford NLP represented as a property graph. This code was extracted from this project coming from Giacomo Bergami's dissertation: https://bitbucket.org/unibogb/sherlock/src

While ```FileReading.java``` provides a program for parsing an entire file of sentences, ```SNLP_Python.java``` provides a simple entrypoint for all the services (Dependency Parsing and Time Unit extractions). The HTTP service variant is available through the following class: ```Main.java```. This acts as the main entrypoint for the jar generated at compile time. To generate a jar with the full set of dependencies, please run the following command:

```bash
mvn clean compile assembly:single
```

As this uses an older version of the library, please use also an older version for Maven (3.6.3).
