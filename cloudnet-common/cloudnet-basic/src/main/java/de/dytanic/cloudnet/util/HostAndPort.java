package de.dytanic.cloudnet.util;

import lombok.*;

@Data
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class HostAndPort {

    private String host;

    private int port;
}