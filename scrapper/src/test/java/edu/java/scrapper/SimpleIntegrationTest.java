package edu.java.scrapper;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.testcontainers.junit.jupiter.Testcontainers;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import static org.hibernate.validator.internal.util.Contracts.assertTrue;

@Testcontainers
public class SimpleIntegrationTest extends IntegrationEnvironment{

    @Test
    @DisplayName("Test Correct Tables Creation Inside Container")
    public void testCorrectTablesCreationInsideContainer() {
        try (
            Connection connection = DriverManager.getConnection(POSTGRES.getJdbcUrl(), POSTGRES.getUsername(), POSTGRES.getPassword());
            Statement statement = connection.createStatement()
        ) {
            ResultSet resultSet = statement.executeQuery("SELECT EXISTS (SELECT 1 FROM information_schema.tables WHERE table_schema = 'public' AND table_name = 'users')");
            resultSet.next();
            assertTrue(resultSet.getBoolean(1), "Table 'users' does not exist");

            ResultSet resultSetAllLinks = statement.executeQuery("SELECT EXISTS (SELECT 1 FROM information_schema.tables WHERE table_schema = 'public' AND table_name = 'all_links')");
            resultSetAllLinks.next();
            assertTrue(resultSetAllLinks.getBoolean(1), "Table 'all_links' does not exist");

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    @DisplayName("Test Existing Data In Tables Inside Container")
    public void testExistingDataInTablesInsideContainer() {
        try (
            Connection connection = DriverManager.getConnection(POSTGRES.getJdbcUrl(), POSTGRES.getUsername(), POSTGRES.getPassword());
            Statement statement = connection.createStatement()) {

            ResultSet resultSetUsersQuantity = statement.executeQuery("SELECT COUNT(*) AS quantity FROM users");
            resultSetUsersQuantity.next();
            assertTrue(resultSetUsersQuantity.getInt("quantity") == 3, "Error occured");


        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
