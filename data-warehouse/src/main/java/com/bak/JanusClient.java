package com.bak;

import org.apache.tinkerpop.gremlin.driver.Client;
import org.apache.tinkerpop.gremlin.driver.Cluster;
import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.GraphTraversalSource;
import org.janusgraph.core.JanusGraph;
import org.janusgraph.core.JanusGraphFactory;
import com.bak.schema.SchemaConstraintRemote;
import org.apache.commons.configuration.Configuration;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.tinkerpop.gremlin.structure.Graph;


public class JanusClient {

  private static GraphTraversalSource graph;
  private static JanusClient janusClient;

  protected Cluster cluster;
  protected Client client;
  protected Configuration conf;

  private JanusClient() {
    SchemaConstraintRemote rem = new SchemaConstraintRemote("conf/remote-graph.properties");
//    graph = JanusGraphFactory.open(getClass().getClassLoader().getResource("janusgraph-cassandra-es.properties").getFile());
    try {
      graph = rem.openGraph();
    } catch (ConfigurationException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
  }

  public static JanusClient getJanusClient() {
    if (janusClient == null) {
      janusClient = new JanusClient();
    }
    return janusClient;
  }

  /**
   * @return the graph
   */
  public GraphTraversalSource getGraph() {
    return graph;
  }

  /**
   * @param graph the graph to set
   */
  public void setGraph(GraphTraversalSource graph) {
    JanusClient.graph = graph;
  }
  
}
