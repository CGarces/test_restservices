package com.routestest.routescalculator.dijkstra.model;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

public class Edge {
  private final String id;
  private final Vertex source;
  private final Vertex destination;
  private final LocalDateTime dateTimeDeparture;
  private final LocalDateTime dateTimeArrival;

  /**
   * Edge object to store each route between nodes.
   * 
   * @param id Unique id.
   * @param source Origin node.
   * @param destination Destination node.
   * @param dateTimeDeparture Time of departure.
   * @param dateTimeArrival Time of arrival.
   */
  public Edge(String id, Vertex source, Vertex destination, LocalDateTime dateTimeDeparture,
      LocalDateTime dateTimeArrival) {
    this.id = id;
    this.source = source;
    this.destination = destination;
    this.dateTimeDeparture = dateTimeDeparture;
    this.dateTimeArrival = dateTimeArrival;
  }

  public String getId() {
    return id;
  }

  public Vertex getDestination() {
    return destination;
  }

  public Vertex getSource() {
    return source;
  }

  public int getWeight() {
    return (int) ChronoUnit.MINUTES.between(dateTimeDeparture, dateTimeArrival);
  }

  @Override
  public String toString() {
    return source + " " + destination;
  }

  public LocalDateTime getDateTimeDeparture() {
    return dateTimeDeparture;
  }

  public LocalDateTime getDateTimeArrival() {
    return dateTimeArrival;
  }
}
