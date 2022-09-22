package no.ssb.dapla.team.github;

import lombok.NonNull;
import no.ssb.dapla.team.teams.Team;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class GitHubServiceTest {
    @Autowired
    private GitHubService gitHubService;

    @Test
    void tokenNotBlank() throws Exception {
        assertThat(gitHubService.getGhAppInstallationToken().getToken())
                .isNotBlank();
    }

    @Test
    void getTeamListWithTopicUsingGitHubApi() throws Exception {
        assertThat(gitHubService.getTeamListWithTopic("terraform"))
                .isNotEmpty();
    }

    @Test
    void deserializeTeamAndAddGroups() throws Exception {
        GitHubServiceDummy gitHubServiceDummy = new GitHubServiceDummy("", "", "");

        List<Team> teams = gitHubServiceDummy.getTeamListWithTopic("");

        assertThat(teams.size() == 2).isTrue();

        assertThat(teams.get(0).getUniformTeamName()).isEqualTo("kostra-test");
        assertThat(teams.get(1).getUniformTeamName()).isEqualTo("kostra-test2");

        assertThat(teams.get(0).getRepo()).isEqualTo("statisticsnorway/kostra-test-iac");
        assertThat(teams.get(1).getRepo()).isEqualTo("statisticsnorway/kostra-test2-iac");

        assertThat(teams.get(0).getGroups().get(0).getId()).isEqualTo("kostra-test-support");
        assertThat(teams.get(1).getGroups().get(0).getId()).isEqualTo("kostra-test2-support");

    }

    static class GitHubServiceDummy extends GitHubService {
        public GitHubServiceDummy(@NonNull String appId, @NonNull String privateKeyPath, @NonNull String organizationName) {
            super(appId, privateKeyPath, organizationName);
        }

        @Override
        protected void updateTokenIfExpired() {
        }

        @Override
        public String getRepositoryInOrganizationWithTopicAsJsonString(String topic) throws Exception {
            return teamsAsJson;
        }

        private final String teamsAsJson = """
                {
                    "total_count": 2,
                    "incomplete_results": false,
                    "items": [
                        {
                            "id": 111,
                            "node_id": "R_kgDOHbOBcQ",
                            "name": "kostra-test-iac",
                            "full_name": "statisticsnorway/kostra-test-iac",
                            "private": true,
                            "owner": {
                                "login": "statisticsnorway",
                                "id": 13213,
                                "node_id": "dasdsadasda=",
                                "avatar_url": "https://avatars.githubusercontent.com/u/31231232?v=4",
                                "gravatar_id": "",
                                "url": "https://api.github.com/users/statisticsnorway",
                                "html_url": "https://github.com/statisticsnorway",
                                "followers_url": "https://api.github.com/users/statisticsnorway/followers",
                                "following_url": "https://api.github.com/users/statisticsnorway/following{/other_user}",
                                "gists_url": "https://api.github.com/users/statisticsnorway/gists{/gist_id}",
                                "starred_url": "https://api.github.com/users/statisticsnorway/starred{/owner}{/repo}",
                                "subscriptions_url": "https://api.github.com/users/statisticsnorway/subscriptions",
                                "organizations_url": "https://api.github.com/users/statisticsnorway/orgs",
                                "repos_url": "https://api.github.com/users/statisticsnorway/repos",
                                "events_url": "https://api.github.com/users/statisticsnorway/events{/privacy}",
                                "received_events_url": "https://api.github.com/users/statisticsnorway/received_events",
                                "type": "Organization",
                                "site_admin": false
                            }
                        },
                        {
                            "id": 222,
                            "node_id": "rrr",
                            "name": "kostra-test2-iac",
                            "full_name": "statisticsnorway/kostra-test2-iac",
                            "private": true,
                            "owner": {
                                "login": "statisticsnorway",
                                "id": 312,
                                "node_id": "dsasadsadasd=",
                                "avatar_url": "https://avatars.githubusercontent.com/u/321312?v=4",
                                "gravatar_id": "",
                                "url": "https://api.github.com/users/statisticsnorway",
                                "html_url": "https://github.com/statisticsnorway",
                                "followers_url": "https://api.github.com/users/statisticsnorway/followers",
                                "following_url": "https://api.github.com/users/statisticsnorway/following{/other_user}",
                                "gists_url": "https://api.github.com/users/statisticsnorway/gists{/gist_id}",
                                "starred_url": "https://api.github.com/users/statisticsnorway/starred{/owner}{/repo}",
                                "subscriptions_url": "https://api.github.com/users/statisticsnorway/subscriptions",
                                "organizations_url": "https://api.github.com/users/statisticsnorway/orgs",
                                "repos_url": "https://api.github.com/users/statisticsnorway/repos",
                                "events_url": "https://api.github.com/users/statisticsnorway/events{/privacy}",
                                "received_events_url": "https://api.github.com/users/statisticsnorway/received_events",
                                "type": "Organization",
                                "site_admin": false
                            }
                        }
                    ]
                }""";
    }
}

