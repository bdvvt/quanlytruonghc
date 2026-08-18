package com.example.quanlytruonghoc.security.principal;

import com.example.quanlytruonghoc.models.constants.UserStatus;
import com.example.quanlytruonghoc.models.data.entities.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.jspecify.annotations.Nullable;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CustomUserDetails implements UserDetails {
    private User user;
    private Collection<? extends GrantedAuthority> authorities;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.authorities;
    }

    @Override
    public @Nullable String getPassword() {
        return this.user.getPassword();
    }

    @Override
    public String getUsername() {
        return this.user.getEmail() != null ? this.user.getEmail() : this.user.getUsername();
    }

    @Override
    public boolean isAccountNonLocked() {
        return this.user.getStatus() != UserStatus.BLOCKED;
    }

    @Override
    public boolean isEnabled() {
        return this.user.getStatus() == UserStatus.ACTIVE;
    }
}
