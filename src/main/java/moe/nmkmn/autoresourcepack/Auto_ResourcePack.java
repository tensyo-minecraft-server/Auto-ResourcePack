package moe.nmkmn.autoresourcepack;

import com.google.gson.Gson;
import moe.nmkmn.autoresourcepack.model.Release;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.Objects;

public final class Auto_ResourcePack extends JavaPlugin implements Listener {
    FileConfiguration configuration = getConfig();

    @Override
    public void onEnable() {
        getLogger().info("Enabling Auto ResourcePack.");

        // Config
        configuration.addDefault("url", "https://api.github.com/repos/tensyo-minecraft-server/ResourcePacks/releases/latest");
        configuration.addDefault("file", "Minigame-Resource.zip");

        configuration.options().copyDefaults(true);
        saveConfig();

        // Event
        getServer().getPluginManager().registerEvents(this, this);
    }

    @Override
    public void onDisable() {
        getLogger().info("Disabling Auto ResourcePack.");
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        try {
            String url = configuration.getString("url");
            String json = readUrl(url);

            Gson gson = new Gson();
            Release release = gson.fromJson(json, Release.class);

            Player player = event.getPlayer();

            if (Objects.equals(release.assets.get(0).name, configuration.getString("file"))) {
                player.setResourcePack(release.assets.get(0).browser_download_url);
            } else if (Objects.equals(release.assets.get(1).name, configuration.getString("file"))) {
                player.setResourcePack(release.assets.get(0).browser_download_url);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static String readUrl(String urlString) throws Exception {
        BufferedReader reader = null;
        try {
            URL url = new URL(urlString);
            reader = new BufferedReader(new InputStreamReader(url.openStream()));
            StringBuffer buffer = new StringBuffer();
            int read;
            char[] chars = new char[1024];
            while ((read = reader.read(chars)) != -1)
                buffer.append(chars, 0, read);

            return buffer.toString();
        } finally {
            if (reader != null)
                reader.close();
        }
    }
}
