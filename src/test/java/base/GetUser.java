package base;

import com.github.javafaker.Faker;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;
import org.testng.ITestContext;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.given;

public class GetUser {
    RequestSpecification requestSpec;
    ResponseSpecification responseSpecification;

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
    @Test()
    public void getUser(ITestContext context){
        int bookingId = (int) context.getAttribute("bookingId");

        given().spec(requestSpec)
                .expect().spec(responseSpecification)
                .when()
                .get("/booking/"+bookingId)
                .then()
                .statusCode(200)
                .log().body();
        // context.getAttribute("bookingId",bookingId);
        System.out.println("user fetch successfully");
        System.out.println(bookingId);


    }
}
