package com.ciaracore.commands;

import com.ciaracore.CiaraCore;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;
import net.md_5.bungee.api.plugin.Plugin;

import java.util.Collection;

import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;
import net.md_5.bungee.api.plugin.Plugin;

import java.util.Collection;

public class PluginCommand extends Command {

    public PluginCommand() {
        super("pls");
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (sender instanceof ProxiedPlayer) {
            ProxiedPlayer player = (ProxiedPlayer) sender;

            // Récupérer la liste des plugins
            Collection<Plugin> plugins = ProxyServer.getInstance().getPluginManager().getPlugins();

            // Afficher la liste des plugins BungeeCord
            player.sendMessage("Liste des plugins BungeeCord :");
            for (Plugin plugin : plugins) {
                player.sendMessage("- " + plugin.getDescription().getName() +
                        " v" + plugin.getDescription().getVersion());
            }
        }
    }
}
