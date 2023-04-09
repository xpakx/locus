package io.github.xpakx.locus.auth.dto;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class AuthenticationResponse {
    private String token;
    private String username;
    private boolean moderator_role;
}
