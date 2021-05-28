package com.gnosyz.deltahuey;

import java.io.Serializable;
import java.sql.Timestamp;

public class Log implements Serializable {
    private String id;
    private String cat;
    private String msg;
    private Timestamp created;
    public Log(String id, String cat, String msg, Timestamp created) {
        this.id = id;
        this.cat = cat;
        this.msg = msg;
        this.created = created;
    }
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCat() {
        return cat;
    }

    public void setCat(String cat) {
        this.cat = cat;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Timestamp getCreated() {
        return created;
    }

    public void setCreated(Timestamp created) {
        this.created = created;
    }
}
