package com.example.emptytest.testglovo.data.repositories.restful;

import com.example.emptytest.testglovo.BuildConfig;

public class ApiUtils {

    private ApiUtils() {}

    public static final String BASE_URL = BuildConfig.HOST + BuildConfig.PATH;

    public static APIService getAPIService() {
        return RetrofitClient.getClient(BASE_URL).create(APIService.class);
    }
}
