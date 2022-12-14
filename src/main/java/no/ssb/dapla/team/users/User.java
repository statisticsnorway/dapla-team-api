package no.ssb.dapla.team.users;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "AdUser")
public class User {
    @Id
    @NonNull
    @Column(unique = true)
    /** AD User principle */
    private String emailShort;
    /** Full name */
    private String name;
    private String email;

}
