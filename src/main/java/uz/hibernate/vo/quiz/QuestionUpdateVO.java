package uz.hibernate.vo.quiz;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import uz.hibernate.enums.QuestionType;
import uz.hibernate.vo.GenericVO;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class QuestionUpdateVO extends GenericVO {
    private String text;
    private String currentText;
    private QuestionType type;

    @Builder(builderMethodName = "childBuilder")
    public QuestionUpdateVO(long id, String text, String currentText, QuestionType type) {
        super(id);
        this.text = text;
        this.currentText = currentText;
        this.type = type;
    }
}