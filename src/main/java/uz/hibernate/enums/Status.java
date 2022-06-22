package uz.hibernate.enums;

import jakarta.persistence.AttributeConverter;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Objects;

@Getter
@AllArgsConstructor
public enum Status {
    BLOCKED(-100),
    ACTIVE(0);

    public final int priority;

    public static class StatusConvertor implements AttributeConverter<Status, String> {
        @Override
        public String convertToDatabaseColumn(Status attribute) {
            if (Objects.isNull(attribute))
                return null;
            return switch (attribute) {
                case ACTIVE -> "Active User";
                case BLOCKED -> "User block for some reason";
            };
        }

        @Override
        public Status convertToEntityAttribute(String dbData) {
            if (Objects.isNull(dbData))
                return null;
                return switch (dbData) {
                case "Active User" -> ACTIVE;
                case "User block for some reason" -> BLOCKED;
                default -> BLOCKED;
            };
        }
    }

}
