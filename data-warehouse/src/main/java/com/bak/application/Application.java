package com.bak.application;

import org.apache.commons.configuration.ConfigurationException;
import org.janusgraph.core.SchemaViolationException;
import com.bak.schema.SchemaConstraintRemote;
import io.vertx.rxjava.core.AbstractVerticle;

public class Application extends AbstractVerticle {


  public static void main(String...args) {
   
    try {
      SchemaConstraintRemote rem = new SchemaConstraintRemote("conf/remote-graph.properties");
      try {
        rem.openGraph();
      } catch (ConfigurationException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
      }
      rem.createSchema();
    } catch (SchemaViolationException e) {
      
      System.out.println("Schema already created");
    }

      System.exit(0);
  }
}
