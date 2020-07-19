package com.github.ssppkenny.impl;

import com.github.ssppkenny.api.CurrencyRatesApiService;
import tech.tablesaw.api.DoubleColumn;
import tech.tablesaw.api.Table;

import java.time.LocalDate;
import java.util.Collections;
import java.util.concurrent.*;
import java.util.stream.IntStream;

public class JobExecutor {

    private ExecutorService executor;

    public JobExecutor(ExecutorService executor) {
        this.executor = executor;
    }

    private static CurrencyRatesApiService currencyRatesApiService = new CurrencyRatesApiServiceImpl();

    public Table getRates(String curFrom, String curTo, int years) {

        LocalDate now = LocalDate.now();
        int year = now.getYear();
        int month = now.getMonthValue();
        int day = now.getDayOfMonth();
        return getRates(curFrom, curTo, day, month, year - years, year);
    }

    private Table getRates(String curFrom, String curTo, int day, int month, int yearFrom, int yearTo) {

        CompletionService<Table> completionService = new ExecutorCompletionService<>(executor);

        IntStream.range(yearFrom, yearTo).boxed().sorted(Collections.reverseOrder()).
                forEach(year -> completionService.submit(new CallableRequest(currencyRatesApiService, curFrom, curTo, day, month, year)));

        Table rates = Table.create();
        rates.addColumns(DoubleColumn.create("Rate", 0));

        IntStream.range(yearFrom, yearTo).
                forEach(
                        year -> completionService.submit(new CallableRequest(currencyRatesApiService, curFrom, curTo, day, month, year)));
        IntStream.range(yearFrom, yearTo).
                forEach(
                        year -> {
                            try {
                                Future<Table> future = completionService.take();
                                Table t = future.get();
                                rates.append(t);
                            } catch (Exception e) {

                            }
                        });


        return rates;
    }

}
