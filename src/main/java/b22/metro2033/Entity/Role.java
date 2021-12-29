package b22.metro2033.Entity;

import javassist.NotFoundException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

public enum Role {
    ADMIN(SetOf(Permission.USERS_READ, Permission.USERS_CREATE,
                Permission.USERS_WRITE, Permission.ARMY_READ, Permission.ARMY_WRITE,
                Permission.DELIVERY_READ, Permission.DELIVERY_WRITE)),
    GENERAL(SetOf(Permission.ARMY_READ, Permission.ARMY_WRITE)),
    SOLDIER(SetOf(Permission.ARMY_READ)),
    HEAD_COURIER(SetOf(Permission.DELIVERY_READ, Permission.DELIVERY_WRITE)),
    COURIER(SetOf(Permission.DELIVERY_READ)),
    GUEST(SetOf(Permission.GUEST_READ));

    private final Set<Permission> permissions;

    Role(Set<Permission> permissions) {
        this.permissions = permissions;
    }

    public Set<Permission> getPermissions() {
        return permissions;
    }

    public Set<SimpleGrantedAuthority> getAuthorities() {
        return getPermissions().stream()
                .map(permission -> new SimpleGrantedAuthority(permission.getPermission()))
                .collect(Collectors.toSet());
    }

    //Because Set.of working from Java 9
    public static Set<Permission> SetOf(Permission... permissions) {
        Set<Permission> set = new HashSet<>();
        Collections.addAll(set, permissions);
        return set;
    }

    public static Role findState(String state) throws Exception {
        switch (state) {
            case "ADMIN":
                return ADMIN;
            case "GENERAL":
                return GENERAL;
            case "SOLDIER":
                return SOLDIER;
            case "HEAD_COURIER":
                return HEAD_COURIER;
            case "COURIER":
                return COURIER;
            case "GUEST":
                return GUEST;
        }
        throw new NotFoundException("STATE NOT FOUND");
    }

}