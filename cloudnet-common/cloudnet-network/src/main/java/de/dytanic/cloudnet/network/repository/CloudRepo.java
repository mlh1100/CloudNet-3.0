package de.dytanic.cloudnet.network.repository;

import de.dytanic.cloudnet.document.BasicDocPropertyable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
@AllArgsConstructor
public class CloudRepo extends BasicDocPropertyable {

    protected String url, authKey;

    protected CloudRepoTrustLevel trustLevel;

    protected boolean defaultRepo, autoUpdate, requireConnection;

}