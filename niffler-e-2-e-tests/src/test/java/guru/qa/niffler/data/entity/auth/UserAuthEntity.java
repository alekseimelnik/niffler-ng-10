package guru.qa.niffler.data.entity.auth;

import guru.qa.niffler.model.auth.UserAuthJson;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserAuthEntity implements Serializable {

  private UUID id;
  private String username;
  private String password;
  private Boolean enabled;
  private Boolean accountNonExpired;
  private Boolean accountNonLocked;
  private Boolean credentialsNonExpired;
  private List<AuthorityEntity> authorities = new ArrayList<>();

  public static UserAuthEntity fromJson(UserAuthJson json) {
    UserAuthEntity uae = new UserAuthEntity();
    uae.setUsername(json.username());
    uae.setPassword(json.password());
    uae.setEnabled(json.enabled());
    uae.setAccountNonExpired(json.accountNonExpired());
    uae.setAccountNonLocked(json.accountNonLocked());
    uae.setCredentialsNonExpired(json.credentialsNonExpired());
    return uae;
  }
}
