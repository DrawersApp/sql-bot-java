import com.drawers.dao.MqttChatMessage;
import com.drawers.dao.mqttinterface.PublisherImpl;
import com.drawers.dao.packets.listeners.NewMessageListener;
import org.drawers.bot.mqtt.DrawersBot;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

/**
 * Created by harshit on 22/5/16.
 */
public class SqlMessageListener implements NewMessageListener {

    private final PublisherImpl publisher;
    private final SqlHelper sqlHelper;
    private final String clientId;

    public SqlMessageListener(DrawersBot bot, SqlHelper sqlHelper, String clientId) {
        this.publisher = bot;
        this.sqlHelper = sqlHelper;
        this.clientId = clientId;
    }

    @Override
    public void receiveMessage(MqttChatMessage mqttChatMessage) {
        String message = null;
        try {
            message = URLDecoder.decode(mqttChatMessage.message, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        if (message != null && message.contains("select")) {
            try {
                sqlHelper.select(message, publisher, mqttChatMessage, clientId);
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        } else if (message != null) {
            try {
                sqlHelper.genericSql(message, publisher, mqttChatMessage, clientId);
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }

    }

    @Override
    public void acknowledgeStanza(MqttChatMessage mqttChatMessage) {

    }
}
