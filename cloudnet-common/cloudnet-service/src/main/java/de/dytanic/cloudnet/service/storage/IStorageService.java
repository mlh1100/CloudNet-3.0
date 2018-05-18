package de.dytanic.cloudnet.service.storage;

import de.dytanic.cloudnet.registry.IRemoteableRegistryService;

import java.nio.file.Path;
import java.util.Collection;

public interface IStorageService extends IRemoteableRegistryService {

    Collection<String> getTemplates(String region);

    Collection<String> getServiceDirectories();

    void clearCaches();

    /*= ---------------------------------------------------------------------------------------- =*/

    boolean hasTemplate(String region, String name);

    byte[] copyTemplate(String region, String name) throws Exception;

    boolean copyTemplate(String region, String name, Path destinationDirectory) throws Exception;

    boolean deployTemplate(String region, String name, byte[] data) throws Exception;

    boolean deployTemplate(String region, String name, Path inputDirectory) throws Exception;

    boolean deleteTemplate(String region, String name) throws Exception;

    /*= ---------------------------------------------------------------------------------------- =*/

    boolean hasServiceDirectory(String name);

    boolean copyServiceDirectory(String name, Path destinationDirectory) throws Exception;

    byte[] copyServiceDirectory(String name) throws Exception;

    boolean deployServiceDirectory(String name, byte[] data) throws Exception;

    boolean deployServiceDirectory(String name, Path inputDirectory) throws Exception;

    boolean deleteServiceDirectory(String name) throws Exception;

    /*= ---------------------------------------------------------------------------------------- =*/

}