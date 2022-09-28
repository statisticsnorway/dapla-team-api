package no.ssb.dapla.team.util;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;
import lombok.experimental.UtilityClass;

import java.util.List;

@UtilityClass
public class GroupNameUtil {

    @AllArgsConstructor
    private enum GroupType {
        MANAGERS("managers"),
        DATA_ADMINS("data-admins"),
        DEVELOPERS("developers"),
        CONSUMERS("consumers"),
        SUPPORT("support");

        @Getter
        private final String value;

        @Override
        public String toString() {
            return value;
        }
    }

    private static final List<String> DEFAULT_GROUP_TYPES = List.of(
            GroupType.MANAGERS.value,
            GroupType.DATA_ADMINS.value,
            GroupType.DEVELOPERS.value,
            GroupType.CONSUMERS.value,
            GroupType.SUPPORT.value
    );

    public static String deduceTeamNameFromGroup(@NonNull String groupName) {
        for (String g : DEFAULT_GROUP_TYPES) {
            int pos = groupName.indexOf("-" + g);
            if (pos > -1) {
                groupName = groupName.substring(0, pos);
            }
        }

        if (groupName.trim().length() == 0) {
            throw new IllegalArgumentException("Unable to deduce teamName from empty groupName");
        }
        return groupName;
    }

    public static List<String> defaultGroupNamesForTeam(String teamName) {
        return DEFAULT_GROUP_TYPES.stream().map(g -> teamName + "-" + g).toList();
    }

    private static String groupNameOf(@NonNull String teamName, GroupType groupType) {
        return teamName + "-" + groupType;
    }

    public static String managersGroupNameOf(String teamName) {
        return groupNameOf(teamName, GroupType.MANAGERS);
    }

    public static String dataAdminsGroupNameOf(String teamName) {
        return groupNameOf(teamName, GroupType.DATA_ADMINS);
    }

    public static String developersGroupNameOf(String teamName) {
        return groupNameOf(teamName, GroupType.DEVELOPERS);
    }

    public static String consumersGroupNameOf(String teamName) {
        return groupNameOf(teamName, GroupType.CONSUMERS);
    }

    public static String supportGroupNameOf(String teamName) {
        return groupNameOf(teamName, GroupType.SUPPORT);
    }

}
