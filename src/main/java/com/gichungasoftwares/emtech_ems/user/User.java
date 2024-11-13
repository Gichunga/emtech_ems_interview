package com.gichungasoftwares.emtech_ems.user;

import com.gichungasoftwares.emtech_ems.role.Role;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.security.Principal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "_user")
@EntityListeners(AuditingEntityListener.class)
public class User implements UserDetails, Principal {

    @Id
    @GeneratedValue(strategy=GenerationType.SEQUENCE)
    private Integer user_Id;

    private String username;
    @Column(unique = true)
    private String email;
    private String firstname;
    private String lastname;
    private LocalDate dateOfBirth;
    private String phoneNumber;
    private String password;
    private String city;
    private String address;
    private String country;
    private String position;
    private String department;
    private String status;
    private Date joinDate;
    private Date lastLoginDate;
    private boolean isAccountLocked;
    private boolean isEnabled;

    // one user can have multiple roles and vice versa
    @ManyToMany(fetch = FetchType.EAGER) // fetch user roles when we fetch the user
    private List<Role> roles;

    // include jpa auditing information
    @CreatedDate
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdDate;

    @LastModifiedDate
    @Column(insertable = false)
    private LocalDateTime lastModifiedDate;

    @Override
    public String getName() {
        return email;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // convert the list of roles to stream
        return this.roles
                .stream()
                .map(r -> new SimpleGrantedAuthority(r.getName()))
                .collect(Collectors.toList());
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
//        return UserDetails.super.isAccountNonExpired();
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
//        return UserDetails.super.isAccountNonLocked();
        return !isAccountLocked;
    }

    @Override
    public boolean isCredentialsNonExpired() {
//        return UserDetails.super.isCredentialsNonExpired();
        return true;
    }

    @Override
    public boolean isEnabled() {
//        return UserDetails.super.isEnabled();
        return isEnabled;
    }

    public String fullName () {
        return firstname + " " + lastname;
    }

}
