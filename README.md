# stanfordnlp_dg_server

This project provides an HTTP server returning an enriched dependency graph returned by Stanford NLP represented as a property graph. This code was extracted from this project coming from Giacomo Bergami's dissertation: https://bitbucket.org/unibogb/sherlock/src

## Java

While ```FileReading.java``` provides a program for parsing an entire file of sentences, ```SNLP_Python.java``` provides a simple entrypoint for all the services (Dependency Parsing and Time Unit extractions). The HTTP service variant is available through the following class: ```Main.java```. This acts as the main entrypoint for the jar generated at compile time. To generate a jar with the full set of dependencies, please run the following command:

```bash
mvn clean compile assembly:single
```

As this uses an older version of the library, please use also an older version for Maven (3.6.3).

## Python

This project can be also installed in Python from the remote repository using git at the following command:

```bash
pip3 install git+https://github.com/datagram-db/stanfordnlp_dg_server.git@main
```

If you prefer to download the sourcecode, you can always install it as follows:

```bash
pip3 install .
```

This provides a singleton class that can access all of the aforementioned services without using Java as a service:

```python3
from StanfordNLPExtractor.OldWrapper import OldWrapper
nlp = OldWrapper.getInstance()
gsm = nlp.generateGSMDatabase(["Hello, beautiful world!"])
tunits = nlp.getTimeUnits(["Yesterday I was feeling ill"])
```
