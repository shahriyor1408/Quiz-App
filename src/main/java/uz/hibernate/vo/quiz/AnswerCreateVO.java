package uz.hibernate.vo.quiz;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import uz.hibernate.vo.BaseVO;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AnswerCreateVO implements BaseVO {
    private String variantA;
    private String variantB;
    private String variantC;
    private String correctAnswer;
}
