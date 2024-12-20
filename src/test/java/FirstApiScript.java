import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.response.Response;
import io.restassured.response.ValidatableResponse;


import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.baseURI;
import static io.restassured.RestAssured.given;

import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.equalTo;

public class FirstApiScript {
    RequestSpecification requestSpec;
    ResponseSpecification responseSpecification;
    static int userId ;
    String token="887038cf01a22079f1a37406ca8a1cd11940feb6388f4e32b5277b92effb7418";
@BeforeClass
        public void setUp() {

    RequestSpecBuilder requestSpecBuilder=new RequestSpecBuilder();
    requestSpecBuilder.setBaseUri("https://fakerestapi.azurewebsites.net");
    Map<String, String> headers = new HashMap<>();
    headers.put("Content-Type", "application/json");
    headers.put("Accept", "application/json");
    requestSpecBuilder.addHeaders(headers);
    requestSpecBuilder.addQueryParam("id",1);
    requestSpec=requestSpecBuilder.build();

    ResponseSpecBuilder responseSpecBuilder=new ResponseSpecBuilder();
    responseSpecBuilder.expectContentType("application/json");
    responseSpecBuilder.expectStatusCode(200);
    responseSpecification=responseSpecBuilder.build();


}

   @Test(priority = 1)
    public void createUser1(){

        PojoClass pojoClass=new PojoClass();
        pojoClass.setFirstName("zafar");
        pojoClass.setLastName("khan");
        pojoClass.setId(1);
        pojoClass.setIdBook(1);


        Response response=

                given().spec(requestSpec)
                .body(pojoClass)
                        .expect().spec(responseSpecification)
                .when()
                .post("/api/v1/Authors")

                .then()
                .log().body()
                .extract().response();
         userId = response.jsonPath().getInt("id");

        System.out.println(userId);
       System.out.println("user created successfully");

    }

   @Test(priority=2, dependsOnMethods = {"createUser1"})
    public void getUser(){

        given().spec(requestSpec)
                .expect().spec(responseSpecification)
                .when()
                .get("/api/v1/Authors/"+userId)
                .then()
                .statusCode(200)
                .log().body();
        System.out.println(userId);


    }
 @Test(priority=3, dependsOnMethods = {"createUser1"})
public void updateUser(){

    baseURI = "https://fakerestapi.azurewebsites.net";
    PojoClass pojoClass=new PojoClass();
    pojoClass.setFirstName("john");
    pojoClass.setLastName("doe");
    pojoClass.setId(2);
    pojoClass.setIdBook(2);

    Response response=given()
            .auth()
            .oauth2(token)
            .body(pojoClass)
            .contentType("application/json")

            .when()
            .put("/api/v1/Authors/"+userId)

            .then()
            .statusCode(200)
            .log().all()
            .extract().response();
    userId = response.jsonPath().getInt("id");
    System.out.println(userId);
     System.out.println("user updated successfully");


}
 @Test(priority=4, dependsOnMethods = {"updateUser"})
 public void deleteUser(){
    baseURI = "https://fakerestapi.azurewebsites.net";

    given()


            .when()
            .delete("/api/v1/Authors/"+userId)

            .then()
            .statusCode(200)
            .log().headers();
    System.out.println("user deleted successfully   ________");


}




}






