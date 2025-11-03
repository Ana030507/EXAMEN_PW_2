package co.edu.usco.pw.spring_restful_swagger.security;

import co.edu.usco.pw.spring_restful_swagger.model.Usuario;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.stream.Collectors;

public class CustomUserDetails implements UserDetails {

    private final Usuario usuario;

    public CustomUserDetails(Usuario usuario) {
        this.usuario = usuario;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        if (usuario.getRoles() == null) return java.util.List.of();
        return usuario.getRoles().stream()
                .map(r -> new SimpleGrantedAuthority(r.getNombre()))
                .collect(Collectors.toList());
    }

    @Override public String getPassword() { return usuario.getPassword(); }
    @Override public String getUsername() { return usuario.getUsername(); }
    @Override public boolean isAccountNonExpired() { return true; }
    @Override public boolean isAccountNonLocked() { return true; }
    @Override public boolean isCredentialsNonExpired() { return true; }
    @Override public boolean isEnabled() { return true; }

    public Usuario getUsuario() { return usuario; }
}
