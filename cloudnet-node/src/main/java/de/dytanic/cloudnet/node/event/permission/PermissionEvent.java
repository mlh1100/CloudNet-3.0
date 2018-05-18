package de.dytanic.cloudnet.node.event.permission;

import de.dytanic.cloudnet.event.Event;
import de.dytanic.cloudnet.permissions.IPermissionProvider;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
abstract class PermissionEvent extends Event {

    private IPermissionProvider permissionProvider;

}