package com.ciaracore.commands;

import com.ciaracore.CiaraCore;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;
import net.md_5.bungee.api.plugin.Plugin;
import org.yaml.snakeyaml.Yaml;

import java.io.InputStream;
import java.util.List;
import java.util.Map;

public class HelpCommand extends Command {
    private CiaraCore ciaraCore;

    public HelpCommand(CiaraCore ciaraCore) {
        super("help");
        this.ciaraCore = ciaraCore;
    }

    @Override
    public void execute(net.md_5.bungee.api.CommandSender sender, String[] args) {
        if (sender instanceof ProxiedPlayer) {
            ProxiedPlayer player = (ProxiedPlayer) sender;
            String serverName = player.getServer().getInfo().getName();

            List<Map<String, Object>> serverHelp = getServerHelp(serverName);

            if (serverHelp != null) {
                for (Map<String, Object> line : serverHelp) {
                    String text = ChatColor.translateAlternateColorCodes('&', (String) line.get("text"));
                    String hover = ChatColor.translateAlternateColorCodes('&', (String) line.get("hover"));
                    String action = (String) line.get("action");

                    // Create a component with hover text
                    BaseComponent[] component = new ComponentBuilder(text)
                            .event(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(hover).create()))
                            .create();

                    // Send the help message to the player
                    player.sendMessage(component);
                }
            } else {
                player.sendMessage(new TextComponent(ChatColor.RED + "Aucune aide disponible pour ce serveur."));
            }
        }
    }

    private List<Map<String, Object>> getServerHelp(String serverName) {
        try (InputStream input = ciaraCore.getResourceAsStream("help.yml")) {
            if (input != null) {
                Yaml yaml = new Yaml();
                List<Map<String, Object>> helpConfig = yaml.load(input);

                for (Map<String, Object> serverEntry : helpConfig) {
                    String entryServerName = (String) serverEntry.get("server");

                    if (serverName.equalsIgnoreCase(entryServerName)) {
                        return (List<Map<String, Object>>) serverEntry.get("lines");
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }
}
