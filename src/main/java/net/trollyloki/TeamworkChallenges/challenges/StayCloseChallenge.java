package net.trollyloki.TeamworkChallenges.challenges;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import net.trollyloki.TeamworkChallenges.TeamworkChallenges;

public class StayCloseChallenge extends BukkitRunnable implements Challenge {

	List<Player> players = new ArrayList<Player>();
	public static final double ANGLE = Math.toRadians(15);

	@Override
	public int start(List<Player> players) {
		this.players = players;
		for (Player player : players) {
			player.teleport(players.get(0));
			player.setBedSpawnLocation(players.get(0).getLocation(), true);
		}

		TeamworkChallenges.getPlugin().getServer().getPluginManager().registerEvents(this,
				TeamworkChallenges.getPlugin());
		runTaskTimer(TeamworkChallenges.getPlugin(), 0L, 20L);
		players.get(0).sendMessage("Loaded");
		return 0;
	}

	@Override
	public int stop() {
		PlayerMoveEvent.getHandlerList().unregister(this);
		cancel();
		return 0;
	}

	@Override
	public void run() {

		Map<Player, Location> playerLocations = new HashMap<Player, Location>();
		for (Player p : players)
			playerLocations.put(p, p.getLocation());

		// calculate particle locations
		new BukkitRunnable() {

			@Override
			public void run() {

				Map<Player, List<Location>> particleLocations = new HashMap<Player, List<Location>>();
				// for each location iterate over the others
				for (Player player : playerLocations.keySet()) {
					List<Location> playerParticles = new ArrayList<Location>();

					List<Location> otherPlayerLocations = new ArrayList<Location>();
					for (Player p : playerLocations.keySet()) {
						if (!p.equals(player))
							otherPlayerLocations.add(playerLocations.get(p));
					}

					for (Location location : otherPlayerLocations) {

						// create sphere of points
						List<Location> points = new ArrayList<Location>();
						Vector vec = new Vector(0, 5, 0);
						points.add(location.clone().add(vec));

						for (int pitch = 15; pitch <= 180; pitch += 15) {

							vec.rotateAroundX(ANGLE);
							points.add(location.clone().add(vec));
							if (pitch != 180)
								for (int yaw = 15; yaw < 360; yaw += 15) {

									vec.rotateAroundY(ANGLE);
									points.add(location.clone().add(vec));

								}

						}

						// remove points that intersect other spheres
						for (Location otherLocation : otherPlayerLocations) {
							if (otherLocation.equals(location))
								continue;

							List<Location> pointsCopy = new ArrayList<Location>();
							pointsCopy.addAll(points);
							for (Location point : pointsCopy) {
								if (point.distance(otherLocation) < 5)
									points.remove(point);
							}

						}

						playerParticles.addAll(points);

					}

					particleLocations.put(player, playerParticles);
				}

				// render particles
				new BukkitRunnable() {

					@Override
					public void run() {
						for (Player player : players) {

							List<Location> points = particleLocations.get(player);
							if (points != null)
								for (Location point : points) {

									player.spawnParticle(Particle.REDSTONE, point, 1,
											new Particle.DustOptions(Color.RED, 1));

								}

						}
					}

				}.runTask(TeamworkChallenges.getPlugin());

			}

		}.runTaskAsynchronously(TeamworkChallenges.getPlugin());

	}

	@EventHandler(priority = EventPriority.MONITOR)
	public void onPlayerMove(PlayerMoveEvent event) {
		if (!players.contains(event.getPlayer())) return;

		if (event.getTo().distance(event.getFrom()) > 0) {

			for (Player player : players) {
				if (!player.equals(event.getPlayer()) && !player.isDead()) {

					if (event.getPlayer().getLocation().distance(player.getLocation()) > 5) {

						for (Player p : players)
							p.setHealth(0);

					}

				}
			}

		}

	}

}
