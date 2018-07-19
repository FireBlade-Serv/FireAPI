package fr.glowstoner.fireapi.bigbrother.console.server.login;

import fr.glowstoner.fireapi.crypto.EncryptionKey;
import fr.glowstoner.fireapi.player.enums.VersionType;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class BigBrotherConnectionInfos {
	
	private String id, password;
	private EncryptionKey key;
	
	private VersionType versionType;

}
