package ru.hutoroff.interview.revolut.api.controller;

import com.google.gson.Gson;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.jooby.test.JoobyRule;
import org.junit.Assert;
import org.junit.ClassRule;
import org.junit.Test;
import ru.hutoroff.interview.revolut.AbstractIntegrationTest;
import ru.hutoroff.interview.revolut.App;
import ru.hutoroff.interview.revolut.controller.dto.AccountResponse;
import ru.hutoroff.interview.revolut.controller.dto.TransferRequest;
import ru.hutoroff.interview.revolut.data.entity.impl.Account;

import java.math.BigDecimal;

public class TransferControllerTest extends AbstractIntegrationTest {

    @ClassRule
    public static JoobyRule app = new JoobyRule(new App());

    @Test
    public void doTransfer() {
        Long srcAccountId = createAccount(BigDecimal.TEN);
        Long trgtAccountId = createAccount(BigDecimal.ZERO);
        BigDecimal amount = new BigDecimal(5);

        String bodyJson = new Gson().toJson(new TransferRequest(srcAccountId, trgtAccountId, amount));
        RequestSpecification request = RestAssured.given();
        request.header("Content-Type", "application/json");
        request.body(bodyJson);
        Response transferRespone = request.post("/api/transfer");

        Assert.assertEquals(200, transferRespone.getStatusCode());

        AccountResponse srcAccount = getAccount(srcAccountId);
        AccountResponse trgtAccount = getAccount(trgtAccountId);
        Assert.assertEquals(BigDecimal.TEN.add(amount.negate()), srcAccount.balance);
        Assert.assertEquals(BigDecimal.ZERO.add(amount), trgtAccount.balance);
    }

    @Test
    public void doTransferValidationFailure() {
        String bodyJson = new Gson().toJson(new TransferRequest());
        RequestSpecification request = RestAssured.given();
        request.header("Content-Type", "application/json");
        request.body(bodyJson);
        Response transferRespone = request.post("/api/transfer");

        Assert.assertEquals(400, transferRespone.getStatusCode());
    }
}
