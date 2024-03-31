package edu.java.scrapper.domain.jpa;

import edu.java.scrapper.controller.exception.LinkNotFoundException;
import edu.java.scrapper.domain.LinkRepository;
import edu.java.scrapper.domain.jpa.entities.LinkEntity;
import edu.java.scrapper.domain.jpa.entities.UserEntity;
import edu.java.scrapper.domain.jpa.entities.UserLinksEntity;
import edu.java.scrapper.model.Link;
import jakarta.persistence.NoResultException;
import java.net.URI;
import java.sql.Timestamp;
import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.List;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@RequiredArgsConstructor
public class JpaLinkRepository implements LinkRepository {

    private static final String GITHUB = "github";
    private static final String STACKOVERFLOW = "stackoverflow";
    private static final int CONVERT_TO_MILLI = 1000;
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
            LinkEntity linkEntity;

            try {
                linkEntity = session
                    .createQuery("FROM LinkEntity WHERE url ='" + uri.toString() + "'", LinkEntity.class)
                    .getSingleResult();
                session.persist(linkEntity);
            } catch (NoResultException e) {
                throw new LinkNotFoundException("Can't delete link because it wasn't found");
            }

            List<UserLinksEntity> usersTrackingLink = session
                .createQuery("from UserLinksEntity where link.id=:linkParam", UserLinksEntity.class)
                .setParameter("linkParam", linkEntity.getId())
                .getResultList();

            boolean moreOneUserTracking = (usersTrackingLink.size() > 1);

            for (var userTracker : usersTrackingLink) {
                if (Objects.equals(userTracker.getUser().getId(), userId)) {
                    session.remove(userTracker);
                }
            }

            if (!moreOneUserTracking) {
                session.remove(session.get(LinkEntity.class, linkEntity.getId()));
            }
            session.flush();
            return new Link(linkEntity.getId(), uri);
        }
    }

    @Override
    public List<Link> findAllUserLinks(Long userId) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();

            List<LinkEntity> usersLinks = session
                .createQuery("select link from UserLinksEntity as ule "
                    + "where ule.user = :userId", LinkEntity.class)
                .setParameter("userId", new UserEntity(userId))
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
                    + "where lastUpdate is null or lastUpdate < :threshold", LinkEntity.class)
                .setParameter(
                    "threshold",
                    new Timestamp(System.currentTimeMillis() - (interval * CONVERT_TO_MILLI))
                )
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
                .createQuery("UPDATE LinkEntity SET lastUpdate = %s WHERE id = %s"
                    .formatted(updateTime, link.getId()), LinkEntity.class);

            session.flush();
            return lastUpdate == null || (updateTime).isAfter(lastUpdate.getLastUpdate().toLocalDateTime().atOffset(
                ZoneOffset.systemDefault().getRules().getOffset(Instant.now())));
        }
    }

    @Override
    public List<Link> findAllLinks() {
        try (Session session = sessionFactory.openSession()) {
            List<LinkEntity> result = session
                .createQuery("from LinkEntity", LinkEntity.class).getResultList();
            return result.stream().map(linkEntity -> new Link(linkEntity.getId(), URI.create(linkEntity.getUrl())))
                .toList();
        }
    }

    private static String parseLinkType(URI uri) {
        String link = uri.toString();

        if (link.contains(GITHUB)) {
            return GITHUB;
        } else if (link.contains(STACKOVERFLOW)) {
            return STACKOVERFLOW;
        } else {
            return null;
        }
    }
}
