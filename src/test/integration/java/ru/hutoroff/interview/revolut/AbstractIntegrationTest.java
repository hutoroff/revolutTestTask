package ru.hutoroff.interview.revolut;

import com.google.gson.Gson;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import ru.hutoroff.interview.revolut.controller.dto.AccountResponse;
import ru.hutoroff.interview.revolut.data.entity.impl.Account;

import java.math.BigDecimal;

public abstract class AbstractIntegrationTest {
    private Long getAccountId(Response response) {
        Number accountId = response.jsonPath().get("accountId");
        return accountId.longValue();
    }

    protected Long createAccount(BigDecimal balance) {
        String bodyJson = new Gson().toJson(new Account(balance));
        RequestSpecification request = RestAssured.given();
        request.header("Content-Type", "application/json");
        request.body(bodyJson);
        Response creationRespone = request.put("/api/account");
        return getAccountId(creationRespone);
    }

    protected AccountResponse getAccount(Long accountId) {
        RequestSpecification request = RestAssured.given();
        Response response = request.get("/api/account/" + accountId);
        return parseResponse(response, AccountResponse.class);
    }

    protected <T> T parseResponse(Response response, Class<T> clazz) {
        String responseJson = response.jsonPath().prettify();
        return new Gson().fromJson(responseJson, clazz);
    }
}
