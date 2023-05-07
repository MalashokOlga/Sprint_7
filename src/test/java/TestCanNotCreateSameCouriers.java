import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import static org.junit.Assert.assertEquals;

@RunWith(Parameterized.class)
public class TestCanNotCreateSameCouriers {
    private CourierClient courierClient;
    private Courier courier;
    private Courier courier2;
    private int courierId;

    @Parameterized.Parameter
    public String password1;
    @Parameterized.Parameter(1)
    public String password2;

    @Parameterized.Parameters(name = "{index}: данные для создания курьера")
    public static Object[][] loginData() {
        return new Object[][] {{"password1", "password1"},
                {"password1", "password2"}
        };
    }
    @Before
    public void setUp() {
        courierClient = new CourierClient();
        courier = new Courier("login1", password1, "firstName1");
        courierClient.create(courier);
        ValidatableResponse loginResponse = courierClient.login(CourierCredentials.from(courier));
        courierId = loginResponse.extract().path("id");
        courier2 = new Courier("login1", password2, "firstName2");
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
