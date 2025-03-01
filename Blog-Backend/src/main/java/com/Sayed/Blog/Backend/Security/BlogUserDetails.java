package com.Sayed.Blog.Backend.Security;

import com.Sayed.Blog.Backend.Entity.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@Getter
@AllArgsConstructor
public class BlogUserDetails implements UserDetails
{

    private final User user;

    public BlogUserDetails(User user) {
        this.user = user;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_USER"));
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getEmail();
    }

    @Override
    public boolean isAccountNonExpired() {       //Not worrying about this abhi
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {       //Not worrying about this abhi
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {       //Not worrying about this abhi
        return true;
    }

    @Override
    public boolean isEnabled() {       //Not worrying about this abhi
        return true;
    }
}
