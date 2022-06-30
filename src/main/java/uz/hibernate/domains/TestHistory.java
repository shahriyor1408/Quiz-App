package uz.hibernate.domains;

import jakarta.persistence.*;
import lombok.*;
import uz.hibernate.domains.auth.AuthUser;

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

    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "subject_id")
    private Subject subject;

    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.MERGE)
    @JoinColumn(name = "user_id")
    private AuthUser authUser;

    @Builder(builderMethodName = "childBuilder")
    public TestHistory(Long id, Timestamp createdAt, Long createdBy, Timestamp updatedAt, Long updatedBy, boolean deleted, Integer quiz_count, Timestamp startedAt, Timestamp finishedAt, Integer correct_quiz_count, Subject subject, AuthUser authUser) {
        super(id, createdAt, createdBy, updatedAt, updatedBy, deleted);
        this.quiz_count = quiz_count;
        this.startedAt = startedAt;
        this.finishedAt = finishedAt;
        this.correct_quiz_count = correct_quiz_count;
        this.subject = subject;
        this.authUser = authUser;
    }
}
