package me.jacestratton;

import java.util.List;

import org.bukkit.Location;
import org.bukkit.Server;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class WorldBorder extends BukkitRunnable {
    private List<Player> players;
    private World world;
    private Server server;

    public WorldBorder(JacesPlugin plugin) {
        server = plugin.getServer();
        world = server.getWorlds().get(0);
        players = world.getPlayers();
    }

    @Override
    public void run() {
        server.broadcastMessage("WOW");
        for (int i = 0; i < players.size(); i++) {
            Player player = players.get(i);
            Location location = player.getLocation();
            Double x = location.getX();
            Double z = location.getZ();
            if (x > 5000 || z > 5000) {
                player.teleport(new Location(world, 0, 150, 0));
            }
        }
    }
    
}
