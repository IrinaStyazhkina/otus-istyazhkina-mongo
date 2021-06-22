package ru.otus.istyazhkina.library.domain.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import ru.otus.istyazhkina.library.security.roles.Role;

import java.util.Set;

@Document(collection = "user")
@AllArgsConstructor
@Getter
@Setter
@Builder
public class User {

    @Id
    private String id;

    @Field(name = "login")
    private String login;

    @Field(name = "password")
    private String password;

    @Field(name = "roles")
    private Set<Role> roles;

}
