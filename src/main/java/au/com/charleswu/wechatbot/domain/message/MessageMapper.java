package au.com.charleswu.wechatbot.domain.message;


import io.github.wechaty.user.Contact;
import io.github.wechaty.user.Room;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ExecutionException;

public class MessageMapper {
    private static final Logger logger = LoggerFactory.getLogger(MessageMapper.class);

    public static Message mapToDomainMessage(
            io.github.wechaty.user.Message wechatyMessage) {

        String text = wechatyMessage.text(); // 消息内容
        Contact from = wechatyMessage.from(); // 来自哪个用户的信息
        Contact toContact = wechatyMessage.to(); // 发送给哪个用户
        Room room = wechatyMessage.room();

        logger.debug(text, from, toContact, room);

        au.com.charleswu.wechatbot.domain.message.Message resultMessage = new Message();

        if (room != null) {
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


            resultMessage = RoomMessage.builder()
                    .content(wechatyMessage.content())
                    .room(domainRoom)
                    .messageType(MessageType.valueOf(wechatyMessage.type().name()))
                    .build();
        }
        else if (from != null) {
            au.com.charleswu.wechatbot.domain.Contact fromPerson = new au.com.charleswu.wechatbot.domain.Contact(from.getId());
            resultMessage = PersonMessage.builder()
                    .content(wechatyMessage.content())
                    .from(fromPerson)
                    .messageType(MessageType.valueOf(wechatyMessage.type().name()))
                    .build();
        }
        return resultMessage;
    }
}

