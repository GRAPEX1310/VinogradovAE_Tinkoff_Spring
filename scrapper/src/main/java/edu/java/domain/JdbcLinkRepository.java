package edu.java.domain;

import edu.java.controller.exception.AddingLinkOneMoreTimeException;
import edu.java.controller.exception.LinkNotFoundException;
import edu.java.model.Link;
import java.net.URI;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public class JdbcLinkRepository {

    private static final String SELECT_LINK_ID_REQUEST = "SELECT id FROM links WHERE url = ?";
    private static final String GITHUB = "github";
    private static final String STACKOVERFLOW = "stackoverflow";

    private final JdbcTemplate jdbcTemplate;
    private final LinkMapper linkMapper;

    @Autowired
    public JdbcLinkRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.linkMapper = new LinkMapper();
    }

    @Transactional
    public void addLink(Long userId, Link link) {
        Long linkId = insertLink(link.getUri());

        if (!isLinkTrackByUser(userId, linkId)) {
            addUserTrackedLink(userId, linkId);
        } else {
            throw new AddingLinkOneMoreTimeException("User - " + userId + " already tracking link" + link.getUri());
        }
        link.setId(linkId);
    }

    @Transactional
    public Link removeLinkByURL(Long userId, URI uri) {
        Long linkId = getLinkIdByURL(uri.toString());
        if (linkId == null) {
            throw new LinkNotFoundException("User %d don't track link %s".formatted(userId, uri));
        }

        removeUserLink(userId, linkId);
        if (!isLinkTracked(linkId)) {
            removeLinkById(linkId);
        }

        return new Link(linkId, uri);
    }

    public List<Link> findAllLinks() {
        String sql = "SELECT id, url FROM links";
        return jdbcTemplate.query(sql, linkMapper);
    }

    @Transactional
    public List<Link> findAllUserLinks(Long userId) {
        String sql =
            "SELECT links.id, url FROM links INNER JOIN user_links"
                + " ON links.id = user_links.link_id WHERE user_id = ?";
        return jdbcTemplate.query(sql, linkMapper, userId);
    }

    @Transactional
    public List<Link> findAllLinksWithCheckInterval(Long interval) {
        String sql = "SELECT id, url FROM links "
            + "WHERE last_update IS NULL OR EXTRACT(EPOCH FROM (CURRENT_TIMESTAMP - last_update)) > ?";
        return jdbcTemplate.query(sql, linkMapper, interval);
    }

    @Transactional
    public boolean updateLink(Link link, OffsetDateTime updateTime) {
        String getSql = "SELECT last_update FROM links WHERE id = ?";
        Optional<OffsetDateTime> lastUpdate = Optional.ofNullable(
            jdbcTemplate.queryForObject(getSql, OffsetDateTime.class, link.getId()));

        String sql = "UPDATE links SET last_update = ? WHERE id = ?";
        jdbcTemplate.update(sql, updateTime, link.getId());
        return updateTime == null || updateTime.isAfter(lastUpdate.get());
    }

    private Long getLinkIdByURL(String url) {
        try {
            return jdbcTemplate.queryForObject(SELECT_LINK_ID_REQUEST, Long.class, url);
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    private Long insertLink(URI uri) {
        String linkType = parseLinkType(uri);
        Long linkId;

        linkId = jdbcTemplate.queryForObject(
            "INSERT INTO links(url, link_type) VALUES (?, ?::enum_link_type) "
                + "ON CONFLICT (url) DO UPDATE SET link_type = EXCLUDED.link_type RETURNING id",
            Long.class,
            uri.toString(),
            linkType
        );

        return linkId;
    }

    private boolean isLinkTrackByUser(long userId, long linkId) {
        String sql = "SELECT COUNT(*) FROM user_links WHERE user_id = ? AND link_id = ?";
        Integer result = jdbcTemplate.queryForObject(sql, Integer.class, userId, linkId);
        return !(result == null || result == 0);
    }

    private void addUserTrackedLink(long userId, long linkId) {
        String sql = "INSERT INTO user_links (user_id, link_id) VALUES (?, ?)";
        jdbcTemplate.update(sql, userId, linkId);
    }

    private void removeUserLink(long userId, long linkId) {
        String sql = "DELETE FROM user_links WHERE user_id = ? AND link_id = ?";
        jdbcTemplate.update(sql, userId, linkId);
    }

    private boolean isLinkTracked(long linkId) {
        String sql = "SELECT COUNT(*) FROM user_links WHERE link_id = ?";
        Integer result = jdbcTemplate.queryForObject(sql, Integer.class, linkId);
        return !(result == null || result == 0);
    }

    private void removeLinkById(long linkId) {
        String sql = "DELETE FROM links WHERE id = ?";
        jdbcTemplate.update(sql, linkId);
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

    private static final class LinkMapper implements RowMapper<Link> {
        @Override
        public Link mapRow(ResultSet rs, int rowNum) throws SQLException {
            Long id = rs.getLong("id");
            URI uri = URI.create(rs.getString("url"));
            return new Link(id, uri);
        }
    }
}
