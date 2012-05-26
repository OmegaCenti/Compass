package net.betterverse.compass.bukkit;

import java.util.HashMap;
import java.util.Map;

import net.betterverse.communities.Communities;
import net.betterverse.compass.bukkit.hooks.MyChunksManager;
import net.betterverse.compass.bukkit.listeners.EventListener;
import net.betterverse.compass.bukkit.player.BukkitCompassPlayer;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * This is the Bukkit implementation of CompassWarp plugin.
 * 
 * @modified 1/8/12
 * @author Julian Trust
 */
public class BukkitCompass extends JavaPlugin implements Runnable {

    /*
     * Standard permission required to warp to or through a block. Players with this permission invoke a PvP cooldown
     * after teleporting.
     */
    public static final String warpUsePermission = "compasswarp.use";

    /*
     * Permission required to warp to or through a block without invoking a cooldown on PvP.
     */
    public static final String warpUnlimitedPermission = "compasswarp.unlimited";

    public static BukkitCompass instance;
    public static Communities communities;
    private Map<String, BukkitCompassPlayer> players;
    private boolean comsEnabled;

    @Override
    public void onEnable() {
        instance = this;
        getConfig().options().copyDefaults(true);
        saveConfig();
        players = new HashMap<String, BukkitCompassPlayer>();
        for (Player p : getServer().getOnlinePlayers())
            registerPlayer(p);
        new EventListener(this);
        if (getServer().getPluginManager().isPluginEnabled("Communities")) {
            communities = (Communities) getServer().getPluginManager().getPlugin("Communities");
            comsEnabled = true;
        }
        if (getServer().getPluginManager().isPluginEnabled("MyChunks")) {
            MyChunksManager.setMyChunks(getServer().getPluginManager().getPlugin("MyChunks"));
        }
        getServer().getScheduler().scheduleSyncRepeatingTask(this, this, 0, 20);
        getServer().getLogger().info("[CompassWarp] - Enabled");
    }

    @Override
    public void onDisable() {
        getServer().getLogger().info("[CompassWarp] - Disabled");
    }

    public BukkitCompassPlayer getPlayer(String name) {
        return players.get(name);
    }

    public int getCooldown() {
        return getConfig().getInt("cooldown", 30);
    }

    @Override
    public void run() {
        for (BukkitCompassPlayer player : players.values()) {
            if (player.getCooldown() > 0) {
                player.setCooldown(player.getCooldown() - 1);
            }
        }
    }

    public void registerPlayer(Player player) {
        players.put(player.getName(), new BukkitCompassPlayer(player));
    }

    public void unregisterPlayer(Player player) {
        players.remove(player.getName());
    }

    public boolean isCommunitiesEnabled() {
        return comsEnabled;
    }
}
