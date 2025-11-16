package com.necpgame.workqueue.security;

import com.necpgame.workqueue.domain.AgentEntity;
import com.necpgame.workqueue.service.AgentDirectoryService;
import com.necpgame.workqueue.service.exception.EntityNotFoundException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@Component
@RequiredArgsConstructor
public class ApiKeyAuthenticationFilter extends OncePerRequestFilter {
    private static final List<String> PUBLIC_PATHS = List.of("/actuator/**");
    private static final String AGENT_ROLE_HEADER = "X-Agent-Role";
    private final SecurityProperties securityProperties;
    private final AgentDirectoryService agentDirectoryService;
    private final AntPathMatcher matcher = new AntPathMatcher();

    @Override
    protected boolean shouldNotFilter(@NonNull HttpServletRequest request) {
        String path = request.getRequestURI();
        return PUBLIC_PATHS.stream().anyMatch(pattern -> matcher.match(pattern, path));
    }

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull FilterChain filterChain) throws ServletException, IOException {
        if (!securityProperties.isEnabled()) {
            filterChain.doFilter(request, response);
            return;
        }
        if (SecurityContextHolder.getContext().getAuthentication() != null) {
            filterChain.doFilter(request, response);
            return;
        }
        String agentHeader = null;
        String roleHeader = request.getHeader(AGENT_ROLE_HEADER);
        AgentEntity agent;
        try {
            agent = resolveAgent(agentHeader, roleHeader);
        } catch (IllegalArgumentException e) {
            response.setStatus(HttpStatus.BAD_REQUEST.value());
            return;
        } catch (EntityNotFoundException e) {
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            return;
        }
        if (agent == null) {
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            return;
        }
        AgentPrincipal principal = new AgentPrincipal(agent.getId(), agent.getRoleKey(), agent.getDisplayName());
        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(principal, "", List.of(new SimpleGrantedAuthority("ROLE_AGENT")));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        filterChain.doFilter(request, response);
    }

    private AgentEntity resolveAgent(String idHeader, String roleHeader) {
        if (StringUtils.hasText(roleHeader)) {
            return agentDirectoryService.requireActiveByRole(roleHeader);
        }
        return null;
    }
}

