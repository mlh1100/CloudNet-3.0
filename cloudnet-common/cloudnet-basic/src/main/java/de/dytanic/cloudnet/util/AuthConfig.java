package de.dytanic.cloudnet.util;

import de.dytanic.cloudnet.document.BasicDocPropertyable;
import de.dytanic.cloudnet.interfaces.INameable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
@AllArgsConstructor
public class AuthConfig extends BasicDocPropertyable implements INameable {

    private String name;

}