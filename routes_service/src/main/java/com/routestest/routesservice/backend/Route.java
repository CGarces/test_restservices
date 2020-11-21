package com.routestest.routesservice.backend;

import java.time.LocalTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "routes")
public class Route {

  @Id
  @Column(name = "id")
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  @Column(name = "city", nullable = false, length = 50)
  private String city;
  @Column(name = "destiny", nullable = false, length = 50)
  private String destiny;

  @Column(name = "departure")
  private LocalTime departure;
  @Column(name = "arrival")
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
