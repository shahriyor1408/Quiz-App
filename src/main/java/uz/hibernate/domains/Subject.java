package uz.hibernate.domains;

import jakarta.persistence.*;
import lombok.*;
import uz.hibernate.domains.auth.AuthUser;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@ToString
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Subject extends Auditable {
    @Column(nullable = false, unique = true)
    private String name;

    @OneToMany(mappedBy = "subject",fetch = FetchType.EAGER)
    private List<Question> questionList = new ArrayList<>();

    @OneToMany(cascade = CascadeType.ALL,fetch = FetchType.EAGER)
    @JoinColumn(name = "subject_id")
    @OrderColumn(name = "type")
    private List<TestHistory> testHistoryList = new ArrayList<>();

    @OneToOne(cascade = CascadeType.ALL,fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    @OrderColumn(name = "type")
    private AuthUser authUser;

    @Builder(builderMethodName = "childBuilder")
    public Subject(Long id, Timestamp createdAt, Long createdBy, Timestamp updatedAt, Long updatedBy, boolean deleted, String name) {
        super(id, createdAt, createdBy, updatedAt, updatedBy, deleted);
        this.name = name;
    }
}
