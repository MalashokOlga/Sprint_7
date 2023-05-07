import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.junit.Before;
import org.junit.Test;
import ru.pojo_objects.OrderList;

import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertEquals;

public class TestOrderList {
    private OrderClient orderClient;

    private OrderList orderList;

    @Before
    public void setUp() {
        orderClient = new OrderClient();
        orderList = new OrderList();
    }

    @Test
    @DisplayName("Check status code is 200 of /api/v1/orders")
    @Description("basic test for GET /api/v1/orders endpoint")
    public void getOrderList() {
        ValidatableResponse listResponse = orderClient.checkOrderList();
        int statusCode = listResponse.extract().statusCode();
        assertEquals("Неверный код ответа!", 200, statusCode);

        orderList = orderClient.getOrderList();
        assertThat("Ответ неправильный", orderList, notNullValue());
    }
}
