package aqashoptest;


import base.DBUtils;
import base.FormPage;
import base.Status;
import com.codeborne.selenide.logevents.SelenideLogger;
import io.qameta.allure.selenide.AllureSelenide;
import org.junit.jupiter.api.*;

import static base.DataGenerator.*;
import static com.codeborne.selenide.Selenide.open;

public class TestCreditGate {
    private FormPage formPage;

    @AfterEach
    void clearAll() {
        DBUtils.clearAllData();}

    @AfterAll
    static void tearDownAll() {
        SelenideLogger.removeListener("allure");}
    @BeforeEach
    void setUp() {
        formPage = open("http://localhost:8080", FormPage.class);
    }

    @BeforeAll
    static void setUpAll() {
        SelenideLogger.addListener("allure", new AllureSelenide());
    }

    @Test
    @DisplayName("Покупка в кредит активной дебетовой картой, валидные данные")
    void shouldPayByApprovedCardOnCreditDb() {
        formPage.buyOnCredit();
        formPage.setCardNumber(getCardNumberValid());
        formPage.setCardMonth(getDateMonth(0));
        formPage.setCardYear(getDateYear(0));
        formPage.setCardOwner(getRandomCardOwner());
        formPage.setCardCVV(getRandomCvCCvV());
        formPage.pushContinueButton();
        formPage.checkMessageSuccess();
        DBUtils.getCreditStatus(Status.APPROVED);
    }

    @Test
    @DisplayName("Покупка в кредит отклоненной дебетовой картой, валидные данные")
    void shouldNotPayByDeclinedCard() {
        formPage.buyOnCredit();
        formPage.setCardNumber(getCardNumberDeclined());
        formPage.setCardMonth(getDateMonth(0));
        formPage.setCardYear(getDateYear(0));
        formPage.setCardOwner(getRandomCardOwner());
        formPage.setCardCVV(getRandomCvCCvV());
        formPage.pushContinueButton();
        formPage.checkMessageError();
        DBUtils.getCreditStatus(Status.DECLINED);
    }

    @Test
    @DisplayName("Покупка в кредит по данным дебетовой карты не из набора, валидные данные")
    void shouldNotPayByUnknownCard() {
        formPage.buyOnCredit();
        formPage.setCardNumber(getCardNumberUnknown());
        formPage.setCardMonth(getDateMonth(0));
        formPage.setCardYear(getDateYear(0));
        formPage.setCardOwner(getRandomCardOwner());
        formPage.setCardCVV(getRandomCvCCvV());
        formPage.pushContinueButton();
        formPage.checkMessageError();
    }

    @Test
    @DisplayName("Покупка в кредит по данным дебетовой карты, ввод месяца, при котором срок действия карты уже истек")
    void shouldNotExpiredDateMonthField() {
        formPage.buyOnCredit();
        formPage.setCardNumber(getCardNumberValid());
        formPage.setCardMonth(getDateMonth(11));;
        formPage.setCardYear(getDateYear(0));
        formPage.setCardOwner(getRandomCardOwner());
        formPage.setCardCVV(getRandomCvCCvV());
        formPage.pushContinueButton();
        formPage.checkMessageWrong("Неверно указан срок действия карты");
    }
    @Test
    @DisplayName("Покупка в кредит по данным дебетовой карты, ввод несуществующего месяца")
    void shouldNotPayInvalidMonthField() {
        formPage.buyOnCredit();
        formPage.setCardNumber(getCardNumberValid());
        formPage.setCardMonth("13");
        formPage.setCardYear(getDateYear(0));
        formPage.setCardOwner(getRandomCardOwner());
        formPage.setCardCVV(getRandomCvCCvV());
        formPage.pushContinueButton();
        formPage.checkMessageWrong("Неверно указан срок действия карты");
    }
    @Test
    @DisplayName("Покупка в кредит по данным дебетовой карты, оставление поля месяц пустым")
    void shouldNotPayEmptyMonthField() {
        formPage.buyOnCredit();
        formPage.setCardNumber(getCardNumberValid());
        formPage.setCardMonth("");
        formPage.setCardYear(getDateYear(0));
        formPage.setCardOwner(getRandomCardOwner());
        formPage.setCardCVV(getRandomCvCCvV());
        formPage.pushContinueButton();
        formPage.checkMessageWrong("Неверный формат");
    }
    @Test
    @DisplayName("Покупка в кредит по данным дебетовой карты, ввод в поле месяц спецсимволов")
    void shouldNotPaySymbolMonthField() {
        formPage.buyOnCredit();
        formPage.setCardNumber(getCardNumberValid());
        formPage.setCardMonth("#$");
        formPage.setCardYear(getDateYear(0));
        formPage.setCardOwner(getRandomCardOwner());
        formPage.setCardCVV(getRandomCvCCvV());
        formPage.pushContinueButton();
        formPage.checkMessageWrong("Неверный формат");
    }
    @Test
    @DisplayName("Покупка в кредит по данным дебетовой карты, ввод в поле Месяц букв (кириллица)")
    void shouldNotPayRuMonthField() {
        formPage.buyOnCredit();
        formPage.setCardNumber(getCardNumberValid());
        formPage.setCardMonth(getRandomCardOwnerRu());
        formPage.setCardYear(getDateYear(0));
        formPage.setCardOwner(getRandomCardOwner());
        formPage.setCardCVV(getRandomCvCCvV());
        formPage.pushContinueButton();
        formPage.checkMessageWrong("Неверный формат");
    }
    @Test
    @DisplayName("Покупка в кредит по данным дебетовой карты, ввод в поле Месяц букв (латинские)")
    void shouldNotPayEnMonthField() {
        formPage.buyOnCredit();
        formPage.setCardNumber(getCardNumberValid());
        formPage.setCardMonth(getRandomCardOwner());
        formPage.setCardYear(getDateYear(0));
        formPage.setCardOwner(getRandomCardOwner());
        formPage.setCardCVV(getRandomCvCCvV());
        formPage.pushContinueButton();
        formPage.checkMessageWrong("Неверный формат");
    }
    @Test
    @DisplayName("Покупка в кредит по данным дебетовой карты, ввод в поле Месяц однозначного числа")
    void shouldNotPayOneNumberMonthField() {
        formPage.buyOnCredit();
        formPage.setCardNumber(getCardNumberValid());
        formPage.setCardMonth("1");
        formPage.setCardYear(getDateYear(0));
        formPage.setCardOwner(getRandomCardOwner());
        formPage.setCardCVV(getRandomCvCCvV());
        formPage.pushContinueButton();
        formPage.checkMessageWrong("Неверный формат");
    }
    @Test
    @DisplayName("Покупка в кредит по данным дебетовой карты, ввод нулевых значений в поле Месяц")
    void shouldNotPayZeroMonthField() {
        formPage.buyOnCredit();
        formPage.setCardNumber(getCardNumberValid());
        formPage.setCardMonth("00");
        formPage.setCardYear(getDateYear(0));
        formPage.setCardOwner(getRandomCardOwner());
        formPage.setCardCVV(getRandomCvCCvV());
        formPage.pushContinueButton();
        formPage.checkMessageWrong("Неверно указан срок действия карты");
    }
    @Test
    @DisplayName("Покупка в кредит по данным дебетовой карты, ввод года, отстоящего от текущей даты более чем на 6 лет")
    void shouldNotPayInvalidYearField() {
        formPage.buyOnCredit();
        formPage.setCardNumber(getCardNumberValid());
        formPage.setCardMonth(getDateMonth(0));;
        formPage.setCardYear(getDateYear(6));
        formPage.setCardOwner(getRandomCardOwner());
        formPage.setCardCVV(getRandomCvCCvV());
        formPage.pushContinueButton();
        formPage.checkMessageWrong("Неверно указан срок действия карты");
    }
    @Test
    @DisplayName("Покупка в кредит по данным дебетовой карты, оставление поля Год пустым")
    void shouldNotPayEmptyYearField() {
        formPage.buyOnCredit();
        formPage.setCardNumber(getCardNumberValid());
        formPage.setCardMonth(getDateMonth(0));;
        formPage.setCardYear("");
        formPage.setCardOwner(getRandomCardOwner());
        formPage.setCardCVV(getRandomCvCCvV());
        formPage.pushContinueButton();
        formPage.checkMessageWrong("Неверный формат");
    }
    @Test
    @DisplayName("Покупка в кредит по данным дебетовой карты, ввод года, при котором срок действия карты уже истек")
    void shouldNoPayExpiredDateYearField() {
        formPage.buyOnCredit();
        formPage.setCardNumber(getCardNumberValid());
        formPage.setCardMonth(getDateMonth(0));;
        formPage.setCardYear(getDateYearMinus(1));
        formPage.setCardOwner(getRandomCardOwner());
        formPage.setCardCVV(getRandomCvCCvV());
        formPage.pushContinueButton();
        formPage.checkMessageWrong("Истёк срок действия карты");
    }
    @Test
    @DisplayName("Покупка в кредит по данным дебетовой карты, ввод в поле Год спецсимволов")
    void shouldNotPaySymbolYearField() {
        formPage.buyOnCredit();
        formPage.setCardNumber(getCardNumberValid());
        formPage.setCardMonth(getDateMonth(0));;
        formPage.setCardYear("#$");
        formPage.setCardOwner(getRandomCardOwner());
        formPage.setCardCVV(getRandomCvCCvV());
        formPage.pushContinueButton();
        formPage.checkMessageWrong("Неверный формат");
    }
    @Test
    @DisplayName("Покупка в кредит по данным дебетовой карты, ввод  в поле Год букв (кириллица)")
    void shouldNotPayRuYearField() {
        formPage.buyOnCredit();
        formPage.setCardNumber(getCardNumberValid());
        formPage.setCardMonth(getDateMonth(0));;
        formPage.setCardYear(getRandomCardOwnerRu());
        formPage.setCardOwner(getRandomCardOwner());
        formPage.setCardCVV(getRandomCvCCvV());
        formPage.pushContinueButton();
        formPage.checkMessageWrong("Неверный формат");
    }
    @Test
    @DisplayName("Покупка в кредит по данным дебетовой карты, ввод  в поле Год букв (латинские)")
    void shouldNotPayEnYearField() {
        formPage.buyOnCredit();
        formPage.setCardNumber(getCardNumberValid());
        formPage.setCardMonth(getDateMonth(0));;
        formPage.setCardYear(getRandomCardOwner());
        formPage.setCardOwner(getRandomCardOwner());
        formPage.setCardCVV(getRandomCvCCvV());
        formPage.pushContinueButton();
        formPage.checkMessageWrong("Неверный формат");
    }
    @Test
    @DisplayName("Покупка в кредит по данным дебетовой карты, ввод  в поле Год однозначного числа")
    void shouldNotPayOneNumberYearField() {
        formPage.buyOnCredit();
        formPage.setCardNumber(getCardNumberValid());
        formPage.setCardMonth(getDateMonth(0));;
        formPage.setCardYear("1");
        formPage.setCardOwner(getRandomCardOwner());
        formPage.setCardCVV(getRandomCvCCvV());
        formPage.pushContinueButton();
        formPage.checkMessageWrong("Неверный формат");
    }
    @Test
    @DisplayName("Покупка в кредит по данным дебетовой карты, ввод нулевых значений в поле Год")
    void shouldNotPayZeroYearField() {
        formPage.buyOnCredit();
        formPage.setCardNumber(getCardNumberValid());
        formPage.setCardMonth(getDateMonth(0));;
        formPage.setCardYear("00");
        formPage.setCardOwner(getRandomCardOwner());
        formPage.setCardCVV(getRandomCvCCvV());
        formPage.pushContinueButton();
        formPage.checkMessageWrong("Истёк срок действия карты");
    }

    @Test
    @DisplayName("Покупка в кредит по данным дебетовой карты, ввод в поле Владелец букв кириллицы.")
    void shouldNotPayInvalidCardOwnerFieldRu() {
        formPage.buyOnCredit();
        formPage.setCardNumber(getCardNumberValid());
        formPage.setCardMonth(getDateMonth(0));;
        formPage.setCardYear(getDateYear(0));
        formPage.setCardOwner(getRandomCardOwnerRu());
        formPage.setCardCVV(getRandomCvCCvV());
        formPage.pushContinueButton();
        formPage.checkMessageError();
    }
    @Test
    @DisplayName("Покупка в кредит по данным дебетовой карты, ввод в поле Владелец спецсимволов.")
    void shouldNotPayInvalidCardOwnerFieldSymbol() {
        formPage.buyOnCredit();
        formPage.setCardNumber(getCardNumberValid());
        formPage.setCardMonth(getDateMonth(0));;
        formPage.setCardYear(getDateYear(0));
        formPage.setCardOwner("#%$@");
        formPage.setCardCVV(getRandomCvCCvV());
        formPage.pushContinueButton();
        formPage.checkMessageError();
    }
    @Test
    @DisplayName("Покупка в кредит по данным дебетовой карты, ввод в поле Владелец цифр.")
    void shouldNotPayInvalidCardOwnerFieldNumbers() {
        formPage.buyOnCredit();
        formPage.setCardNumber(getCardNumberValid());
        formPage.setCardMonth(getDateMonth(0));;
        formPage.setCardYear(getDateYear(0));
        formPage.setCardOwner(getRandomNumbers());
        formPage.setCardCVV(getRandomCvCCvV());
        formPage.pushContinueButton();
        formPage.checkMessageError();
    }
    @Test
    @DisplayName("Покупка в кредит по данным дебетовой карты, оставление поля Владелец пустым.")
    void shouldNotPayInvalidCardOwnerFieldEmpty() {
        formPage.buyOnCredit();
        formPage.setCardNumber(getCardNumberValid());
        formPage.setCardMonth(getDateMonth(0));;
        formPage.setCardYear(getDateYear(0));
        formPage.setCardOwner("");
        formPage.setCardCVV(getRandomCvCCvV());
        formPage.pushContinueButton();
        formPage.checkMessageWrong("Поле обязательно для заполнения");
    }

    @Test
    @DisplayName("Покупка в кредит по данным дебетовой карты, оставление поля CVC/CVV пустым.")
    void shouldNoPayInvalidCVVField() {
        formPage.buyOnCredit();
        formPage.setCardNumber(getCardNumberValid());
        formPage.setCardMonth(getDateMonth(0));;
        formPage.setCardYear(getDateYear(0));
        formPage.setCardOwner(getRandomCardOwner());
        formPage.setCardCVV("");
        formPage.pushContinueButton();
        formPage.checkMessageWrong("Неверный формат");
    }
    @Test
    @DisplayName("Покупка в кредит по данным дебетовой карты, ввод в поле CVC/CVV спецсимволов.")
    void shouldNoPayInvalidCVVFieldSymbol() {
        formPage.buyOnCredit();
        formPage.setCardNumber(getCardNumberValid());
        formPage.setCardMonth(getDateMonth(0));;
        formPage.setCardYear(getDateYear(0));
        formPage.setCardOwner(getRandomCardOwner());
        formPage.setCardCVV("#$@");
        formPage.pushContinueButton();
        formPage.checkMessageWrong("Неверный формат");
    }
    @Test
    @DisplayName("Покупка в кредит по данным дебетовой карты, ввод в поле CVC/CVV букв (кириллица).")
    void shouldNoPayInvalidCVVFieldRu() {
        formPage.buyOnCredit();
        formPage.setCardNumber(getCardNumberValid());
        formPage.setCardMonth(getDateMonth(0));;
        formPage.setCardYear(getDateYear(0));
        formPage.setCardOwner(getRandomCardOwner());
        formPage.setCardCVV(getRandomCardOwnerRu());
        formPage.pushContinueButton();
        formPage.checkMessageWrong("Неверный формат");
    }
    @Test
    @DisplayName("Покупка в кредит по данным дебетовой карты, ввод в поле CVC/CVV букв (латинские).")
    void shouldNoPayInvalidCVVFieldEn() {
        formPage.buyOnCredit();
        formPage.setCardNumber(getCardNumberValid());
        formPage.setCardMonth(getDateMonth(0));;
        formPage.setCardYear(getDateYear(0));
        formPage.setCardOwner(getRandomCardOwner());
        formPage.setCardCVV(getRandomCardOwner());
        formPage.pushContinueButton();
        formPage.checkMessageWrong("Неверный формат");
    }
    @Test
    @DisplayName("Покупка в кредит по данным дебетовой карты, ввод в поле CVC/CVV однозначного числа.")
    void shouldNoPayInvalidCVVFieldOneNumber() {
        formPage.buyOnCredit();
        formPage.setCardNumber(getCardNumberValid());
        formPage.setCardMonth(getDateMonth(0));;
        formPage.setCardYear(getDateYear(0));
        formPage.setCardOwner(getRandomCardOwner());
        formPage.setCardCVV("1");
        formPage.pushContinueButton();
        formPage.checkMessageWrong("Неверный формат");
    }
    @Test
    @DisplayName("Покупка в кредит по данным дебетовой карты, ввод в поле CVC/CVV двузначного числа.")
    void shouldNoPayInvalidCVVFieldTwoNumber() {
        formPage.buyOnCredit();
        formPage.setCardNumber(getCardNumberValid());
        formPage.setCardMonth(getDateMonth(0));;
        formPage.setCardYear(getDateYear(0));
        formPage.setCardOwner(getRandomCardOwner());
        formPage.setCardCVV("01");
        formPage.pushContinueButton();
        formPage.checkMessageWrong("Неверный формат");
    }

    @Test
    @DisplayName("Попытка покупки в кредит по карте c пустым номером")
    void shouldNoPayEmptyCardNumberField() {
        formPage.buyOnCredit();
        formPage.setCardNumber("");
        formPage.setCardMonth(getDateMonth(0));
        formPage.setCardYear(getDateYear(0));
        formPage.setCardOwner(getRandomCardOwner());
        formPage.setCardCVV(getRandomCvCCvV());
        formPage.pushContinueButton();
        formPage.checkMessageWrong("Неверный формат");
    }
}
