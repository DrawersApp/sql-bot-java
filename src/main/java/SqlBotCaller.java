import com.drawers.dao.MqttChatMessage;
import com.drawers.dao.packets.MqttProviderManager;
import com.drawers.dao.packets.SubscribeOthers;
import org.drawers.bot.listener.DrawersMessageListener;
import org.drawers.bot.mqtt.DrawersBot;
import org.drawers.bot.util.SendMail;

import java.util.concurrent.TimeUnit;

/**
 * Created by harshit on 22/5/16.
 */
class SqlBotCaller implements DrawersMessageListener {
    private static DrawersBot bot;
    private static SqlBotCaller client;
    private MqttProviderManager mqttProviderManager;
    private SqlHelper sqlHelper;
    private String clientId;

    public SqlBotCaller(String clientId, String password) {
        bot = new DrawersBot(clientId, password, this);
        mqttProviderManager = MqttProviderManager.getInstanceFor(bot);
    }

    public static void main(String[] args) {
        if(args.length != 6) {
            System.out.println("Usage: java DrawersClientCli <clientId> <password> <admin-email-id> <db url> <db user> <db pwd>");
        } else {
            String clientId = args[0];
            String password = args[1];
            String adminEmail = args[2];
            String dbUrl = args[3];
            String dbUser = args[4];
            String dbPwd = args[5];
            SendMail.getInstance().setAdminEmail(adminEmail);
            SendMail.getInstance().sendMail("Welcome to Drawers Bot", "Your bot is up and running now.");
            client = new SqlBotCaller(clientId, password);
            client.clientId = clientId;
            client.sqlHelper = new SqlHelper(dbUrl, dbUser, dbPwd);
            client.startBot();
        }
    }

    private void startBot() {
        mqttProviderManager.addMessageListener(new SqlMessageListener(bot, sqlHelper, clientId));
        bot.start();
        try {
            bot.getExecutorService().awaitTermination(100000000L, TimeUnit.SECONDS);
        } catch (InterruptedException var2) {
            var2.printStackTrace();
        }

    }

    public void onConnected() {
        bot.subscribe(new SubscribeOthers(SubscribeOthers.OTHERS_NAMESPACE, clientId).getChannel(), 1, null, null);
    }

    public MqttChatMessage processMessageAndReply(MqttChatMessage message) {
        System.out.println("Received new message: " + message);
        return message;
    }
}
