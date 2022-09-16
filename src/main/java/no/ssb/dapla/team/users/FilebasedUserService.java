package no.ssb.dapla.team.users;

import com.univocity.parsers.annotations.Parsed;
import com.univocity.parsers.common.processor.BeanListProcessor;
import com.univocity.parsers.csv.CsvParser;
import com.univocity.parsers.csv.CsvParserSettings;
import lombok.Data;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.*;
import java.nio.file.Files;
import java.util.List;

/**
 * UserService that retrieves data from an offline user database (csv-file). The following outlines how the user file
 * can be created: https://github.com/statisticsnorway/dapla-start-toolkit#update-ssb-users-list-served-by-dapla-start-api
 */
@Service
public class FilebasedUserService {

    private final String usersFilePath;

    public FilebasedUserService(@NonNull @Value("${ad.users.exportfile}") String usersFilePath) {
        this.usersFilePath = usersFilePath;
    }

    public List<User> fetchAllUsers() {
        try (Reader inputReader = new InputStreamReader(Files.newInputStream(new File(usersFilePath).toPath()), "UTF-8")) {
            BeanListProcessor<AdUser> rowProcessor = new BeanListProcessor<>(AdUser.class);
            CsvParserSettings settings = new CsvParserSettings();
            settings.setHeaderExtractionEnabled(true);
            settings.setProcessor(rowProcessor);
            CsvParser parser = new CsvParser(settings);
            parser.parse(inputReader);
            return rowProcessor.getBeans().stream()
                    .map(adUser -> User.builder()
                            .name(adUser.getDisplayName())
                            .email(adUser.getMail())
                            .emailShort(adUser.getUserPrincipalName())
                            .build()).toList();

        } catch (IOException e) {
            throw new UserServiceException("Failed to read ssb users from file " + usersFilePath, e);
        }
    }

    @Data
    public static class AdUser {
        @Parsed
        private String userPrincipalName;
        @Parsed
        private String displayName;
        @Parsed
        private String mail;
    }
}

