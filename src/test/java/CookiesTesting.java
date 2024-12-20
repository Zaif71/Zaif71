
import io.restassured.http.Header;
import io.restassured.http.Headers;
import io.restassured.response.Response;
import org.testng.annotations.Test;

import java.io.IOException;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.equalTo;

public class CookiesTesting {
    @Test
    public void testCookie() throws IOException {

        String filePath = "C:\\Users\\zaffar\\OneDrive\\Desktop\\RestAssuredAPI\\ApiTestScript\\src\\test\\ApiTestData\\ApiTestData.xlsx";


        String baseURL = ExcelReader.getCellData(filePath, "Sheet1", 1, 0);
        String endpoint = ExcelReader.getCellData(filePath, "Sheet1", 1, 1);
        String paramKey = ExcelReader.getCellData(filePath, "Sheet1", 1, 2);
        String paramValue= ExcelReader.getCellData(filePath, "Sheet1", 1, 3);


        baseURI = baseURL;
        Response response=
                given()
                        .log().uri()
                        .queryParam(paramKey,paramValue)
                        .cookies(Util_Class.cookie())

                .when()
                    .get(endpoint);
        response.then()
                    .statusCode(200)
                    .log().all();

          String myHeader= response.getHeader("Content-Type");
          System.out.println("header value is---"+myHeader);
          Headers allHeaders=response.getHeaders();
          for (Header hd:allHeaders){

              System.out.println(hd.getName()+"    "+hd.getValue());

          }



    }



}
