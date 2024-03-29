package base;

import com.codeborne.selenide.SelenideElement;
import lombok.SneakyThrows;

import java.time.Duration;
import java.util.List;

import static com.codeborne.selenide.Condition.exactText;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$$;
import static com.codeborne.selenide.Selenide.open;

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
        System.setProperty("webdriver.chrome.driver", "C:/Users/User/IdeaProjects/aqaShopdDw/driver/chromedriver.exe");
        open(appURL +":"+appPORT);
        $$(".button__content").find(exactText("Купить")).click();
        $$(".heading_theme_alfa-on-white").find(exactText("Оплата по карте")).shouldBe(visible);
    }

    public void buyOnCredit(){
        System.setProperty("webdriver.chrome.driver", "C:/Users/User/IdeaProjects/aqaShopdDw/driver/chromedriver.exe");
        open(appURL +":"+appPORT);
        $$(".button__content").find(exactText("Купить в кредит")).click();
        $$(".heading_theme_alfa-on-white").find(exactText("Кредит по данным карты")).shouldBe(visible);
    }

    @SneakyThrows
    public void checkMessageSuccess() {
        $$(".notification__title").find(exactText("Успешно")).shouldBe(visible,  Duration.ofSeconds(35));
    }

    @SneakyThrows
    public void checkMessageError() {
        $$(".notification__title").find(exactText("Ошибка")).shouldBe(visible, Duration.ofSeconds(25));
    }

    public void checkMessageWrongFormat() {
        $$(".input__sub").find(exactText("Неверный формат")).shouldBe(visible);
    }

    public void checkMessageWrongDate() {
        $$(".input__sub").find(exactText("Неверно указан срок действия карты")).shouldBe(visible);
    }

    public void checkMessageOverDate() {
        $$(".input__sub").find(exactText("Истёк срок действия карты")).shouldBe(visible);
    }

    public void checkMessageRequiredField() {
        $$(".input__sub").find(exactText("Поле обязательно для заполнения")).shouldBe(visible);
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

    public void pushСontinueButton(){
        $$(".button__content").find(exactText("Продолжить")).click();
    }

}