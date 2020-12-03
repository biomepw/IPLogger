package pw.biome.iplogger.listeners;

import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import pro.husk.whitelistsql.utility.UUIDFetcher;
import pw.biome.iplogger.IPLogger;

import java.util.HashSet;
import java.util.UUID;

public class LoggerListener implements Listener {

    @EventHandler
    public void asyncPlayerPreLoginEvent(AsyncPlayerPreLoginEvent event) {
        UUID uuid = event.getUniqueId();
        String address = event.getAddress().getHostAddress();

        // Post the task async
        Bukkit.getScheduler().runTaskAsynchronously(IPLogger.getInstance(), () -> {
            HashSet<UUID> clashSet = IPLogger.getClashes(address);

            if (clashSet != null && !clashSet.isEmpty()) {
                // Don't report users who only clash with themselves
                if (clashSet.size() == 1 && clashSet.contains(uuid)) {
                    return;
                }

                Bukkit.broadcast(ChatColor.RED + event.getName() + " has an IP clash with users:", "iplogger.admin");

                clashSet.forEach(clashUUID -> {
                    if (!clashUUID.equals(uuid)) {
                        String name = UUIDFetcher.getName(uuid);
                        Bukkit.broadcast(ChatColor.GOLD + "- " + name, "iplogger.admin");
                    }
                });
            }

            IPLogger.addAddress(uuid, address);
        });
    }
}
