package pw.biome.iplogger.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandCompletion;
import co.aikar.commands.annotation.CommandPermission;
import co.aikar.commands.annotation.Description;
import co.aikar.commands.annotation.Subcommand;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import pro.husk.whitelistsql.WhitelistSQL;
import pro.husk.whitelistsql.utility.UUIDFetcher;
import pw.biome.iplogger.IPLogger;

@CommandAlias("iplogger|ipl")
@Description("IPLogger commands")
public class LoggerCommand extends BaseCommand {

    @Subcommand("check|c")
    @CommandPermission("iplogger.admin")
    @CommandCompletion("@players")
    @Description("Checks a users prior IPs")
    public void check(CommandSender sender, OfflinePlayer player) {
        if (player != null) {
            Bukkit.getScheduler().runTaskAsynchronously(IPLogger.getInstance(), () -> {
                sender.sendMessage(ChatColor.GOLD + "Clashes for user: " + ChatColor.AQUA + player.getName());
                IPLogger.getClashesForUUID(player.getUniqueId()).forEach(clash -> sender.sendMessage(ChatColor.RED + UUIDFetcher.getName(clash)));
            });
        }
    }

    @Subcommand("ip|i")
    @CommandPermission("iplogger.admin")
    @Description("Checks who has logged in with given IP")
    public void ip(CommandSender sender, String address) {
        Bukkit.getScheduler().runTaskAsynchronously(IPLogger.getInstance(), () -> {
            sender.sendMessage(ChatColor.GOLD + "Clashes for IP: " + ChatColor.AQUA + address);
            IPLogger.getClashes(address).forEach(clash -> sender.sendMessage(ChatColor.RED + UUIDFetcher.getName(clash)));
        });
    }
}
