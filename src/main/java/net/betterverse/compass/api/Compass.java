package net.betterverse.compass.api;

import net.betterverse.compass.api.player.CompassPlayer;

/**
 * Interface to be used by a Minecraft plugin system that represents the
 * CompassWarp plugin.
 * 
 * @modified 1/8/12
 * @author Julian Trust
 */
public interface Compass {
    
    /*
     * Standard permission required to warp to or through a block. Players with
     * this permission invoke a PvP cooldown after teleporting.
     */
    public static final String warpUsePermission = "compasswarp.use";
    
    /*
     * Permission required to warp to or through a block without invoking a
     * cooldown on PvP.
     */
    public static final String warpUnlimitedPermission = "compasswarp.unlimited";
    
    /*
     * Returns the CompassWarpPlayer mapped to the name provided.
     * 
     * @param String name of player to find
     * @return the player define by name
     */
    public CompassPlayer getPlayer(String name);
    
    /*
     * Get the default PvP cooldown for teleporting.
     * 
     * @return default cooldown to use after teleporting.
     */
    public int getCooldown();
    
}