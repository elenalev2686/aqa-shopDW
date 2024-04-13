package aqashoptest;

import base.DBUtils;
import base.FormPage;
import base.Status;
import com.codeborne.selenide.logevents.SelenideLogger;
import io.qameta.allure.selenide.AllureSelenide;
import org.junit.jupiter.api.*;

import java.sql.SQLException;

import static base.DataGenerator.*;
import static com.codeborne.selenide.Selenide.open;

public class TestPaymentGate {
     FormPage formPage;

    @BeforeEach
    void setUp() {
        formPage = open("http://localhost:8080", FormPage.class);
    }

    @BeforeAll
    static void setUpAll() {
        SelenideLogger.addListener("allure", new AllureSelenide());
    }

    @AfterEach
    void clearAll() throws SQLException {
        DBUtils.clearAllData();
    }

    @AfterAll
    static void tearDownAll() {
        SelenideLogger.removeListener("allure");
    }

    @Test
    @DisplayName("Покупка активной дебетовой картой, валидные данные")
    void shouldPayByApprovedCard() {
        formPage.buyForYourMoney();
        formPage.setCardNumber(getCardNumberValid());
        formPage.setCardMonth(getDateMonth(0));
        formPage.setCardYear(getDateYear(0));
        formPage.setCardOwner(getRandomCardOwner());
        formPage.setCardCVV(getRandomCvCCvV());
        formPage.pushContinueButton();
        formPage.checkMessageSuccess();
       DBUtils.getPaymentStatus(Status.APPROVED);
    }

    @Test
    @DisplayName("Покупка отклоненной дебетовой картой, валидные данные")
    void shouldNoPayByDeclinedCard() {
        formPage.buyForYourMoney();
        formPage.setCardNumber(getCardNumberDeclined());
        formPage.setCardMonth(getDateMonth(0));
        formPage.setCardYear(getDateYear(0));
        formPage.setCardOwner(getRandomCardOwner());
        formPage.setCardCVV(getRandomCvCCvV());
        formPage.pushContinueButton();
        formPage.checkMessageError();
        DBUtils.getPaymentStatus(Status.DECLINED);
    }

    @Test
    @DisplayName("Покупка дебетовой картой не из набора, валидные данные")
    void shouldNoPayByUnknownCard() {
        formPage.buyForYourMoney();
        formPage.setCardNumber(getCardNumberUnknown());
        formPage.setCardMonth(getDateMonth(0));
        formPage.setCardYear(getDateYear(0));
        formPage.setCardOwner(getRandomCardOwner());
        formPage.setCardCVV(getRandomCvCCvV());
        formPage.pushContinueButton();
        formPage.checkMessageError();
    }

    @Test
    @DisplayName("Покупка дебетовой картой, ввод месяца, при котором срок действия карты уже истек")
    void shouldNotExpiredDateMonthField() {
        formPage.buyForYourMoney();
        formPage.setCardNumber(getCardNumberValid());
        formPage.setCardMonth(getDateMonth(11));;
        formPage.setCardYear(getDateYear(0));
        formPage.setCardOwner(getRandomCardOwner());
        formPage.setCardCVV(getRandomCvCCvV());
        formPage.pushContinueButton();
        formPage.checkMessageWrong("Неверно указан срок действия карты");
    }
    @Test
    @DisplayName("Покупка дебетовой картой, ввод несуществующего месяца")
    void shouldNotPayInvalidMonthField() {
        formPage.buyForYourMoney();
        formPage.setCardNumber(getCardNumberValid());
        formPage.setCardMonth("13");
        formPage.setCardYear(getDateYear(0));
        formPage.setCardOwner(getRandomCardOwner());
        formPage.setCardCVV(getRandomCvCCvV());
        formPage.pushContinueButton();
        formPage.checkMessageWrong("Неверно указан срок действия карты");
    }
    @Test
    @DisplayName("Покупка дебетовой картой, оставление поля месяц пустым")
    void shouldNotPayEmptyMonthField() {
        formPage.buyForYourMoney();
        formPage.setCardNumber(getCardNumberValid());
        formPage.setCardMonth("");
        formPage.setCardYear(getDateYear(0));
        formPage.setCardOwner(getRandomCardOwner());
        formPage.setCardCVV(getRandomCvCCvV());
        formPage.pushContinueButton();
        formPage.checkMessageWrong("Неверный формат");
    }
    @Test
    @DisplayName("Покупка дебетовой картой, ввод в поле месяц спецсимволов")
    void shouldNotPaySymbolMonthField() {
        formPage.buyForYourMoney();
        formPage.setCardNumber(getCardNumberValid());
        formPage.setCardMonth("#$");
        formPage.setCardYear(getDateYear(0));
        formPage.setCardOwner(getRandomCardOwner());
        formPage.setCardCVV(getRandomCvCCvV());
        formPage.pushContinueButton();
        formPage.checkMessageWrong("Неверный формат");
    }
    @Test
    @DisplayName("Покупка дебетовой картой, ввод в поле Месяц букв (кириллица)")
    void shouldNotPayRuMonthField() {
        formPage.buyForYourMoney();
        formPage.setCardNumber(getCardNumberValid());
        formPage.setCardMonth(getRandomCardOwnerRu());
        formPage.setCardYear(getDateYear(0));
        formPage.setCardOwner(getRandomCardOwner());
        formPage.setCardCVV(getRandomCvCCvV());
        formPage.pushContinueButton();
        formPage.checkMessageWrong("Неверный формат");
    }
    @Test
    @DisplayName("Покупка дебетовой картой, ввод в поле Месяц букв (латинские)")
    void shouldNotPayEnMonthField() {
        formPage.buyForYourMoney();
        formPage.setCardNumber(getCardNumberValid());
        formPage.setCardMonth(getRandomCardOwner());
        formPage.setCardYear(getDateYear(0));
        formPage.setCardOwner(getRandomCardOwner());
        formPage.setCardCVV(getRandomCvCCvV());
        formPage.pushContinueButton();
        formPage.checkMessageWrong("Неверный формат");
    }
    @Test
    @DisplayName("Покупка дебетовой картой, ввод в поле Месяц однозначного числа")
    void shouldNotPayOneNumberMonthField() {
        formPage.buyForYourMoney();
        formPage.setCardNumber(getCardNumberValid());
        formPage.setCardMonth("1");
        formPage.setCardYear(getDateYear(0));
        formPage.setCardOwner(getRandomCardOwner());
        formPage.setCardCVV(getRandomCvCCvV());
        formPage.pushContinueButton();
        formPage.checkMessageWrong("Неверный формат");
    }
    @Test
    @DisplayName("Покупка дебетовой картой, ввод нулевых значений в поле Месяц")
    void shouldNotPayZeroMonthField() {
        formPage.buyForYourMoney();
        formPage.setCardNumber(getCardNumberValid());
        formPage.setCardMonth("00");
        formPage.setCardYear(getDateYear(0));
        formPage.setCardOwner(getRandomCardOwner());
        formPage.setCardCVV(getRandomCvCCvV());
        formPage.pushContinueButton();
        formPage.checkMessageWrong("Неверно указан срок действия карты");
    }
    @Test
    @DisplayName("Покупка дебетовой картой, ввод года, отстоящего от текущей даты более чем на 6 лет")
    void shouldNotPayInvalidYearField() {
        formPage.buyForYourMoney();
        formPage.setCardNumber(getCardNumberValid());
        formPage.setCardMonth(getDateMonth(0));;
        formPage.setCardYear(getDateYear(6));
        formPage.setCardOwner(getRandomCardOwner());
        formPage.setCardCVV(getRandomCvCCvV());
        formPage.pushContinueButton();
        formPage.checkMessageWrong("Неверно указан срок действия карты");
    }
    @Test
    @DisplayName("Покупка дебетовой картой, оставление поля Год пустым")
    void shouldNotPayEmptyYearField() {
        formPage.buyForYourMoney();
        formPage.setCardNumber(getCardNumberValid());
        formPage.setCardMonth(getDateMonth(0));;
        formPage.setCardYear("");
        formPage.setCardOwner(getRandomCardOwner());
        formPage.setCardCVV(getRandomCvCCvV());
        formPage.pushContinueButton();
        formPage.checkMessageWrong("Неверный формат");
    }
    @Test
    @DisplayName("Покупка дебетовой картой, ввод года, при котором срок действия карты уже истек")
    void shouldNoPayExpiredDateYearField() {
        formPage.buyForYourMoney();
        formPage.setCardNumber(getCardNumberValid());
        formPage.setCardMonth(getDateMonth(0));;
        formPage.setCardYear(getDateYearMinus(1));
        formPage.setCardOwner(getRandomCardOwner());
        formPage.setCardCVV(getRandomCvCCvV());
        formPage.pushContinueButton();
        formPage.checkMessageWrong("Истёк срок действия карты");
    }
    @Test
    @DisplayName("Покупка дебетовой картой, ввод в поле Год спецсимволов")
    void shouldNotPaySymbolYearField() {
        formPage.buyForYourMoney();
        formPage.setCardNumber(getCardNumberValid());
        formPage.setCardMonth(getDateMonth(0));;
        formPage.setCardYear("#$");
        formPage.setCardOwner(getRandomCardOwner());
        formPage.setCardCVV(getRandomCvCCvV());
        formPage.pushContinueButton();
        formPage.checkMessageWrong("Неверный формат");
    }
    @Test
    @DisplayName("Покупка дебетовой картой, ввод  в поле Год букв (кириллица)")
    void shouldNotPayRuYearField() {
        formPage.buyForYourMoney();
        formPage.setCardNumber(getCardNumberValid());
        formPage.setCardMonth(getDateMonth(0));;
        formPage.setCardYear(getRandomCardOwnerRu());
        formPage.setCardOwner(getRandomCardOwner());
        formPage.setCardCVV(getRandomCvCCvV());
        formPage.pushContinueButton();
        formPage.checkMessageWrong("Неверный формат");
    }
    @Test
    @DisplayName("Покупка дебетовой картой, ввод  в поле Год букв (латинские)")
    void shouldNotPayEnYearField() {
        formPage.buyForYourMoney();
        formPage.setCardNumber(getCardNumberValid());
        formPage.setCardMonth(getDateMonth(0));;
        formPage.setCardYear(getRandomCardOwner());
        formPage.setCardOwner(getRandomCardOwner());
        formPage.setCardCVV(getRandomCvCCvV());
        formPage.pushContinueButton();
        formPage.checkMessageWrong("Неверный формат");
    }
    @Test
    @DisplayName("Покупка дебетовой картой, ввод  в поле Год однозначного числа")
    void shouldNotPayOneNumberYearField() {
        formPage.buyForYourMoney();
        formPage.setCardNumber(getCardNumberValid());
        formPage.setCardMonth(getDateMonth(0));;
        formPage.setCardYear("1");
        formPage.setCardOwner(getRandomCardOwner());
        formPage.setCardCVV(getRandomCvCCvV());
        formPage.pushContinueButton();
        formPage.checkMessageWrong("Неверный формат");
    }
    @Test
    @DisplayName("Покупка дебетовой картой, ввод нулевых значений в поле Год")
    void shouldNotPayZeroYearField() {
        formPage.buyForYourMoney();
        formPage.setCardNumber(getCardNumberValid());
        formPage.setCardMonth(getDateMonth(0));;
        formPage.setCardYear("00");
        formPage.setCardOwner(getRandomCardOwner());
        formPage.setCardCVV(getRandomCvCCvV());
        formPage.pushContinueButton();
        formPage.checkMessageWrong("Истёк срок действия карты");
    }

    @Test
    @DisplayName("Покупка дебетовой картой, ввод в поле Владелец букв кириллицы.")
    void shouldNotPayInvalidCardOwnerFieldRu() {
        formPage.buyForYourMoney();
        formPage.setCardNumber(getCardNumberValid());
        formPage.setCardMonth(getDateMonth(0));;
        formPage.setCardYear(getDateYear(0));
        formPage.setCardOwner(getRandomCardOwnerRu());
        formPage.setCardCVV(getRandomCvCCvV());
        formPage.pushContinueButton();
        formPage.checkMessageError();
    }
    @Test
    @DisplayName("Покупка дебетовой картой, ввод в поле Владелец спецсимволов.")
    void shouldNotPayInvalidCardOwnerFieldSymbol() {
        formPage.buyForYourMoney();
        formPage.setCardNumber(getCardNumberValid());
        formPage.setCardMonth(getDateMonth(0));;
        formPage.setCardYear(getDateYear(0));
        formPage.setCardOwner("#%$@");
        formPage.setCardCVV(getRandomCvCCvV());
        formPage.pushContinueButton();
        formPage.checkMessageError();
    }
    @Test
    @DisplayName("Покупка дебетовой картой, ввод в поле Владелец цифр.")
    void shouldNotPayInvalidCardOwnerFieldNumbers() {
        formPage.buyForYourMoney();
        formPage.setCardNumber(getCardNumberValid());
        formPage.setCardMonth(getDateMonth(0));;
        formPage.setCardYear(getDateYear(0));
        formPage.setCardOwner(getRandomNumbers());
        formPage.setCardCVV(getRandomCvCCvV());
        formPage.pushContinueButton();
        formPage.checkMessageError();
    }
    @Test
    @DisplayName("Покупка дебетовой картой, оставление поля Владелец пустым.")
    void shouldNotPayInvalidCardOwnerFieldEmpty() {
        formPage.buyForYourMoney();
        formPage.setCardNumber(getCardNumberValid());
        formPage.setCardMonth(getDateMonth(0));;
        formPage.setCardYear(getDateYear(0));
        formPage.setCardOwner("");
        formPage.setCardCVV(getRandomCvCCvV());
        formPage.pushContinueButton();
        formPage.checkMessageWrong("Поле обязательно для заполнения");
    }

    @Test
    @DisplayName("Покупка дебетовой картой, оставление поля CVC/CVV пустым.")
    void shouldNoPayInvalidCVVField() {
        formPage.buyForYourMoney();
        formPage.setCardNumber(getCardNumberValid());
        formPage.setCardMonth(getDateMonth(0));;
        formPage.setCardYear(getDateYear(0));
        formPage.setCardOwner(getRandomCardOwner());
        formPage.setCardCVV("");
        formPage.pushContinueButton();
        formPage.checkMessageWrong("Неверный формат");
    }
    @Test
    @DisplayName("Покупка дебетовой картой, ввод в поле CVC/CVV спецсимволов.")
    void shouldNoPayInvalidCVVFieldSymbol() {
        formPage.buyForYourMoney();
        formPage.setCardNumber(getCardNumberValid());
        formPage.setCardMonth(getDateMonth(0));;
        formPage.setCardYear(getDateYear(0));
        formPage.setCardOwner(getRandomCardOwner());
        formPage.setCardCVV("#$@");
        formPage.pushContinueButton();
        formPage.checkMessageWrong("Неверный формат");
    }
    @Test
    @DisplayName("Покупка дебетовой картой, ввод в поле CVC/CVV букв (кириллица).")
    void shouldNoPayInvalidCVVFieldRu() {
        formPage.buyForYourMoney();
        formPage.setCardNumber(getCardNumberValid());
        formPage.setCardMonth(getDateMonth(0));;
        formPage.setCardYear(getDateYear(0));
        formPage.setCardOwner(getRandomCardOwner());
        formPage.setCardCVV(getRandomCardOwnerRu());
        formPage.pushContinueButton();
        formPage.checkMessageWrong("Неверный формат");
    }
    @Test
    @DisplayName("Покупка дебетовой картой, ввод в поле CVC/CVV букв (латинские).")
    void shouldNoPayInvalidCVVFieldEn() {
        formPage.buyForYourMoney();
        formPage.setCardNumber(getCardNumberValid());
        formPage.setCardMonth(getDateMonth(0));;
        formPage.setCardYear(getDateYear(0));
        formPage.setCardOwner(getRandomCardOwner());
        formPage.setCardCVV(getRandomCardOwner());
        formPage.pushContinueButton();
        formPage.checkMessageWrong("Неверный формат");
    }
    @Test
    @DisplayName("Покупка дебетовой картой, ввод в поле CVC/CVV однозначного числа.")
    void shouldNoPayInvalidCVVFieldOneNumber() {
        formPage.buyForYourMoney();
        formPage.setCardNumber(getCardNumberValid());
        formPage.setCardMonth(getDateMonth(0));;
        formPage.setCardYear(getDateYear(0));
        formPage.setCardOwner(getRandomCardOwner());
        formPage.setCardCVV("1");
        formPage.pushContinueButton();
        formPage.checkMessageWrong("Неверный формат");
    }
    @Test
    @DisplayName("Покупка дебетовой картой, ввод в поле CVC/CVV двузначного числа.")
    void shouldNoPayInvalidCVVFieldTwoNumber() {
        formPage.buyForYourMoney();
        formPage.setCardNumber(getCardNumberValid());
        formPage.setCardMonth(getDateMonth(0));;
        formPage.setCardYear(getDateYear(0));
        formPage.setCardOwner(getRandomCardOwner());
        formPage.setCardCVV("01");
        formPage.pushContinueButton();
        formPage.checkMessageWrong("Неверный формат");
    }

    @Test
    @DisplayName("Попытка покупки по карте c пустым номером")
    void shouldNoPayEmptyCardNumberField() {
        formPage.buyForYourMoney();
        formPage.setCardNumber("");
        formPage.setCardMonth(getDateMonth(0));
        formPage.setCardYear(getDateYear(0));
        formPage.setCardOwner(getRandomCardOwner());
        formPage.setCardCVV(getRandomCvCCvV());
        formPage.pushContinueButton();
        formPage.checkMessageWrong("Неверный формат");
    }
}
