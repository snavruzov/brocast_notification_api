package com.brocast.api.notification.beans;

import com.dgtz.mcache.api.utils.GsonInsta;
import com.google.gson.Gson;

import java.io.Serializable;

/**
 * Created by Sardor Navruzov on 8/17/15.
 * Copyrights BroCast Co.
 */
public class EmailRecipients implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long idUser;
    private String email;

    public EmailRecipients() {
    }

    public Long getIdUser() {
        return idUser;
    }

    public void setIdUser(Long idUser) {
        this.idUser = idUser;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public String toString() {
        Gson gson = GsonInsta.getInstance();
        return gson.toJson(this);

    }
}
