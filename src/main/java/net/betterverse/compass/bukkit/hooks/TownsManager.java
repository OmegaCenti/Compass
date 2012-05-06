package net.betterverse.compass.bukkit.hooks;

import net.betterverse.communities.player.CommunityPlayer;
import net.betterverse.communities.player.PlayerManager;
import net.betterverse.communities.town.ChunkLocation;
import net.betterverse.communities.town.Town;
import net.betterverse.communities.town.TownManager;
import org.bukkit.Location;
import org.bukkit.entity.Player;

/**
 * Static class for any sort of region checking required
 * of Towny (Bukkit plugin).
 *
 * @modified 1/8/12
 * @author Julian Trust
 */
public class TownsManager {
	
    public static boolean canWarpFrom(Player player) {
				String playerTownName = PlayerManager.getCommunityPlayer(player.getName()).getCurrentTown();
				Town chunkTown = TownManager.getChunkTown(new ChunkLocation(player.getLocation().getChunk()));
        String chunkTownName = "";
				if(chunkTown != null) {
					chunkTownName = chunkTown.getName();
				}
				return playerTownName.equalsIgnoreCase(chunkTownName);
    }
    
    public static boolean canWarpTo(Player player, Location location) {
				String playerTownName = PlayerManager.getCommunityPlayer(player.getName()).getCurrentTown();
				Town chunkTown = TownManager.getChunkTown(new ChunkLocation(location.getChunk()));
        String chunkTownName = "";
				if(chunkTown != null) {
					chunkTownName = chunkTown.getName();
				}
				return playerTownName.equalsIgnoreCase(chunkTownName);
    }
}