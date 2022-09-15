package no.ssb.dapla.team.groups;

import jakarta.persistence.*;
import lombok.*;
import no.ssb.dapla.team.users.User;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

import java.util.Set;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "AuthGroup")
public class Group {
    @Id
    @NonNull
    @Column(unique = true)
    private String id;

    @OneToMany(cascade = CascadeType.ALL)
    @NotFound(action = NotFoundAction.IGNORE)
    private Set<User> users;

}
