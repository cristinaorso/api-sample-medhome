package br.edu.atitus.apisample.components;

import br.edu.atitus.apisample.services.UserService;
import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class AuthTokenFilter extends OncePerRequestFilter {

    private final UserService userService;

    public AuthTokenFilter(UserService userService ) {
        this.userService = userService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        String token = JwtUtil.getJwtFromRequest(request); //recebe o token da requisição
        if (token != null) {
            Claims payload = JwtUtil.validateToken(token); //the class Claims represents the payload data of a token
            if (payload != null) {
                String email = payload.get("email", String.class);
                //estamos conferindo o user pelo email, mas poderia ser pelo id também
                var userAuth = userService.loadUserByUsername(email); //userAuth -> user autenticado
                //aqui estou definino que esta requisicao está autenticada
                //e autenticada com esse usuario "userAuth"
                SecurityContextHolder.getContext()
                        .setAuthentication(new UsernamePasswordAuthenticationToken(userAuth, null, null));
            }
        }
        filterChain.doFilter(request, response);
    }
}
