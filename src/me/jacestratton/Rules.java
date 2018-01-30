package me.jacestratton;

import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Creates and / or reads from a text file of a list of rules.
 * 
 * @author JaceStratton
 * 
 * @version 1.0
 */
public class Rules {
    private Scanner scanner;
    private File file;
    private JacesPlugin plugin;
    private ArrayList<String> rules;

    /**
     * Creates / reads from a text file and makes an array list of rules.
     * 
     * @param plugin
     *            The plugin to which this class belongs.
     */
    public Rules(JacesPlugin main) {
        plugin = main;
        rules = new ArrayList<String>();

        // Tries to create a rules file.
        try {
            file = new File("rules.txt");
            if (file.createNewFile()) {
                FileWriter writer = new FileWriter(file, true);
                writer.append("This is the first rule.");
                writer.close();
            }
        }
        catch (Exception e) {
            plugin.printError(e);
        }

        // Tries to create a scanner and create a list of rules.
        try {
            scanner = new Scanner(file);
            while (scanner.hasNextLine()) {
                rules.add(scanner.nextLine() + "\n");
            }
        }
        catch (Exception e) {
            plugin.printError(e);
        }
    }

    /**
     * Adds a rule to the end of the list.
     * 
     * @param rule
     *            The rule to add.
     */
    public void addRule(String rule) {
        rules.add(rule);
        try {
            FileWriter writer = new FileWriter(file, true);
            writer.append("\n" + rule);
            writer.close();
        }
        catch (Exception e) {
            plugin.printError(e);
        }
    }

    /**
     * Removes a rule from the list.
     * 
     * @param rule
     *            The number of rule to remove.
     */
    public void removeRule(int rule) {
        rules.remove(rule - 1);
        try {
            file.delete();
            file.createNewFile();
            FileWriter writer = new FileWriter(file, false);
            for (String s : rules) {
                writer.write(s);
                plugin.getLogger().info(s);
            }
            writer.close();
        }
        catch (Exception e) {
            plugin.printError(e);
        }
    }

    /**
     * Returns the list of rules.
     */
    public ArrayList<String> getRules() {
        return rules;
    }

}
