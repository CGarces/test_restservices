package com.routestest.routescalculator.rest;

import com.routestest.routescalculator.dijkstra.engine.DijkstraAlgorithm;
import com.routestest.routescalculator.dijkstra.model.Edge;
import com.routestest.routescalculator.dijkstra.model.Graph;
import com.routestest.routescalculator.dijkstra.model.Vertex;
import com.routestest.routescalculator.rest.CalculatedRoute.Result;
import io.swagger.annotations.ApiOperation;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
public class RouteController {
  @Autowired
  private RestTemplate restTemplate;

  private HashMap<String, Vertex> nodes;
  private List<Edge> edges;

  public Route[] getRoutes() {
    // Encapsulate the rest call to made more easy mock it.
    //return restTemplate.getForObject("http://routes-service/routes", Route[].class);
    ResponseEntity<Route[]> res = restTemplate
                .exchange("http://routes-service/routes", HttpMethod.GET, null,
                        new ParameterizedTypeReference<Route[]>() {});
    return res.getBody();

  }

  @RequestMapping(path = "/{origin}/{destination}", method = RequestMethod.GET)
  @ApiOperation(value = "Find the fastest route and the route with less stops between two cities")
  public ResponseEntity<CalculatedRoute> getRoutes(@PathVariable String origin, 
                                                   @PathVariable String destination) {

    // Populate the nodes and vertex variables
    populateNodes();
    Graph graph = new Graph(new ArrayList<Vertex>(nodes.values()), edges);
    CalculatedRoute calculatedRoute = calculateGraph(graph, origin, destination);
    // TODO check nulls
    if (calculatedRoute.getFastRoute().getPath() == null) {
      return ResponseEntity.notFound().build();
    } else {
      return ResponseEntity.ok(calculatedRoute);
    }
  }

  /**
   * Populate two matrix (nodes and edges) to use it as base for the Dijkstra
   * algorithm 1.-Create one node per location per departure time. 2.-Create one
   * node per location per arrival time. 3.-Create one node per location per
   * departure time. 4.-Create edges to connect departures to arrivals. 5.-Create
   * edges to connect nodes in the same location to the nearest future time.
   */
  private void populateNodes() {
    Route[] routes = getRoutes();
    nodes = new HashMap<>();
    edges = new ArrayList<Edge>();

    for (Route route : routes) {
      // Convert to LocalDateTime to add 24h if need
      LocalDateTime dateTimeArrival = LocalDateTime.of(LocalDate.now(), route.getArrival());
      LocalDateTime dateTimeDeparture = LocalDateTime.of(LocalDate.now(), route.getDeparture());
      if (dateTimeDeparture.isAfter(dateTimeArrival)) {
        dateTimeArrival = dateTimeArrival.plusDays(1);
      }

      // Create one node per origin per departure time.
      String originId = route.getCity() + "_" + dateTimeDeparture.toString();
      if (!nodes.containsKey(originId)) {
        nodes.put(originId, new Vertex(originId, route.getCity(), dateTimeDeparture));
      }
      // Create one node per destination per arrival time.
      String destinationId = route.getDestiny() + "_" + dateTimeArrival.toString();
      if (!nodes.containsKey(destinationId)) {
        nodes.put(destinationId, new Vertex(destinationId, route.getDestiny(), dateTimeArrival));
      }
      // Create edges to connect departures to arrivals.
      Edge lane = new Edge("Edge_" + route.getId(), nodes.get(originId), nodes.get(destinationId), 
          dateTimeDeparture, dateTimeArrival);
      edges.add(lane);
    }

    // Create edges to connect a given node to the node belonging to the same
    // location at the nearest future time.
    for (Vertex node : nodes.values()) {
      String cityName = node.getName();
      LocalDateTime nodeDeparture = node.getDeparture();
      Vertex bestNode = null;
      // Find other vertex in the same location.
      for (Vertex nearestnode : nodes.values()) {
        if ((!nearestnode.getId().equals(node.getId())) && nearestnode.getName().equals(cityName)) {
          LocalDateTime nearestDeparture = nearestnode.getDeparture();
          if (nearestDeparture.isAfter(nodeDeparture)) {
            if (bestNode == null) {
              bestNode = nearestnode;
            } else if (bestNode.getDeparture().isAfter(nearestDeparture)) {
              bestNode = nearestnode;
            }
          }
        }
      }
      if (bestNode != null) {
        // add new one that connect current vertex with the next available Departure
        // time.
        Edge newLane = new Edge(cityName + "_" + bestNode.getDeparture().toString(), 
            node, bestNode, nodeDeparture, bestNode.getDeparture());
        edges.add(newLane);
      }
    }
  }

  /**
   * Calculate the best routes.
   * 
   * @param graph           graph with vertex and nodes to resolve the path search
   * @param cityOrigin      node of origin
   * @param cityDestination node of destination
   * @return The two possible routes (the fastest and the one with less stops)
   */
  private CalculatedRoute calculateGraph(Graph graph, String cityOrigin, String cityDestination) {
    DijkstraAlgorithm dijkstra = new DijkstraAlgorithm(graph);
    int shortestPath = Integer.MAX_VALUE;
    CalculatedRoute calculatedRoute = new CalculatedRoute();
    for (Vertex origin : nodes.values()) {
      if (origin.getName().equals(cityOrigin)) {
        dijkstra.execute(origin);
        for (Vertex destination : nodes.values()) {
          if (destination.getName().equals(cityDestination)) {
            LinkedList<Vertex> path = dijkstra.getPath(destination);
            if (path != null) {
              if (shortestPath > path.size()) {
                shortestPath = path.size();
                calculatedRoute.setShortRoute(populateResult(path));
              }
              double pathCost = ChronoUnit.MINUTES.between(path.get(0).getDeparture(), 
                  path.getLast().getDeparture());
              if (calculatedRoute.getFastRoute().getPath() == null) {
                calculatedRoute.setFastRoute(populateResult(path));
              } else if (calculatedRoute.getFastRoute().getCost() > pathCost) {
                calculatedRoute.setFastRoute(populateResult(path));
              }
            }
          }
        }
      }
    }
    return calculatedRoute;
  }

  /**
   * Fill the route information with the path found.
   * 
   * @param path Object with all the vertex in the solution.
   * @return route details with initial arrival and final destination dates.
   */
  private Result populateResult(LinkedList<Vertex> path) {
    Result routeResult = new CalculatedRoute().new Result();
    routeResult.setDateArrival(path.getLast().getDeparture());
    routeResult.setPath(path.toString());
    routeResult.setDateDeparture(path.get(0).getDeparture());
    return routeResult;
  }

}
