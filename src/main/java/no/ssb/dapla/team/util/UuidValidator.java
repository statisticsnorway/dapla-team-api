package no.ssb.dapla.team.util;

import lombok.experimental.UtilityClass;

import java.util.UUID;

@UtilityClass
public class UuidValidator {

    public static void validateUuid(String id) {
        try {
            UUID.fromString(id);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Assumed arg to be an UUID, but was " + id);
        }
    }
}
