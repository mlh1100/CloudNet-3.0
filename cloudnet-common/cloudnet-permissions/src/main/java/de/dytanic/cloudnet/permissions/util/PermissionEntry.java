package de.dytanic.cloudnet.permissions.util;

import de.dytanic.cloudnet.document.BasicDocPropertyable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode
@AllArgsConstructor
public class PermissionEntry extends BasicDocPropertyable {

    private String environment, region, permission;

    private boolean access;

    private int accessLevel;

    public boolean isGlobalPermission()
    {
        return environment == null;
    }

    public boolean inRegion(String environment, String region)
    {
        if (environment == null && isGlobalPermission()) return true;

        if (environment != null && this.environment != null && environment.equalsIgnoreCase(this.environment))
        {
            if (this.region == null && region == null) return true;

            if (this.region != null && region != null && region.equalsIgnoreCase(this.region)) return true;
        }

        return false;
    }

    public static PermissionEntry newPermissionEntry(String permission, boolean access, int accessLevel)
    {
        return new PermissionEntry(null, null, permission, access, accessLevel);
    }

    public static PermissionEntry newPermissionEntry(String environment, String region, String permission, boolean access, int accessLevel)
    {
        return new PermissionEntry(environment, region, permission, access, accessLevel);
    }

}