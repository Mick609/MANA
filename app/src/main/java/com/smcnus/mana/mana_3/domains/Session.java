package com.smcnus.mana.mana_3.domains;

import java.util.Date;

public class Session {
    private long timestamp;
    private Participant participant;

    public Session() {
        Date date = new Date();
        timestamp = date.getTime();
    }

    public Session(Participant participant) {
        this.participant = participant;

        Date date = new Date();
        timestamp = date.getTime();
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public Participant getParticipant() {
        return participant;
    }

    public void setParticipant(Participant participant) {
        this.participant = participant;
    }
}
