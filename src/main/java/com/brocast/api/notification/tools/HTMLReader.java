package com.brocast.api.notification.tools;

import com.brocast.api.notification.beans.Notificator;
import com.dgtz.mcache.api.factory.Constants;
import com.dgtz.mcache.api.factory.RMemoryAPI;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

/**
 * BroCast.
 * User: Sardor Navuzov
 * Date: 6/12/14
 */
public final class HTMLReader {

    private static final org.slf4j.Logger log = LoggerFactory.getLogger(HTMLReader.class);
    private static final String WEB_URL = "https://www.brocast.com/";

    public HTMLReader() {
    }

    public String[] htmlAccount(Notificator info) {
        String[] body = {"", ""};
        String cod = RMemoryAPI.getInstance().pullHashFromMemory(Constants.USER_KEY + info.getToId(), "language");
        cod = cod == null ? "en" : cod;

        String hashnum = info.getActivation();
        log.debug("EMAIL ACCOUNT {}", info.toString());
        switch (info.getType()) {
            case "14": {
                body[0] = "Thank you for your registration." +
                        "<br>" +
                        "To activate your new account, please visit the following URL: " +
                        "\n" +
                        "<a href='https://www.brocast.com/my/profile/activate?ACT=8&id=2F9DCyYrRs&red="+hashnum+"'>" +
                        "activate</a>" +
                        "<br><br>" +
                        "Thank You!" +
                        "<br>" +
                        "The BroCast Team" +
                        "<br>" +
                        "<a href='https://www.brocast.com'>https://www.brocast.com</a>";
                body[1] = "Thank you for your registration. brocast.com";
                break;
            }
            case "15": {
                body[0] = "You've changed your email address<br>" +
                        "To verify the email address and unlock your full account, please visit the following URL:\n" +
                        "<a href='https://api.brocast.com/web/api/body/email/verify?ACT=8&id=2F9DCyYrRs&red="+hashnum+"'>" +
                        "verify</a>" +
                        "<br><br>" +
                        "Thank You!" +
                        "<br>" +
                        "The BroCast Team" +
                        "<br>" +
                        "<a href='https://www.brocast.com'>https://www.brocast.com</a>";
                body[1] = "You've changed your email address. brocast.com";
                break;
            }
            case "13": {
                body[0] = "You've requested to change your account password<br>" +
                        "To confirm the new password, please visit the following URL:\n" +
                        "<a href='https://www.brocast.com/my/password/restore?uid="+hashnum+"'>" +
                        "confirm</a>" +
                        "<br><br>" +
                        "Thank You!" +
                        "<br>" +
                        "The BroCast Team" +
                        "<br>" +
                        "<a href='https://www.brocast.com'>https://www.brocast.com</a>";
                body[1] = "Account password change confirmation. brocast.com";
                break;
            }
            case "11": {
                body[0] = "We have noticed some issues while processing your video! – "+info.title+"\n" +
                        "Please contact brocast support to resole this issue.\n\n" +
                        "<br><br>" +
                        "Thank You!" +
                        "<br>" +
                        "The BroCast Team" +
                        "<br>" +
                        "<a href='https://www.brocast.com'>https://www.brocast.com</a>";
                body[1] = "Video content processing issues. brocast.com";
                break;
            }
        }

        return body;

    }

    private String readHtmlFromFile(String htmlFile) {
        StringBuilder builder = new StringBuilder();
        try (BufferedReader br = new BufferedReader(new FileReader(htmlFile))) {

            String sCurrentLine;

            while ((sCurrentLine = br.readLine()) != null) {
                builder.append(sCurrentLine);
            }

        } catch (IOException e) {
            log.error("ERROR IN MAIL SENDING, READ FILE HTML ", e);
        }


        return builder.toString();
    }

    private String[] defineLanguageTemplate(int type, String cod) {

        String LEVEL_1 = RMemoryAPI.getInstance()
                .pullHashFromMemory(Constants.TRANSLATION + "eml:notification:" + type, "level1" + ":" + cod);
        String LEVEL_2 = RMemoryAPI.getInstance()
                .pullHashFromMemory(Constants.TRANSLATION + "eml:notification:" + type, "level2" + ":" + cod);
        String LEVEL_3 = RMemoryAPI.getInstance()
                .pullHashFromMemory(Constants.TRANSLATION + "eml:notification:" + type, "level3" + ":" + cod);
        String LEVEL_4 = RMemoryAPI.getInstance()
                .pullHashFromMemory(Constants.TRANSLATION + "eml:notification:" + type, "level4" + ":" + cod);
        String LEVEL_5 = RMemoryAPI.getInstance()
                .pullHashFromMemory(Constants.TRANSLATION + "eml:notification:" + type, "level5" + ":" + cod);
        String LEVEL_6 = RMemoryAPI.getInstance()
                .pullHashFromMemory(Constants.TRANSLATION + "eml:notification:" + type, "level6" + ":" + cod);

        return new String[]{LEVEL_1, LEVEL_2, LEVEL_3, LEVEL_4, LEVEL_5, LEVEL_6};
    }
}
