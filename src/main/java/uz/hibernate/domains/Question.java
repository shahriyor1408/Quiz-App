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
public class Question extends Auditable {
    @Column(nullable = false, unique = true)
    private String text;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "question_id")
    @OrderColumn(name = "type")
    List<Answer> answerList = new ArrayList<>();

    private Integer answer_count;

    @Builder(builderMethodName = "childBuilder")
    public Question(Long id, Timestamp createdAt, Long createdBy, Timestamp updatedAt, Long updatedBy, boolean deleted, String text) {
        super(id, createdAt, createdBy, updatedAt, updatedBy, deleted);
        this.text = text;
        this.answer_count = 4;
    }
}
