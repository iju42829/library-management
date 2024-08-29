package com.example.library.library_management.auth.constants;

public enum Role {
    USER, ADMIN;

    public String getRoleName() {
        return "ROLE_" + this.name();
    }

    public String getSimpleName() {
        return this.name();
    }

    public static Role fromString(String roleName) {
        return Role.valueOf(roleName.substring(5));
    }
}
