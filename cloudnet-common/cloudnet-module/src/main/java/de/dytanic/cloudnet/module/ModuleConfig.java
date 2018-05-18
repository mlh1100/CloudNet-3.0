package de.dytanic.cloudnet.module;

import com.google.gson.annotations.SerializedName;
import com.google.gson.reflect.TypeToken;
import de.dytanic.cloudnet.document.Document;
import de.dytanic.cloudnet.module.util.ModuleLibrary;
import de.dytanic.cloudnet.module.util.ModuleRepositoryConfig;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.lang.reflect.Type;

@Getter
@AllArgsConstructor
public final class ModuleConfig {

    public static final Type TYPE = new TypeToken<ModuleConfig>(){}.getType();

    private final String name, version, author, main, description, website;

    private final boolean ignoreDependencies, ignoreReload;

    @SerializedName("dependencies")
    private final String[] moduleDependencies;

    @SerializedName("libraries")
    private final ModuleLibrary[] moduleLibraries;

    private final ModuleRepositoryConfig repository;

    private final Document properties;

}