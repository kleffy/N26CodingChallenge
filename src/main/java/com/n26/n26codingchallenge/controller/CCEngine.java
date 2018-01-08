/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.n26.n26codingchallenge.controller;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.n26.n26codingchallenge.dao.DAO;
import com.n26.n26codingchallenge.model.Statistics;
import com.n26.n26codingchallenge.model.Transaction;
import java.text.DecimalFormat;
import java.time.Instant;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 *
 * @author mrkle
 */
public class CCEngine {
  
    public int transaction(String request) {
        int response = 204;

        JsonObject jo = (JsonObject) new JsonParser().parse(request);
        String amt = (jo.get("amount") == null || jo.get("amount").getAsString() == null) ? "" : jo.get("amount").getAsString();
        String time = (jo.get("timestamp") == null || jo.get("timestamp").getAsString() == null) ? "" : jo.get("timestamp").getAsString();

        if (amt.isEmpty() || time.isEmpty()) {
            //error
        } else {
            double amount = Double.parseDouble(amt);
            long timestamp = Long.parseLong(time);
            long difference = getNow() - timestamp;
            System.out.println(difference);
            if (difference <= 60000) {
                response = 201;
                Transaction t = new Transaction(amount, timestamp);
                DAO.getDao().save(t);
                System.out.println(DAO.getDao().findAll());
            }
        }
        return response;
    }

    private long getNow() {
        return Instant.now().toEpochMilli();
    }

    public Statistics statistics() {
        Map<Long, Double> allTransaction = DAO.getDao().findAll();
        System.out.println(allTransaction);
        double total = 0;
        double avg = 0;
        double max = 0;
        double min = 0;
        int count = 0;
        if (!allTransaction.isEmpty()) {
            long now = getNow();
            Set<Transaction> ts = new HashSet<>();
            for (Map.Entry entry : allTransaction.entrySet()) {
                long time = (long) entry.getKey();
                long difference = now - time;
                if (difference <= 60000) {
                    count++;
                    double amount = (double) entry.getValue();
                    total += amount;
                    ts.add(new Transaction(amount, time));
                }
            }
            if (!ts.isEmpty()) {
                Transaction maxT = ts.stream().max((p1, p2) -> Double.compare(p1.getAmount(), p2.getAmount())).get();
                Transaction minT = ts.stream().min((s, y) -> Double.compare(s.getAmount(), y.getAmount())).get();
                max = maxT.getAmount();
                min = minT.getAmount();
            }
            if (count != 0) {
                avg = total / count;
            }
            total = cleanNumber(total);
            avg = cleanNumber(avg);
        }

        Statistics s = new Statistics(total, avg, max, min, count);
        return s;
    }

    private double cleanNumber(double total) {
        String t = new DecimalFormat(".##").format(total);
        return Double.parseDouble(t);
    }

}
