package au.com.charleswu.wechatbot.task;

import au.com.charleswu.wechatbot.application.in.ChatbotService;
import au.com.charleswu.wechatbot.domain.message.Message;
import au.com.charleswu.wechatbot.domain.message.mapper.MessageMapper;
import au.com.charleswu.wechatbot.domain.message.mapper.MessageMapperFactory;
import io.github.wechaty.LoginListener;
import io.github.wechaty.RoomJoinListener;
import io.github.wechaty.RoomLeaveListener;
import io.github.wechaty.Wechaty;
import io.github.wechaty.io.github.wechaty.schemas.EventEnum;
import io.github.wechaty.schemas.ContactQueryFilter;
import io.github.wechaty.schemas.FriendshipType;
import io.github.wechaty.schemas.RoomQueryFilter;
import io.github.wechaty.user.*;
import io.github.wechaty.user.manager.RoomManager;
import io.github.wechaty.utils.QrcodeUtils;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutionException;

import static au.com.charleswu.wechatbot.adaptor.util.CommonWechatyUtil.getTopicByRoom;
import static au.com.charleswu.wechatbot.adaptor.util.CommonWechatyUtil.loadContact;

@Component
public class BotThread implements Runnable {
    Logger logger = LoggerFactory.getLogger(this.getClass());

    private String wechatToken;

    public void setWechatToken (String wechatToken) {
        this.wechatToken = wechatToken;
    }

    @Autowired
    ChatbotService chatbotService;

    @Autowired
    MessageMapperFactory messageMapperFactory;

    @Override
    public void run() {

        Wechaty wechaty = Wechaty.instance(this.wechatToken);
        SysConstant.wechatyBot = wechaty;
        // 监听扫描登录
        wechaty.onScan((qrcode, statusScanStatus, data) -> {
            logger.info("scan: {}", QrcodeUtils.getQr(qrcode));
        });

        // 监听退出的情况
        wechaty.on(EventEnum.LOGOUT, logOut->{
            logger.info("the bot logout: {}, logOutTime:{}", logOut, LocalDateTime.now());
            wechaty.stop();
            wechaty.start(true);
        });

        // 有人加入群的时候
        wechaty.onRoomJoin(new RoomJoinListener() {
            @Override
            public void handler(@NotNull Room room, @NotNull List<? extends Contact> list, @NotNull Contact contact, @NotNull Date date) {
                logger.info("joinRoomName: {}, contactListSize:{}, contactName: {}, dateTime:{}", room.getTopic(), list.size(), contact.name(), date);
            }
        });

        // 当有人离群的时候触发
        wechaty.onRoomLeave(new RoomLeaveListener(){
            @Override
            public void handler(@NotNull Room room, @NotNull List<? extends Contact> list, @NotNull Contact contact, @NotNull Date date) {
                logger.info("leaveRoomName: {}, contactListSize: {}, contactName: {}, dateTime:{}", room.getTopic(), list.size(), contact.name(), date);
            }
        });

        // 监听登录
        wechaty.onLogin(new LoginListener(){
            @Override
            public void handler(@NotNull ContactSelf contactSelf) {
                logger.info("login userName:{}, userId:{}", contactSelf.name(), contactSelf.getId());
                SysConstant.loginUserName = contactSelf.name();
                SysConstant.loginUserId = contactSelf.getId();
                ContactQueryFilter contactQueryFilter = new ContactQueryFilter();
                //contactQueryFilter.setId("wxid_1194601945911"); //wuchen
                //TODO lazy load if possible
                List<Contact> contacts = wechaty.getContactManager().findAll(contactQueryFilter);
                contacts.stream()
                        .forEach(contact -> {
                            logger.info("is ready : " + contact.isReady());
                            logger.info(String.format("contact ID:%s, contact name:%s", contact.getId(), contact.name()));
                            if (!contact.isReady()) {
                                contact.sync();
                            }
                            if (contact.getId().equals("wxid_1194601945911")) {
                                contact.say(String.format("您的专属机器人 %s logged in", contactSelf.name()));
                            }
                        });

                RoomQueryFilter roomQueryFilter = new RoomQueryFilter();
                List<Room> rooms = wechaty.getRoomManager().findAll(roomQueryFilter);
                rooms.stream().forEach(room -> logger.info("room topic:" + getTopicByRoom(room)));

            }
        });

        // 监听好友请求
        wechaty.on(EventEnum.FRIENDSHIP, friendshipList -> {
            logger.info("listen friendship: friendshipId:{}, time:{}", friendshipList, LocalDateTime.now());
            RoomManager roomManager = SysConstant.wechatyBot.getRoomManager();
            List<Room> roomList = roomManager.findAll(new RoomQueryFilter());
            for(Object friendshipObj : friendshipList){
                Friendship friendship = (Friendship) friendshipObj;
                if(friendship.type().equals(FriendshipType.Receive)){
                    friendship.accept();
                    Contact contact = friendship.contact();
                    contact.say("hello 机器人智囊团 欢迎您");
                    for(Room room : roomList){
                        try {
                            String topicName = room.getTopic().get();
                            if("机器人智囊团".equals(topicName)){
                                room.add(contact);
                                logger.info("room id:{}", topicName);
                            }
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        } catch (ExecutionException e) {
                            e.printStackTrace();
                        }

                    }
                }
            }
        });
        // 监听发送消息
        wechaty.onMessage(message -> {
            logger.info("listen message, messageId:{}, isFromRoom:{}, fromName:{}, toName:{},  content:{}, time:{}", message.getId(), message.room() != null, message.from().name(), message.to().name(), message.text(), LocalDateTime.now());
            if(SysConstant.localMessageId.equals(message.getId())){ // 处理重复消息
                logger.info("skip duplication message with same id");
                return;
            }
            SysConstant.localMessageId = message.getId();
            String text = message.text(); // 消息内容
            Contact from = loadContact(message.from()); // 来自哪个用户的信息
            Contact toContact = message.to(); // 发送给哪个用户
            Room room = message.room();

            if (from == null || from.name() == null) { //skip if contact is not ready.
                logger.info("skip empty contact");
                return;
            }
            if(SysConstant.loginUserName.equals(message.from().name())){ // 自己发送出去的消息不做处理
                logger.info("skip contact self message");
                return;
            }

            if (toContact.name().contains("微信")
                    || from.name().contains("微信")
                    || from.name().contains("文件传输助手")) {
                return;
            }

            MessageMapper messageMapper = messageMapperFactory.initMessageMapper(message.from(), message.room());

            Message messageEntity = messageMapper.mapToMessage(message);
            chatbotService.handleMessage(messageEntity);
        });
        logger.info("bot thread wechaty start");
        wechaty.start(false);
    }

}
