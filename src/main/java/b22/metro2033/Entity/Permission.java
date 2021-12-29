package b22.metro2033.Entity;

public enum Permission {

    USERS_READ("users:read"),
    USERS_WRITE("users:write"),
    USERS_CREATE("users:create"),
    ARMY_READ("army:read"),
    ARMY_WRITE("army:write"),
    DELIVERY_READ("delivery:read"),
    DELIVERY_WRITE("delivery:write"),
    GUEST_READ("guest:read");

    private final String permission;

    Permission(String permission) {
        this.permission = permission;
    }

    public String getPermission() {
        return permission;
    }
}
