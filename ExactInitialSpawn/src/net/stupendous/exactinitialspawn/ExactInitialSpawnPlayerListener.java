package net.stupendous.exactinitialspawn;

import java.io.File;

import net.stupendous.util.*;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.event.player.PlayerEvent;
import org.bukkit.event.player.PlayerListener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerPreLoginEvent;

public class ExactInitialSpawnPlayerListener extends PlayerListener {
	protected final ExactInitialSpawnPlugin plugin;
	protected final Log log;
	
	ExactInitialSpawnPlayerListener(ExactInitialSpawnPlugin plugin) {
		this.plugin = plugin;
		this.log = plugin.log;
	}
	
	public void onPlayerPreLogin(PlayerPreLoginEvent event) {
		String playerName = event.getName();
		
		File playerFile = new File(plugin.serverConfig.getProperty("level-name") + "/players/" + playerName + ".dat");

		if (!playerFile.exists()) {
			log.info("%s is logging in for the first time.", playerName);
			plugin.prelogins.put(playerName, true);
		}
	}
	
	public void onPlayerJoin ( PlayerJoinEvent event ) {
		String playerName = event.getPlayer().getName();
		
		if (plugin.prelogins.containsKey(playerName)) {
			plugin.prelogins.remove(playerName);
			
			String defaultWorldName = plugin.serverConfig.getProperty("level-name");
			World defaultWorld = plugin.getServer().getWorld(defaultWorldName);

			event.getPlayer().teleport(defaultWorld.getSpawnLocation());
			log.info("%s teleported to default spawn location.", playerName);
		}
	}
}
