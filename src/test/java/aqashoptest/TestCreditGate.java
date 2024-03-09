package aqashoptest;


import base.DBUtils;
import base.FormPage;
import base.Status;
import com.codeborne.selenide.logevents.SelenideLogger;
import io.qameta.allure.selenide.AllureSelenide;
import org.junit.jupiter.api.*;

import java.sql.SQLException;

import static base.DataGenerator.*;

public class TestCreditGate {
    private FormPage formPage;

    @AfterEach
    void clearAll() throws SQLException{
        DBUtils.clearAllData();
    }

    @AfterAll
    static void tearDownAll() {
        SelenideLogger.removeListener("allure");
    }

    @BeforeEach
    void setUpPage() {
        formPage = new FormPage();
    }

    @BeforeAll
    static void setUpAll() {
        SelenideLogger.addListener("allure", new AllureSelenide());
    }


    @Test
    @DisplayName("Покупка в кредит активной дебетовой картой, валидные данные")
    void shouldPayByApprovedCardOnCreditDb() throws SQLException {
        formPage.buyOnCredit();
        formPage.setCardNumber("4444444444444441");
        formPage.setCardMonth(getDateMonth(0));
        formPage.setCardYear(getDateYear(0));
        formPage.setCardOwner(getRandomCardOwner());
        formPage.setCardCVV(getRandomCvCCvV());
        formPage.pushСontinueButton();
        formPage.checkMessageSuccess();
        DBUtils.checkCreditStatus(Status.APPROVED);
    }

    @Test
    @DisplayName("Покупка в кредит отклоненной дебетовой картой, валидные данные")
    void shouldNotPayByDeclinedCard() throws SQLException {
        formPage.buyOnCredit();
        formPage.setCardNumber("4444444444444442");
        formPage.setCardMonth(getDateMonth(0));
        formPage.setCardYear(getDateYear(0));
        formPage.setCardOwner(getRandomCardOwner());
        formPage.setCardCVV(getRandomCvCCvV());
        formPage.pushСontinueButton();
        formPage.checkMessageError();
        DBUtils.checkCreditStatus(Status.DECLINED);
    }

    @Test
    @DisplayName("Покупка в кредит по данным дебетовой карты не из набора, валидные данные")
    void shouldNotPayByUnknownCard() throws SQLException {
        formPage.buyOnCredit();
        formPage.setCardNumber("4444444444444443");
        formPage.setCardMonth(getDateMonth(0));
        formPage.setCardYear(getDateYear(0));
        formPage.setCardOwner(getRandomCardOwner());
        formPage.setCardCVV(getRandomCvCCvV());
        formPage.pushСontinueButton();
        formPage.checkMessageError();
    }

    @Test
    @DisplayName("Покупка в кредит по данным дебетовой карты, ввод месяца, при котором срок действия карты уже истек")
    void shouldNotExpiredDateMonthField() throws SQLException {
        formPage.buyOnCredit();
        formPage.setCardNumber("4444444444444441");
        formPage.setCardMonth(getDateMonth(11));;
        formPage.setCardYear(getDateYear(0));
        formPage.setCardOwner(getRandomCardOwner());
        formPage.setCardCVV(getRandomCvCCvV());
        formPage.pushСontinueButton();
        formPage.checkMessageWrongDate();
    }
    @Test
    @DisplayName("Покупка в кредит по данным дебетовой карты, ввод несуществующего месяца")
    void shouldNotPayInvalidMonthField() throws SQLException {
        formPage.buyOnCredit();
        formPage.setCardNumber("4444444444444441");
        formPage.setCardMonth("13");
        formPage.setCardYear(getDateYear(0));
        formPage.setCardOwner(getRandomCardOwner());
        formPage.setCardCVV(getRandomCvCCvV());
        formPage.pushСontinueButton();
        formPage.checkMessageWrongDate();
    }
    @Test
    @DisplayName("Покупка в кредит по данным дебетовой карты, оставление поля месяц пустым")
    void shouldNotPayEmptyMonthField() throws SQLException {
        formPage.buyOnCredit();
        formPage.setCardNumber("4444444444444441");
        formPage.setCardMonth("");
        formPage.setCardYear(getDateYear(0));
        formPage.setCardOwner(getRandomCardOwner());
        formPage.setCardCVV(getRandomCvCCvV());
        formPage.pushСontinueButton();
        formPage.checkMessageWrongFormat();
    }
    @Test
    @DisplayName("Покупка в кредит по данным дебетовой карты, ввод в поле месяц спецсимволов")
    void shouldNotPaySymbolMonthField() throws SQLException {
        formPage.buyOnCredit();
        formPage.setCardNumber("4444444444444441");
        formPage.setCardMonth("#$");
        formPage.setCardYear(getDateYear(0));
        formPage.setCardOwner(getRandomCardOwner());
        formPage.setCardCVV(getRandomCvCCvV());
        formPage.pushСontinueButton();
        formPage.checkMessageWrongFormat();
    }
    @Test
    @DisplayName("Покупка в кредит по данным дебетовой карты, ввод в поле Месяц букв (кириллица)")
    void shouldNotPayRuMonthField() throws SQLException {
        formPage.buyOnCredit();
        formPage.setCardNumber("4444444444444441");
        formPage.setCardMonth(getRandomCardOwnerRu());
        formPage.setCardYear(getDateYear(0));
        formPage.setCardOwner(getRandomCardOwner());
        formPage.setCardCVV(getRandomCvCCvV());
        formPage.pushСontinueButton();
        formPage.checkMessageWrongFormat();
    }
    @Test
    @DisplayName("Покупка в кредит по данным дебетовой карты, ввод в поле Месяц букв (латинские)")
    void shouldNotPayEnMonthField() throws SQLException {
        formPage.buyOnCredit();
        formPage.setCardNumber("4444444444444441");
        formPage.setCardMonth(getRandomCardOwner());
        formPage.setCardYear(getDateYear(0));
        formPage.setCardOwner(getRandomCardOwner());
        formPage.setCardCVV(getRandomCvCCvV());
        formPage.pushСontinueButton();
        formPage.checkMessageWrongFormat();
    }
    @Test
    @DisplayName("Покупка в кредит по данным дебетовой карты, ввод в поле Месяц однозначного числа")
    void shouldNotPayOneNumberMonthField() throws SQLException {
        formPage.buyOnCredit();
        formPage.setCardNumber("4444444444444441");
        formPage.setCardMonth("1");
        formPage.setCardYear(getDateYear(0));
        formPage.setCardOwner(getRandomCardOwner());
        formPage.setCardCVV(getRandomCvCCvV());
        formPage.pushСontinueButton();
        formPage.checkMessageWrongFormat();
    }
    @Test
    @DisplayName("Покупка в кредит по данным дебетовой карты, ввод нулевых значений в поле Месяц")
    void shouldNotPayZeroMonthField() throws SQLException {
        formPage.buyOnCredit();
        formPage.setCardNumber("4444444444444441");
        formPage.setCardMonth("00");
        formPage.setCardYear(getDateYear(0));
        formPage.setCardOwner(getRandomCardOwner());
        formPage.setCardCVV(getRandomCvCCvV());
        formPage.pushСontinueButton();
        formPage.checkMessageWrongDate();
    }
    @Test
    @DisplayName("Покупка в кредит по данным дебетовой карты, ввод года, отстоящего от текущей даты более чем на 6 лет")
    void shouldNotPayInvalidYearField() throws SQLException {
        formPage.buyOnCredit();
        formPage.setCardNumber("4444444444444441");
        formPage.setCardMonth(getDateMonth(0));;
        formPage.setCardYear(getDateYear(6));
        formPage.setCardOwner(getRandomCardOwner());
        formPage.setCardCVV(getRandomCvCCvV());
        formPage.pushСontinueButton();
        formPage.checkMessageWrongDate();
    }
    @Test
    @DisplayName("Покупка в кредит по данным дебетовой карты, оставление поля Год пустым")
    void shouldNotPayEmptyYearField() throws SQLException {
        formPage.buyOnCredit();
        formPage.setCardNumber("4444444444444441");
        formPage.setCardMonth(getDateMonth(0));;
        formPage.setCardYear("");
        formPage.setCardOwner(getRandomCardOwner());
        formPage.setCardCVV(getRandomCvCCvV());
        formPage.pushСontinueButton();
        formPage.checkMessageWrongFormat();
    }
    @Test
    @DisplayName("Покупка в кредит по данным дебетовой карты, ввод года, при котором срок действия карты уже истек")
    void shouldNoPayExpiredDateYearField() throws SQLException {
        formPage.buyOnCredit();
        formPage.setCardNumber("4444444444444441");
        formPage.setCardMonth(getDateMonth(0));;
        formPage.setCardYear(getDateYearMinus(1));
        formPage.setCardOwner(getRandomCardOwner());
        formPage.setCardCVV(getRandomCvCCvV());
        formPage.pushСontinueButton();
        formPage.checkMessageOverDate();
    }
    @Test
    @DisplayName("Покупка в кредит по данным дебетовой карты, ввод в поле Год спецсимволов")
    void shouldNotPaySymbolYearField() throws SQLException {
        formPage.buyOnCredit();
        formPage.setCardNumber("4444444444444441");
        formPage.setCardMonth(getDateMonth(0));;
        formPage.setCardYear("#$");
        formPage.setCardOwner(getRandomCardOwner());
        formPage.setCardCVV(getRandomCvCCvV());
        formPage.pushСontinueButton();
        formPage.checkMessageWrongFormat();
    }
    @Test
    @DisplayName("Покупка в кредит по данным дебетовой карты, ввод  в поле Год букв (кириллица)")
    void shouldNotPayRuYearField() throws SQLException {
        formPage.buyOnCredit();
        formPage.setCardNumber("4444444444444441");
        formPage.setCardMonth(getDateMonth(0));;
        formPage.setCardYear(getRandomCardOwnerRu());
        formPage.setCardOwner(getRandomCardOwner());
        formPage.setCardCVV(getRandomCvCCvV());
        formPage.pushСontinueButton();
        formPage.checkMessageWrongFormat();
    }
    @Test
    @DisplayName("Покупка в кредит по данным дебетовой карты, ввод  в поле Год букв (латинские)")
    void shouldNotPayEnYearField() throws SQLException {
        formPage.buyOnCredit();
        formPage.setCardNumber("4444444444444441");
        formPage.setCardMonth(getDateMonth(0));;
        formPage.setCardYear(getRandomCardOwner());
        formPage.setCardOwner(getRandomCardOwner());
        formPage.setCardCVV(getRandomCvCCvV());
        formPage.pushСontinueButton();
        formPage.checkMessageWrongFormat();
    }
    @Test
    @DisplayName("Покупка в кредит по данным дебетовой карты, ввод  в поле Год однозначного числа")
    void shouldNotPayOneNumberYearField() throws SQLException {
        formPage.buyOnCredit();
        formPage.setCardNumber("4444444444444441");
        formPage.setCardMonth(getDateMonth(0));;
        formPage.setCardYear("1");
        formPage.setCardOwner(getRandomCardOwner());
        formPage.setCardCVV(getRandomCvCCvV());
        formPage.pushСontinueButton();
        formPage.checkMessageWrongFormat();
    }
    @Test
    @DisplayName("Покупка в кредит по данным дебетовой карты, ввод нулевых значений в поле Год")
    void shouldNotPayZeroYearField() throws SQLException {
        formPage.buyOnCredit();
        formPage.setCardNumber("4444444444444441");
        formPage.setCardMonth(getDateMonth(0));;
        formPage.setCardYear("00");
        formPage.setCardOwner(getRandomCardOwner());
        formPage.setCardCVV(getRandomCvCCvV());
        formPage.pushСontinueButton();
        formPage.checkMessageOverDate();
    }

    @Test
    @DisplayName("Покупка в кредит по данным дебетовой карты, ввод в поле Владелец букв кириллицы.")
    void shouldNotPayInvalidCardOwnerFieldRu() throws SQLException {
        formPage.buyOnCredit();
        formPage.setCardNumber("4444444444444441");
        formPage.setCardMonth(getDateMonth(0));;
        formPage.setCardYear(getDateYear(0));
        formPage.setCardOwner(getRandomCardOwnerRu());
        formPage.setCardCVV(getRandomCvCCvV());
        formPage.pushСontinueButton();
        formPage.checkMessageError();
    }
    @Test
    @DisplayName("Покупка в кредит по данным дебетовой карты, ввод в поле Владелец спецсимволов.")
    void shouldNotPayInvalidCardOwnerFieldSymbol() throws SQLException {
        formPage.buyOnCredit();
        formPage.setCardNumber("4444444444444441");
        formPage.setCardMonth(getDateMonth(0));;
        formPage.setCardYear(getDateYear(0));
        formPage.setCardOwner("#%$@");
        formPage.setCardCVV(getRandomCvCCvV());
        formPage.pushСontinueButton();
        formPage.checkMessageError();
    }
    @Test
    @DisplayName("Покупка в кредит по данным дебетовой карты, ввод в поле Владелец цифр.")
    void shouldNotPayInvalidCardOwnerFieldNumbers() throws SQLException {
        formPage.buyOnCredit();
        formPage.setCardNumber("4444444444444441");
        formPage.setCardMonth(getDateMonth(0));;
        formPage.setCardYear(getDateYear(0));
        formPage.setCardOwner(getRandomNumbers());
        formPage.setCardCVV(getRandomCvCCvV());
        formPage.pushСontinueButton();
        formPage.checkMessageError();
    }
    @Test
    @DisplayName("Покупка в кредит по данным дебетовой карты, оставление поля Владелец пустым.")
    void shouldNotPayInvalidCardOwnerFieldEmpty() throws SQLException {
        formPage.buyOnCredit();
        formPage.setCardNumber("4444444444444441");
        formPage.setCardMonth(getDateMonth(0));;
        formPage.setCardYear(getDateYear(0));
        formPage.setCardOwner("");
        formPage.setCardCVV(getRandomCvCCvV());
        formPage.pushСontinueButton();
        formPage.checkMessageRequiredField();
    }

    @Test
    @DisplayName("Покупка в кредит по данным дебетовой карты, оставление поля CVC/CVV пустым.")
    void shouldNoPayInvalidCVVField() throws SQLException {
        formPage.buyOnCredit();
        formPage.setCardNumber("4444444444444441");
        formPage.setCardMonth(getDateMonth(0));;
        formPage.setCardYear(getDateYear(0));
        formPage.setCardOwner(getRandomCardOwner());
        formPage.setCardCVV("");
        formPage.pushСontinueButton();
        formPage.checkMessageWrongFormat();
    }
    @Test
    @DisplayName("Покупка в кредит по данным дебетовой карты, ввод в поле CVC/CVV спецсимволов.")
    void shouldNoPayInvalidCVVFieldSymbol() throws SQLException {
        formPage.buyOnCredit();
        formPage.setCardNumber("4444444444444441");
        formPage.setCardMonth(getDateMonth(0));;
        formPage.setCardYear(getDateYear(0));
        formPage.setCardOwner(getRandomCardOwner());
        formPage.setCardCVV("#$@");
        formPage.pushСontinueButton();
        formPage.checkMessageWrongFormat();
    }
    @Test
    @DisplayName("Покупка в кредит по данным дебетовой карты, ввод в поле CVC/CVV букв (кириллица).")
    void shouldNoPayInvalidCVVFieldRu() throws SQLException {
        formPage.buyOnCredit();
        formPage.setCardNumber("4444444444444441");
        formPage.setCardMonth(getDateMonth(0));;
        formPage.setCardYear(getDateYear(0));
        formPage.setCardOwner(getRandomCardOwner());
        formPage.setCardCVV(getRandomCardOwnerRu());
        formPage.pushСontinueButton();
        formPage.checkMessageWrongFormat();
    }
    @Test
    @DisplayName("Покупка в кредит по данным дебетовой карты, ввод в поле CVC/CVV букв (латинские).")
    void shouldNoPayInvalidCVVFieldEn() throws SQLException {
        formPage.buyOnCredit();
        formPage.setCardNumber("4444444444444441");
        formPage.setCardMonth(getDateMonth(0));;
        formPage.setCardYear(getDateYear(0));
        formPage.setCardOwner(getRandomCardOwner());
        formPage.setCardCVV(getRandomCardOwner());
        formPage.pushСontinueButton();
        formPage.checkMessageWrongFormat();
    }
    @Test
    @DisplayName("Покупка в кредит по данным дебетовой карты, ввод в поле CVC/CVV однозначного числа.")
    void shouldNoPayInvalidCVVFieldOneNumber() throws SQLException {
        formPage.buyOnCredit();
        formPage.setCardNumber("4444444444444441");
        formPage.setCardMonth(getDateMonth(0));;
        formPage.setCardYear(getDateYear(0));
        formPage.setCardOwner(getRandomCardOwner());
        formPage.setCardCVV("1");
        formPage.pushСontinueButton();
        formPage.checkMessageWrongFormat();
    }
    @Test
    @DisplayName("Покупка в кредит по данным дебетовой карты, ввод в поле CVC/CVV двузначного числа.")
    void shouldNoPayInvalidCVVFieldTwoNumber() throws SQLException {
        formPage.buyOnCredit();
        formPage.setCardNumber("4444444444444441");
        formPage.setCardMonth(getDateMonth(0));;
        formPage.setCardYear(getDateYear(0));
        formPage.setCardOwner(getRandomCardOwner());
        formPage.setCardCVV("01");
        formPage.pushСontinueButton();
        formPage.checkMessageWrongFormat();
    }

    @Test
    @DisplayName("Попытка покупки в кредит по карте c пустым номером")
    void shouldNoPayEmptyCardNumberField() throws SQLException {
        formPage.buyOnCredit();
        formPage.setCardNumber("");
        formPage.setCardMonth(getDateMonth(0));
        formPage.setCardYear(getDateYear(0));
        formPage.setCardOwner(getRandomCardOwner());
        formPage.setCardCVV(getRandomCvCCvV());
        formPage.pushСontinueButton();
        formPage.checkMessageWrongFormat();
    }
}
