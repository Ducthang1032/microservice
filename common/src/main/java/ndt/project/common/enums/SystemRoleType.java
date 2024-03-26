package ndt.project.common.enums;

import lombok.Getter;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Getter
public enum SystemRoleType {
    ROOT("ROOT"),
    ADMIN("ADMIN"),
    COLLABORATOR("COLLABORATOR"),
    USER("USER");

    private final String roleName;

    SystemRoleType(String roleName) {
        this.roleName = roleName;
    }

    public static List<SystemRoleType> adminRole() {
        return Arrays.asList(ROOT, ADMIN, COLLABORATOR);
    }

    public static List<String> adminRoleName() {
        return adminRole().stream().map(SystemRoleType::getRoleName).collect(Collectors.toList());
    }

    public static Boolean isUserRole(String value) {
        return USER.getRoleName().equalsIgnoreCase(value);
    }

    public static Boolean isNotUserRole(String value) {
        return !isUserRole(value);
    }

    public static boolean isRootAccount(String value) {
        return ROOT.getRoleName().equalsIgnoreCase(value);
    }

    public static boolean isNotRootAccount(String value) {
        return !isRootAccount(value);
    }
}