package fr.glowstoner.connectionsapi.network.packets.ping;

import java.util.Timer;
import java.util.TimerTask;

public class PingGetter extends TimerTask{

	private long lag;
	private Timer timer;
	
	public PingGetter() {
		this.timer = new Timer();
	}
	
	public void start() {
		this.timer.scheduleAtFixedRate(this, 0L, 1L);
	}
	
	public void stop() {
		this.timer.cancel();
	}
	
	public long getLag() {
		return this.lag;
	}
	
	@Override
	public void run() {
		this.lag++;
	}
}
