package net.trollyloki.TeamworkChallenges.challenges;

import java.util.List;

import org.bukkit.entity.Player;
import org.bukkit.event.Listener;

public interface Challenge extends Listener {
	
	public int start(List<Player> players);
	public int stop();
	
}
