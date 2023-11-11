package com.kotyk.realtorconnect.entity.user;

import com.kotyk.realtorconnect.entity.realestate.RealEstate;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.Instant;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Entity
@Table(name = "users")
@Inheritance(strategy = InheritanceType.JOINED)
@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String email;
    private String username;
    private String password;
    private String avatar;
    private String phone;
    @Column(name = "role_id")
    private Role role;
    @Column(name = "is_blocked")
    private boolean blocked = false;
    private Instant lastLogin;
    private Instant created = Instant.now();
    @ManyToMany
    @JoinTable(name = "favorite_real_estates",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "real_estate_id"))
    private Set<RealEstate> favoriteRealEstates = new HashSet<>();

    public User(String username, Role role) {
        this.username = username;
        this.role = role;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return role.getPermissions().stream()
                .map(p -> new SimpleGrantedAuthority(p.name()))
                .collect(Collectors.toSet());
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return !blocked;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
