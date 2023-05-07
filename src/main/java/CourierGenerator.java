import org.apache.commons.lang3.RandomStringUtils;

public class CourierGenerator {
    public static Courier getRandom() {
        final String login = RandomStringUtils.randomAlphabetic(5);
        final String password = RandomStringUtils.randomAlphabetic(5);
        final String firstName = RandomStringUtils.randomAlphabetic(5);
        return new Courier(login, password, firstName);
    }

    public static Courier getRandomWithoutLogin() {
        final String password = RandomStringUtils.randomAlphabetic(5);
        final String firstName = RandomStringUtils.randomAlphabetic(5);
        return new Courier(null, password, firstName);
    }

    public static Courier getRandomWithoutPassword() {
        final String login = RandomStringUtils.randomAlphabetic(5);
        final String firstName = RandomStringUtils.randomAlphabetic(5);
        return new Courier(login, null, firstName);
    }
}
