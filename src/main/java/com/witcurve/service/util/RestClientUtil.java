package com.witcurve.service.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import okhttp3.*;

import java.io.IOException;

public class RestClientUtil {

    private static final MediaType MediaTypeJSON = MediaType.parse("application/json; charset=utf-8");

    public static Response post(String url, Object requestBody) throws IOException {
            OkHttpClient client = new OkHttpClient();
            ObjectMapper mapper = new ObjectMapper();
            String objectJson = mapper.writeValueAsString(requestBody);

            Request request = new Request.Builder().url(url)
                .post(RequestBody.create(MediaTypeJSON, objectJson)).build();

            Call call = client.newCall(request);
            return call.execute();
    }
}
