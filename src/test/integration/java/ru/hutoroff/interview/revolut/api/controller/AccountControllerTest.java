package ru.hutoroff.interview.revolut.api.controller;

import static io.restassured.RestAssured.get;
import static io.restassured.RestAssured.put;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertEquals;

import com.google.gson.Gson;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.jooby.test.JoobyRule;
import org.jooby.test.MockRouter;
import org.junit.Assert;
import org.junit.ClassRule;
import org.junit.Ignore;
import org.junit.Test;
import ru.hutoroff.interview.revolut.AbstractIntegrationTest;
import ru.hutoroff.interview.revolut.App;
import ru.hutoroff.interview.revolut.controller.dto.AccountCreationResponse;
import ru.hutoroff.interview.revolut.controller.dto.AccountResponse;
import ru.hutoroff.interview.revolut.data.entity.impl.Account;

import java.math.BigDecimal;

public class AccountControllerTest extends AbstractIntegrationTest {

  @ClassRule
  public static JoobyRule app = new JoobyRule(new App());

  @Test
  public void createAccount() {
    String bodyJson = new Gson().toJson(new Account(BigDecimal.TEN));
    RequestSpecification request = RestAssured.given();
    request.header("Content-Type", "application/json");
    request.body(bodyJson);

    Response response = request.put("/api/account");
    Assert.assertEquals(200, response.getStatusCode());

    AccountCreationResponse respObj = parseResponse(response, AccountCreationResponse.class);
    Assert.assertNotNull(respObj);
    Assert.assertNotNull(respObj.accountId);
  }

  @Test
  public void createAccountBalanceRequired() {
    String bodyJson = new Gson().toJson(new Account());
    RequestSpecification request = RestAssured.given();
    request.header("Content-Type", "application/json");
    request.body(bodyJson);

    Response response = request.put("/api/account");
    Assert.assertEquals(400, response.getStatusCode());
  }

  @Test
  public void getAccount() {
    Long accountId = createAccount(BigDecimal.TEN);
    RequestSpecification request = RestAssured.given();

    Response response = request.get("/api/account/" + accountId);
    Assert.assertEquals(200, response.getStatusCode());

    AccountResponse accountResponse = parseResponse(response, AccountResponse.class);
    Assert.assertEquals(accountId, accountResponse.accountId);
    Assert.assertEquals(BigDecimal.TEN, accountResponse.balance);
  }

  @Test
  public void getAccountNotFound() {
    RequestSpecification request = RestAssured.given();
    Response response = request.get("/api/account/" + -1);

    Assert.assertEquals(404, response.getStatusCode());
  }
}
