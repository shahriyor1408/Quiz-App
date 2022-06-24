package uz.hibernate.domains;

import jakarta.persistence.*;
import lombok.*;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class Answer extends Auditable {
    @Column(nullable = false, unique = true)
    private String variantA;
    @Column(nullable = false, unique = true)
    private String variantB;
    @Column(nullable = false, unique = true)
    private String variantC;

    @ManyToOne(fetch = FetchType.EAGER)
    private Question question;
    private String correctAnswer;

    @Builder(builderMethodName = "childBuilder")
    public Answer(Long id, Timestamp createdAt, Long createdBy, Timestamp updatedAt, Long updatedBy, boolean deleted, String variantA, String variantB, String variantC, String correctAnswer, Question question) {
        super(id, createdAt, createdBy, updatedAt, updatedBy, deleted);
        this.variantA = variantA;
        this.variantB = variantB;
        this.variantC = variantC;
        this.correctAnswer = correctAnswer;
        this.question = question;
    }
}