package uz.hibernate.domains;

import jakarta.persistence.*;
import lombok.*;

import java.sql.Timestamp;

@Entity
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class TestHistory extends Auditable {
    @Column(nullable = false)
    private Integer quiz_count;

    @Column(nullable = false)
    private Timestamp startedAt;

    @Column(nullable = false)
    private Timestamp finishedAt;
    @Column(nullable = false)
    private Integer correct_quiz_count;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "subject_id")
    private Subject subject;

    @Builder(builderMethodName = "childBuilder")
    public TestHistory(Long id, Timestamp createdAt, Long createdBy, Timestamp updatedAt, Long updatedBy, boolean deleted, Integer quiz_count, Integer correct_quiz_count) {
        super(id, createdAt, createdBy, updatedAt, updatedBy, deleted);
        this.quiz_count = quiz_count;
        this.correct_quiz_count = correct_quiz_count;
    }
}
