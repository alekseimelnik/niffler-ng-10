package guru.qa.niffler.test.web;

import guru.qa.niffler.jupiter.annotation.UserQueue;
import guru.qa.niffler.jupiter.annotation.WebTest;
import org.junit.jupiter.api.Test;

@WebTest
public class FriendsTest {

  @UserQueue
  @Test
  void friendsShouldBePresentedInFriendsTable() {
  }

  @UserQueue
  @Test
  void friendsTableShouldBeEmptyForNewUser() {
  }

  @UserQueue
  @Test
  void incomeInvitationBePresentedInFriendsTable() {
  }

  @UserQueue
  @Test
  void outcomeInvitationShouldBePresentedInFriendsTable() {
  }
}
