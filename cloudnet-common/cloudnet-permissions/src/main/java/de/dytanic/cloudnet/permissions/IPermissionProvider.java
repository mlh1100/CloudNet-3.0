package de.dytanic.cloudnet.permissions;

import de.dytanic.cloudnet.$;
import de.dytanic.cloudnet.document.Document;
import de.dytanic.cloudnet.permissions.util.AuthEntry;
import de.dytanic.cloudnet.permissions.util.PermissionEntry;
import de.dytanic.cloudnet.permissions.util.PermissionGroupEntry;
import de.dytanic.cloudnet.registry.IRemoteableRegistryService;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.Collection;
import java.util.function.Predicate;

public interface IPermissionProvider extends IRemoteableRegistryService {

    PermissionUser createUser(String name, String password, Iterable<PermissionGroupEntry> groups, PermissionEntry... permissionEntries);

    boolean updateUser(PermissionUser permissionUser);

    PermissionUser deleteUser(String name);

    PermissionUser getUser(String name);

    boolean containsUser(String name);

    Collection<PermissionUser> getUsers(String group);

    Collection<PermissionUser> getUsers();

    /*= ------------------------------------------------------- =*/

    PermissionGroup createGroup(String name, PermissionEntry... permissionEntries);

    boolean updateGroup(PermissionGroup group);

    PermissionGroup deleteGroup(String name);

    boolean containsGroup(String name);

    Collection<PermissionGroup> getGroups();

    PermissionGroup getGroup(String name);

    Collection<PermissionGroup> getGroups(String environment, String region, String permission);

    /*= -------------------------------------------------------- =*/

    default String hashPassword(String password)
    {
        try
        {

            MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");
            messageDigest.update(password.getBytes(StandardCharsets.UTF_8));

            return new String(messageDigest.digest(), StandardCharsets.UTF_8);

        } catch (Exception ex)
        {

        }

        return null;
    }

    default boolean auth(String name, String password)
    {
        if (name == null || password == null) return false;

        PermissionUser permissionUser = getUser(name);

        if (permissionUser == null) return false;

        return permissionUser.getHashedPassword().equals(hashPassword(password));
    }

    default PermissionUser createUser(String name, String password, Iterable<PermissionGroupEntry> groups)
    {
        return createUser(name, password, groups, new PermissionEntry[0]);
    }

    default PermissionGroup createGroup(String name)
    {
        return createGroup(name, new PermissionEntry[0]);
    }

    default boolean auth(AuthEntry entry)
    {
        if (entry == null) return false;

        return auth(entry.getUserName(), entry.getPassword());
    }

    default boolean hasPermission(PermissionUser permissionUser, String permission)
    {
        return hasPermission(permissionUser, permission, 0);
    }

    default boolean hasPermission(PermissionUser permissionUser, String permission, int accessLevel)
    {
        return hasPermission(permissionUser, null, null, permission, accessLevel);
    }

    default boolean hasPermission(PermissionUser permissionUser, String environment, String region, String permission, int accessLevel)
    {
        if (permissionUser == null || permission == null) return false;

        PermissionEntry permissionEntry = $.filterFirst(permissionUser.getPermissions(), new Predicate<PermissionEntry>() {
            @Override
            public boolean test(PermissionEntry permissionEntry)
            {
                return permissionEntry.getPermission().equalsIgnoreCase(permission) && permissionEntry.inRegion(environment, region);
            }
        });

        if (permissionEntry == null)
            for (PermissionGroupEntry permissionGroupEntry : permissionUser.getGroups())
            {
                if (permissionGroupEntry == null) continue;

                PermissionGroup permissionGroup = getGroup(permissionGroupEntry.getName());

                PermissionEntry groupPermissionEntry = $.filterFirst(permissionGroup.getPermissions(), new Predicate<PermissionEntry>() {
                    @Override
                    public boolean test(PermissionEntry permissionEntry)
                    {
                        return permissionEntry.getPermission().equalsIgnoreCase(permission) && permissionEntry.inRegion(environment, region);
                    }
                });

                if (groupPermissionEntry != null)
                    return groupPermissionEntry.isAccess() && groupPermissionEntry.getAccessLevel() >= accessLevel;
            }
        else return permissionEntry.isAccess() && permissionEntry.getAccessLevel() >= accessLevel;

        return false;
    }

    default Document toDocument()
    {
        return new Document("users", this.getUsers()).append("groups", this.getGroups());
    }

}