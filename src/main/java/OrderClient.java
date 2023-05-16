import io.restassured.response.ValidatableResponse;
import ru.pojo_objects.OrderList;

import java.util.HashMap;

import static io.restassured.RestAssured.*;

public class OrderClient extends RestClient {
    private static final String ORDER_PATH = "/api/v1/orders";
    private static final String ORDER_TRACK = "/api/v1/orders/track?t=";
    private static final String ORDER_CANCEL = "/api/v1/orders/cancel";
    private static HashMap<String, Integer> trackNum;

    public ValidatableResponse create(Order order) {
        return given()
                .spec(getBaseSpec())
                .body(order)
                .when()
                .post(ORDER_PATH)
                .then();
    }
    public ValidatableResponse checkOrderList() {
        return (ValidatableResponse) given()
                .spec(getBaseSpec())
                .when()
                .get(ORDER_PATH)
                .then();
    }

    public OrderList getOrderList() {
        return given()
                .spec(getBaseSpec())
                .when()
                .get(ORDER_PATH)
                .then()
                .extract().body().as(OrderList.class);
    }

    public ValidatableResponse track(int track) {
        String endpoint = ORDER_TRACK + track;
        return given()
                .spec(getBaseSpec())
                .when()
                .get(endpoint)
                .then();
    }
    public ValidatableResponse cancel(int track) {
        trackNum = new HashMap<>();
        trackNum.put("track", track);
        return given()
                .spec(getBaseSpec())
                .body(trackNum)
                .when()
                .put(ORDER_CANCEL)
                .then();
    }
}
