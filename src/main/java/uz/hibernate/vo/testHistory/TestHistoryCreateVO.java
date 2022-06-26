package uz.hibernate.vo.testHistory;

import lombok.*;
import uz.hibernate.vo.BaseVO;

import java.sql.Timestamp;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TestHistoryCreateVO implements BaseVO {
    private Integer quizNumber;
    private Timestamp startedAt;
    private Timestamp finishedAt;
    private Integer correctAnswers;
    private String subjectName;
}