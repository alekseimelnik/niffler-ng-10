package guru.qa.niffler.data.entity.auth;

import guru.qa.niffler.model.auth.Authority;
import java.io.Serializable;
import java.util.UUID;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AuthorityEntity implements Serializable {

  private UUID id;
  private Authority authority;
  private UserAuthEntity user;

  public static AuthorityEntity fromJson(Authority authority, UserAuthEntity user) {
    AuthorityEntity ae = new AuthorityEntity();
    ae.setAuthority(authority);
    ae.setUser(user);
    return ae;
  }
}
