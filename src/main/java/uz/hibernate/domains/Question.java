package uz.hibernate.domains;

import jakarta.persistence.*;
import lombok.*;
import uz.hibernate.enums.QuestionType;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class Question extends Auditable {
    @Column(nullable = false, unique = true)
    private String text;

    @Enumerated(EnumType.STRING)
    private QuestionType type;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "subject_id")
    @OrderColumn(name = "type")
    List<Answer> answerList = new ArrayList<>();

    private final Integer answer_count = 4;

    @Builder(builderMethodName = "childBuilder")
    public Question(Long id, Timestamp createdAt, Long createdBy, Timestamp updatedAt, Long updatedBy, boolean deleted, String text, QuestionType type) {
        super(id, createdAt, createdBy, updatedAt, updatedBy, deleted);
        this.text = text;
        this.type = type;
    }
}