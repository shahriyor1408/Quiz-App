package uz.hibernate.vo.testHistory;

import lombok.*;
import uz.hibernate.vo.GenericVO;

import java.sql.Timestamp;
import java.time.LocalDateTime;

@Data
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class TestHistoryVO extends GenericVO {
    private Integer quizNumber;
    private Timestamp startedAt;
    private Timestamp finishedAt;
    private Integer correctAnswers;
    private Long userId;
    private String subjectName;

    @Builder(builderMethodName = "childBuilder")
    public TestHistoryVO(long id, Integer quizNumber, Timestamp startedAt, Timestamp finishedAt, Integer correctAnswers, String subjectName, Long userId) {
        super(id);
        this.quizNumber = quizNumber;
        this.startedAt = startedAt;
        this.finishedAt = finishedAt;
        this.correctAnswers = correctAnswers;
        this.subjectName = subjectName;
        this.userId = userId;
    }
}
