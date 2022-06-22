package uz.hibernate.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum AuthRole {
    ADMIN(1000),
    TEACHER(100),
    USER(10);

    private final int priority;
}
