package edu.java.bot;

import com.pengrad.telegrambot.model.Chat;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.Update;
import edu.java.bot.utils.LinkValidator;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import java.util.stream.Stream;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;

public class LinkValidatorTest {

    @ParameterizedTest(name = "#{index} - Run with args: {0}")
    @MethodSource("argumentsProvider")
    @DisplayName("Link validator work test")
    public void testLinkValidator(String givenLink, boolean correctAnswer) {
        assertThat(LinkValidator.isLinkValid(givenLink)).isEqualTo(correctAnswer);
    }

    private static Stream<Arguments> argumentsProvider() {
        return Stream.of(
            Arguments.of("aboba", false),
            Arguments.of("github", false),
            Arguments.of("stackoverflow", false),
            Arguments.of("http://google.com", false),
            Arguments.of("http://github.com", true),
            Arguments.of("http://stackoverflow.com", true),
            Arguments.of("https://github.com", true),
            Arguments.of("https://stackoverflow.com", true)
        );
    }
}
