/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package me.barryg.EasyPM;

import java.io.IOException;
import java.util.logging.Logger;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

/**
 *
 * @author BarryG
 */
public class EasyPM extends JavaPlugin {

    private static final Logger logger = Logger.getLogger("Minecraft");
    public PluginDescriptionFile pdfFile;
    public String name;
    public String version;
    public EasyPMConfig c;
    private EasyPMPlayerListener easyPMPlayerListener;

    @Override
    public void onDisable() {
        System.out.println(this + " is now disabled!");
    }

    @Override
    public void onEnable() {
        // Register plugin description file
        pdfFile = this.getDescription();
        name = pdfFile.getName();
        version = pdfFile.getVersion();

        // Read config
        c = new EasyPMConfig(this);

        writeLog("== " + name + " " + version + " ENABLING ==");

        // Register PluginManager
        PluginManager pm = getServer().getPluginManager();

        // Register events
        easyPMPlayerListener = new EasyPMPlayerListener(this);
        pm.registerEvents(easyPMPlayerListener, this);

        EasyPMCommandExecutor ce = new EasyPMCommandExecutor(this);
        getCommand("epm").setExecutor(ce);
        getCommand("lockpm").setExecutor(ce);

        try {
            Metrics metrics = new Metrics();
            metrics.beginMeasuringPlugin(this);
        } catch (IOException e) {
            writeLog(e.getMessage());
        }        
        
        writeLog("== " + name + " " + version + " ENABLED ==");
    }

    public void writeLog(String text) {
        this.logger.info("[" + name + "] " + text);
    }

    public void writeDebug(String text) {
        if (c.getConfigDebug()) {
            this.logger.info("[" + name + " DEBUG] " + text);
        }
    }
}
