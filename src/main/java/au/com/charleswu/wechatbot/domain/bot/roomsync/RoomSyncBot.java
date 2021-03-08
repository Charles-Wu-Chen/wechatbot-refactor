package au.com.charleswu.wechatbot.domain.bot.roomsync;

import au.com.charleswu.wechatbot.application.out.SendMessagePort;
import au.com.charleswu.wechatbot.domain.Room;
import au.com.charleswu.wechatbot.domain.bot.ChatBot;
import au.com.charleswu.wechatbot.domain.message.Message;
import au.com.charleswu.wechatbot.domain.message.content.MessageContent;
import au.com.charleswu.wechatbot.domain.message.RoomMessage;

import au.com.charleswu.wechatbot.domain.message.content.TextMessageContent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class RoomSyncBot implements ChatBot {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    private final SendMessagePort sendMessagePort;

    public RoomSyncBot(SendMessagePort sendMessagePort) {
        this.sendMessagePort = sendMessagePort;
    }

    @Override
    public void handleMessage(Message message) {
        switch (message.getMessageType()) {
            case Text:
                handleTextMessage(message);
                break;
            case Image:
                handleImageMessage(message);
                break;
            default:
                logger.info("not supported message format:" + message.getMessageType());
        }
    }

    private void handleTextMessage(Message message) {
        RoomMessage incomingMessage = (RoomMessage) message;

        MessageContent content = TextMessageContent.builder()
                .content(String.format("[%s]:%n%s", incomingMessage.getFrom().getName(),
                        incomingMessage.getContent().getContent()))
                .build();

        //TODO will be handy if store room in cache
        getMessageRoutes().stream()
                .filter(messageRoute -> messageRoute.getSourceName().equals(incomingMessage.getRoom().getTopic()))
                .forEach(messageRoute -> {
                    Room destRoom = new Room("", messageRoute.getDestinationName());

                    Message outgoingMessage = RoomMessage.builder()
                            .content(content)
                            .room(destRoom)
                            .build();
                    sendMessagePort.sendMessage(outgoingMessage);
                });
    }

    public void handleImageMessage(Message message) {
        RoomMessage incomingMessage = (RoomMessage) message;


        //TODO will be handy if store room in cache
        getMessageRoutes().stream()
                .filter(messageRoute -> messageRoute.getSourceName().equals(incomingMessage.getRoom().getTopic()))
                .forEach(messageRoute -> {
                    Room destRoom = new Room("", messageRoute.getDestinationName());

                    Message outgoingMessage = RoomMessage.builder()
                            .content(incomingMessage.getContent())
                            .room(destRoom)
                            .build();
                    sendMessagePort.sendMessage(outgoingMessage);
                });

    }

    @Override
    public String getUsage() {
        return "自动转发群消息";
    }

    @Override
    public boolean isSupported(Message message) {
        return message instanceof RoomMessage;
    }

    //TODO externalize this to properties and initialization
    private static List<MessageRoute> getMessageRoutes() {
        MessageRoute route1 = new MessageRoute("测试区危险", "测试区不危险");
        MessageRoute route2 = new MessageRoute("测试区不危险", "测试区危险");
        MessageRoute route3 = new MessageRoute("3133好邻居群三", "测试区危险");
        MessageRoute route4 = new MessageRoute("3133好邻居群三", "3133好邻居群二");
        MessageRoute route5 = new MessageRoute("3133好邻居群二", "3133好邻居群三");
        MessageRoute route6 = new MessageRoute("3133好邻居群三", "3133好邻居群一");
        MessageRoute route7 = new MessageRoute("3133好邻居群二", "3133好邻居群一");
        MessageRoute route8 = new MessageRoute("3133好邻居群一", "3133好邻居群三");
        MessageRoute route9 = new MessageRoute("3133好邻居群一", "3133好邻居群二");

        List<MessageRoute> routes = new ArrayList<>();
        routes.add(route1);
        routes.add(route2);
        routes.add(route3);
        routes.add(route4);
        routes.add(route5);
        routes.add(route6);
        routes.add(route7);
        routes.add(route8);
        routes.add(route9);

        return routes;
    }
}
