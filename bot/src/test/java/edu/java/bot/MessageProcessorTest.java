package edu.java.bot;

import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.Update;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import java.lang.reflect.Field;
import java.util.stream.Stream;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
public class MessageProcessorTest extends BaseForTest {

    @ParameterizedTest(name = "#{index} - Run with args: {0}")
    @MethodSource("argumentsProvider")
    @DisplayName("Message processor work test")
    public void testMessageProcessor(String command, String response)
        throws NoSuchFieldException, IllegalAccessException {
        Update update = new Update();
        Message message = new Message();


        Field commandField = message.getClass().getDeclaredField("text");
        commandField.setAccessible(true);
        commandField.set(message, command);

        Field answerField = update.getClass().getDeclaredField("message");
        answerField.setAccessible(true);
        answerField.set(update, message);

        assertThat(messageProcessor.checkUpdate(update, bot)).isEqualTo(response);
    }

    private static Stream<Arguments> argumentsProvider() {
        return Stream.of(
            Arguments.of("/start", "Регистрация выполнена"),
            Arguments.of("/list", "Здесь будет список отслеживаемых ресурсов. А пока что - заглушка 👌"),
            Arguments.of("/track", "Жду ссылку на ресурс, который вы хотите начать отслеживать"),
            Arguments.of("/untrack", "Жду ссылку на ресурс, который вы хотите прекратить отслеживать"),
            Arguments.of("/help", """
            Как взаимодействовать с ботом?
            Слева снизу находится кнопка "Меню", в которой показаны и описаны все команды данного бота
            /start - регистрация пользователя
            /help - помощь во взаимодействии с ботом
            /track - начать отслеживание обновлений ресурса
            /untrack - прекратить отслеживание обновлений ресурса
            /list - список отслеживаемых ресурсов
            """)
        );
    }
}
