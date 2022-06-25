package uz.hibernate.vo.testHistory;

import lombok.*;
import uz.hibernate.vo.GenericVO;

import java.time.LocalDateTime;

@Data
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class TestHistoryVO extends GenericVO {
    private Integer quizNumber;
    private LocalDateTime startedAt;
    private LocalDateTime finishedAt;
    private Integer correctAnswers;
    private String subjectName;

    @Builder(builderMethodName = "childBuilder")
    public TestHistoryVO(long id, Integer quizNumber, LocalDateTime startedAt, LocalDateTime finishedAt, Integer correctAnswers, String subjectName) {
        super(id);
        this.quizNumber = quizNumber;
        this.startedAt = startedAt;
        this.finishedAt = finishedAt;
        this.correctAnswers = correctAnswers;
        this.subjectName = subjectName;
    }
}
