package fr.glowstoner.fireapi.gediminas.spy;

import fr.glowstoner.fireapi.gediminas.spy.packets.PacketSpyAction;

public class GediminasSpyUtils {
	
	public static GediminasSpyHistory mergeHistory(GediminasSpyHistory base, PacketSpyAction ps) {
		base.putMessage(ps.getActionDate(), ps.getAction(), ps.getFormatedMsg());
		
		return base;
	}

}
