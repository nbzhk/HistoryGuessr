package org.softuni.finalproject.model.entity;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "users")
public class UserEntity extends BaseEntity {
    @Column(nullable = false)
    private String username;
    @Column(name = "password_hash", nullable = false)
    private String password;
    @Column(nullable = false)
    private String email;
    @Column(name = "registration_date", nullable = false)
    private LocalDate registrationDate;
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "users_roles",
                joinColumns = @JoinColumn(
                        name = "user_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(
                        name = "role_id", referencedColumnName = "id"))
    private List<UserRoleEntity> userRoles = new ArrayList<>();
    @OneToOne
    @JoinTable(name = "admin_dropbox_access_token",
                joinColumns = @JoinColumn(
                        name = "user_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(
                        name = "dropbox_token_id", referencedColumnName = "id"))
    private DropboxCredentialEntity dropboxCredentialEntity;
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ChallengeParticipantEntity> challengeParticipants = new ArrayList<>();



    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public LocalDate getRegistrationDate() {
        return registrationDate;
    }

    public void setRegistrationDate(LocalDate registrationDate) {
        this.registrationDate = registrationDate;
    }

    public List<UserRoleEntity> getUserRoles() {
        return userRoles;
    }

    public void setUserRoles(List<UserRoleEntity> userRoles) {
        this.userRoles = userRoles;
    }

    public DropboxCredentialEntity getDropboxCredential() {
        return dropboxCredentialEntity;
    }

    public void setDropboxCredential(DropboxCredentialEntity dropboxCredentialEntity) {
        this.dropboxCredentialEntity = dropboxCredentialEntity;
    }
}
