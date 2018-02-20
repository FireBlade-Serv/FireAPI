package fr.glowstoner.fireapi.gediminas.console.login;

import fr.glowstoner.fireapi.player.enums.VersionType;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class GediminasConnectionInfos {
	
	private String id, key, password;
	private VersionType versionType;

}
