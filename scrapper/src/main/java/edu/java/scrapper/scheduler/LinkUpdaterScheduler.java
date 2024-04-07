package edu.java.scrapper.scheduler;

import edu.java.scrapper.model.Link;
import edu.java.scrapper.service.database.LinkService;
import edu.java.scrapper.service.database.UserService;
import edu.java.scrapper.service.sender.UpdateSenderService;
import edu.java.scrapper.service.updater.LinkUpdater;
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
    private final UpdateSenderService updateSenderService;
    private final List<LinkUpdater> linkUpdaters;
    private final Duration checkInterval;

    @Autowired
    public LinkUpdaterScheduler(
        LinkService linkService,
        UserService userService,
        UpdateSenderService updateSenderService,
        List<LinkUpdater> linkUpdaters,
        @Value("#{@scheduler.checkInterval()}") Duration checkInterval
    ) {
        this.linkService = linkService;
        this.userService = userService;
        this.updateSenderService = updateSenderService;
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
        updateSenderService.sendUpdate(link.getId(), link.getUri(), message, usersIds);
    }
}
