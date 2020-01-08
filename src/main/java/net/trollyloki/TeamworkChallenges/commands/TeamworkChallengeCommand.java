package net.trollyloki.TeamworkChallenges.commands;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import net.trollyloki.TeamworkChallenges.TeamworkChallenges;
import net.trollyloki.TeamworkChallenges.challenges.Challenge;

public class TeamworkChallengeCommand implements CommandExecutor, TabCompleter {

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		
		if (args.length < 3) {
			sender.sendMessage(ChatColor.RED + "Usage: /" + label + " <challenge> <player1> <player2> ...");
			return false;
		}
		
		else {
			
			List<Player> players = new ArrayList<Player>();
			for (int i = 1; i < args.length; i++) {
				Player p = TeamworkChallenges.getPlugin().getServer().getPlayerExact(args[i]);
				if (p == null) {
					sender.sendMessage(ChatColor.RED + args[i] + " is not online");
					return false;
				}
				players.add(p);
			}
			
			Challenge challenge;
			try {
				
				challenge = (Challenge) Class.forName("net.trollyloki.TeamworkChallenges.challenges." + args[0]).getConstructor().newInstance();
				
			} catch (ClassNotFoundException | ClassCastException | InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException e) {
				sender.sendMessage(e.toString());
				sender.sendMessage(ChatColor.RED + "Challenge '" + args[0] + "' does not exist");
				return false;
			}
			
			challenge.start(players);
			return true;
		}
		
	}
	
	@Override
	public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
		return new ArrayList<String>();
	}

}
