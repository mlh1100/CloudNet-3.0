package de.dytanic.cloudnet.permissions.util;

import de.dytanic.cloudnet.document.BasicDocPropertyable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode
@AllArgsConstructor
public final class PermissionGroupEntry extends BasicDocPropertyable {

    private String environment, region, name;

    private long timeOut;

    public boolean isGlobalGroup()
    {
        return environment == null;
    }

    public boolean inRegion(String environment, String region)
    {
        if (environment == null && isGlobalGroup()) return true;

        if (environment != null && this.environment != null && environment.equalsIgnoreCase(this.environment))
        {
            if (this.region == null && region == null) return true;

            if (this.region != null && region != null && region.equalsIgnoreCase(this.region)) return true;
        }

        return false;
    }

}