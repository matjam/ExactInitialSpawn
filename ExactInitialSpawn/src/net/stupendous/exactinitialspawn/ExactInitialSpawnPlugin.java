package net.stupendous.exactinitialspawn;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Properties;

import net.stupendous.util.Log;
import net.stupendous.util.Util;

import org.bukkit.command.CommandExecutor;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.Event.Priority;

public class ExactInitialSpawnPlugin extends JavaPlugin {
	protected PluginDescriptionFile pdf = null;
	protected String pluginName = null;
	protected Log log = null;
	protected Properties serverConfig = new Properties();
	
	private ExactInitialSpawnPlayerListener playerListener = null;
	
	protected final HashMap<String, Boolean> prelogins = new HashMap<String, Boolean>();
	
    public void onEnable() {
        pdf = this.getDescription();
        pluginName = pdf.getName();
        log = new Log(pluginName);

        try {
			serverConfig.load(new BufferedReader(new FileReader("server.properties")));
		} catch (FileNotFoundException e) {
			log.severe("Couldn't find the server.properties file: %s", e.getMessage());
		} catch (IOException e) {
			log.severe("Couldn't load the server.properties file: %s", e.getMessage());
		}
        
        // Initialise static stuff for the Util class.
        Util.init(pluginName, log);

        playerListener = new ExactInitialSpawnPlayerListener(this);
        
        PluginManager pm = getServer().getPluginManager();
        pm.registerEvent(Event.Type.PLAYER_PRELOGIN, playerListener, Priority.Normal, this);
        pm.registerEvent(Event.Type.PLAYER_JOIN, playerListener, Priority.Normal, this);

        log.info("Version %s enabled.", pdf.getVersion());
    }
    
    public void onDisable() {
    	if (pdf == null)
    		return;
    	
        log.info("Version %s disabled.", pdf.getVersion());
    }

}
