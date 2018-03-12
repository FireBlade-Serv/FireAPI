package fr.glowstoner.fireapi.bigbrother.console.login;

import fr.glowstoner.fireapi.player.enums.VersionType;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class BigBrotherConnectionInfos {
	
	private String id, key, password;
	private VersionType versionType;

}
