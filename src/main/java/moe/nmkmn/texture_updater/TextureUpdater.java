package moe.nmkmn.texture_updater;

import moe.nmkmn.texture_updater.listeners.TextureAdaptation;
import org.bukkit.plugin.java.JavaPlugin;

public final class TextureUpdater extends JavaPlugin {
    // Retain texture download links
    public boolean first;
    public String texture;
    public String sha1;

    @Override
    public void onEnable() {
        // Load Config
        saveDefaultConfig();

        // Event Listeners
        getServer().getPluginManager().registerEvents(new TextureAdaptation(this), this);
    }
}
