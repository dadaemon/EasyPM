/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package me.barryg.EasyPM;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChatEvent;

/**
 *
 * @author BarryG
 */
public class EasyPMPlayerListener implements Listener {

    private EasyPM plugin;

    public EasyPMPlayerListener(EasyPM plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerChat(PlayerChatEvent chat) {
        Player p = chat.getPlayer();
        String playerName = p.getName();
        String message = chat.getMessage();
        // Check if player activated EasyPM
        if (plugin.c.isEPMActive(playerName)) {
            Player toPlayer = null;

            // Check if line starts with @
            if (message.startsWith("@") || plugin.c.isLocked(playerName)) {
                String msg = null;
                String sendTo = null;

                //if (plugin.c.isLocked(playerName)) {
                if (!message.startsWith("@")) {
                    sendTo = plugin.c.getLocked(playerName);
                    toPlayer = Bukkit.getPlayer(sendTo);
                    if (toPlayer != null) {
                        msg = message;
                    }
                } else {
                    if (message.length() < 2) {
                        return;
                    }

                    if (message.charAt(1) == (' ')) {
                        if (plugin.c.hasHistory(playerName)) {
                            sendTo = plugin.c.getHistory(playerName);
                            msg = message.substring(2);
                            toPlayer = Bukkit.getPlayer(sendTo);
                        }
                    } else if (message.charAt(1) == ('@')) {
                        if (plugin.c.hasFrom(playerName)) {
                            sendTo = plugin.c.getFrom(playerName);
                            msg = message.substring(2);
                            toPlayer = Bukkit.getPlayer(sendTo);
                        }
                    } else {
                        String[] split = message.split(" ");
                        if (split.length > 1) {
                            sendTo = split[0].substring(1);
                            msg = message.substring(sendTo.length() + 2);
                            toPlayer = Bukkit.getPlayer(sendTo);
                        }
                    }
                }

                chat.setCancelled(true);
                if (toPlayer == null && msg == null) {
                    p.sendMessage(ChatColor.WHITE + "You should PM someone before quick replying!");
                } else if (toPlayer == null) {
                    p.sendMessage(ChatColor.WHITE + "Player '" + ChatColor.RED + sendTo + ChatColor.WHITE + "' can't be found!");
                } else {
                    plugin.c.setHistory(playerName, toPlayer.getName());
                    plugin.c.setFrom(toPlayer.getName(), playerName);
                    p.sendMessage(plugin.c.ColorPMToName() + "Whisper to " + toPlayer.getName() + ": " + plugin.c.ColorPMToText() + msg);
                    toPlayer.sendMessage(plugin.c.ColorPMFromName() + "Whisper from " + p.getName() + ": " + plugin.c.ColorPMFromText() + msg);
                    if(plugin.c.isLogChat()) {
                        plugin.writeLog(p.getName() + " -> " + toPlayer.getName() + ": " + msg);
                    }
                }
            }
        }
    }
}
