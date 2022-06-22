package uz.hibernate.domains;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import lombok.*;

import java.sql.Timestamp;
import java.util.Objects;

@Entity
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class Answer extends Auditable {
    @Column(nullable = false, unique = true)
    private String text;

    @Column(nullable = false, columnDefinition = "default false")
    private Boolean is_correct;

    @Builder(builderMethodName = "childBuilder")
    public Answer(Long id, Timestamp createdAt, Long createdBy, Timestamp updatedAt, Long updatedBy, boolean deleted, String text, Boolean is_correct) {
        super(id, createdAt, createdBy, updatedAt, updatedBy, deleted);
        this.text = text;
        if (Objects.isNull(is_correct)) {
            this.is_correct = false;
        }
    }
}
