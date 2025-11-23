package ru.netology.banklogin.page.test;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.netology.banklogin.page.data.DataHelper;
import ru.netology.banklogin.page.data.SQLHelper;
import ru.netology.banklogin.page.page.LoginPage;

import static com.codeborne.selenide.Selenide.open;
import static ru.netology.banklogin.page.data.SQLHelper.cleanAuthCodes;
import static ru.netology.banklogin.page.data.SQLHelper.cleanDatabase;

public class BankLoginTest {

    LoginPage loginPage;
    DataHelper.AuthInfo authInfo = DataHelper.getAuthInfoWithTestData();

    @AfterAll
    static void tearDownAll() {
        cleanDatabase();
    }

    @AfterEach
    void tearDown() {
        cleanAuthCodes();
    }

    @BeforeEach
    void setup() {
        loginPage = open("http://localhost:9999/", LoginPage.class);
    }

    @Test
    void shouldSuccessfulLogin() {
        var verificationPage = loginPage.validLogin(authInfo);
        var verificationCode = SQLHelper.getVerificationCode();
        verificationPage.validVerify(verificationCode.getCode());

    }

    @Test
    void shouldGetErrorLogin() {
        var authInfo = DataHelper.generateRandomUser();
        loginPage.login(authInfo);
        loginPage.verifyErrorNotification("Ошибка\n" +
                "Ошибка! Неверно указан логин или пароль");
    }

    @Test
    void shouldGetErrorCode() {
        var verificationPage = loginPage.validLogin(authInfo);
        var verificationCode = DataHelper.generateRandomVerificationCode();
        verificationPage.verify(verificationCode.getCode());
        verificationPage.verifyErrorNotification("Ошибка\n" +
                "Ошибка! Неверно указан код! Попробуйте ещё раз.");

    }
}