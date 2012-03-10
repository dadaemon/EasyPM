/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package me.barryg.EasyPM;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;

/**
 *
 * @author BarryG
 */
public class EasyPMConfig {

    private EasyPM plugin;
    private FileConfiguration c;
    private String n;
    private Map<String, String> history = new HashMap<String, String>();
    private Map<String, String> locked = new HashMap<String, String>();
    private Map<String, String> from = new HashMap<String, String>();

    public EasyPMConfig(EasyPM plugin) {
        this.plugin = plugin;

        if (!(new File(plugin.getDataFolder(), "config.yml")).exists()) {
            plugin.writeLog("Config file defaults are being copied");
            plugin.saveDefaultConfig();
            //plugin.saveConfig();
        }
        c = plugin.getConfig();
        n = plugin.name;
        
        String version = c.getString(n + ".Version", "1.2");
        if (!version.equals(plugin.version)) {
            // Version 1.2 -> 1.3
            // * Added ListWidth
            // * Added ListHeight
            if (version.equals("1.2")) {
                c.set(n + ".LogChat", false);
                version = "1.3";
            }

            // Version 1.3 -> ...
            if (version.equals("1.3")) {
            }

            c.set(n + ".Version", plugin.version);
            plugin.saveConfig();
        }
    }

    private void reload() {
        plugin.reloadConfig();
        c = plugin.getConfig();
        n = plugin.name;
    }

    public boolean getConfigDebug() {
        return c.getBoolean(n + ".Debug", false);
    }

    boolean isEPMActive(String p) {
        boolean active;
        if (c.isBoolean(n + ".Active." + p)) {
            active = c.getBoolean(n + ".Active." + p);
        } else {
            c.set(n + ".Active." + p, plugin.c.getActiveDefault());
            saveConfig();
            active = c.getBoolean(n + ".Active." + p);
        }
        //plugin.writeDebug("Active: " + active);
        return active;
    }

    private void saveConfig() {
        plugin.writeLog("Saving config!");
        plugin.saveConfig();
        reload();
    }

    public boolean setEPMActive(String p) {
        boolean active = isEPMActive(p);

        active = !(active);

        c.set(n + ".Active." + p, active);
        saveConfig();
        return active;
    }

    public String getHistory(String player) {
        return history.get(player);
    }

    public boolean hasHistory(String player) {
        return history.containsKey(player);
    }

    public void setHistory(String player, String hist) {
        this.history.put(player, hist);
    }

    public String getFrom(String player) {
        return from.get(player);
    }

    public boolean hasFrom(String player) {
        return from.containsKey(player);
    }

    public void setFrom(String player, String from) {
        this.from.put(player, from);
    }

    public boolean isLocked(String player) {
        return this.locked.containsKey(player);
    }

    public String getLocked(String player) {
        return this.locked.get(player);
    }

    public void removeLocked(String player) {
        this.locked.remove(player);
    }

    public String setLocked(String player) {
        if (this.locked.containsKey(player)) {
            this.locked.remove(player);
        } else {
            this.locked.put(player, this.getHistory(player));
        }
        return this.locked.get(player);
    }

    public boolean getActiveDefault() {
        return c.getBoolean(n + ".ActiveDefault", false);
    }

    public ChatColor ColorPMToName() {
        return getColor(c.getString(n + ".ColorPMToName", "yellow").toUpperCase());
    }

    public ChatColor ColorPMFromName() {
        return getColor(c.getString(n + ".ColorPMFromName", "green").toUpperCase());
    }

    public ChatColor ColorPMToText() {
        return getColor(c.getString(n + ".ColorPMToText", "gray").toUpperCase());
    }

    public ChatColor ColorPMFromText() {
        return getColor(c.getString(n + ".ColorPMFromText", "gray").toUpperCase());
    }

    private ChatColor getColor(String color) {
        ChatColor col = ChatColor.valueOf(color);
        if (col == null) {
            col = ChatColor.WHITE;
        }
        return col;
    }

    boolean isLogChat() {
        return c.getBoolean(n + ".LogChat", false);
    }
}
