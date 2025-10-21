package guru.qa.niffler.model.auth;

import guru.qa.niffler.data.entity.auth.UserAuthEntity;
import java.util.UUID;

public record UserAuthJson(
    UUID id,
    String username,
    String password,
    Boolean enabled,
    Boolean accountNonExpired,
    Boolean accountNonLocked,
    Boolean credentialsNonExpired
) {
  public static UserAuthJson fromEntity(UserAuthEntity entity) {
    return new UserAuthJson(
        entity.getId(),
        entity.getUsername(),
        entity.getPassword(),
        entity.getEnabled(),
        entity.getAccountNonExpired(),
        entity.getAccountNonLocked(),
        entity.getCredentialsNonExpired()
    );
  }
}