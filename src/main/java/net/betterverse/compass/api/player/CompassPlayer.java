package net.betterverse.compass.api.player;

/**
 * Interface to be used by a Minecraft plugin system that depicts a player
 * on a Minecraft server.
 * 
 * @author Julian Trust
 */
public interface CompassPlayer {
    
    /**
     * Set the players current cooldown on PvP as a result of teleporting.
     */
    public void setCooldown(int cooldown);
    
    /**
     * Get the time (in seconds) until this player may participate in PvP.
     * 
     * @return the number of seconds until PvP can be participated in.
     */
    public int getCooldown();
    
    /**
     * Checks in this player has the permission defined by the caller
     * 
     * @param String permission to check
     * @return does this player have this permission
     */
    public boolean hasPermission(String permission);
    
    /**
     * Causes this player to warp to the location specified by x, y, and z.
     * 
     * @param x int value of x-coordinate to move to
     * @param y int value of y-coordinate to move to
     * @param z int value of z-coordinate to move to
     */
    public void teleport(int x, int y, int z);
    
    /**
     * Causes this player to warp to their current target.
     */
    public void warpToTarget();
    
    /**
     * Causes this player to warp through their current target.
     */
    public void warpThroughTarget();
}