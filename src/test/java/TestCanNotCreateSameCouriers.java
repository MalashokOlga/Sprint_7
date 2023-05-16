import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class TestCanNotCreateSameCouriers {
    private static CourierClient courierClient;
    private static Courier courier;
    private static Courier courier2;
    private static int courierId;

    @Before
    public void setUp() {
        courierClient = new CourierClient();
        courier = CourierGenerator.getRandom();
        courierClient.create(courier);
        ValidatableResponse loginResponse = courierClient.login(CourierCredentials.from(courier));
        courierId = loginResponse.extract().path("id");
        courier2 = new Courier(courier.getLogin(), courier.getPassword(), courier.getFirstName());
    }

    @Test
    @DisplayName("Check status code is 409 of /api/v1/courier")
    @Description("basic test for POST /api/v1/courier endpoint")
    public void canNotCreateSameCouriers() {
        ValidatableResponse createResponse = courierClient.create(courier2);
        int statusCode = createResponse.extract().statusCode();
        assertEquals("Неверный код ответа!", 409, statusCode);
        String isCourierCreated = createResponse.extract().path("message");
        assertEquals("Неверный ответ!", "Этот логин уже используется. Попробуйте другой.", isCourierCreated);
    }

    @After
    public void cleanUp() {
        courierClient.delete(courierId);
    }
}
