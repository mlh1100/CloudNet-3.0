package de.dytanic.cloudnet.service.process;

import de.dytanic.cloudnet.document.Document;
import de.dytanic.cloudnet.service.util.IncludedTemplate;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Collection;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public abstract class AbstractServiceProcessMeta {

    protected String commandLine, name;

    protected int limitedHeapMemory, port;

    protected Collection<IncludedTemplate> templates;

    protected Collection<String> groups, servicesDirectories;

    protected Document properties;

}