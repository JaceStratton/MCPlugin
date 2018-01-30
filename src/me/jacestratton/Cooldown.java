package me.jacestratton;

import java.util.HashMap;

import org.bukkit.entity.Player;

/**
 * Allows wait times to be applied to players.
 * 
 * @author JaceStratton
 *
 * @version 1
 */
public class Cooldown {
    private final int seconds;
    private HashMap<Player, Long> cooldown;

    /**
     * Constructor that takes time as a parameter.
     * 
     * @param time
     *            The integer number of seconds to make a player wait.
     */
    public Cooldown(int time) {
        cooldown = new HashMap<Player, Long>();
        seconds = time;
    }

    /**
     * Whether or not the player still has to wait to perform an action.
     * 
     * @param player
     *            The player to check for a cooldown.
     * @return True if the player has a cooldown and needs to wait. False if the
     *         player does not have a cooldown.
     */
    public boolean hasCooldown(Player player) {
        if (cooldown.get(player) == null) {
            setCooldown(player);
            return false;
        }
        if (cooldown.get(player) < (System.currentTimeMillis() - seconds * 1000)) {
            setCooldown(player);
            return false;
        }
        return true;
    }
    
    public String getCooldown(Player player) {
        return "Please wait " + (seconds - (int) (System.currentTimeMillis() - cooldown.get(player)) / 1000) + " seconds.";
    }

    /**
     * Applies a cooldown to a player who does not already have one.
     * 
     * @param player
     *            The player to apply a cooldown to.
     */
    private void setCooldown(Player player) {
        cooldown.put(player, System.currentTimeMillis());
    }

}
