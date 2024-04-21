package edu.java.scrapper.service.updater;

import edu.java.scrapper.clients.stackoverflow.StackOverflowClient;
import edu.java.scrapper.model.Link;
import edu.java.scrapper.service.database.LinkUpdateService;
import java.util.Arrays;
import java.util.Objects;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;

public class StackOverflowLinkUpdater implements LinkUpdater {

    private static final String HOST = "stackoverflow.com";

    private final StackOverflowClient stackOverflowClient;
    private final LinkUpdateService linkUpdateService;

    @Autowired
    public StackOverflowLinkUpdater(StackOverflowClient stackOverflowClient, LinkUpdateService linkUpdateService) {
        this.stackOverflowClient = stackOverflowClient;
        this.linkUpdateService = linkUpdateService;
    }

    @Override
    public Optional<String> checkUpdate(Link link) {
        Optional<Integer> questionId = Arrays.stream(link
                .getUri()
                .getPath()
                .split("/"))
            .skip(2)
            .findFirst()
            .map(Integer::parseUnsignedInt);

        if (questionId.isEmpty()) {
            return Optional.empty();
        }

        var response = stackOverflowClient.getQuestionData(questionId.get()).block();
        if (Objects.requireNonNull(response).isEmpty()) {
            return Optional.empty();
        }
        return linkUpdateService.update(link, response.get().getLastActivityDate());
    }

    @Override
    public boolean isAppropriateLink(Link link) {
        return link.getUri().getHost().equals(HOST);
    }
}
