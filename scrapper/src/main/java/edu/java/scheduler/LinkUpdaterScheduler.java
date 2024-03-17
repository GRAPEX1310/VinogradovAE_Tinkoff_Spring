package edu.java.scheduler;

import edu.java.clients.bot.BotClient;
import edu.java.model.Link;
import edu.java.service.LinkService;
import edu.java.service.UserService;
import edu.java.service.updater.LinkUpdater;
import java.time.Duration;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class LinkUpdaterScheduler {
    static int count = 1;
    private final LinkService linkService;
    private final UserService userService;
    private final BotClient botClient;
    private final List<LinkUpdater> linkUpdaters;
    private final Duration checkInterval;

    @Autowired
    public LinkUpdaterScheduler(
        LinkService linkService,
        UserService userService,
        BotClient botClient,
        List<LinkUpdater> linkUpdaters,
        @Value("#{@scheduler.checkInterval()}") Duration checkInterval
    ) {
        this.linkService = linkService;
        this.userService = userService;
        this.botClient = botClient;
        this.linkUpdaters = linkUpdaters;
        this.checkInterval = checkInterval;
    }

    @Scheduled(fixedDelayString = "#{@scheduler.interval().toMillis()}")
    public void update() {
        log.info("Update number - " + count);
        count++;

        Collection<Link> linkCollection = linkService.findLinksForUpdate(checkInterval.getSeconds());
        for (Link link : linkCollection) {
            updateLink(link);
        }
    }

    private void updateLink(Link link) {
        for (LinkUpdater linkUpdater : linkUpdaters) {
            if (linkUpdater.isAppropriateLink(link)) {
                Optional<String> result = linkUpdater.checkUpdate(link);
                result.ifPresent(update -> sendUpdate(link, update));
            }
        }
    }

    private void sendUpdate(Link link, String message) {
        List<Long> usersIds = userService.getUsersTrackLink(link);
        botClient.sendUpdates(link.getId(), link.getUri(), message, usersIds).block();
    }
}
