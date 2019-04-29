package com.example.newpc.qrcode.Retrofit;


import com.example.newpc.qrcode.Entities.ResponseTemplate;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.HTTP;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.Url;

public interface APIInterface {
    @HTTP(method = "PUT", hasBody = true)
    Call<ResponseTemplate> doCheck(@Url String url);

    @HTTP(method = "GET",path = "/driver/get_booking/{bookingId}")
    Call<ResponseTemplate> dogetBooking(@Path("bookingId") int bookingId);

}
