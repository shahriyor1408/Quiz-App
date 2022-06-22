package uz.hibernate.vo.quiz;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import uz.hibernate.enums.QuestionType;
import uz.hibernate.vo.GenericVO;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class QuestionUpdateVO extends GenericVO {
    private String text;
    private QuestionType type;
}