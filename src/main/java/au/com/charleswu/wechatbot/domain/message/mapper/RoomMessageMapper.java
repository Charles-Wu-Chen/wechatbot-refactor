package au.com.charleswu.wechatbot.domain.message.mapper;

import au.com.charleswu.wechatbot.domain.message.Message;
import au.com.charleswu.wechatbot.domain.message.MessageType;
import au.com.charleswu.wechatbot.domain.message.RoomMessage;
import au.com.charleswu.wechatbot.domain.message.content.MessageContent;
import au.com.charleswu.wechatbot.domain.message.content.MessageContentMapper;
import au.com.charleswu.wechatbot.domain.message.content.TextMessageContent;
import io.github.wechaty.user.Contact;
import io.github.wechaty.user.Room;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.concurrent.ExecutionException;

import static au.com.charleswu.wechatbot.adaptor.util.CommonWechatyUtil.loadContact;

@Component
public class RoomMessageMapper implements MessageMapper {
    Logger logger = LoggerFactory.getLogger(this.getClass());

    private final MessageMapperFactory messageMapperFactory;

    public RoomMessageMapper(MessageMapperFactory messageMapperFactory) {
        this.messageMapperFactory = messageMapperFactory;
    }


    public Message mapToMessage(
            io.github.wechaty.user.Message wechatyMessage) {

        String text = wechatyMessage.text(); // 消息内容
        Contact from = loadContact(wechatyMessage.from()); // 来自哪个用户的信息
        Contact toContact = wechatyMessage.to(); // 发送给哪个用户
        Room room = wechatyMessage.room();

        logger.debug(text, from, toContact, room);

        //GROUP message doesn't have from contact ID

        String roomTopic = "";

        try {
            roomTopic = room.getTopic().get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        au.com.charleswu.wechatbot.domain.Room domainRoom = new au.com.charleswu.wechatbot.domain.Room(room.getId(), roomTopic);

        MessageContentMapper messageContentMapper =
                messageMapperFactory.initMessageContentMapper(wechatyMessage.type().name());

        Message resultMessage = RoomMessage.builder()
                .content(messageContentMapper.getContentFrom(wechatyMessage))
                .room(domainRoom)
                .messageType(MessageType.valueOf(wechatyMessage.type().name()))
                .from(new au.com.charleswu.wechatbot.domain.Contact(from.getId(), from.getAlias(), from.name()))
                .build();

        return resultMessage;
    }
}
