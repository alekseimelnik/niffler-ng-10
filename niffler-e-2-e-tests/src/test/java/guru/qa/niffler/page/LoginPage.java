package guru.qa.niffler.page;

import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;

public class LoginPage {
    private final SelenideElement usernameInput = $("#username");
    private final SelenideElement passwordInput = $("#password");
    private final SelenideElement submitBtn = $("#login-button");
    private final SelenideElement registerBtn = $("#register-button");
    private final SelenideElement errorMessage = $(".form__error");

    @Step("Login with username: '{username}' and password: '{password}'")
    public MainPage login(String username, String password) {
        usernameInput.val(username);
        passwordInput.val(password);
        submitBtn.click();
        return new MainPage();
    }

    @Step("Login with username: '{username}' and uncorrect password: '{password}'")
    public LoginPage loginWithBadCredentials(String username, String wrongPassword) {
        usernameInput.val(username);
        passwordInput.val(wrongPassword);
        submitBtn.click();
        return this;
    }

    @Step("Check error message: '{message}'")
    public LoginPage checkErrorMessage(String message) {
        errorMessage.shouldHave(text(message));
        return this;
    }

    @Step("Go to registration page")
    public RegistrationPage goToRegistrationPage() {
        registerBtn.click();
        return new RegistrationPage();
    }

    @Step("Check that login page is loaded")
    public LoginPage checkThatLoginPageIsLoaded() {
        usernameInput.shouldBe(visible);
        passwordInput.shouldBe(visible);
        submitBtn.shouldBe(visible);
        registerBtn.shouldBe(visible);
        return this;
    }
}
