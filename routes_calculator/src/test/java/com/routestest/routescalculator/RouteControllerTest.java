package com.routestest.routescalculator;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.spy;

import java.io.IOException;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Spy;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.routestest.routescalculator.rest.CalculatedRoute;
import com.routestest.routescalculator.rest.Route;
import com.routestest.routescalculator.rest.CalculatorController;

/**
 * Test cases for RouteController class See route.json file to check data used
 *
 */
public class RouteControllerTest {

  @Spy
  private CalculatorController calculatorController;

  private static Route[] routes;

  /**
   * Load route.json file with the test cases
   */
  @BeforeAll
  public static void loadRoutes() {

    ObjectMapper objectMapper = new ObjectMapper();
    try {
      Resource resource = new ClassPathResource("route.json");
      routes = objectMapper.readValue(resource.getInputStream(), Route[].class);
    } catch (JsonParseException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    } catch (JsonMappingException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    } catch (IOException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
  }

  @BeforeEach
  public void setUp() throws Exception {
    calculatorController = spy(CalculatorController.class);
    doReturn(routes).when(calculatorController).getRoutes(); // Mock implementation
  }

  /**
   * Test route with a departure in the next day.
   */
  @Test
  public void testNextDayRoute() {

    ResponseEntity<CalculatedRoute> res = calculatorController.getRoutes("Barcelona", "Alicante");
    CalculatedRoute calculatedRoute = res.getBody();

    // Barcelona-Madrid-Valencia-Alicante.
    assertEquals(485, calculatedRoute.getFastRoute().getCost());
    // Both routes are the same Barcelona-Valencia-Alicante not is valid due the
    // schedule.
    assertEquals(485, calculatedRoute.getShortRoute().getCost());
  }

  /**
   * Test route with two possible combinations.
   */
  @Test
  public void testMultipleRoute() {

    ResponseEntity<CalculatedRoute> res = calculatorController.getRoutes("Barcelona", "Valencia");
    CalculatedRoute calculatedRoute = res.getBody();

    // Barcelona-Madrid-Valencia.
    assertEquals(300, calculatedRoute.getFastRoute().getCost());
    // Barcelona-Valencia.
    assertEquals(420, calculatedRoute.getShortRoute().getCost());

  }

  /**
   * Test invalid route to incorrect destination.
   */
  @Test
  public void testNoroute() {
    ResponseEntity<CalculatedRoute> res = calculatorController.getRoutes("Barcelona", "NotExists");
    assertEquals(res, ResponseEntity.notFound().build());
  }
}
