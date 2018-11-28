package com.brocast.api.notification.enums;

/**
 * Created with IntelliJ IDEA.
 * User: root
 * Date: 12/15/13
 * Time: 5:31 PM
 * To change this template use File | Settings | File Templates.
 */
public enum EnumErrors {

    NO_ERRORS(0x01),
    ERROR_IN_COMPRESSING(0x02),
    ERROR_IN_IMG_CAPTURING(0x03),
    UNKNOWN_ERROR(0x04),
    MAIL_MATCH_ERR(0x05),
    MKDIR_ERR(0x06),
    PUBLISH_ERR(0x07),
    MAIL_SEND_ERR(0x08),
    RATE_ERROR(0x09),
    SOCIAL_AUTH_ERROR(0x0A),
    REPORT_ERROR(0x0B),
    CHANNEL_REMOVE_ERROR(0x0C),
    MEDIA_REMOVE_ERROR(0x0D),
    ALREADY_JOINED(0x0E),
    FILE_FORMAT_ERROR(0x0F),
    BIG_OR_SMALL_DURATION_ERROR(0x10),
    SMALL_DURATION_ERROR(0x11),
    NULL_FIELD_ERROR(0x12),
    BYTE_CONVERT_ERROR(0x13),
    NO_SUCH_MAIL(0x14);

    public int value;

    EnumErrors(int value) {
        this.value = value;
    }
}
