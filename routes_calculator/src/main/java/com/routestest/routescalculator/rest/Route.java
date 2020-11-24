package com.routestest.routescalculator.rest;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalTimeSerializer;
import java.time.LocalTime;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Route {

  private Long id;
  private String city;
  private String destiny;

  @JsonDeserialize(using = LocalTimeDeserializer.class)
  @JsonSerialize(using = LocalTimeSerializer.class)
  private LocalTime departure;
  @JsonDeserialize(using = LocalTimeDeserializer.class)
  @JsonSerialize(using = LocalTimeSerializer.class)
  private LocalTime arrival;

  public LocalTime getDeparture() {
    return departure;
  }

  public void setDeparture(LocalTime departure) {
    this.departure = departure;
  }

  public LocalTime getArrival() {
    return arrival;
  }

  public void setArrival(LocalTime arrival) {
    this.arrival = arrival;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getCity() {
    return city;
  }

  public void setCity(String city) {
    this.city = city;
  }

  public String getDestiny() {
    return destiny;
  }

  public void setDestiny(String destiny) {
    this.destiny = destiny;
  }

}
