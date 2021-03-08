package au.com.charleswu.wechatbot.domain.message.mapper;

import au.com.charleswu.wechatbot.domain.message.Message;
import au.com.charleswu.wechatbot.domain.message.MessageType;
import au.com.charleswu.wechatbot.domain.message.PersonMessage;
import au.com.charleswu.wechatbot.domain.message.content.MessageContent;
import au.com.charleswu.wechatbot.domain.message.content.MessageContentMapper;
import au.com.charleswu.wechatbot.domain.message.content.TextMessageContent;
import io.github.wechaty.user.Contact;
import io.github.wechaty.user.Room;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import static au.com.charleswu.wechatbot.adaptor.util.CommonWechatyUtil.loadContact;

@Component
public class PersonalMessageMapper implements MessageMapper {
    Logger logger = LoggerFactory.getLogger(this.getClass());

    private final MessageMapperFactory messageMapperFactory;

    public PersonalMessageMapper(MessageMapperFactory messageMapperFactory) {
        this.messageMapperFactory = messageMapperFactory;
    }

    public Message mapToMessage(
            io.github.wechaty.user.Message wechatyMessage) {

        String text = wechatyMessage.text(); // 消息内容
        Contact from = loadContact(wechatyMessage.from()); // 来自哪个用户的信息
        Contact toContact = wechatyMessage.to(); // 发送给哪个用户
        Room room = wechatyMessage.room();

        logger.debug(text, from, toContact, room);

        MessageContentMapper messageContentMapper =
                messageMapperFactory.initMessageContentMapper(wechatyMessage.type().name());

        au.com.charleswu.wechatbot.domain.Contact fromPerson = new au.com.charleswu.wechatbot.domain.Contact(from.getId(), from.getAlias(), from.name());
        au.com.charleswu.wechatbot.domain.message.Message resultMessage = PersonMessage.builder()
                .content(messageContentMapper.getContentFrom(wechatyMessage))
                .from(fromPerson)
                .messageId(wechatyMessage.getId())
                .messageType(MessageType.valueOf(wechatyMessage.type().name()))
                .build();

        return resultMessage;
    }
}
