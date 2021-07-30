package com.wjw.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.util.ObjectUtils;

public enum RoleTemplate {
    EB("role_eb", "EB Admin"), SUPPORT("role_support", "EB Support"), ACCOUNT_ADMIN("role_account_admin",
            "Account Admin"), ORGANIZATION_ADMIN("role_org_admin", "Organization Admin"), GROUP_LEADER("role_group_leader",
            "Group Manager"), MASS_NOTIFICATION_OPERATOR("role_mn_operator", "Mass Notification Operator"), INCIDENT_OPERATOR(
            "role_incident_operator", "Incident Operator"), DISPATCHER("role_dispatcher", "Dispatcher"), DISPATCHER_IPAWS_CMAS(
            "role_dispatcher_ipaws_cmas", "Dispatcher IPAWS/CMAS"), DATA_MANAGER("role_data_manager",
            "Data Manager"), INCIDENT_ADMIN("role_incident_admin", "Incident Administrator"), CUSTOM("role_custom",
            "Custom Role");

    private String identity;
    private String name;
    public static final Map<RoleTemplate, Integer> priorityMap = new HashMap<>();
    public final static List<RoleTemplate> ALLOWABLE_ROLE_TEMPLATES =new ArrayList<>(2);

    static {
        ALLOWABLE_ROLE_TEMPLATES.add(RoleTemplate.INCIDENT_ADMIN);
        ALLOWABLE_ROLE_TEMPLATES.add(RoleTemplate.INCIDENT_OPERATOR);
    }

    static {
        priorityMap.put(EB, 4);
        priorityMap.put(SUPPORT, 3);
        priorityMap.put(ACCOUNT_ADMIN, 2);
        priorityMap.put(ORGANIZATION_ADMIN, 1);
        for (RoleTemplate roleTemplate : RoleTemplate.values()) {
            if (!priorityMap.keySet().contains(roleTemplate)) {
                priorityMap.put(roleTemplate, 0);
            }
        }
    }

    RoleTemplate(String identity, String name) {
        this.identity = identity;
        this.name = name;
    }

    public String getIdentity() {
        return this.identity;
    }

    public String getName() {
        return this.name;
    }


    public static RoleTemplate convertRoleTemplate(String input) {
        if (ObjectUtils.isEmpty(input)) {
            return null;
        }
        RoleTemplate roleTemplate = null;
        for (RoleTemplate rt : RoleTemplate.values()) {
            if (rt.toString().equalsIgnoreCase(input)) {
                roleTemplate = rt;
                break;
            }
        }
        return roleTemplate;
    }

    public static boolean isHighAuthority(Role role) {
        if (role == null || role.getRoleTemplate() == null) {
            return false;
        }
        return priorityMap.get(role.getRoleTemplate()) >= priorityMap.get(ORGANIZATION_ADMIN);
    }
}
