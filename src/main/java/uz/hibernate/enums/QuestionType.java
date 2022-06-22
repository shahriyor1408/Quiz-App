package uz.hibernate.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum QuestionType {
    EASY(10),
    MEDIUM(100),
    HARD(1000);
    private final int priority;
}
