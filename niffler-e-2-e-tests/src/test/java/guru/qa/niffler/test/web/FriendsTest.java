package guru.qa.niffler.test.web;

import static guru.qa.niffler.jupiter.annotation.UserType.FriendType.*;

import com.codeborne.selenide.Selenide;
import guru.qa.niffler.config.Config;
import guru.qa.niffler.jupiter.annotation.UserQueue;
import guru.qa.niffler.jupiter.annotation.UserType;
import guru.qa.niffler.jupiter.annotation.WebTest;
import guru.qa.niffler.model.user.StaticUser;
import guru.qa.niffler.page.LoginPage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@WebTest
public class FriendsTest {

  private static final Config CFG = Config.getInstance();
  private LoginPage loginPage;

  @BeforeEach
  void setUp() {
    loginPage = Selenide.open(CFG.frontUrl(), LoginPage.class);
  }

  @Test
  @DisplayName("Friends should be presented in friends table")
  @UserQueue
  void friendsShouldBePresentedInFriendsTable(@UserType(WITH_FRIEND) StaticUser user) {
    loginPage
        .login(user.username(), user.password())
        .goToFriendsPage()
        .checkThatFriendsTableContains(user.friend());
  }

  @Test
  @DisplayName("Friends table should be empty for a user without friends")
  @UserQueue
  void friendsTableShouldBeEmptyForNewUser(@UserType(EMPTY) StaticUser user) {
    loginPage
        .login(user.username(), user.password())
        .goToFriendsPage()
        .checkThatFriendsTableIsEmpty();
  }

  @Test
  @DisplayName("Income invitation should be presented in friends table")
  @UserQueue
  void incomeInvitationBePresentedInFriendsTable(@UserType(WITH_INCOME_REQUEST) StaticUser user) {
    loginPage
        .login(user.username(), user.password())
        .goToFriendsPage()
        .checkThatIncomeInvitationIsPresented(user.income());
  }

  @Test
  @DisplayName("Outcome invitation should be presented in friends table")
  @UserQueue
  void outcomeInvitationShouldBePresentedInFriendsTable(@UserType(WITH_OUTCOME_REQUEST) StaticUser user) {
    loginPage
        .login(user.username(), user.password())
        .goToFriendsPage()
        .checkThatOutcomeInvitationIsPresented(user.outcome());
  }
}
