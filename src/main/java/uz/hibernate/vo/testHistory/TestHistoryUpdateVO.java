package uz.hibernate.vo.testHistory;

import lombok.*;
import uz.hibernate.vo.GenericVO;

@Data
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class TestHistoryUpdateVO extends GenericVO {
    private Integer correctAnswer;

    @Builder(builderMethodName = "childBuilder")
    public TestHistoryUpdateVO(long id, Long id1, Integer correctAnswer) {
        super(id);
        this.id = id1;
        this.correctAnswer = correctAnswer;
    }
}
