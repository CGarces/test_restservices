package com.routestest.routescalculator.rest;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

public class CalculatedRoute {
  public class Result {

    /**
     * Arrival to the last node of the path.
     */
    private LocalDateTime dateArrival;
    /**
     * Departure from then first node of the path.
     */
    private LocalDateTime dateDeparture;
    private String path;

    /**
     * Cost in minutes.
     */
    public double getCost() {
      return ChronoUnit.MINUTES.between(dateDeparture, dateArrival);
    }

    public String getPath() {
      return path;
    }

    public void setPath(String path) {
      this.path = path;
    }

    public LocalDateTime getDateArrival() {
      return dateArrival;
    }

    public void setDateArrival(LocalDateTime dateArrival) {
      this.dateArrival = dateArrival;
    }

    public LocalDateTime getDateDeparture() {
      return dateDeparture;
    }

    public void setDateDeparture(LocalDateTime dateDeparture) {
      this.dateDeparture = dateDeparture;
    }

  }

  /**
   * Contains the route with less time cost.
   */
  private Result fastRoute;
  /**
   * Contains the route with less stops.
   */
  private Result shortRoute;

  public CalculatedRoute() {
    this.fastRoute = new Result();
    this.shortRoute = new Result();
  }

  public Result getFastRoute() {
    return fastRoute;
  }

  public void setFastRoute(Result fastRoute) {
    this.fastRoute = fastRoute;
  }

  public Result getShortRoute() {
    return shortRoute;
  }

  public void setShortRoute(Result shortRoute) {
    this.shortRoute = shortRoute;
  }
}
