package com.github.ssppkenny.api;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;
import tech.tablesaw.api.Table;

public interface CurrencyRatesApiService {

    Table getRates(String curFrom, String curTo, int month, int day, int year);

    public interface Api {
        // A=1&C1=CHF&C2=RUB&TR=1&DD1=19&MM1=07&YYYY1=2019&B=1&P=&I=1&DD2=19&MM2=07&YYYY2=2020&btnOK=Go%21
        @GET("/en/historical-exchange-rates.php?A=1&TR=1&B=1&P=&I=1&btnOK=Go%21")
        Call<ResponseBody> getRates(@Query("C1") String curFrom, @Query("C2") String curTo, @Query("DD1") String dayFrom,
                                    @Query("DD2") String dayTo, @Query("MM1") String monthFrom, @Query("MM2") String monthTo, @Query("YYYY1") int yearFrom, @Query("YYYY2") int yearTo);

    }

}
