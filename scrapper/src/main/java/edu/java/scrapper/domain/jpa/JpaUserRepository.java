package edu.java.scrapper.domain.jpa;

import edu.java.scrapper.controller.exception.DoubleRegistrationException;
import edu.java.scrapper.domain.UserRepository;
import edu.java.scrapper.domain.jpa.entities.UserEntity;
import edu.java.scrapper.model.Link;
import edu.java.scrapper.model.User;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@RequiredArgsConstructor
public class JpaUserRepository implements UserRepository {

    private final JpaLinkRepository linkRepository;
    private final SessionFactory sessionFactory;

    @Override
    public void addUser(Long userId) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            UserEntity newUser = new UserEntity(userId);
            session.save(new UserEntity(userId));
            session.flush();
        } catch (Exception e) {
            System.out.println(e.getMessage());
            System.out.println(e.getCause());
            throw new DoubleRegistrationException("This user has already exists!");
        }
    }

    @Override
    public void removeUser(Long userId) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();

            List<Link> userLinks = linkRepository.findAllUserLinks(userId);
            for (var link : userLinks) {
                linkRepository.removeLinkByURL(userId, link.getUri());
            }

            var userForDeleting = session.get(UserEntity.class, userId);
            session.remove(userForDeleting);
            session.flush();
        }
    }

    @Override
    public Optional<User> findUser(Long userId) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            var userEntity = session.get(UserEntity.class, userId);
            session.flush();
            return Optional.of(new User(userEntity.getId()));
        }
    }

    @Override
    public List<Long> findUsersTrackLink(Long linkId) {
        try (Session session = sessionFactory.openSession()) {
            List<UserEntity> result = session
                .createQuery(
                    "from UserLinksEntity WHERE link = " + linkId,
                    UserEntity.class
                ).getResultList();
            return result.stream().map(UserEntity::getId).toList();
        }
    }

    @Override
    public List<User> findAllUsers() {
        try (Session session = sessionFactory.openSession()) {
            List<UserEntity> result = session
                .createQuery("from UserEntity", UserEntity.class).getResultList();
            return result.stream().map(userEntity -> new User(userEntity.getId())).toList();
        }
    }
}
