package no.ssb.dapla.team.groups;

import lombok.*;
import no.ssb.dapla.team.users.User;

import javax.persistence.*;
import java.util.List;

@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "AuthGroup")
public class Group {
    @Id
    @NonNull
    @Column(unique = true)
    private String id;

    /** Azure AD object ID */
    private String azureAdId;

    /** Group name */
    private String name;

    /** List of users in group */
    @ManyToMany(cascade = CascadeType.MERGE)
    private List<User> users;

}
