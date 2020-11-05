package pw.biome.iplogger;

import co.aikar.commands.PaperCommandManager;
import lombok.Getter;
import org.bukkit.plugin.java.JavaPlugin;
import pw.biome.iplogger.commands.LoggerCommand;

import java.util.*;

public final class IPLogger extends JavaPlugin {

    @Getter
    private static final HashMap<String, HashSet<UUID>> playerIpMap = new HashMap<>();

    @Getter
    private static IPLogger instance;

    @Override
    public void onEnable() {
        instance = this;
        saveDefaultConfig();
        loadAllToMemory();

        PaperCommandManager manager = new PaperCommandManager(instance);
        manager.registerCommand(new LoggerCommand());
    }

    @Override
    public void onDisable() {
        saveAllToFile();
    }

    public static HashSet<UUID> getClashes(String address) {
        return playerIpMap.get(address);
    }

    public static List<UUID> getClashesForUUID(UUID uuid) {
        List<UUID> clashList = new ArrayList<>();
        playerIpMap.forEach((address, clashUuid) -> {
            if (clashUuid.contains(uuid)) {
                clashUuid.forEach(clash -> {
                    if (!clash.equals(uuid)) clashList.add(clash);
                });
            }
        });

        return clashList;
    }

    /**
     * Add address to a user
     *
     * @param uuid    to add address to
     * @param address to add
     */
    public static void addAddress(UUID uuid, String address) {
        HashSet<UUID> users = playerIpMap.getOrDefault(address, new HashSet<>());
        users.add(uuid);
        playerIpMap.put(address, users);
        instance.saveAllToFile();
    }

    /**
     * Load all entries from file
     */
    public void loadAllToMemory() {
        for (String key : getConfig().getKeys(false)) {
            HashSet<UUID> hashSet = new HashSet<>();
            getConfig().getStringList(key).forEach(uuidString -> hashSet.add(UUID.fromString(uuidString)));
            playerIpMap.put(key, hashSet);
        }
        getLogger().info("Loaded all data to memory");
    }

    /**
     * Save all entries to file
     */
    public void saveAllToFile() {
        playerIpMap.forEach((address, users) -> {
            List<String> userList = new ArrayList<>();
            users.forEach(uuid -> userList.add(uuid.toString()));
            getConfig().set(address, userList);
        });
        saveConfig();
    }
}
