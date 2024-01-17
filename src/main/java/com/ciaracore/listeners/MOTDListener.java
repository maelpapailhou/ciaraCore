package com.ciaracore.listeners;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.event.ProxyPingEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

public class MOTDListener implements Listener {

    @EventHandler
    public void onProxyPing(ProxyPingEvent event) {

        String motdLine1 = ChatColor.GOLD + "                 " + ChatColor.MAGIC + "||" +
                ChatColor.RESET + "" + ChatColor.AQUA + "" + ChatColor.BOLD + " CiaraCube"
                + "" + ChatColor.RESET + "" + ChatColor.AQUA + " (1.19) "
                + ChatColor.GOLD + "" + ChatColor.MAGIC + "||";

        String motdLine2 = ChatColor.GOLD + "     " + "... " + ChatColor.RESET + "" +
                ChatColor.YELLOW + "FreeCube, Bedwars, Skywars, PVP Swap" +
                ChatColor.GOLD + " ...";

        String motd = motdLine1 + "\n" + motdLine2;

        event.getResponse().setDescriptionComponent(new TextComponent(motd));
    }
}
