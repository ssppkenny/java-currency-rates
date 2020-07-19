package com.github.ssppkenny.impl;

import com.github.ssppkenny.api.CurrencyRatesApiService;
import okhttp3.ResponseBody;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import tech.tablesaw.api.DoubleColumn;
import tech.tablesaw.api.Table;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class CurrencyRatesApiServiceImpl implements CurrencyRatesApiService {

    private CurrencyRatesApiService.Api api;

    public CurrencyRatesApiServiceImpl() {
        api = new Retrofit.Builder().baseUrl("https://fxtop.com/en/historical-exchange-rates.php/").build().create(Api.class);

    }

    @Override
    public Table getRates(String curFrom, String curTo, int day, int month, int year) {

        Table df = Table.create();
        String d = String.format("%02d", day);
        String m = String.format("%02d", month);
        Call<ResponseBody> callRates = api.getRates(curFrom, curTo, d, d, m, m, year - 1, year);
        try {
            Response<ResponseBody> bodyResponse = callRates.execute();
            if (bodyResponse.isSuccessful()) {

                ResponseBody body = bodyResponse.body();
                String s = body.string();
                Document document = Jsoup.parse(s);

                Elements tables = document.select("table[border='1']");
                Element table = tables.get(0);
                Elements rows = table.select("tr");
                List<Double> values = new ArrayList<>();
                for (int j = 2; j < rows.size(); j++) {
                    Elements columns = rows.get(j).select("td");
                    values.add(Double.valueOf(columns.get(1).text()));

                }
                df.addColumns(DoubleColumn.create("Rate", values));
            }


        } catch (IOException e) {

        }


        return df;
    }
}
