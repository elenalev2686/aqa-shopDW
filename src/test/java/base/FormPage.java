package base;

import com.codeborne.selenide.SelenideElement;
import lombok.SneakyThrows;

import java.time.Duration;
import java.util.List;

import static com.codeborne.selenide.Condition.exactText;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$$;

public class FormPage {
    private static String url = System.getProperty("db.url");
    private static String appURL = System.getProperty("app.url");
    private static String appPORT = System.getProperty("app.port");
    private static String userDB = System.getProperty("app.userDB");
    private static String password = System.getProperty("app.password");

    List<SelenideElement> input = $$(".input__control");
    SelenideElement cardNumber = input.get(0);
    SelenideElement month = input.get(1);
    SelenideElement year = input.get(2);
    SelenideElement cardOwner = input.get(3);
    SelenideElement cvcOrCvvNumber = input.get(4);

    public void buyForYourMoney() {
        $$(".button__content").find(exactText("Купить")).click();
        $$(".heading_theme_alfa-on-white").find(exactText("Оплата по карте")).shouldBe(visible);
    }

    public void buyOnCredit(){
        $$(".button__content").find(exactText("Купить в кредит")).click();
        $$(".heading_theme_alfa-on-white").find(exactText("Кредит по данным карты")).shouldBe(visible);
    }

    @SneakyThrows
    public void checkMessageSuccess() {
        $$(".notification__title").find(exactText("Успешно")).shouldBe(visible,  Duration.ofSeconds(25));
    }

    @SneakyThrows
    public void checkMessageError() {
        $$(".notification__title").find(exactText("Ошибка")).shouldBe(visible, Duration.ofSeconds(25));
    }

    public void checkMessageWrong(String expectedText) {
        $$(".input__sub").find(exactText(expectedText)).shouldBe(visible);;
    }


    public void setCardNumber(String cNumber) {
        cardNumber.setValue(cNumber);
    }

    public void setCardMonth(String cMonth) {
        month.setValue(cMonth);
    }

    public void setCardYear(String cYear) {
        year.setValue(cYear);
    }

    public void setCardOwner(String cOwner) {
        cardOwner.setValue(cOwner);
    }

    public void setCardCVV(String cCvv) {
        cvcOrCvvNumber.setValue(cCvv);
    }

    public void pushContinueButton(){
        $$(".button__content").find(exactText("Продолжить")).click();
    }

}