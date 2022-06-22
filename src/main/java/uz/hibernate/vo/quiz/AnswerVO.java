package uz.hibernate.vo.quiz;

import uz.hibernate.vo.GenericVO;

import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class AnswerVO extends GenericVO {
    private String variantA;
    private String variantB;
    private String variantC;

    private String correctAnswer;

    public AnswerVO(long id, String variantA, String variantB, String variantC, String correctAnswer) {
        super(id);
        this.variantA = variantA;
        this.variantB = variantB;
        this.variantC = variantC;
        this.correctAnswer = correctAnswer;
    }
}
