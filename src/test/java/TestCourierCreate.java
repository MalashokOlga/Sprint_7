import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.greaterThan;
import static org.junit.Assert.assertEquals;

@RunWith(Parameterized.class)
public class TestCourierCreate {

    private CourierClient courierClient;
    private Courier courier;
    private int courierId;

    @Parameterized.Parameter
    public String login;
    @Parameterized.Parameter(1)
    public String password;
    @Parameterized.Parameter(2)
    public String firstName;

    @Parameterized.Parameters(name = "{index}: данные для создания курьера")
    public static Object[][] loginData() {
        return new Object[][] {{"login1", "password1", "firstName1"},
                {"olmal", "1234", ""}};
    }

    @Before
    public void setUp() {
        courierClient = new CourierClient();
        courier = new Courier(login, password, firstName);
    }

    @Test
    @DisplayName("Check status code is 201 of /api/v1/courier")
    @Description("basic test for POST /api/v1/courier endpoint")
    public void canCreateCourier() {
        ValidatableResponse createResponse = courierClient.create(courier);
        int statusCode = createResponse.extract().statusCode();
        assertEquals("Неверный код ответа!", 201, statusCode);
        boolean isCourierCreated = createResponse.extract().path("ok");
        assertEquals("Неверный ответ!", true, isCourierCreated);

        ValidatableResponse loginResponse = courierClient.login(CourierCredentials.from(courier));
        courierId = loginResponse.extract().path("id");
        System.out.println("courierId: " + courierId);
        assertThat("Id курьера не положительное число", courierId, greaterThan(0));
    }

    @After
    public void cleanUp() {
        courierClient.delete(courierId);
    }
}
