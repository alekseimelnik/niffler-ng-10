package guru.qa.niffler.page;

import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;

public class RegistrationPage {

    public final SelenideElement
            usernameInput = $("#username"),
            passwordInput = $("#password"),
            confirmPasswordInput = $("#passwordSubmit"),
            signUpBtn = $("#register-button"),
            successSignInBtn = $(".form_sign-in"),
            errorLabel = $(".form__error");

    @Step("Set username: '{username}'")
    public RegistrationPage setUsername(String username) {
        usernameInput.setValue(username);
        return this;
    }

    @Step("Set password")
    public RegistrationPage setPassword(String password) {
        passwordInput.setValue(password);
        return this;
    }

    @Step("Set password confirmation")
    public RegistrationPage setConfirmPassword(String password) {
        confirmPasswordInput.setValue(password);
        return this;
    }

    @Step("Click sign up button")
    public RegistrationPage clickSignUpButton() {
        signUpBtn.click();
        return this;
    }

    @Step("Submit registration")
    public LoginPage submitSuccessRegistration() {
        signUpBtn.click();
        successSignInBtn.click();
        return new LoginPage();
    }

    @Step("Submit fail registration")
    public RegistrationPage submitFailRegistration() {
        signUpBtn.click();
        return this;
    }

    @Step("Fill and submit successful registration with username: '{username}' and password: '{password}'")
    public LoginPage fillAndSubmitSuccessRegistration(String username, String password) {
        return this.setUsername(username)
                .setPassword(password)
                .setConfirmPassword(password)
                .submitSuccessRegistration();
    }

    @Step("Verify error label visible")
    public boolean verifyErrorLabelVisible() {
        return errorLabel.shouldBe(visible).isDisplayed();
    }

    @Step("Get error message: '{message}'")
    public RegistrationPage getErrorMessage(String message) {
        errorLabel.shouldHave(visible).shouldHave(text(message));
        return this;
    }

    @Step("Check that registration page is loaded")
    public RegistrationPage checkThatPageIsLoaded() {
        usernameInput.shouldBe(visible);
        passwordInput.shouldBe(visible);
        confirmPasswordInput.shouldBe(visible);
        signUpBtn.shouldBe(visible);
        return this;
    }
}
