package me.jacestratton;

import java.util.logging.Logger;

import org.bukkit.Server;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import net.md_5.bungee.api.ChatColor;

/**
 * The main class to initialize the plugin and run commands.
 * 
 * @author JaceStratton
 * 
 * @version 1.0
 */
public class JacesPlugin extends JavaPlugin {
    private Guide tips;
    private String message;
    private Logger logger;
    private Cooldown tipCooldown;
    private Server server;
    private Rules rules;

    /**
     * Initializes fields and logs the plugin initialization.
     */
    @Override
    public void onEnable() {
        server = getServer();
        logger = getLogger();
        logger.info("JacesPlugin is online!");
        tips = new Guide(this);
        rules = new Rules(this);
        tipCooldown = new Cooldown(10);
    }

    /**
     * Closes any open processes and stops the plugin.
     */
    @Override
    public void onDisable() {

    }

    /**
     * Catches commands.
     * 
     * @param sender
     *            The player who sent the command.
     * @param cmd
     *            The command that was entered by the player.
     * @param label
     *            ???
     * @param args
     *            The arguments included for the command.
     */
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            String command = cmd.getName();
            if (command.equalsIgnoreCase("tip")) {
                return tip(player, args);
            }
            if (command.equalsIgnoreCase("addtip")) {
                return addtip(player, args);
            }
            if (command.equalsIgnoreCase("rules")) {
                return rules(player, args);
            }
            if (command.equalsIgnoreCase("addrule")) {
                return addRule(player, args);
            }
            if (command.equalsIgnoreCase("removerule")) {
                return removeRule(player, args);
            }
        }
        return false;
    }

    /**
     * Returns a tip to the player, either random or searched for.
     * 
     * @param player
     *            The player who sent the command.
     * @param args
     *            The number or keyword used to search for a tip.
     * @return True if the player sent a valid command. False if the player
     *         included too many arguments.
     */
    public boolean tip(Player player, String[] args) {
        if (args.length == 0) {
            if (tipCooldown.hasCooldown(player)) {
                player.sendMessage(ChatColor.RED + "Guide: " + tipCooldown.getCooldown(player));
                return true;
            }
            message = tips.randomTip();
            server.broadcastMessage("[Server] " + player.getName() + " got tip " + message);
            return true;
        }
        else {
            Exception exception = null;
            int parse = 0;
            try {
                parse = Integer.parseInt(args[0]);
            }
            catch (Exception e) {
                exception = e;
            }

            // If there wasn't a problem getting an integer from the string, the
            // exception will be null and the player wants to search by number.
            if (exception == null) {
                message = tips.searchTip(parse);
                if (message == null) {
                    player.sendMessage(
                            ChatColor.RED + "You must specify a number between 1 and " + tips.getNumberOfTips() + ".");
                    return false;
                }
                if (tipCooldown.hasCooldown(player)) {
                    player.sendMessage(ChatColor.RED + "Guide: " + tipCooldown.getCooldown(player));
                    return true;
                }
                server.broadcastMessage("[Server] " + player.getName() + " searched for tip " + message);
            }

            // If an integer could not be retrieved from the string, the method
            // will return an exception and the argument is a keyword.
            else {
                StringBuilder string = new StringBuilder(args[0]);
                for (int i = 1; i < args.length; i++) {
                    string.append(" " + args[i]);
                }
                message = tips.searchTip(string.toString());
                if (message == null) {
                    player.sendMessage(ChatColor.RED + "No matches found.");
                    return false;
                }
                if (tipCooldown.hasCooldown(player)) {
                    player.sendMessage(ChatColor.RED + "Tips: " + tipCooldown.getCooldown(player));
                    return true;
                }
                server.broadcastMessage("[Server] " + player.getName() + " searched for tip " + message);
            }
            return true;
        }
    }

    /**
     * Adds a tip to the guidebook.
     * 
     * @param player
     *            The player who wants to add a tip.
     * @param args
     *            The tip the player wants to add.
     * @return True if the tip was not empty. False if the tip was empty.
     */
    public boolean addtip(Player player, String[] args) {
        if (args.length != 0) {
            if (tipCooldown.hasCooldown(player)) {
                player.sendMessage(ChatColor.RED + "Guide: " + tipCooldown.getCooldown(player));
                return true;
            }
            StringBuilder string = new StringBuilder(args[0]);
            for (int i = 1; i < args.length; i++) {
                string.append(" " + args[i]);
            }
            tips.addTip(string.toString());
            message = tips.searchTip(tips.getNumberOfTips());
            server.broadcastMessage("[Server] " + player.getName() + " added tip " + message);
            return true;
        }
        player.sendMessage(ChatColor.RED + "You can't add an empty tip.");
        return false;
    }

    /**
     * Returns page of rules.
     * 
     * @param player
     *            Player requesting rules.
     * @param args
     *            Rule page.
     * @return True if page exists. False if not.
     */
    public boolean rules(Player player, String[] args) {
        int pageNumber = 1;

        if (args.length != 0) {
            // Try to parse int from string.
            try {
                pageNumber = Integer.parseInt(args[0]);
            }
            catch (Exception e) {
                printError(e);
                player.sendMessage(ChatColor.RED + "Must specify number as digit.");
                return false;
            }
        }

        // If no exception, run second part.
        int numberOfPages = rules.getRules().size() / 5;
        if (rules.getRules().size() % 5 != 0) {
            numberOfPages++;
        }
        if (pageNumber > numberOfPages || pageNumber <= 0) {
            player.sendMessage(ChatColor.RED + "Must specify digit between 1 and " + numberOfPages);
            return false;
        }
        else {
            player.sendMessage(ChatColor.GOLD + "Rules(" + ChatColor.YELLOW + "Page " + pageNumber + "/" + numberOfPages
                    + ChatColor.GOLD + ")");
            int j = 0;
            for (int i = (pageNumber * 5) - 5; i < rules.getRules().size(); i++) {
                if (j == 5) {
                    return true;
                }
                j++;
                player.sendMessage(
                        ChatColor.GOLD + Integer.toString(i + 1) + ChatColor.WHITE + " - " + rules.getRules().get(i));
            }
        }
        return true;
    }

    /**
     * Adds rule to list.
     * 
     * @param player
     *            Player adding rule.
     * @param args
     *            Rule to add.
     * @return True if args has entry. False if not.
     */
    public boolean addRule(Player player, String[] args) {
        if (args.length == 0) {
            player.sendMessage(ChatColor.RED + "Must include rule.");
            return false;
        }
        StringBuilder rule = new StringBuilder();
        rule.append(args[0]);
        for (int i = 1; i < args.length; i++) {
            rule.append(" " + args[i]);
        }
        rules.addRule(rule.toString());
        player.sendMessage(ChatColor.GREEN + "Rule added.");
        return true;
    }

    /**
     * Removes rule from list.
     * 
     * @param player
     *            Player removing rule.
     * @param args
     *            Rule to remove.
     * @return True if rule removed. False if not.
     */
    public boolean removeRule(Player player, String[] args) {
        int ruleNumber = 0;

        if (args.length != 0) {
            // Try to parse int from string.
            try {
                ruleNumber = Integer.parseInt(args[0]);
            }
            catch (Exception e) {
                printError(e);
                player.sendMessage(ChatColor.RED + "Must specify number as digit.");
                return false;
            }
        }
        else {
            player.sendMessage(ChatColor.RED + "Must include digit.");
            return false;
        }

        // If no exception, run second part.
        if (ruleNumber <= 0 || ruleNumber > rules.getRules().size()) {
            player.sendMessage(ChatColor.RED + "Must specify number between 1 and " + rules.getRules().size() + ".");
            return false;
        }
        rules.removeRule(ruleNumber);
        player.sendMessage(ChatColor.GREEN + "Rule removed.");
        return true;

    }

    /**
     * Prints any error from GuideBook methods to the logger.
     * 
     * @param e
     *            The error to print.
     */
    public void printError(Exception e) {
        logger.info(e.getMessage());
    }

}
