package com.wjw.model;

import java.util.List;

import lombok.Data;

@Data
public class CrisisEvent {
    private String id;
    private String title;
    private String eventType;
    private List<CrisisNote> notes;
    private List<CrisisTaskList> taskLists;
    
}
