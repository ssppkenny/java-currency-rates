package com.github.ssppkenny;

import com.github.ssppkenny.impl.JobExecutor;
import tech.tablesaw.api.Table;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Main {
    public static void main(String[] args) {

        ExecutorService executor = Executors.newFixedThreadPool(4);
        JobExecutor jobExecutor= new JobExecutor(executor);
        Table rates = jobExecutor.getRates("CHF", "RUB", 3);
        System.out.println(rates.rowCount());
        executor.shutdown();

    }
}
