package com.routestest.routesservice.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.routestest.routesservice.backend.Route;

public interface RoutesDAO extends JpaRepository<Route, Long> {

}
