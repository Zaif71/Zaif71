package base;

import com.github.javafaker.Faker;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.response.Response;


import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;
import org.json.JSONObject;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.given;

import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.equalTo;

public class BookingApi {
    RequestSpecification requestSpec;
    ResponseSpecification responseSpecification;
    static int bookingId ;
    private static final Faker faker = new Faker();
    String token="YWRtaW46cGFzc3dvcmQxMjM";
    String username = ConfigReader.getProperty("username");
    String password = ConfigReader.getProperty("password");

    @BeforeClass
    public void setUp() {

        RequestSpecBuilder requestSpecBuilder=new RequestSpecBuilder();
        requestSpecBuilder.setBaseUri("https://restful-booker.herokuapp.com");
        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/json");
        headers.put("Accept", "application/json");
        requestSpecBuilder.addHeaders(headers);
        requestSpec=requestSpecBuilder.build();

        ResponseSpecBuilder responseSpecBuilder=new ResponseSpecBuilder();
        responseSpecBuilder.expectContentType("application/json");
        responseSpecBuilder.expectStatusCode(200);
        responseSpecification=responseSpecBuilder.build();


    }

    @Test(priority = 1)
    public void createUser1(){

        String firstName = faker.name().firstName();
        String lastName = faker.name().lastName();
        int totalPrice = faker.number().numberBetween(100, 5000);
        boolean depositPaid = faker.bool().bool();
        String additionalNeeds = faker.food().dish();
        String checkInDate = "2023-03-25"; // Static or can use Faker for dynamic
        String checkOutDate = "2023-03-30"; // Static or can use Faker for dynamic

        // Create JSON objects for the booking payload
        JSONObject booking = new JSONObject();
        JSONObject bookingDates = new JSONObject();

        // Add data to bookingDates
        bookingDates.put("checkin", checkInDate);
        bookingDates.put("checkout", checkOutDate);

        // Add data to booking
        booking.put("firstname", firstName);
        booking.put("lastname", lastName);
        booking.put("totalprice", totalPrice);
        booking.put("depositpaid", depositPaid);
        booking.put("additionalneeds", additionalNeeds);
        booking.put("bookingdates", bookingDates);



        Response response=

                given().spec(requestSpec)
                        .body(booking.toString())
                        .expect().spec(responseSpecification)

                        .when()
                        .post("/booking")

                        .then()
                        .log().body()
                        .extract().response();
        bookingId = response.jsonPath().getInt("bookingid");

        System.out.println(bookingId);
        System.out.println("user created successfully");

    }

    @Test(priority=3, dependsOnMethods = {"createUser1"})
    public void getUser(){

        given().spec(requestSpec)
                .expect().spec(responseSpecification)
                .when()
                .get("/booking/"+bookingId)
                .then()
                .statusCode(200)
                .log().body();
        System.out.println("user fetch successfully");
        System.out.println(bookingId);


    }
    @Test(priority=4, dependsOnMethods = {"createUser1"})
    public void updateUser(){

        JSONObject booking = new JSONObject();
        JSONObject bookingDates = new JSONObject();

        booking.put("firstname", "zafar");
        booking.put("lastname", "tech");
        booking.put("totalprice", 1000);
        booking.put("depositpaid", true);
        booking.put("additionalneeds", "breakfast");
        booking.put("bookingdates", bookingDates);

        bookingDates.put("checkin", "2023-03-25");
        bookingDates.put("checkout", "2023-03-30");

        Response response=given()
                .auth().preemptive().basic(username,password)
                .spec(requestSpec)
                .body(booking.toString())
                .when()
                .put("/booking/"+bookingId)
                .then()
                .statusCode(200)
                .log().all()
                .extract().response();
        System.out.println(bookingId);
        System.out.println("user updated successfully");


    }
    @Test(priority=5, dependsOnMethods = {"updateUser"})
    public void deleteUser(){
        Response deleteResponse =given()

                .auth().preemptive().basic(username,password)
                .spec(requestSpec)

                .when()
                .delete("/booking/"+bookingId)

                .then()
                .statusCode(201)
                .body( equalTo("Created"))
                .extract()
                        .response();
        System.out.println("Headers: " + deleteResponse.getHeaders());

        System.out.println("user deleted successfully   ________");


    }
    @Test(priority=6,dependsOnMethods = {"deleteUser"})
    public void negativeTest1(){
        given().spec(requestSpec)

                .when()
                .get("/booking/"+bookingId)
                .then()
                .statusCode(404)
                .body( equalTo("Not Found"))
                .log().body();
        System.out.println("user deleted successfully");

    }

    //try to hit the request without giving authorization

    @Test(priority = 2,dependsOnMethods = {"createUser1"})
    public void negativeTest2(){
        given().spec(requestSpec)

                .when()
                .delete("/booking/"+bookingId)
                .then()
                .statusCode(403)
                .body( equalTo("Forbidden"))
                .log().body();
        System.out.println("unauthorised user");
    }
    // try to create the user without body
    @Test()
    public void negativeTest3(){
        given().spec(requestSpec)


                .when()
                .post("/booking")
                .then()
                .statusCode(500)
                .body( equalTo("Internal Server Error"))
                .log().body();
        System.out.println("can not create user without body");
    }
    @Test
    public void negativeTest4(){
        given().spec(requestSpec)
                .when()
                .then();



    }



}







