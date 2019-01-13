# Task description
Design and implement a RESTful API (including data model and the backing implementation)
for money transfers between accounts.

## Explicit requirements:
1. You can use Java, Scala or Kotlin.
1. Keep it simple and to the point (e.g. no need to implement any authentication).
1. Assume the API is invoked by multiple systems and services on behalf of end users.
1. You can use frameworks/libraries if you like (except Spring), but don't forget about
requirement #2 – keep it simple and avoid heavy frameworks.
1. The datastore should run in-memory for the sake of this test.
1. The final result should be executable as a standalone program (should not require
a pre-installed container/server).
1. Demonstrate with tests that the API works as expected.

## Implicit requirements:
1. The code produced by you is expected to be of high quality.
1. There are no detailed requirements, use common sense.

# Implementation comment
## Status
[![Build Status](https://travis-ci.org/hutoroff/revolutTestTask.svg?branch=master)](https://travis-ci.org/hutoroff/revolutTestTask)
[![Coverage Status](https://coveralls.io/repos/github/hutoroff/revolutTestTask/badge.svg?branch=master)](https://coveralls.io/github/hutoroff/revolutTestTask?branch=master)
## Build
To build run:
```
mvn clean package
```

## Run
To run application execute after build:
```
java -jar target/revolutTestTask-0.0.1.jar
```

## API Description
 Method | URL | Params | Body | Response | Description
--------|-----|--------|------|----------|-------------
PUT     | /api/account | | { "balance": $initialBalanceValue } | { "balance": $initialBalance } | Creates new account with initial amount of money on balance. Return ID of created account
GET     | /api/account/{id} | {id} - id of account to get | | { "accountId": $requestedAccountId, "balance": $currentBalance } | Provide information for requested by ID account
POST    | /api/transfer | | { "from": $accountIdToTakeMoney, "to": $accountIdToPutMoney, "amount": $amountToTransfer } | | Transfer requested amount of money between two accounts with provided IDs
  