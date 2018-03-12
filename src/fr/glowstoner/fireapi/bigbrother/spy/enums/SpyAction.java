package fr.glowstoner.fireapi.bigbrother.spy.enums;

public enum SpyAction {
	
	//init
	INIT,
	
	//player
	PLAYER_CHAT,
	PLAYER_JOIN,
	PLAYER_LEAVE,
	PLAYER_LOGGED,
	
	//server
	PLAYER_SERVER_CONNECTION,
	PLAYER_SERVER_DISCONNECT,
	
	//mod
	PLAYER_MUTE,
	PLAYER_KICK,
	PLAYER_BAN,
	PLAYER_FULLBAN,
	
	//BigBrother anti-cheat
	PLAYER_GAC_DETECTION
}
