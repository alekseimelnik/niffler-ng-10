package guru.qa.niffler.config;

public interface Config {

  static Config getInstance() {
    return LocalConfig.INSTANCE;
  }

  String frontUrl();

  String authUrl();

  String getawayUrl();

  String userdataUrl();

  String spendUrl();

  String spendJdbcUrl();

  String githubUrl();
}
