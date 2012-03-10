/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package me.barryg.EasyPM;

import java.util.Arrays;
import java.util.List;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 *
 * @author BarryG
 */
public class EasyPMCommandExecutor implements CommandExecutor {

    private EasyPM plugin;
    // Initialise executable commands for players
    // aBCDefgHijklMnopQrsTuVwxyz
    private List<String> commandList = Arrays.asList("help", "h");
    // Initialise executable commands for console
    // abcdefgHijklmnopqrStuVwxyz
    private List<String> consoleCommandList = Arrays.asList("help", "h");

    public EasyPMCommandExecutor(EasyPM plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        Player player = null;
        if (sender instanceof Player) {
            player = (Player) sender;
        }

        if (player != null) {
            String playerName = player.getName();
            // Player commands
            if (cmd.getName().equalsIgnoreCase("epm")) { // If the player typed /epm then do the following...
                if (args.length > 0) {
                    if (isConsoleCommand(args[0])) {
                        //sm("Doing command " + args[0], sender);
                        if (doCommand(sender, cmd, label, args)) {
                        } else {
                            plugin.writeLog("Something went wrong executing command '" + cmd + "' (or command isn't implemented yet)");
                        }
                    } else {
                        sm("This is not a recognised command.", sender);
                        sm("For help on EasyPM use /epm help.", sender);
                    }
                    return true;
                } else {
                    if (plugin.c.setEPMActive(playerName)) {
                        player.sendMessage(plugin.name + " is now " + ChatColor.GREEN + "active!");
                    } else {
                        player.sendMessage(plugin.name + " is now " + ChatColor.RED + "deactivated!");
                    }
                    return true;
                }
            }

            if (cmd.getName().equalsIgnoreCase("lockpm")) {
                if (args.length > 0) {
                    if (isConsoleCommand(args[0])) {
                        //sm("Doing command " + args[0], sender);
                        if (doCommand(sender, cmd, label, args)) {
                        } else {
                            plugin.writeLog("Something went wrong executing command '" + cmd + "' (or command isn't implemented yet)");
                        }
                    } else {
                        sm("This is not a recognised command.", sender);
                        sm("For help on EasyPM use /lockpm help.", sender);
                    }
                    return true;
                } else {
                    if (plugin.c.isEPMActive(playerName) && plugin.c.hasHistory(player.getName())) {
                        String oldLockedPlayerName = plugin.c.getLocked(playerName);
                        String newLockedPlayer = plugin.c.setLocked(playerName);
                        if (newLockedPlayer != null) {
                            sm("Locked your chat with '" + newLockedPlayer + "'", sender);
                        } else {
                            sm("Unlocked your chat with '" + oldLockedPlayerName + "'", sender);
                        }
                    } else {
                        sm("Can't set PM lock. You haven't PMed anyone yet!", sender);
                    }
                    return true;
                }
            }
        } else {
            // Console commands
            if (cmd.getName().equalsIgnoreCase("epm")) {
                if (args.length > 0) {
                    if (isConsoleCommand(args[0])) {
                        if (!doCommand(sender, cmd, label, args)) {
                            sm("Something went wrong executing command '" + cmd + "' (or command isn't implemented yet)", sender);
                        }
                    } else {
                        sm("This is not a recognised command.", sender);
                        sm("For help on EasyPM use epm help.", sender);
                    }
                } else {
                    sm("For help on EasyPM use epm help.", sender);
                }
                return true;
            }
            return true;
        }
        return false;
    }

    private boolean isCommand(String string) {
        return this.commandList.contains(string);
    }

    private void sm(String string, CommandSender sender) {
        sender.sendMessage(ChatColor.BLUE + "[" + plugin.name + "] " + ChatColor.WHITE + string);
    }

    private boolean doCommand(CommandSender sender, Command cmd, String label, String[] args) {
        Player player = null;
        if (sender instanceof Player) {
            player = (Player) sender;
        }

        if (cmd.getName().equalsIgnoreCase("epm")) {
            if (args[0].equals("h") || args[0].equals("help")) {
                sm(plugin.pdfFile.getName() + " version " + plugin.pdfFile.getVersion() + " /" + cmd.getName() + " help", sender);
                sm("-=-=-=-=-=-=-=-=-=-", sender);

                if (player != null) {
                    // Player help
                    sm("Use /epm to enable/disable sending messages.", sender);
                    sm("To whisper to a player start your chat message with " + ChatColor.RED + "@" + ChatColor.BLUE + "playername message", sender);
                    sm("Use " + ChatColor.BLUE + "/lockpm help" + ChatColor.WHITE + " for help on PM lock.", sender);
                } else {
                    // Console help
                    sm("No console commands available.", sender);
                }

                return true;
            }

        } else if (cmd.getName().equalsIgnoreCase("lockpm")) {
            if (args[0].equals("h") || args[0].equals("help")) {
                sm(plugin.pdfFile.getName() + " version " + plugin.pdfFile.getVersion() + " /" + cmd.getName() + " help", sender);
                sm("-=-=-=-=-=-=-=-=-=-", sender);

                if (player != null) {
                    // Player help
                    sm("Use /lockpm to enable/disable locked chat with a player.", sender);
                } else {
                    // Console help
                    sm("No console commands available.", sender);
                }

                return true;
            }

        }

        return false;
    }

    private String getCommandList() {
        String itemList = "";
        for (String item : commandList) {
            if (item.length() > 1) {
                itemList = itemList + item + ", ";
            }
        }
        itemList = itemList.substring(0, itemList.length() - 2);
        return itemList;
    }

    private String getConsoleCommandList() {
        String itemList = "";
        for (String item : consoleCommandList) {
            if (item.length() > 1) {
                itemList = itemList + item + ", ";
            }
        }
        itemList = itemList.substring(0, itemList.length() - 2);
        return itemList;
    }

    private boolean isConsoleCommand(String string) {
        return this.consoleCommandList.contains(string);
    }
}
