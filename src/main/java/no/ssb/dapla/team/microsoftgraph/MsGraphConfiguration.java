package no.ssb.dapla.team.microsoftgraph;

import com.azure.identity.ClientCertificateCredential;
import com.azure.identity.ClientCertificateCredentialBuilder;
import com.microsoft.graph.authentication.TokenCredentialAuthProvider;
import com.microsoft.graph.requests.GraphServiceClient;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class MsGraphConfiguration {

    private static final List<String> SCOPES = List.of("https://graph.microsoft.com/.default");

    @Bean
    public GraphServiceClient createGraphClient(@NonNull @Value("${ad.app.client.id}") String clientId,
                                   @NonNull @Value("${ad.app.tenant.id}") String tenantId,
                                   @NonNull @Value("${ad.app.certificate.file}") String certPath,
                                   @NonNull @Value("#{systemEnvironment['DAPLA_TEAM_API_AD_APP_CERTIFICATE_PASSWORD'] ?: '${ad.app.certificate.password}'}") String certPassword) {
        final ClientCertificateCredential credential = new ClientCertificateCredentialBuilder()
                .tenantId(tenantId)
                .clientId(clientId)
                .pfxCertificate(certPath, certPassword)
                .build();
        final TokenCredentialAuthProvider authProvider = new TokenCredentialAuthProvider(SCOPES, credential);

        return GraphServiceClient
                .builder()
                .authenticationProvider(authProvider)
                .buildClient();
   }
}
