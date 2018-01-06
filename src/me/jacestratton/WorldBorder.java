package me.jacestratton;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerEvent;
import org.bukkit.util.Vector;

public class WorldBorder implements Listener {

    public WorldBorder(JacesPlugin plugin) {
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void playerRepel(PlayerEvent event) {
        Player player = event.getPlayer();
        Double x = player.getLocation().getX();
        Double z = player.getLocation().getZ();
        if (x> 5000) {
            player.setVelocity(player.getVelocity().add(new Vector(-0.3, .1, 0)));
        }
        if (x < -5000) {
            player.setVelocity(player.getVelocity().add(new Vector(0.3, .1, 0)));
        }
        if (z > 5000) {
            player.setVelocity(player.getVelocity().add(new Vector(0, .1, -0.3)));
        }
        if (z < -5000) {
            player.setVelocity(player.getVelocity().add(new Vector(0, .1, 0.3)));
        }
    }

    @EventHandler
    public void placementStop(BlockPlaceEvent event) {
        Double x = Math.abs(event.getBlock().getLocation().getX());
        Double z = Math.abs(event.getBlock().getLocation().getZ());
        if (x >= 5000 || z >= 5000) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void breakStop(BlockBreakEvent event) {
        Double x = Math.abs(event.getBlock().getLocation().getX());
        Double z = Math.abs(event.getBlock().getLocation().getZ());
        if (x >= 5000 || z >= 5000) {
            event.setCancelled(true);
        }
    }

}
