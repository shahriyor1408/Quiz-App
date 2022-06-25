package uz.hibernate.domains;

import jakarta.persistence.*;
import lombok.*;
import uz.hibernate.enums.QuestionType;

import java.sql.Timestamp;

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

    @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "answer_id")
    Answer answer = new Answer();

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "subject_id")
    private Subject subject;
    private final Integer answer_count = 3;

    @Builder(builderMethodName = "childBuilder")
    public Question(Long id, Timestamp createdAt, Long createdBy, Timestamp updatedAt, Long updatedBy, boolean deleted, String text, QuestionType type, Subject subject, Answer answer) {
        super(id, createdAt, createdBy, updatedAt, updatedBy, deleted);
        this.text = text;
        this.type = type;
        this.subject = subject;
        this.answer = answer;
    }
}