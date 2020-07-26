ARG VERSION=8u212-jdk

FROM openjdk:${VERSION}

ARG JANUS_VERSION=0.4.1
RUN ls && pwd
RUN wget https://github.com/JanusGraph/janusgraph/releases/download/v${JANUS_VERSION}/janusgraph-${JANUS_VERSION}-hadoop2.zip && \
    unzip janusgraph-${JANUS_VERSION}-hadoop2.zip

WORKDIR /janusgraph-${JANUS_VERSION}-hadoop2

ADD http-janusgraph-cassandra-es-server.properties conf/gremlin-server/http-janusgraph-cassandra-es-server.properties 

ADD http-gremlin-server.yaml conf/gremlin-server/http-gremlin-server.yaml

CMD ["bin/gremlin-server.sh", "./conf/gremlin-server/http-gremlin-server.yaml"]

