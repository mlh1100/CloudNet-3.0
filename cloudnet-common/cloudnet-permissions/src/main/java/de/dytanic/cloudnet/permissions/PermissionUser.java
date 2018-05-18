package de.dytanic.cloudnet.permissions;

import de.dytanic.cloudnet.document.BasicDocPropertyable;
import de.dytanic.cloudnet.permissions.util.PermissionEntry;
import de.dytanic.cloudnet.permissions.util.PermissionGroupEntry;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Collection;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PermissionUser extends BasicDocPropertyable {

    private String name;

    private String hashedPassword;

    private Collection<PermissionEntry> permissions;

    private Collection<PermissionGroupEntry> groups;

}