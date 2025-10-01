package guru.qa.niffler.page;

import static com.codeborne.selenide.Selenide.$;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;

public class FriendsPage {

  private final SelenideElement
      searchInput = $("input[aria-label='search']"),
      friendsTable = $(".MuiTable-root").as("Friends Table"),
      peopleTab = $("a[href='/people/friends']"),
      allTab = $("a[href='/people/all']");

  public final ElementsCollection friendsTableRows = friendsTable.$$("tbody tr").as("Friends Table Rows");

  public FriendsPage checkThatFriendsTableContains(String friend) {
    SelenideElement tbodyTr = friendsTableRows.findBy(Condition.text(friend))
        .shouldBe(Condition.visible);
    tbodyTr.$("button").shouldHave(Condition.text("Unfriend"));
    return this;
  }

  public FriendsPage checkThatFriendsTableIsEmpty() {
    friendsTableRows.first().shouldNotBe(Condition.visible);
    return this;
  }

  public FriendsPage checkThatIncomeInvitationIsPresented(String friend) {
    searchInput.setValue(friend).pressEnter();
    SelenideElement tbodyTr = friendsTableRows.findBy(Condition.text(friend))
        .shouldBe(Condition.visible);
    tbodyTr.$$("button").first().shouldHave(Condition.text("Accept"));
    return this;
  }

  public FriendsPage checkThatOutcomeInvitationIsPresented(String friend) {
    allTab.click();
    searchInput.setValue(friend).pressEnter();
    SelenideElement tbodyTr = friendsTableRows.findBy(Condition.text(friend))
        .shouldBe(Condition.visible);
    tbodyTr.$(".MuiChip-label").shouldHave(Condition.text("Waiting..."));
    return this;
  }
}
