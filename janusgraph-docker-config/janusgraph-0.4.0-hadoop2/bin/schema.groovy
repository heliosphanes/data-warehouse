
    JanusGraphManagement management = graph.openManagement(); 
    boolean created = false;

  // properties
    PropertyKey symbol = management.makePropertyKey("symbol").dataType(String.class).make();
    PropertyKey information = management.makePropertyKey("information").dataType(String.class).make();
    PropertyKey outputSize = management.makePropertyKey("outputSize").dataType(String.class).make();
    PropertyKey timeZone = management.makePropertyKey("timeZone").dataType(String.class).make();
    PropertyKey lastRefreshed = management.makePropertyKey("lastRefreshed").dataType(String.class).make();
    PropertyKey date = management.makePropertyKey("date").datatype(Date.class).make();
    PropertyKey open = management.makePropertyKey("open").datatype(Double.class).make();
    PropertyKey high = management.makePropertyKey("high").datatype(Double.class).make();
    PropertyKey low = management.makePropertyKey("low").datatype(Double.class).make();
    PropertyKey close = management.makePropertyKey("close").dataType(Double.class).make();
    PropertyKey adjustedClose = management.makePropertyKey("adjustedClose").dataType(Double.class).make();
    PropertyKey dividendAmount = management.makePropertyKey("dividendAmount").dataType(Double.class).make();
    PropertyKey splitCoefficient = management.makePropertyKey("splitCoefficient").dataType(Double.class).make();
    PropertyKey volume = management.makePropertyKey("volume").dataType(Integer.class).make();
    PropertyKey dailySeries = management.makePropertyKey("dailySeries").dataType(Date.class).make();
    PropertyKey info = management.makePropertyKey("info").datatype(String.class).make();

    // vertex labels
    Vertex stock = management.makeVertexLabel("stock").make();
    management.addProperties(stock, symbol, information, outputSize, + timeZone, lastRefreshed, date, open, + high, low, close, adjustedClose, + dividendAmount, splitCoefficient, volume, dailySeries,info);


    //composite indexes
    management.buildIndex(BY_USERNAME_COMPOSITE, Vertex.class).addKey(+SYMBOL+).indexOnly(+STOCK+).buildMixedIndex(SEARCH);
    management.buildIndex(BY_DAILY_MIX, Vertex.class).addKey(+DAILY_SERIES+).buildMixedIndex(SEARCH);

    

    management.commit();

