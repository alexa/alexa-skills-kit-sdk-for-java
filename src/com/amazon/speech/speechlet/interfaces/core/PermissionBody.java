package com.amazon.speech.speechlet.interfaces.core;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class PermissionBody {
    private final List<Permission> permissionList;

    public  static PermissionBody.Builder builder() {
        return new PermissionBody.Builder();
    }

    private PermissionBody(final Builder builder) {
        permissionList = Collections.unmodifiableList(builder.permissionList);
    }

    private PermissionBody(@JsonProperty("acceptedPermissions") final List<Permission> permissionList) {
        if(permissionList != null) {
            this.permissionList = Collections.unmodifiableList(permissionList);
        } else {
            this.permissionList = Collections.emptyList();
        }
    }

    public List<Permission> getAcceptedPermissions() {
        return permissionList;
    }

    public static final class Builder {
        private List<Permission> permissionList = new ArrayList<>();

        public Builder withAcceptPermissions(final List<Permission> permissionList) {
            this.permissionList.addAll(permissionList);
            return this;
        }

        public PermissionBody build() {
            return new PermissionBody(this);
        }
    }
}
