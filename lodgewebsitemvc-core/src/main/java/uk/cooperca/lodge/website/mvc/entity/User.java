package uk.cooperca.lodge.website.mvc.entity;

import org.hibernate.annotations.Type;
import org.joda.time.DateTime;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Collection;
import java.util.List;

/**
 * A registered user on the platform.
 *
 * @author Charlie Cooper
 */
@Entity
@Table(name = "users")
public class User implements Serializable, UserDetails {

    public static enum Language {
        // our supported locales
        EN, RU
    }

    private static final long serialVersionUID = -880044838119981686L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "users_seq")
    @SequenceGenerator(name = "users_seq", sequenceName = "users_id_seq", allocationSize = 1)
    @Column(name = "id", nullable = false, unique = true)
    private int id;

    @Column(name = "email", nullable = false)
    private String email;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "first_name", nullable = false)
    private String firstName;

    @Column(name = "last_name", nullable = false)
    private String lastName;

    @ManyToOne
    @JoinColumn(name = "role_id", nullable = false)
    private Role role;

    @Enumerated(EnumType.STRING)
    @Column(name = "language_preference", nullable = false)
    private Language language;

    @Column(name = "verified", nullable = false)
    private boolean verified;

    @Type(type="org.jadira.usertype.dateandtime.joda.PersistentDateTime")
    @Column(name = "created_at", nullable = false)
    private DateTime createdAt;

    @Type(type="org.jadira.usertype.dateandtime.joda.PersistentDateTime")
    @Column(name = "verification_request_at", nullable = false)
    private DateTime verificationRequestAt;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "user")
    private List<Review> reviews;

    public User() {
        // for Hibernate
    }

    public User(String email, String password, String firstName, String lastName, Role role, Language language,
                boolean verified, DateTime createdAt) {
        this.email = email;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
        this.role = role;
        this.language = language;
        this.createdAt = createdAt;
        this.verificationRequestAt = createdAt;
        this.verified = verified;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public DateTime getCreatedAt() {
        return createdAt;
    }

    public DateTime getVerificationRequestAt() {
        return verificationRequestAt;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public Language getLanguage() {
        return language;
    }

    public void setLanguage(Language language) {
        this.language = language;
    }

    public boolean isVerified() {
        return verified;
    }

    public void setVerified(boolean verified) {
        this.verified = verified;
    }

    public List<Review> getReviews() {
        return reviews;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return AuthorityUtils.createAuthorityList(role.getRoleName().name());
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    // TODO: commons for equals/hashcode
    @Override
    public boolean equals(Object obj) {
        if (obj instanceof User) {
            User user = (User) obj;
            return user.getId() == id;
        }
        return false;
    }

    @Override
    public int hashCode() {
        return Integer.hashCode(id);
    }
}
