package com.miro.test.api;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

import static com.miro.test.configs.ConfigManager.getApiUri;
import static com.miro.test.configs.ConfigManager.getDefaultAccessToken;
import static io.restassured.RestAssured.given;

public class BaseApi {
    private String basePath;


    public BaseApi(String basePath) {
        this.basePath = basePath;
    }
    protected Response post(RequestSpecification requestSpecification) {
        return given()
                .spec(requestSpecification)
                .when()
                .post();
    }

    protected Response post(RequestSpecification requestSpecification, String path) {
        return given()
                .spec(requestSpecification)
                .when()
                .post(path);
    }

    protected Response get(RequestSpecification requestSpecification, String path) {
        return given()
                .spec(requestSpecification)
                .when()
                .get(path);
    }

    protected Response get(RequestSpecification requestSpecification) {
        return given()
                .spec(requestSpecification)
                .when()
                .get();
    }

    protected RequestSpecification baseRequest() {
        return new RequestSpecBuilder()
                .setBaseUri(getApiUri())
                .setBasePath(basePath)
                .addFilter(new RequestLoggingFilter())
                .addFilter(new ResponseLoggingFilter())
                .addHeader("Authorization", "Bearer " + getDefaultAccessToken())
                .build();
    }
}
