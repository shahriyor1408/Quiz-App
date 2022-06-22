package uz.hibernate.vo.quiz;

import lombok.*;
import uz.hibernate.enums.QuestionType;
import uz.hibernate.vo.GenericVO;

import java.sql.Timestamp;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class QuestionVO extends GenericVO {
    private String text;
    private QuestionType type;
    private Timestamp createdAt;

    @Builder(builderMethodName = "childBuilder")
    public QuestionVO(long id, String text, QuestionType type, Timestamp createdAt) {
        super(id);
        this.text = text;
        this.type = type;
        this.createdAt = createdAt;
    }
}
