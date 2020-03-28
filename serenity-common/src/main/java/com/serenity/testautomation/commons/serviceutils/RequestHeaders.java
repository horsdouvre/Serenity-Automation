package com.serenity.testautomation.commons.serviceutils;

import io.restassured.http.Header;
import io.restassured.http.Headers;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public enum RequestHeaders {
    SAMPLE_HEADER(new ArrayList<Header>(){{
        add(new Header("Accept", "application/json"));
    }});

    private Headers headers;

    RequestHeaders(List<Header> headerList){
        this.headers = new Headers(headerList);
    }

}
