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
public class TestOrderCreate {

    private OrderClient orderClient;
    private Order order;
    private int orderTrack;

    @Parameterized.Parameter
    public String colorBlack;
    @Parameterized.Parameter(1)
    public String colorGrey;

    @Parameterized.Parameters(name = "{index}: цвет")
    public static Object[][] colorScooter() {
        return new Object[][] {{"BLACK ", "GREY"},
                {"BLACK", null},
                {null, "GREY"},
                {null, null}};
    }

    @Before
    public void setUp() {
        orderClient = new OrderClient();
        order = new Order("firstName1", "lastName1", "address1", "1", "79998886655", 3, "10", new String [] {colorBlack, colorGrey},"comment");
    }
    @Test
    @DisplayName("Check status code is 200 of /api/v1/orders")
    @Description("basic test for POST /api/v1/orders endpoint")
    public void canCreateOrder() {
        ValidatableResponse createResponse = orderClient.create(order);
        orderTrack = createResponse.extract().path("track");
        System.out.println("orderTrack: " + orderTrack);
        int statusCode = createResponse.extract().statusCode();
        assertEquals("Неверный код ответа!", 201, statusCode);
        int isOrderCreated = createResponse.extract().path("track");
        assertThat("Неверный track!", isOrderCreated, greaterThan(0));

        ValidatableResponse trackResponse = orderClient.track(orderTrack);
        int statusCode2 = trackResponse.extract().statusCode();
        assertEquals("Неверный код ответа!", 200, statusCode2);
    }
    @After
    public void cleanUp() {
        orderClient.cancel(orderTrack);
    }

}
