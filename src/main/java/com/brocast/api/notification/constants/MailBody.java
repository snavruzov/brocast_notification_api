package com.brocast.api.notification.constants;


import com.brocast.api.notification.beans.MailInfo;

/**
 * BroCast.
 * User: Sardor Navuzov
 * Date: 2/26/14
 */
public class MailBody {

    public MailBody() {
    }

    public static String getMailActivationBody(String activationNum) {
        return "<h1>Welcome to BroCast</h1><p>Please activate your account for further registration!</p>" +
                "<p><a href=\"https://api.brocast.com/web/api/body/activate?ref=JfyGll5Y34Q0&ei=4p_KUp3rCoL9sQal0YGQDg&ved=0CBIQ1S4&red=" + activationNum + "\">" +
                "CLICK HERE to activate your account.</a>";

    }


    public static String getMailRestorePassBody(String username, String hash) {
        return "<h1>Welcome to BroCast</h1><p>Forgot your password?\n" +
                "\n" +
                "Don't worry about it, you can get a new one within a minute.\n" +
                " \n" +
                "Reset your password(" + hash + ")\n" +
                "\n" +
                "Your username is " + username + "\t\n" +
                "If you didn't request to reset your password, please disregard this email.\n</p>" +
                "<a href='https://api.brocast.com/web/api/body/confirm/restore?uid=" + hash + "'>LINK</a>";

    }

    public static String getMailNotificationBody(MailInfo info) {

        String body = "";
        switch (info.getType()) {
            case 1: {
                body = "User " + info.getInfo0() + " wants to JOIN your channel " + info.getInfo1() + " " +
                        "Please check your feed list to make decision.";
                break;
            }
            case 2: {
                body = "User " + info.getInfo0() + " just has added new content " + info.getInfo1() + " " +
                        "Please check your feed list to browse it.";
                break;
            }
            case 3: {
                body = "User " + info.getInfo0() + " wants you JOIN a channel " + info.getInfo1() + " " +
                        "Please check your feed list to make decision.";
                break;
            }
            case 6: {
                body = "User " + info.getInfo0() + " just has started live streaming " + info.getInfo1() + " " +
                        "Click to Watch Live to browse the live.(Sorry we don`t have a web page yet!)";
                break;
            }
            case 4: {
                body = "User/s " + info.getInfo0() + " liked your content " + info.getInfo1() + " " +
                        "Please check your feed list to browse it.";
                break;
            }
            case 5: {
                body = "User/s " + info.getInfo0() + " left comments on your content wall " + info.getInfo1() + " " +
                        "Please check your feed list to make decision.";
                break;
            }
            case 7: {
                body = "Channel " + info.getInfo1() + " has been removed, now your contents are out of the channel";
                break;
            }
            case 8: {
                body = "You are successfully JOINED to channel " + info.getInfo1();
                break;
            }
        }
        return "<h1>Welcome to BroCast</h1><p>" + body + "</p>";

    }


}
