package uz.hibernate.vo.quiz;

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
}
