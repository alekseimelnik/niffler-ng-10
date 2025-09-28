package guru.qa.niffler.test.web;

import com.codeborne.selenide.Selenide;
import guru.qa.niffler.config.Config;
import guru.qa.niffler.jupiter.annotation.WebTest;
import guru.qa.niffler.page.LoginPage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static guru.qa.niffler.utils.DataUtils.getRandomPassword;
import static guru.qa.niffler.utils.DataUtils.getRandomUserName;
import static org.junit.jupiter.api.Assertions.assertTrue;

@WebTest
public class RegistrationTest {

    private static final Config CFG = Config.getInstance();
    private LoginPage loginPage;

    @BeforeEach
    void setUp() {
        loginPage = Selenide.open(CFG.frontUrl(), LoginPage.class);
    }

    @Test
    void shouldRegisterNewUser() {
        loginPage
                .goToRegistrationPage()
                .checkThatPageIsLoaded()
                .fillAndSubmitSuccessRegistration(getRandomUserName(), getRandomPassword())
                .checkThatLoginPageIsLoaded();
    }

    @Test
    void shouldNotRegisterWithExistingUserName() {
        String username = getRandomUserName();
        String password = getRandomPassword();
        String errorMessage = "Username `" + username + "` already exists";

        loginPage
                .goToRegistrationPage()
                .checkThatPageIsLoaded()
                .fillAndSubmitSuccessRegistration(username, password)
                .goToRegistrationPage()
                .checkThatPageIsLoaded()
                .setUsername(username)
                .setPassword(password)
                .setConfirmPassword(password)
                .clickSignUpButton()
                .getErrorMessage(errorMessage);
    }

    @Test
    void shouldShowErrorIfPasswordAndConfirmPasswordAreNotEqual() {
        String username = getRandomUserName();
        String password = getRandomPassword();
        String wrongConfirmPassword = getRandomPassword();
        String errorMessage = "Passwords should be equal";

        loginPage
                .goToRegistrationPage()
                .checkThatPageIsLoaded()
                .setUsername(username)
                .setPassword(password)
                .setConfirmPassword(wrongConfirmPassword)
                .clickSignUpButton()
                .getErrorMessage(errorMessage);
    }

    @Test
    void mainPageShouldBeDisplayedAfterSuccessLogin() {
        String username = getRandomUserName();
        String password = getRandomPassword();
        loginPage
                .goToRegistrationPage()
                .checkThatPageIsLoaded()
                .fillAndSubmitSuccessRegistration(username, password)
                .login(username, password)
                .checkTheMainPageIsLoaded();
    }

    @Test
    void userShouldStayOnLoginPageAfterLoginWithBadCredentials() {
        String username = getRandomUserName();
        String correctPassword = getRandomPassword();
        String wrongPassword = getRandomPassword();
        loginPage
                .goToRegistrationPage()
                .checkThatPageIsLoaded()
                .fillAndSubmitSuccessRegistration(username, correctPassword)
                .loginWithBadCredentials(username, wrongPassword)
                .checkThatLoginPageIsLoaded();
    }
}
