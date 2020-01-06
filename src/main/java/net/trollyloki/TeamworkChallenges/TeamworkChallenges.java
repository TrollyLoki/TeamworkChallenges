package net.trollyloki.TeamworkChallenges;

import org.bukkit.plugin.java.JavaPlugin;

public class TeamworkChallenges extends JavaPlugin {
	
	private static TeamworkChallenges plugin = null;
	
	@Override
	public void onEnable() {
		plugin = this;
		
		registerCommands();
		registerListeners();
	}
	
	@Override
	public void onDisable() {
		plugin = null;
	}
	
	private void registerCommands() {
		getCommand("teamwork-challenge").setExecutor(new TeamworkChallengeCommand());
	}
	
	private void registerListeners() {
		getServer().getPluginManager().registerEvents(new StayCloseChallenge(), this);
		getServer().getPluginManager().registerEvents(new HeadHandsLegsChallenge(), this);
	}
	
	public TeamworkChallenges getPlugin() {
		return plugin;
	}
	
}
