package com.wjw.model;

import lombok.Data;

@Data
public class CrisisNote {
    private String id;
    private String scopeId;
    private String noteContent;
    private long createdDate;
    private String createdName;
}
