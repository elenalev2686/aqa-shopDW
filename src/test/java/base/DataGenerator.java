package base;

import com.github.javafaker.Faker;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class DataGenerator {
    private static final Faker faker = new Faker(new Locale("en"));
    private DataGenerator() {
    }

    public static String getDateMonth(int shift) {
        return LocalDate.now().plusMonths(shift).format(DateTimeFormatter.ofPattern("MM"));
    }

    public static String getDateYear(int shift) {
        return LocalDate.now().plusYears(shift).format(DateTimeFormatter.ofPattern("yy"));
    }
    public static String getDateYearMinus(int shift) {
        return LocalDate.now().minusYears(shift).format(DateTimeFormatter.ofPattern("yy"));
    }

    public static String getRandomCardOwner() {
        return faker.name().lastName() + " " + faker.name().firstName();
    }
    public static String getRandomCardOwnerRu() {
        Faker faker1 = new Faker(new Locale("ru"));
        return faker1.name().lastName() + " " + faker1.name().firstName();
    }

    public static String getRandomCvCCvV() {
        return faker.internet().password(000,999);
    }
    public static String getRandomNumbers() {
        return faker.numerify("#####");
    }

}