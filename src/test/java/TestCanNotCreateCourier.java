import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import static org.junit.Assert.assertEquals;

@RunWith(Parameterized.class)
public class TestCanNotCreateCourier {
    private CourierClient courierClient;
    private static Courier courier;

    @Parameterized.Parameter
    public String login;
    @Parameterized.Parameter(1)
    public String password;
    @Parameterized.Parameter(2)
    public String firstName;

    @Parameterized.Parameters(name = "{index}: данные для создания курьера")
    public static Object[][] loginData() {
        courier = CourierGenerator.getRandom();
        return new Object[][] {
                {courier.getLogin(), null, courier.getFirstName()},
                {null, courier.getPassword(), courier.getFirstName()},
                {null, null, courier.getFirstName()}};
    }

    @Before
    public void setUp() {
        courierClient = new CourierClient();
        courier = new Courier(login, password, firstName);
    }

    @Test
    @DisplayName("Check status code is 400 of /api/v1/courier")
    @Description("basic test for POST /api/v1/courier endpoint")
    public void canNotCreateCourier() {
        ValidatableResponse createResponse = courierClient.create(courier);
        int statusCode = createResponse.extract().statusCode();
        assertEquals("Неверный код ответа!", 400, statusCode);
        String isCourierCreated = createResponse.extract().path("message");
        assertEquals("Неверный ответ!", "Недостаточно данных для создания учетной записи", isCourierCreated);
    }
}
