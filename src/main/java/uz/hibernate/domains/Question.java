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

    @OneToMany(cascade = CascadeType.ALL,fetch = FetchType.EAGER)
    @JoinColumn(name = "question_id")
    @OrderColumn(name = "type")
    List<Answer> answerList = new ArrayList<>();

    @ManyToOne(cascade = CascadeType.ALL,fetch = FetchType.EAGER)
    @JoinColumn(name = "subject_id")
    private Subject subject;

    private final Integer answer_count = 3;

    @Builder(builderMethodName = "childBuilder")
    public Question(Long id, Timestamp createdAt, Long createdBy, Timestamp updatedAt, Long updatedBy, boolean deleted, String text, QuestionType type, Subject subject) {
        super(id, createdAt, createdBy, updatedAt, updatedBy, deleted);
        this.text = text;
        this.type = type;
        this.subject = subject;
    }
}