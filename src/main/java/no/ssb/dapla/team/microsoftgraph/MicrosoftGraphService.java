package no.ssb.dapla.team.microsoftgraph;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.microsoft.aad.msal4j.ClientCredentialFactory;
import com.microsoft.aad.msal4j.ClientCredentialParameters;
import com.microsoft.aad.msal4j.ConfidentialClientApplication;
import com.microsoft.aad.msal4j.IAuthenticationResult;
import com.nimbusds.oauth2.sdk.http.HTTPResponse;
import com.univocity.parsers.annotations.Parsed;
import lombok.Data;
import lombok.NonNull;
import no.ssb.dapla.team.teams.Team;
import no.ssb.dapla.team.users.User;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@Service
public class MicrosoftGraphService {

    private final String scopeUrl;
    private ConfidentialClientApplication confidentialClientApplication;

    public MicrosoftGraphService(@NonNull @Value("${ad.app.client.id}") String clientId,
                                 @NonNull @Value("${ad.app.tenant.id}") String tenantId,
                                 @NonNull @Value("${ad.app.privatekey.file}") String keyPath,
                                 @NonNull @Value("${ad.app.certificate.file}") String certPath) {

        this.scopeUrl = "https://graph.microsoft.com/.default";
        String authorityUrl = "https://login.microsoftonline.com/" + tenantId +"/";

        try {
            PKCS8EncodedKeySpec spec = new PKCS8EncodedKeySpec(Files.readAllBytes(Paths.get(keyPath)));
            PrivateKey key = KeyFactory.getInstance("RSA").generatePrivate(spec);

            InputStream certStream = new ByteArrayInputStream(Files.readAllBytes(Paths.get(certPath)));
            X509Certificate cert = (X509Certificate) CertificateFactory.getInstance("X.509").generateCertificate(certStream);
            InputStream keyPathInputStream = new FileInputStream(keyPath);

            confidentialClientApplication = ConfidentialClientApplication.builder(
                            clientId,
                            ClientCredentialFactory.createFromCertificate(keyPathInputStream, certPath))
                    .authority(authorityUrl)
                    .build();

        } catch (Exception e) {
            new RuntimeException("Could not create instance of MicrosoftGraphService");
        }

    }

    private IAuthenticationResult getAccessTokenByClientCredentialGrant() throws Exception {
        // With client credentials flows the scope is ALWAYS of the shape "resource/.default", as the
        // application permissions need to be set statically (in the portal), and then granted by a tenant administrator
        ClientCredentialParameters clientCredentialParam = ClientCredentialParameters.builder(
                        Collections.singleton(scopeUrl))
                .build();

        CompletableFuture<IAuthenticationResult> future = confidentialClientApplication.acquireToken(clientCredentialParam);
        return future.get();
    }

    private String getJsonTeamListFromGraph() throws Exception {

        String accessToken = getAccessTokenByClientCredentialGrant().accessToken();

        URL url = new URL("https://graph.microsoft.com/v1.0/groups");
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();

        conn.setRequestMethod("GET");
        conn.setRequestProperty("Authorization", "Bearer " + accessToken);
        conn.setRequestProperty("Accept","application/json");

        int httpResponseCode = conn.getResponseCode();
        if(httpResponseCode == HTTPResponse.SC_OK) {

            StringBuilder response;
            try(BufferedReader in = new BufferedReader(
                    new InputStreamReader(conn.getInputStream()))){

                String inputLine;
                response = new StringBuilder();
                while (( inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
            }
            return response.toString();
        } else {
            return String.format("Connection returned HTTP code: %s with message: %s",
                    httpResponseCode, conn.getResponseMessage());
        }

    }

    public AdGroup getAdGroupFromGraph(String jsonString) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(DeserializationFeature.ACCEPT_EMPTY_ARRAY_AS_NULL_OBJECT, false);
        AdGroup adGroup = objectMapper.readValue(jsonString, AdGroup.class);
        return adGroup;
    }

    public List<AdGroup> getAdGroupListFromGraph(){

        return null;
    }


    @Data
    public static class AdGroup{
        private String id;
        private List<AdUser> adUsers;
    }
    @Data
    public static class AdUser {
        private String emailShort;
        private String name;
        private String email;
    }


}
