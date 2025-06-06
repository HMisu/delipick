package com.delipick.user.domain.model;

import com.delipick.user.common.model.BaseEntity;
import com.delipick.user.domain.enums.UserRoleEnum;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Table(name = "p_users")
@AllArgsConstructor
@NoArgsConstructor
@Builder(access = AccessLevel.PRIVATE)
public class User extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(name = "phone_number", nullable = false)
    private String phone;

    @Column(name = "birth_date")
    private String birthdate;

    @Column
    private String address;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private UserRoleEnum role = UserRoleEnum.ROLE_USER;

    @PrePersist
    public void prePersist() {
        if (getCreatedBy() == null) {
            markAsCreated(String.valueOf(this.id));
        }
    }

    public static User create(String email,
                              String password,
                              String phone,
                              String birthdate,
                              String address) {
        return User.builder()
                .email(email)
                .password(password)
                .phone(phone)
                .birthdate(birthdate)
                .address(address)
                .build();
    }

    public void update(String email,
                       String phone,
                       String birthdate,
                       String address) {
        this.email = email;
        this.phone = phone;
        this.birthdate = birthdate;
        this.address = address;
        markAsUpdated(String.valueOf(this.id));
    }

    public void updateRole(UserRoleEnum role) {
        this.role = role;
    }
}
