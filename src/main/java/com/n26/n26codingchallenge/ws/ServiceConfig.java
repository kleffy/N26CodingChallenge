/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.n26.n26codingchallenge.ws;

import java.util.HashSet;
import java.util.Set;
import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;

/**
 *
 * @author mrkle
 */
@ApplicationPath("service")
public class ServiceConfig extends Application{
  private final Set<Object> singletons = new HashSet<>();
    private final Set<Class<?>> resources = new HashSet<>();

    public ServiceConfig() {
        singletons.add(new Service());
    }

    @Override
    public Set<Object> getSingletons() {
        return singletons;
    } 

    @Override
    public Set<Class<?>> getClasses() {
        return resources;
    }
}
