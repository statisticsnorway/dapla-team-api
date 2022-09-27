package no.ssb.dapla.team.groups;

import com.microsoft.graph.requests.GraphServiceClient;
import com.microsoft.graph.requests.UserCollectionPage;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import no.ssb.dapla.team.users.User;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static no.ssb.dapla.team.util.UuidValidator.validateUuid;

@Service
@RequiredArgsConstructor
public class GroupService {

    @NonNull
    private final GraphServiceClient graph;

    /**
     * Find a Dapla group by name.
     *
     * @param groupName e.g. demo-enhjoern-a-support
     * @return Group and its users, or empty Optional if no group was found
     */
    public Optional<Group> findGroupByName(@NonNull String groupName) {
        com.microsoft.graph.models.Group g = graph.groups().buildRequest()
                .filter("startswith(displayName, '%s')".formatted(groupName))
                .select("id, displayName, mail")
                .get().getCurrentPage().stream().findFirst().orElse(null);

        if (g == null) {
            return Optional.empty();
        }

        // Retrieve group members
        List<User> users = getGroupMembers(g.id);

        return Optional.of(Group.builder()
                .id(g.displayName)
                .azureAdId(g.id)
                .users(users)
                .name(g.displayName)
                .build()
        );
    }

    /**
     * Retrieve group members (users) of a given Dapla group (identified by azureAdId)
     *
     * @param azureAdGroupId object id of the Azure AD Group (UUID)
     * @return List of users in the group, or empty List. Never null.
     */
    public List<User> getGroupMembers(@NonNull String azureAdGroupId) {
        validateUuid(azureAdGroupId);
        List<User> users = new ArrayList<>();

        UserCollectionPage pages = graph.groups(azureAdGroupId)
                .membersAsUser().buildRequest().get();

        while (pages != null) {
            users.addAll(pages.getCurrentPage()
                    .stream()
                    .map(u -> User.builder()
                            .emailShort(u.userPrincipalName)
                            .email(u.mail)
                            .name(u.displayName)
                            .build())
                    .toList()
            );

            if (pages.getNextPage() == null) {
                break;
            } else {
                pages = pages.getNextPage().buildRequest().get();
            }
        }

        return users;
    }

}
