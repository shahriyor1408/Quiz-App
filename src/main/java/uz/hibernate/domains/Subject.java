package uz.hibernate.domains;

import jakarta.persistence.*;
import lombok.*;
import uz.hibernate.domains.auth.AuthUser;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class Subject extends Auditable {
    @Column(nullable = false, unique = true)
    private String name;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "subject_id")
    @OrderColumn(name = "type")
    private List<Question> questionList = new ArrayList<>();

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "subject_id")
    @OrderColumn(name = "type")
    private List<TestHistory> testHistoryList = new ArrayList<>();

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id")
    @OrderColumn(name = "type")
    private AuthUser authUser = new AuthUser();

    @Builder(builderMethodName = "childBuilder")
    public Subject(Long id, Timestamp createdAt, Long createdBy, Timestamp updatedAt, Long updatedBy, boolean deleted, String name) {
        super(id, createdAt, createdBy, updatedAt, updatedBy, deleted);
        this.name = name;
    }
}
