package uz.hibernate.vo.subject;


import lombok.*;
import uz.hibernate.vo.GenericVO;

@Data
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class SubjectVO extends GenericVO {
    private String name;
    @Builder(builderMethodName = "childBuilder")
    public SubjectVO(long id, String name) {
        super(id);
        this.name = name;
    }
}
