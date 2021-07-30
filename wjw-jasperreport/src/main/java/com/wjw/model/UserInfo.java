package com.wjw.model;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import lombok.Data;

/**
 * Created by Mia on 2018/8/28.
 */
@Data
public class UserInfo implements Serializable {
    private static final long serialVersionUID = -8905822174874541568L;
    
    private Long id;
    private Long organizationId;
    private String userName;
    private String type;
    private String firstName;
    private String lastName;
    private String timeZoneId;
    private String userStatus;
    private String fullName;
    private String email;
    private Long roleId;
    private List<Long> groupIds;
    private List<Long> contactIds;
    private List<Long> ruleIds;
    private String uuid;//if mobile user,may set the value.
    private List<String> filterFolderIds;// if folder Ids are null,means not use this folder to control access.
    private List<String> accessFolderIds;// if folder Ids are null,means not use this folder to control access.
    private List<String> contactGroupRuleFilters;//this value contain the current contact id,contact belong to groupId and ruleIds,such as the current
    private List<String> authorizedUserRoleFilters;// this value contains the current user id and role id
    private Role role;
    private String samlIdentiry;
    private Map<String, String> additionalInfo;

    public UserInfo() {
    }

    public UserInfo(Long id, String userName) {
        this.id = id;
        this.userName = userName;
    }

    public UserInfo(Long id, String userName, String fullName, String type, String email) {
        this.id = id;
        this.userName = userName;
        this.fullName = fullName;
        this.type = type;
        this.email = email;
    }

    public UserInfo(Long id, String userName, String fullName, List<Long> groupIds, List<Long> ruleIds) {
        this.id = id;
        this.userName = userName;
        this.fullName = fullName;
        this.groupIds = groupIds;
        this.ruleIds = ruleIds;
    }

}
