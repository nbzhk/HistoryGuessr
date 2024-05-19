package org.softuni.finalproject.model.entity;

import jakarta.persistence.*;
import org.softuni.finalproject.model.enums.UserRoleEnum;

@Table(name = "roles")
@Entity
public class UserRoleEntity extends BaseEntity {

    @Enumerated(EnumType.STRING)
    @Column(name = "user_role")
    private UserRoleEnum userRole;

    public UserRoleEnum getUserRole() {
        return userRole;
    }

    public void setUserRole(UserRoleEnum userRole) {
        this.userRole = userRole;
    }
}
