package edu.java.domain.jpa;

import edu.java.domain.LinkRepository;
import edu.java.domain.jpa.entities.LinkEntity;
import edu.java.domain.jpa.entities.LinkType;
import edu.java.domain.jpa.entities.UserEntity;
import edu.java.domain.jpa.entities.UserLinksEntity;
import edu.java.model.Link;
import java.net.URI;
import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.List;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
@RequiredArgsConstructor
public class JpaLinkRepository implements LinkRepository {

    private static final String GITHUB = "github";
    private static final String STACKOVERFLOW = "stackoverflow";
    private final SessionFactory sessionFactory;

    @Override
    public void addLink(Long userId, Link link) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();

            UserEntity userEntity = session.get(UserEntity.class, userId);

            if (userEntity == null) {
                return;
            }

            LinkEntity linkEntity =
                new LinkEntity(null, parseLinkType(link.getUri()), link.getUri().toString(), null);

            session.persist(linkEntity);
            session.persist(new UserLinksEntity(userEntity, linkEntity));

            session.flush();
        }
    }

    @Override
    public Link removeLinkByURL(Long userId, URI uri) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();

            LinkEntity linkEntity = session
                .createQuery("FROM LinkEntity WHERE url =" + uri.toString(), LinkEntity.class)
                .getSingleResult();

            Long linkId = linkEntity.getId();

            List<UserLinksEntity> usersTrackingLink = session
                .createQuery("from UserLinksEntity where link_id=" + linkId, UserLinksEntity.class)
                .getResultList();

            boolean moreOneUserTracking = (usersTrackingLink.size() > 1);

            for (var userTracker : usersTrackingLink) {
                if (Objects.equals(userTracker.getUser().getId(), userId)) {
                    session.remove(userTracker);
                }
            }

            if (!moreOneUserTracking) {
                session.remove(session.get(LinkEntity.class, linkId));
            }
            session.flush();
            return new Link(linkId, uri);
        }
    }

    @Override
    public List<Link> findAllUserLinks(Long userId) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();

            List<LinkEntity> usersLinks = session
                .createQuery("from LinkEntity inner join UserLinksEntity "
                    + "on LinkEntity.id = UserLinksEntity.link_id where user_id =" + userId, LinkEntity.class)
                .getResultList();
            session.flush();
            return usersLinks.stream()
                .map(linkEntity -> new Link(linkEntity.getId(), URI.create(linkEntity.getUrl()))).toList();
        }
    }

    @Override
    public List<Link> findAllLinksWithCheckInterval(Long interval) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();

            List<LinkEntity> usersLinks = session
                .createQuery("from LinkEntity "
                    + "where last_update is null or extract(epoch from (current_timestamp - last_update)) > "
                    + interval, LinkEntity.class)
                .getResultList();
            session.flush();
            return usersLinks.stream()
                .map(linkEntity -> new Link(linkEntity.getId(), URI.create(linkEntity.getUrl()))).toList();
        }
    }

    @Override
    public boolean updateLink(Link link, OffsetDateTime updateTime) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();

            List<LinkEntity> usersLinks = session
                .createQuery("FROM LinkEntity WHERE id = " + link.getId(), LinkEntity.class)
                .getResultList();

            LinkEntity lastUpdate = usersLinks.getFirst();

            session
                .createQuery("UPDATE LinkEntity SET last_update = %s WHERE id = %s"
                    .formatted(updateTime, link.getId()), LinkEntity.class);

            session.flush();
            return lastUpdate == null || (updateTime).isAfter(lastUpdate.getLastUpdate().toLocalDateTime().atOffset(
                ZoneOffset.systemDefault().getRules().getOffset(Instant.now())));
        }
    }

    private static LinkType parseLinkType(URI uri) {
        String link = uri.toString();

        if (link.contains(GITHUB)) {
            return LinkType.github;
        } else if (link.contains(STACKOVERFLOW)) {
            return LinkType.stackoverflow;
        } else {
            return null;
        }
    }
}
