package uz.hibernate.vo.quiz;

import lombok.Builder;
import uz.hibernate.vo.GenericVO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AnswerUpdateVO extends GenericVO {
    private String variantA;
    private String variantB;
    private String variantC;

    private String correctAnswer;

    @Builder(builderMethodName = "childBuilder")
    public AnswerUpdateVO(long id, String variantA, String variantB, String variantC, String correctAnswer) {
        super(id);
        this.variantA = variantA;
        this.variantB = variantB;
        this.variantC = variantC;
        this.correctAnswer = correctAnswer;
    }
}
