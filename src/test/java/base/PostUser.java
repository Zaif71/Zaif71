package base;

import com.github.javafaker.Faker;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;
import org.json.JSONObject;
import org.testng.ITestContext;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.given;

public class PostUser {
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
    public void createUser1(ITestContext context){

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
        context.setAttribute("bookingId",bookingId);
        bookingId = response.jsonPath().getInt("bookingid");

        System.out.println(bookingId);
        System.out.println("user created successfully");

    }
}
