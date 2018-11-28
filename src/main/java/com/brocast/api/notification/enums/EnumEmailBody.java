package com.brocast.api.notification.enums;

/**
 * BroCast.
 * User: Sardor Navuzov
 * Date: 6/12/14
 */
public enum EnumEmailBody {

    PASSWORD_RESTORE(1),
    ACCOUNT_CONFIRM(2),
    EMAIL_CONFIRM(3);

    public int value;

    EnumEmailBody(int value) {
        this.value = value;
    }
}
