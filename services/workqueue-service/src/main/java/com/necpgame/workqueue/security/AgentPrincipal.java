package com.necpgame.workqueue.security;

import java.security.Principal;
import java.util.UUID;

public record AgentPrincipal(
        UUID id,
        String roleKey,
        String displayName
) implements Principal {
    @Override
    public String getName() {
        return displayName;
    }
}


