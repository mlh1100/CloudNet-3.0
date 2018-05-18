package de.dytanic.cloudnet.permissions;

import de.dytanic.cloudnet.document.BasicDocPropertyable;
import de.dytanic.cloudnet.permissions.util.PermissionEntry;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.Collection;

@Data
@NoArgsConstructor
@EqualsAndHashCode
@AllArgsConstructor
public class PermissionGroup extends BasicDocPropertyable {

    private String name;

    private Collection<PermissionEntry> permissions;

}