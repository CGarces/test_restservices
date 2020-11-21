package com.routestest.routesservice.rest;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.routestest.routesservice.backend.Route;
import com.routestest.routesservice.dao.RoutesDAO;

import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping("routes")
public class RouteController {

  @Autowired
  private RoutesDAO routesDAO;

  @GetMapping
  @ApiOperation(value = "Get all routes from database")
  public ResponseEntity<List<Route>> getRoutes() {
    return ResponseEntity.ok(routesDAO.findAll());
  }

  @RequestMapping(value = "{routeId}")
  @ApiOperation(value = "Get a single route by id")
  public ResponseEntity<Route> getCityById(@PathVariable("routeId") Long routeId) {
    Optional<Route> opRoute = routesDAO.findById(routeId);
    if (opRoute.isPresent()) {
      return ResponseEntity.ok(opRoute.get());
    } else {
      return ResponseEntity.noContent().build();
    }
  }

  @PostMapping
  @ApiOperation(value = "Insert a new route into the database")
  public ResponseEntity<Route> createCity(@RequestBody Route route) {
    Route newRoute = routesDAO.save(route);
    return ResponseEntity.ok(newRoute);
  }

  @DeleteMapping(value = "{routeId}")
  @ApiOperation(value = "Delete the route identifyed by id")
  public ResponseEntity<Void> deleteCity(@PathVariable("routeId") Long routeId) {
    routesDAO.deleteById(routeId);
    return ResponseEntity.ok(null);
  }

  @PutMapping
  @ApiOperation(value = "Update the route", notes = "The route is identified by id")
  public ResponseEntity<Route> updateCity(@RequestBody Route route) {
    Optional<Route> opRoute = routesDAO.findById(route.getId());
    if (opRoute.isPresent()) {
      routesDAO.save(route);
      return ResponseEntity.ok(route);
    } else {
      return ResponseEntity.notFound().build();
    }
  }
}
