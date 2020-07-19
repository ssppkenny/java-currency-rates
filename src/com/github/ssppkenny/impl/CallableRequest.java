package com.github.ssppkenny.impl;

import com.github.ssppkenny.api.CurrencyRatesApiService;
import tech.tablesaw.api.Table;

import java.util.concurrent.Callable;

public class CallableRequest implements Callable<Table> {

    private int day, month, year;
    private CurrencyRatesApiService service;
    private String curFrom, curTo;

    public CallableRequest(CurrencyRatesApiService service, String curFrom, String curTo, int day, int month, int year) {
        this.day = day;
        this.month = month;
        this.year = year;
        this.service = service;
        this.curFrom = curFrom;
        this.curTo = curTo;
    }

    @Override
    public Table call() throws Exception {
        return service.getRates(curFrom, curTo, day, month, year);
    }
}
