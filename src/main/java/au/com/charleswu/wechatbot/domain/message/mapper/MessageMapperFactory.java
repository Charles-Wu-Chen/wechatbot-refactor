package au.com.charleswu.wechatbot.domain.message.mapper;

import au.com.charleswu.wechatbot.domain.message.MessageType;
import au.com.charleswu.wechatbot.domain.message.content.ImageMessageContentMapper;
import au.com.charleswu.wechatbot.domain.message.content.MessageContentMapper;
import au.com.charleswu.wechatbot.domain.message.content.TextMessageContentMapper;
import io.github.wechaty.user.Contact;
import io.github.wechaty.user.Message;
import io.github.wechaty.user.Room;
import io.github.wechaty.user.manager.RoomManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class MessageMapperFactory {
    @Autowired
    private RoomMessageMapper roomMessageMapper;

    @Autowired
    private PersonalMessageMapper personalMessageMapper;

    @Autowired
    private TextMessageContentMapper textMessageContentMapper;

    @Autowired
    private ImageMessageContentMapper imageMessageContentMapper;

    public MessageMapper initMessageMapper(Contact from, Room room) {
        if (room != null) {
            return roomMessageMapper;
        }
        else if (from != null) {
            return personalMessageMapper;
        }
        return null;
    }

    public MessageContentMapper initMessageContentMapper(String wechatyMessageType) {
        if (MessageType.valueOf(wechatyMessageType).equals(MessageType.Text) ) {
            return textMessageContentMapper;
        }
        else if (MessageType.valueOf(wechatyMessageType).equals(MessageType.Image) ) {
            return imageMessageContentMapper;
        }
        return null;
    }
}
