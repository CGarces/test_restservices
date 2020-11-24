package com.routestest.routescalculator.dijkstra.model;

import java.time.LocalDateTime;

public class Vertex {

  private final  LocalDateTime departure;
  private final String name;
  private final String id;

  /**
   * Vertex object to store each node stop (location + departure time).
   * @param id Unique id of vertex
   * @param name Location name.
   * @param departure Time of departure from the location
   */
  public Vertex(String id, String name, LocalDateTime departure) {
    this.id = id;
    this.name = name;
    this.departure = departure;
  }

  public String getId() {
    return id;
  }

  public String getName() {
    return name;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((id == null) ? 0 : id.hashCode());
    return result;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null) {
      return false;
    }
    if (getClass() != obj.getClass()) {
      return false;
    }
    Vertex other = (Vertex) obj;
    if (id == null) {
      if (other.id != null) {
        return false;
      }
    } else if (!id.equals(other.id)) {
      return false;
    }
    return true;
  }

  @Override
  public String toString() {
    return name;
  }

  public LocalDateTime getDeparture() {
    return departure;
  }
}
