package uz.itransition.collectin.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import uz.itransition.collectin.entity.enums.AuthProvider;
import uz.itransition.collectin.entity.enums.Language;
import uz.itransition.collectin.entity.enums.Role;
import uz.itransition.collectin.entity.enums.Status;

import javax.persistence.*;


@Getter
@Setter
@Entity
@NoArgsConstructor
@Table(name = "users")
public class User extends BaseEntity {

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(columnDefinition = "integer default 1")
    private int roles = Role.USER.getFlag();     // default role is USER

    @Enumerated(EnumType.STRING)
    @Column(columnDefinition = "varchar(255) default 'ENG'")
    private Language language = Language.ENG;   //default language is ENGLISH

    @Enumerated(EnumType.STRING)
    @Column(columnDefinition = "varchar(255) default 'ACTIVE'")
    private Status status = Status.ACTIVE;      // default status is ACTIVE

    @Enumerated(EnumType.STRING)
    @Column(columnDefinition = "varchar(255) default 'local'")
    private AuthProvider provider=AuthProvider.local;

    private String providerId;

    private String imageUrl;

    public User(String name, String email, String password) {
        this.name=name;
        this.email = email;
        this.password = password;
    }

    public User(Long id) {
        super(id);
    }
}
