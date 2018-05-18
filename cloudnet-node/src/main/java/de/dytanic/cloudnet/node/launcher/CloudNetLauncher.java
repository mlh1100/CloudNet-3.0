package de.dytanic.cloudnet.node.launcher;

public final class CloudNetLauncher {

    private CloudNetLauncher() {}

    public static synchronized void main(String[] args) throws Throwable
    {
        if(System.getProperty("user.name").equals("root"))
        {
            System.out.println("Your using CloudNet start with ROOT user!");
        }

        if(Float.parseFloat(System.getProperty("java.class.version")) < 52D)
        {
            System.out.println("Your Java version is invalid! You must have Java 8 before you can use this software");
            return;
        }

        System.setProperty("file.encoding", "UTF-8");
        System.setProperty("java.net.preferIPv4Stack", "true");
        System.setProperty("io.netty.noPreferDirect", "true");
        System.setProperty("client.encoding.override", "UTF-8");
        System.setProperty("io.netty.maxDirectMemory", "0");
        System.setProperty("io.netty.leakDetectionLevel", "DISABLED");
        System.setProperty("io.netty.recycler.maxCapacity", "0");
        System.setProperty("io.netty.recycler.maxCapacity.default", "0");

        CloudNetBootstrap.main(args);
    }

}