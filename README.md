# Demo microservice project with Security implementation

Demo project using microservices, Spring security with JWT implementation and caching JWT with Redis

## Description:
In this project present 4 microservices:
- service-registry: Eureka server registry;
- gateway: API Gateway for routing between microservices;
- auth-service: Microservice that contains all authentication and validation logic;
- user-service: Microservice that contains GET endpoint for testing purposes;

### Used tools/technologies

![Java](https://img.shields.io/badge/java-%23ED8B00.svg?style=for-the-badge&logo=openjdk&logoColor=white)
![Spring](https://img.shields.io/badge/spring-%236DB33F.svg?style=for-the-badge&logo=spring&logoColor=white)
![JWT](https://img.shields.io/badge/JWT-black?style=for-the-badge&logo=JSON%20web%20tokens)
![Postgres](https://img.shields.io/badge/postgres-%23316192.svg?style=for-the-badge&logo=postgresql&logoColor=white)
![Redis](https://img.shields.io/badge/redis-%23DD0031.svg?style=for-the-badge&logo=redis&logoColor=white)
![Postman](https://img.shields.io/badge/Postman-FF6C37?style=for-the-badge&logo=postman&logoColor=white)
![Git](https://img.shields.io/badge/git-%23F05033.svg?style=for-the-badge&logo=git&logoColor=white)
![GitHub](https://img.shields.io/badge/github-%23121011.svg?style=for-the-badge&logo=github&logoColor=white)

## Overview

I created few microservices.
Firstly i create and launch Eureka Server to register other microservices. By default Eureka work on port **8761**.
I dont have much configuration on this service so let's just skip it.

### Gateway
Gateway listen port **8765** and have implemented Redise cache for valid tokens.
In configuration file have routes config for 2 services:
- AUTH-SERVICE;
- USER-SERVICE;

In general how it work: User enter web service (example.com or whatever...) and want to use its functions. He can do it by logging in, or register. And the he can for example see list of users.
For login or register user fill form on front, and then that form goes to **GATEWAY**, gateway check AuthFilter to wich endpoint user trying to get.
Sees that it's login/registration endpoint and give access to that service without token.

If user trying to reach user-service without provided token - access denied.

If user trying to reach user-service WITH token, gateway checks if that token is present in Redise cache:
- **IF NOT:** make request with provided token to auth-service to validate that token. If validation successfull, user have access to user-service and that token is saved to Redis cache with 24 hrs exp time.
- **IF PRESENT:** give user access to service without any request to auth-service

### Auth-service (/auth)
Contains all logic for authentication purposes. Have 3 endpoints:

|METHOD|ROUTE|ARGS|DESC|
|-|-|-|-|
|POST|/register|AuthRR|To register new user|
|POST|/token|AuthRR|To login existed user/Return token|
|POST|/validate|AuthRR|To validate token|

/register and /token take AuthRR as request body, that AuthRR looks like this:
```
private int statusCode;
private String message;
private String token;
private String expirationDate;

private String username;
private String password;
private String email;
private String role;

private UserCredential user;
```
For testing i use Postman:
/register
```
{
    "username":"4exan",
    "password":"4exan",
    "email":"ivan.chekhanovskyi@gmail.com",
    "role":"ADMIN"
}
```
/token
```
{
    "username":"4exan",
    "password":"4exan",
}
```
For /validate i did some stupid shit and create another dto wich contains only token. Maybe in future i discover some more professional way, btw it's demo so nvm...

### JWT
For JWT configuration i used config from delivery project but kinda edited so i can implement it for microservice architecture.
At general default config exept method validateToken() which in this case validate token by taking username from that token -> search in db user with that username:
- **IF PRESENT:** Good, now check isTokenExpired(), if not -> return statusCode 200 and let user use out service;
- **IF EMPTY:** Bad, validation failed -> return status 500 or whatever and denie access;

### User-service (/user)
Made only for testing purposes, have only 2 endpoint:
|METHOD|ROUTE|ARGS|DESC|
|-|-|-|-|
|GET|/get-all|-|Return all users in db|
|GET|/{id}|userId|Return user with given id if exist|
|GET|/test|-|Testin purpose|
