package no.ssb.dapla.team.groups;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;
@Data
@Entity
public class Group {
    @Id
    private String id;
}
