/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.n26.n26codingchallenge.dao;

import com.n26.n26codingchallenge.model.Transaction;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author mrkle
 */
public class DAO {
  private static DAO dao;
//    private static Set<Transaction> inMemory;
    private static Map<Long, Double> inMemory;
    
    private DAO(){
        inMemory = new HashMap<>();
    }
    
    public static DAO getDao(){
        if(dao == null){
            dao = new DAO();
        }
        return dao;
    }
    
    public Transaction save(Transaction t){
        inMemory.put(t.getTimestamp(), t.getAmount());
        return t;
    }
    
    public Map<Long, Double> findAll(){
        return inMemory;
    }
}
