package de.dytanic.cloudnet.test;

import de.dytanic.cloudnet.permissions.IPermissionProvider;
import de.dytanic.cloudnet.permissions.PermissionGroup;
import de.dytanic.cloudnet.permissions.PermissionUser;
import de.dytanic.cloudnet.permissions.impl.FilePermissionProvider;
import de.dytanic.cloudnet.permissions.util.PermissionEntry;
import de.dytanic.cloudnet.permissions.util.PermissionGroupEntry;
import org.junit.Assert;
import org.junit.Test;

import java.io.File;
import java.util.Arrays;

public final class PermissionProviderTest {

    @Test
    public void testPermissionProvider()
    {
        String name = "test", group = "admin", password = "123456", testPermission = "test.test", userPermission = "test.post2";

        IPermissionProvider permissionProvider = new FilePermissionProvider("target/permissions.json");
        Assert.assertTrue(new File("target/permissions.json").exists());

        PermissionUser permissionUser = permissionProvider
                .createUser(
                        name,
                        password,
                        Arrays.asList(new PermissionGroupEntry(null, null, group, 0L)),
                        new PermissionEntry("myenv", "myreg", userPermission, true, 50)
                );

        Assert.assertTrue(permissionUser != null);
        Assert.assertTrue(permissionProvider.containsUser(permissionUser.getName()));
        Assert.assertTrue(permissionProvider.getUsers().size() == 1);
        Assert.assertTrue(permissionProvider.getUser(name) != null);

        Assert.assertTrue(permissionProvider.hasPermission(permissionUser, "myenv", "myreg", userPermission, 20));
        Assert.assertFalse(permissionProvider.hasPermission(permissionUser, "myenv", "myreg", userPermission, 70));

        //Test Auth
        Assert.assertTrue(permissionProvider.auth(name, password));

        PermissionGroup permissionGroup = permissionProvider.createGroup(
                group,
                new PermissionEntry(null, null, testPermission, true, 100)
        );

        Assert.assertTrue(permissionGroup != null);
        Assert.assertTrue(permissionProvider.containsGroup(permissionGroup.getName()));
        Assert.assertTrue(permissionProvider.getGroups().size() == 1);
        Assert.assertTrue(permissionProvider.getGroups(null, null, testPermission).size() == 1);

        Assert.assertTrue(permissionProvider.hasPermission(permissionUser, testPermission, 1));
        Assert.assertFalse(permissionProvider.hasPermission(permissionUser, testPermission, 101));

        /*= ---------------------------------------------------------------- =*/

        permissionUser.getProperties().append("testProperty", 32);
        permissionProvider.updateUser(permissionUser);

        Assert.assertTrue(permissionProvider.getUser(permissionUser.getName()) != null);
        Assert.assertTrue(permissionProvider.getUser(permissionUser.getName()).getProperties().getInt("testProperty") == 32);

        permissionGroup.getProperties().append("testProperty", 32);
        permissionProvider.updateGroup(permissionGroup);

        Assert.assertTrue(permissionProvider.getGroup(permissionGroup.getName()) != null);
        Assert.assertTrue(permissionProvider.getGroup(permissionGroup.getName()).getProperties().getInt("testProperty") == 32);

        /*= ---------------------------------------------------------------- =*/

        permissionProvider.deleteUser(permissionUser.getName());
        Assert.assertTrue(permissionProvider.getUsers().size() == 0);
        Assert.assertTrue(permissionProvider.getUser(name) == null);

        permissionProvider.deleteGroup(permissionGroup.getName());
        Assert.assertTrue(permissionProvider.getGroups().size() == 0);
        Assert.assertTrue(permissionProvider.getGroup(name) == null);
    }

}