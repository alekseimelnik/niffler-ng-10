package guru.qa.niffler.utils;

import com.github.javafaker.Faker;

public class DataUtils {

  private static final Faker faker = new Faker();

  public static String getRandomUserName() {
    return faker.name().username();
  }

  public static String getRandomPassword() {
    return faker.internet().password(3, 12, true, true, true);
  }

  public static String getRandomName() {
    return faker.name().firstName();
  }

  public static String getRandomSurname() {
    return faker.name().lastName();
  }

  public static String getRandomCategoryName() {
    return faker.commerce().department();
  }

  public static String getRandomSentence(int wordCount) {
    return faker.lorem().sentence(wordCount);
  }
}
