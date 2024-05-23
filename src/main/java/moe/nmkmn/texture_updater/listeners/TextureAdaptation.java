package moe.nmkmn.texture_updater.listeners;

import moe.nmkmn.texture_updater.TextureUpdater;
import moe.nmkmn.texture_updater.utils.Texture;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class TextureAdaptation implements Listener {
    private final TextureUpdater plugin;

    public TextureAdaptation(TextureUpdater plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        Texture texture = new Texture(plugin);

        // Adapting textures to the player
        Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> texture.apply(event.getPlayer()));
    }
}
