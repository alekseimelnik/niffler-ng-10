package guru.qa.niffler.page;

import com.codeborne.selenide.SelenideElement;

import static com.codeborne.selenide.Selenide.$;

public class RegistrationPage {

    public final SelenideElement
            usernameInput = $("#username"),
            passwordInput = $("#password"),
            matchingPasswordInput = $("#matchingPassword"),
            submitBtn = $("#register-button");

    protected RegistrationPage setUsername(String username) {
        usernameInput.setValue(username);
        return this;
    }

    protected RegistrationPage setPassword(String password) {
        passwordInput.setValue(password);
        return this;
    }

    protected RegistrationPage setPasswordSubmit(String password) {
        matchingPasswordInput.setValue(password);
        return this;
    }

    protected LoginPage submitRegistration() {
        submitBtn.click();
        return new LoginPage();
    }
}
