package guru.qa.niffler.model;

import guru.qa.niffler.data.entity.spend.CategoryEntity;
import guru.qa.niffler.data.entity.spend.UserEntity;

public record UserJson (
    String username,
    CurrencyValues currency,
    String firstname,
    String surname,
    byte[] photo,
    byte[] photoSmall,
    String fullName
) {
  public static   UserJson fromEntity(UserEntity entity) {
    return new UserJson(
        entity.getUsername(),
        entity.getCurrency(),
        entity.getFirstName(),
        entity.getSurname(),
        entity.getPhoto(),
        entity.getPhotoSmall(),
        entity.getFullName()
    );
  }
}
