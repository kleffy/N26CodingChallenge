/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.n26.n26codingchallenge.ws;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.google.gson.JsonParser;
import com.n26.n26codingchallenge.controller.CCEngine;
import com.n26.n26codingchallenge.model.Statistics;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.concurrent.ExecutorService;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.container.AsyncResponse;
import javax.ws.rs.container.Suspended;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 *
 * @author mrkle
 */
@Path("")
public class Service {
  private final ExecutorService executorService;
    private final CCEngine cce;
    
    public Service(){
        cce = new CCEngine();
        executorService = java.util.concurrent.Executors.newCachedThreadPool();
    }
    
    @POST
    @Path("transaction")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public void transaction(@Suspended
    final AsyncResponse asyncResponse, final String request){
        executorService.submit(() -> {
            asyncResponse.resume(doTransaction(request));
        });
    }

    private Response doTransaction(String request) {
        JsonElement element = new JsonParser().parse(request);
        int controllerRes = 204;
        if(element instanceof JsonNull){
            // request may be null
        }else{
            controllerRes = cce.transaction(request);
        }
        
        if(controllerRes == 204){
            return Response.noContent().build();
        }else if(controllerRes == 201){
            URI uri = null;
            try {
                uri = new URI("/transaction");
            } catch (URISyntaxException ex) {
                Logger.getLogger(Service.class.getName()).log(Level.SEVERE, null, ex);
            }
            return Response.created(uri).build();
        }
        return Response.ok(request).build();
    }
    
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("statistics")
    public void statistics(@Suspended
    final AsyncResponse asyncResponse){
        executorService.submit(() ->  {
            asyncResponse.resume(doStatistics());
        });
    }

    private Response doStatistics() {
        Statistics s = cce.statistics();
        return Response.ok(new Gson().toJson(s), MediaType.APPLICATION_JSON).build();
    }
}
