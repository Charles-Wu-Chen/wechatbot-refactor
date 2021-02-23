package au.com.charleswu.wechatbot.domain.bot.roomsync;

import au.com.charleswu.wechatbot.application.out.SendMessagePort;
import au.com.charleswu.wechatbot.domain.Contact;
import au.com.charleswu.wechatbot.domain.Room;
import au.com.charleswu.wechatbot.domain.message.*;
import au.com.charleswu.wechatbot.domain.message.content.ImageMessageContent;
import au.com.charleswu.wechatbot.domain.message.content.MessageContent;
import au.com.charleswu.wechatbot.domain.message.content.TextMessageContent;
import io.github.wechaty.Wechaty;
import io.github.wechaty.user.Image;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;

class RoomSyncBotTest {

    private SendMessagePort sendMessagePort  = Mockito.mock(SendMessagePort.class);
    private RoomSyncBot roomSyncBot;

    @BeforeEach
    void initUseCase() {
        roomSyncBot = new RoomSyncBot(sendMessagePort);
    }

    @Test
    void shallSyncGroupMessageTextTypeIfGroupInMessageRoute() {
        MessageContent content = TextMessageContent.builder()
                .content("TextMessageContent")
                .build();

        Message incomingMessage = RoomMessage.builder()
                .messageType(MessageType.Text)
                .room(new Room("roomId", "测试区危险"))
                .content(content)
                .from(new Contact("ID", "NAME"))
                .build();
        roomSyncBot.handleMessage(incomingMessage);


        ArgumentCaptor<Message> messageCaptor = ArgumentCaptor.forClass(Message.class);
        verify(sendMessagePort).sendMessage(messageCaptor.capture());
        assertTrue(messageCaptor.getValue().getContent().getContent().toString().contains("TextMessageContent"));
        Assertions.assertTrue(messageCaptor.getValue() instanceof RoomMessage);

        assertEquals("测试区不危险", ((RoomMessage)messageCaptor.getValue()).getRoom().getTopic());

    }

    @Test
    void shallSyncGroupMessageImageTypeIfGroupInMessageRoute() {
        MessageContent content = ImageMessageContent.builder()
                .content(null)
                .build();

        Message incomingMessage = RoomMessage.builder()
                .messageType(MessageType.Image)
                .room(new Room("roomId", "测试区危险"))
                .content(content)
                .build();
        roomSyncBot.handleMessage(incomingMessage);


        ArgumentCaptor<Message> messageCaptor = ArgumentCaptor.forClass(Message.class);
        verify(sendMessagePort).sendMessage(messageCaptor.capture());
        assertEquals(null, messageCaptor.getValue().getContent().getContent());
        Assertions.assertTrue(messageCaptor.getValue() instanceof RoomMessage);
        assertEquals("测试区不危险", ((RoomMessage)messageCaptor.getValue()).getRoom().getTopic());

    }

}
