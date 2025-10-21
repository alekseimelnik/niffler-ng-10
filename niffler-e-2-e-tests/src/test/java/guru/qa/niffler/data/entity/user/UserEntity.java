package guru.qa.niffler.data.entity.user;

import guru.qa.niffler.model.spend.CurrencyValues;
import guru.qa.niffler.model.user.UserJson;
import java.io.Serializable;
import java.util.UUID;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserEntity implements Serializable {

  private UUID id;
  private String username;
  private CurrencyValues currency;
  private String fullName;
  private String firstName;
  private String surname;
  private byte[] photo;
  private byte[] photoSmall;

  public static UserEntity fromJson(UserJson json) {
    UserEntity ue = new UserEntity();
    ue.setUsername(json.username());
    ue.setCurrency(json.currency());
    ue.setFirstName(json.firstname());
    ue.setSurname(json.surname());
    ue.setPhoto(json.photo());
    ue.setPhotoSmall(json.photoSmall());
    ue.setFullName(json.fullName());
    return ue;
  }
}
