package edu.java.bot;

import com.pengrad.telegrambot.model.Chat;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.Update;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.stream.Stream;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;

@ExtendWith(MockitoExtension.class)
public class MessageProcessorTest extends AbstractTest {

    @ParameterizedTest(name = "#{index} - Run with args: {0}")
    @MethodSource("argumentsProvider")
    @DisplayName("Message processor work test")
    public void testMessageProcessor(String commandName, String response) {
        Update update = mock(Update.class);
        Message message = mock(Message.class);
        Chat chat = mock(Chat.class);

        doReturn(13L).when(chat).id();
        doReturn(commandName).when(message).text();
        doReturn(chat).when(message).chat();
        doReturn(message).when(update).message();

        assertThat(messageProcessor.checkUpdate(update, bot)).isEqualTo(response);
    }

    private static Stream<Arguments> argumentsProvider() {
        return Stream.of(
            Arguments.of("/list", "Здесь появятся ссылки на все отслеживаемые ресурсы"),
            Arguments.of("/track", "Для начала работы нужна регистрация через команду /start"),
            Arguments.of("/untrack", "Для начала работы нужна регистрация через команду /start"),
            Arguments.of("/cancel", "Отправка ссылки отменена!"),
            Arguments.of("/start", """
                Привет! Ты только что успешно зарегистрировался в боте
                Данный бот создан для отслеживания ссылок с github и stackoverflow
                Нажми на команду /help и изучи способы взаимодействия с этим ботом
                """),
            Arguments.of("/help", """
                Как взаимодействовать с ботом?
                Слева снизу находится кнопка "Меню", в которой показаны и описаны все команды данного бота
                /start - регистрация пользователя
                /help - помощь во взаимодействии с ботом
                /track - начать отслеживание обновлений ресурса
                /untrack - прекратить отслеживание обновлений ресурса
                /list - список отслеживаемых ресурсов
                /cancel - отменить ввод ссылки
                """)
        );
    }
}
