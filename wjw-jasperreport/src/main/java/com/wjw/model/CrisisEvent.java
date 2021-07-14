package com.wjw.model;

import java.util.List;

import lombok.Data;

@Data
public class CrisisEvent implements Comparable<CrisisEvent> {
    private String id;
    private String title;
    private String description;
    private String eventStatus;
    private String eventType;
    private List<CrisisNote> notes;
    private List<CrisisTaskList> taskLists;
    private long createdDate;
    @Override
    public int compareTo(CrisisEvent e) {
        return this.eventType.compareTo(e.getEventType());
    }
    
}
