package no.ssb.dapla.team.groups;

import jakarta.persistence.*;
import lombok.*;
import no.ssb.dapla.team.users.User;

import java.util.List;

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

    @ManyToMany(cascade = CascadeType.MERGE)
    private List<User> users;

}
