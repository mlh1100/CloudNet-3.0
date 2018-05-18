package de.dytanic.cloudnet.webserver;

import de.dytanic.cloudnet.util.HostAndPort;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
@AllArgsConstructor
public class WebConfig {

    private boolean ssl;

    private HostAndPort bindAddress;

}