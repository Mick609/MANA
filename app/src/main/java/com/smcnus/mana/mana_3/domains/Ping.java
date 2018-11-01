package com.smcnus.mana.mana_3.domains;

public class Ping {
    private boolean isSentBack;
    private long timestamp;

    public Ping(long timestamp) {
        this.timestamp = timestamp;
        this.isSentBack = false;
    }

    public boolean isSentBack() {
        return isSentBack;
    }

    public void setSentBack(boolean sentBack) {
        isSentBack = sentBack;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }
}
