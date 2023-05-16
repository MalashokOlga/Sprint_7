import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import static org.junit.Assert.assertEquals;

@RunWith(Parameterized.class)public class TestCourierLoginWrongData {
    private CourierClient courierClient;
    private Courier courier;
    private CourierCredentials courierCredentials;
    private int courierId;
    @Parameterized.Parameter
    public String login;
    @Parameterized.Parameter(1)
    public String password;

    @Parameterized.Parameters(name = "{index}: данные для логина")
    public static Object[][] loginData() {
        return new Object[][] {{"logini", "password"},
                {"login", "passworde"},
                {"notExist", "noPassword"}
        };
    }

    @Before
    public void setUp() {
        courierClient = new CourierClient();
        courier = new Courier("login", "password", "firstName");
        courierClient.create(courier);
        ValidatableResponse loginResponse = courierClient.login(CourierCredentials.from(courier));
        courierId = loginResponse.extract().path("id");
    }
    @Test
    @DisplayName("Check status code is 404 of /api/v1/courier/login")
    @Description("basic test for POST /api/v1/courier/login endpoint")
    public void CourierLoginWrongData() {
        courierCredentials = CourierCredentials.from(courier);
        courierCredentials.setLogin(login);
        courierCredentials.setPassword(password);
        ValidatableResponse loginResponse = courierClient.login(courierCredentials);
        int statusCode = loginResponse.extract().statusCode();
        assertEquals("Неверный код ответа!", 404, statusCode);
        String isCourierLogin = loginResponse.extract().path("message");
        assertEquals("Неверный ответ!", "Учетная запись не найдена", isCourierLogin);
    }

    @After
    public void cleanUp() {
        courierClient.delete(courierId);
    }
}
