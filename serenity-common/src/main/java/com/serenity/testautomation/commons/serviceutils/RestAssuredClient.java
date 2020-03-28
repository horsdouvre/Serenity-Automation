package com.serenity.testautomation.commons.serviceutils;

import com.google.common.collect.ImmutableMap;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.http.Headers;
import io.restassured.specification.RequestSpecification;
import lombok.extern.slf4j.Slf4j;
import net.serenitybdd.rest.SerenityRest;
import net.thucydides.core.annotations.Step;
import net.thucydides.core.annotations.Steps;

import java.io.File;
import java.util.Map;

@Slf4j
public class RestAssuredClient {

    private static String URL;
    private static String ctSessionUser;
    private static String ctSessionPass;
    private final static String CTSESSION = "CTSESSION";
    private final static String WEBSSO_URL = "";

    private RequestSpecification requestSpecification;

    public RestAssuredClient switchURL(String restURL) {
        URL = restURL;
        return this;
    }

    public static void main(String[] args) {

    }

    public RestAssuredClient setCTSessCredentials(String userName, String password) {
        this.ctSessionUser = userName;
        this.ctSessionPass = password;
        return this;
    }

    private String getCTSession() {
        String ctSession = RestAssured.given()
                .with().contentType("application/x-www-form-urlencoded")
                .with().params(ImmutableMap.<String, String>builder()
                        .put("CTAuthMode", "BASIC")
                        .put("auth mode", "BASIC")
                        .put("ct_orig_uri", "")
                        .put("sso_operation", "authenticate_user")
                        .put("user", ctSessionUser)
                        .put("password", ctSessionPass)
                        .build()
                ).post(WEBSSO_URL).getCookie(CTSESSION);
        return ctSession;
    }

    /**
     * Mandatory to use before triggering any of the REST operations using this class
     */
    public RestAssuredClient initRequest() {
        SerenityRest.clear();
        this.requestSpecification = SerenityRest.given();
        return this;
    }

    /**
     * Method set the security parameters for REST operations
     */
    public RestAssuredClient setRequestAuth(String auth) {
        switch (auth) {
            case "CTSESSION":
                this.requestSpecification = this.requestSpecification.with()
                        .cookie(CTSESSION, getCTSession());
                break;
        }
        return this;
    }

    /**
     * Method to set request headers for REST operations
     */
    public <T> RestAssuredClient setRequetHeaders(Headers headers) {
        this.requestSpecification.headers(headers);
        return this;
    }

    @Step
    public <T> RestAssuredClient post(String endPoint, T bodyContent, Map<String, String> pathParams) {
        this.requestSpecification.baseUri(URL)
                .basePath(endPoint)
                .pathParams(pathParams)
                .contentType(ContentType.JSON)
                .body(bodyContent)
                .when()
                .post();

        return this;
    }

    @Step
    public RestAssuredClient postWithFileBody(String endPoint, File bodyContent, Map<String, String> pathParams) {
        this.requestSpecification.baseUri(URL)
                .basePath(endPoint)
                .pathParams(pathParams)
                .contentType(ContentType.JSON)
                .body(bodyContent)
                .when()
                .post();

        return this;
    }

    @Step
    public <T> RestAssuredClient put(String endPoint, T bodyContent, Map<String, String> pathParams) {
        this.requestSpecification.baseUri(URL)
                .basePath(endPoint)
                .pathParams(pathParams)
                .contentType(ContentType.JSON)
                .body(bodyContent)
                .when()
                .put();

        return this;
    }

    @Step
    public <T> RestAssuredClient get(String endPoint, Map<String, String> pathParams, Map<String, ?> qryParams) {
        this.requestSpecification.baseUri(URL)
                .basePath(endPoint)
                .pathParams(pathParams)
                .contentType(ContentType.JSON)
                .queryParams(qryParams)
                .when()
                .put();

        return this;
    }

    public int getResponseCode() {
        return SerenityRest.lastResponse().getStatusCode();
    }

    public <T> T getResponseBody(Class<T> clazz){
        if(clazz.getSimpleName().equalsIgnoreCase("String")){
            return (T) SerenityRest.lastResponse().getBody().asString();
        }
        return SerenityRest.lastResponse().getBody().as(clazz);
    }

    public RestAssuredClient clearRequestSpec(){
        SerenityRest.clear();
        return this;
    }

}
