package de.dytanic.cloudnet.permissions.impl;

import com.google.gson.reflect.TypeToken;
import de.dytanic.cloudnet.$;
import de.dytanic.cloudnet.document.Document;
import de.dytanic.cloudnet.permissions.IPermissionProvider;
import de.dytanic.cloudnet.permissions.PermissionGroup;
import de.dytanic.cloudnet.permissions.PermissionUser;
import de.dytanic.cloudnet.permissions.util.PermissionEntry;
import de.dytanic.cloudnet.permissions.util.PermissionGroupEntry;
import lombok.Getter;

import java.io.File;
import java.util.Arrays;
import java.util.Collection;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Predicate;

public final class FilePermissionProvider implements IPermissionProvider {

    @Getter
    private final String serviceName = "file";

    private final String property_document_users = "users", property_document_groups = "groups";

    private final File file;

    private final Map<String, PermissionUser> users = $.newConcurrentHashMap();

    private final Map<String, PermissionGroup> groups = $.newConcurrentHashMap();

    public FilePermissionProvider(String filePath)
    {
        this.file = new File(filePath);
        this.file.getParentFile().mkdirs();

        if (!file.exists()) save();

        loadConfig();
    }


    @Override
    public PermissionUser createUser(String name, String password, Iterable<PermissionGroupEntry> groups, PermissionEntry... permissionEntries)
    {
        if (name == null || password == null || groups == null || permissionEntries == null) return null;

        deleteUser(name.toLowerCase());

        PermissionUser permissionUser = new PermissionUser(
                name.toLowerCase(),
                hashPassword(password),
                Arrays.asList(permissionEntries),
                $.newArrayList(groups)
        );

        this.users.put(name.toLowerCase(), permissionUser);
        save();

        return permissionUser;
    }

    @Override
    public boolean updateUser(PermissionUser permissionUser)
    {
        if (!this.users.containsKey(permissionUser.getName().toLowerCase())) return false;

        this.users.replace(permissionUser.getName().toLowerCase(), permissionUser);
        save();

        return true;
    }

    @Override
    public PermissionUser deleteUser(String name)
    {
        if (name == null) return null;

        PermissionUser permissionUser = this.users.remove(name.toLowerCase());
        save();

        return permissionUser;
    }

    @Override
    public PermissionUser getUser(String name)
    {
        return name == null ? null : this.users.get(name.toLowerCase());
    }

    @Override
    public boolean containsUser(String name)
    {
        return name != null && this.users.containsKey(name.toLowerCase());
    }

    @Override
    public Collection<PermissionUser> getUsers(String group)
    {
        return $.filter(this.users.values(), new Predicate<PermissionUser>() {
            @Override
            public boolean test(PermissionUser permissionUser)
            {
                return permissionUser != null && $.filterFirst(permissionUser.getGroups(), new Predicate<PermissionGroupEntry>() {
                    @Override
                    public boolean test(PermissionGroupEntry permissionGroupEntry)
                    {
                        return permissionGroupEntry.isGlobalGroup() && permissionGroupEntry.getName().equalsIgnoreCase(group);
                    }
                }) != null;
            }
        });
    }

    @Override
    public Collection<PermissionUser> getUsers()
    {
        return $.newArrayList(this.users.values());
    }

    @Override
    public PermissionGroup createGroup(String name, PermissionEntry... permissionEntries)
    {
        if (name == null || permissionEntries == null) return null;

        deleteGroup(name.toLowerCase());

        PermissionGroup permissionGroup = new PermissionGroup(name.toLowerCase(), Arrays.asList(permissionEntries));
        this.groups.put(permissionGroup.getName(), permissionGroup);

        save();

        return permissionGroup;
    }

    @Override
    public boolean updateGroup(PermissionGroup group)
    {
        if (!this.groups.containsKey(group.getName().toLowerCase())) return false;

        this.groups.replace(group.getName().toLowerCase(), group);
        save();

        return true;
    }

    @Override
    public PermissionGroup deleteGroup(String name)
    {
        if (name == null) return null;

        PermissionGroup permissionGroup = this.groups.remove(name.toLowerCase());
        save();

        return permissionGroup;
    }

    @Override
    public boolean containsGroup(String name)
    {
        return name != null && this.groups.containsKey(name.toLowerCase());
    }

    @Override
    public Collection<PermissionGroup> getGroups()
    {
        return $.newArrayList(this.groups.values());
    }

    @Override
    public PermissionGroup getGroup(String name)
    {
        return name == null ? null : this.groups.get(name.toLowerCase());
    }

    @Override
    public Collection<PermissionGroup> getGroups(String environment, String region, String permission)
    {
        if (permission == null) return $.newArrayList();

        return $.filter(this.groups.values(), new Predicate<PermissionGroup>() {
            @Override
            public boolean test(PermissionGroup permissionGroup)
            {
                return $.filterFirst(permissionGroup.getPermissions(), new Predicate<PermissionEntry>() {
                    @Override
                    public boolean test(PermissionEntry permissionEntry)
                    {
                        return permissionEntry.getPermission().equalsIgnoreCase(permission) && permissionEntry.inRegion(environment, region);
                    }
                }) != null;
            }
        });
    }

    /*= -------------------------------------------------------------------- =*/

    private void save()
    {
        new Document(property_document_users, this.users.values())
                .append(property_document_groups, this.groups.values()).save(file);
    }

    private void loadConfig()
    {
        Document document = Document.newDocument(file);

        $.addAll(this.users, document.getObject(property_document_users, new TypeToken<Collection<PermissionUser>>() {
        }.getType()), new Function<PermissionUser, String>() {
            @Override
            public String apply(PermissionUser permissionUser)
            {
                return permissionUser.getName();
            }
        });

        $.addAll(this.groups, document.getObject(property_document_groups, new TypeToken<Collection<PermissionGroup>>() {
        }.getType()), new Function<PermissionGroup, String>() {
            @Override
            public String apply(PermissionGroup permissionGroup)
            {
                return permissionGroup.getName();
            }
        });
    }

    @Override
    public boolean isRemoteService()
    {
        return false;
    }
}