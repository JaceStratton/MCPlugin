package me.jacestratton;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;
import java.util.Scanner;

/**
 * Array of tips that can be added to and searched through.
 * 
 * @author JaceStratton
 * 
 * @version 1.0
 */
public class Guide {
    private Scanner scanner;
    private File file;
    private String[] tips;
    private int numberOfTips;
    private Random random;
    private JacesPlugin plugin;

    /**
     * Default constructor that constructs an array of tips.
     * 
     * @throws FileNotFoundException
     */
    public Guide(JacesPlugin plugin) {
        // Tries to create a new file from the list of tips. Logs errors.
        try {
            file = new File("tips.txt");
            if (file.createNewFile()) {
                FileWriter writer = new FileWriter(file, true);
                writer.append("this is the first tip");
                writer.close();
            }
        }
        catch (Exception e) {
            plugin.printError(e);
        }

        // Tries to create a scanner from the file. Logs errors.
        try {
            scanner = new Scanner(file);
        }
        catch (Exception e) {
            plugin.printError(e);
        }

        this.plugin = plugin;
        tips = new String[10000];
        numberOfTips = 0;
        random = new Random();
        int i = 0;
        while (scanner.hasNextLine()) {
            tips[i] = scanner.nextLine();
            numberOfTips++;
            i++;
        }
        scanner.close();
    }

    /**
     * Prints a random tip to send to chat.
     * 
     * @return A random tip.
     */
    public String randomTip() {
        int pos = random.nextInt(numberOfTips) + 1;
        return printTip(pos);
    }

    /**
     * Finds and prints the tip specified by the given number.
     * 
     * @param pos
     *            The number of the tip's position in the array.
     * @return The tip specified by the number.
     */
    public String searchTip(int pos) {
        if (pos > 0 && pos <= numberOfTips) {
            return printTip(pos);
        }
        return null;
    }

    /**
     * Searches through the array of tips with a keyword.
     * 
     * @param keyword
     *            The string to search tips for.
     * @return The tip to return.
     */
    public String searchTip(String keyword) {
        Integer[] matches = new Integer[numberOfTips];
        int j = 0;
        for (int i = 0; i < numberOfTips; i++) {
            if (tips[i].toLowerCase().contains(keyword.toLowerCase())) {
                matches[j] = i + 1;
                j++;
            }
        }
        if (j == 0) {
            return null;
        }
        return printTip(matches[random.nextInt(j)]);
    }

    /**
     * Adds a tip to the tip array.
     * 
     * @param tip
     *            The tip to add.
     */
    public void addTip(String tip) {
        try {
            FileWriter writer = new FileWriter(file, true);
            writer.append("\n" + tip);
            writer.close();
            tips[numberOfTips] = tip;
            numberOfTips++;
        }
        catch (IOException e) {
            plugin.printError(e);
        }
    }
    
    /**
     * @return The number of tips.
     */
    public int getNumberOfTips() {
        return numberOfTips;
    }

    /**
     * Prints the specified tip.
     * 
     * @param pos
     *            The position of the tip in the array.
     * @return The tip specified by position.
     */
    private String printTip(int pos) {
        return pos + "/" + numberOfTips + " - " + tips[pos - 1];
    }

}
