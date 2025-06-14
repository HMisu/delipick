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

    @Column(nullable = false)
    private String name;

    @Column(name = "phone_number", nullable = false)
    private String phone;

    @Column(name = "birth_date")
    private String birthdate;

    @Column
    private String address;

    @Builder.Default
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private UserRoleEnum role = UserRoleEnum.ROLE_USER;

    public static User create(String email,
                              String password,
                              String phone,
                              String name,
                              String birthdate,
                              String address) {
        User user = User.builder()
                .email(email)
                .password(password)
                .phone(phone)
                .name(name)
                .birthdate(birthdate)
                .address(address)
                .build();

        user.markAsCreated(email);
        return user;
    }

    public void update(String phone,
                       String name,
                       String birthdate,
                       String address) {
        this.phone = phone;
        this.name = name;
        this.birthdate = birthdate;
        this.address = address;
        markAsUpdated(this.email);
    }

    public void updatePassword(String password) {
        this.password = password;
        markAsUpdated(this.email);
    }


    public void updateRole(UserRoleEnum role) {
        this.role = role;
    }
}
