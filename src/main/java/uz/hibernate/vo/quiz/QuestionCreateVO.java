package uz.hibernate.vo.quiz;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import uz.hibernate.enums.QuestionType;
import uz.hibernate.vo.BaseVO;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor

public class QuestionCreateVO implements BaseVO {
    private String text;
    private QuestionType type;
}
