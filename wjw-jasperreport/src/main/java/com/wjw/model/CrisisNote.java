package com.wjw.model;

import java.util.Date;

import lombok.Data;

@Data
public class CrisisNote {
    private String id;
    private String scopeId;
    private String noteContent;
    private Date createdDate;
    private String createdName;
}
