package com.wjw.model;

import java.io.Serializable;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Role implements Serializable {
    private static final long serialVersionUID = 4242416851434920374L;
    private long id;
    private String name;
    private long organizationId;
    private String identity;
    private RoleTemplate roleTemplate;

    public Role(long id, String name, RoleTemplate roleTemplate) {
        this.id = id;
        this.name = name;
        this.roleTemplate = roleTemplate;
    }

}
