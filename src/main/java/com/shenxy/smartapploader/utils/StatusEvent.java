package com.shenxy.smartapploader.utils;

/**
 * Created by David on 14-9-10.
 */
public class StatusEvent {

    public String eventName;
    public Integer id;
    public Object params;

    public StatusEvent(String name, int id, Object params) {
        this.eventName = name;
        this.id = id;
        this.params = params;
    }

    public StatusEvent(String name) {
        this.eventName = name;
        this.id = null;
        this.params = null;
    }

    public StatusEvent(String name, int id) {
        this.eventName = name;
        this.id = id;
        this.params = null;
    }


    public Integer getEventId() {
        return id;
    }

    public void setEventId(int eventId) {
        this.id = eventId;
    }

    public void setParams(Object params){
        this.params = params;
    }

    public Object getParams() {
        return params;
    }

    public String getEventName() {
        return eventName;
    }

}
