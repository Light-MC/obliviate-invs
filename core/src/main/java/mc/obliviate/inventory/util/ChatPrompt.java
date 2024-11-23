package mc.obliviate.inventory.util;

import mc.obliviate.inventory.Gui;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.plugin.Plugin;

import java.util.function.Consumer;

public class ChatPrompt implements Listener {
    private static Plugin plugin;

    private final Player player;
    private final Consumer<String> onPrompt;
    private final Gui gui;
    public boolean completed = false;

    public ChatPrompt(Player player, Consumer<String> onPrompt, Gui gui) {
        this.player = player;
        this.onPrompt = onPrompt;
        this.gui = gui;
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onChat(AsyncPlayerChatEvent e) {
        if (e.getPlayer().equals(player)) {
            e.setCancelled(true);
            onPrompt.accept(e.getMessage());
        }
    }

    @EventHandler
    public void onInventoryOpen(InventoryOpenEvent e) {
        if (e.getPlayer().equals(player)) {
            finish(null);
        }
    }

    private void finish(String input) {
        completed = true;
        onPrompt.accept(input);

    }

    /**
     * This method must be called on plugin startup to initialize the plugin instance.
     * @param plugin The plugin instance.
     */
    public static void init(Plugin plugin) {
        ChatPrompt.plugin = plugin;
    }

    public static void promptPlayer(Player player, String prompt, Consumer<String> onPrompt, Long timeoutSec) {
        // save stack
        player.sendMessage(prompt);

        ChatPrompt chatPrompt = new ChatPrompt(player, onPrompt, null); //todo: get from stack
        Bukkit.getScheduler().runTaskLater(plugin, () -> {
            if (!chatPrompt.completed) {
                chatPrompt.finish(null);
            }
        }, timeoutSec * 20L);
    }
}
