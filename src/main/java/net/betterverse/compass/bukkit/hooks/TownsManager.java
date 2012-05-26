package net.betterverse.compass.bukkit.hooks;

import net.betterverse.communities.Communities;
import net.betterverse.communities.community.Community;
import net.betterverse.communities.community.CommunityPlayer;
import net.betterverse.compass.bukkit.BukkitCompass;

import org.bukkit.Location;
import org.bukkit.entity.Player;

public class TownsManager {

    public static boolean canWarpFrom(Player player) {
        if (!BukkitCompass.instance.isCommunitiesEnabled()) {
            return true;
        }

        Communities coms = BukkitCompass.communities;
        CommunityPlayer comPlayer = coms.fromBukkitPlayer(player.getName());
        String playerTownName = "";
        if (comPlayer.getCommunity() != null) {
            playerTownName = comPlayer.getCommunity().getName();
        }
        Community chunkTown = coms.getChunkOwner(player.getLocation().getChunk());
        return chunkTown == null || chunkTown.getName().equals(playerTownName);
    }

    public static boolean canWarpTo(Player player, Location location) {
        if (!BukkitCompass.instance.isCommunitiesEnabled()) {
            return true;
        }

        Communities coms = BukkitCompass.communities;
        CommunityPlayer comPlayer = coms.fromBukkitPlayer(player.getName());
        String playerTownName = "";
        if (comPlayer.getCommunity() != null) {
            playerTownName = comPlayer.getCommunity().getName();
        }
        Community chunkTown = coms.getChunkOwner(location.getChunk());
        return chunkTown == null || chunkTown.getName().equals(playerTownName);
    }
}
