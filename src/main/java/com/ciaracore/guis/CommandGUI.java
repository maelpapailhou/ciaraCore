package com.ciaracore.guis;

import com.ciaracore.CiaraCore;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

public class CommandGUI extends Command {

    private final GUIManager guiManager;

    public CommandGUI(CiaraCore ciaraCore, GUIManager guiManager, String trigger) {
        super(trigger);

        this.guiManager = guiManager;
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (sender instanceof ProxiedPlayer) {
            ProxiedPlayer player = (ProxiedPlayer) sender;

            // Vérifiez si l'utilisateur a la permission d'utiliser la commande
            if (player.hasPermission("gui.use." + getName())) {
                // Ouvrez le GUI associé à cette commande
                guiManager.openGUI(player, getName());
            } else {
                player.sendMessage(ChatColor.RED + "Vous n'avez pas la permission d'utiliser cette commande.");
            }
        } else {
            sender.sendMessage(ChatColor.RED + "Cette commande doit être exécutée par un joueur.");
        }
    }
}
