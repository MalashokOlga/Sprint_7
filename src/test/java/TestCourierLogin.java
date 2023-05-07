import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.greaterThan;
import static org.junit.Assert.assertEquals;

public class TestCourierLogin {

    private CourierClient courierClient;
    private Courier courier;
    private int courierId;

    @Before
    public void setUp() {
        courierClient = new CourierClient();
        courier = CourierGenerator.getRandom();
        courierClient.create(courier);
    }

    @Test
    @DisplayName("Check status code is 200 of /api/v1/courier/login")
    @Description("basic test for POST /api/v1/courier/login endpoint")
    public void canLoginCourier() {
        ValidatableResponse loginResponse = courierClient.login(CourierCredentials.from(courier));
        int statusCode = loginResponse.extract().statusCode();
        assertEquals("Неверный код ответа!", 200, statusCode);
        courierId = loginResponse.extract().path("id");
        System.out.println("courierId: " + courierId);
        assertThat("Id курьера не положительное число", courierId, greaterThan(0));
    }

    @After
    public void cleanUp() {
        courierClient.delete(courierId);
    }
}