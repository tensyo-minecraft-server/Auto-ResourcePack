package moe.nmkmn.texture_updater.utils;

import com.google.gson.Gson;
import moe.nmkmn.texture_updater.TextureUpdater;
import moe.nmkmn.texture_updater.model.Assets;
import moe.nmkmn.texture_updater.model.Release;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.HoverEvent;
import okhttp3.*;
import okio.BufferedSink;
import okio.Okio;
import org.apache.commons.codec.digest.DigestUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Objects;
import java.util.function.Consumer;

public class Texture {
    private final TextureUpdater plugin;

    @SuppressWarnings("deprecation")
    private final String prefix = ChatColor.GRAY + "[" + ChatColor.AQUA + ChatColor.BOLD + "TextureUpdater" + ChatColor.GRAY + "] ";
    private final OkHttpClient client = new OkHttpClient();

    public Texture(TextureUpdater plugin) {
        this.plugin = plugin;
    }

    public void getLatest(Consumer<String> callback) {
        Request request = new Request.Builder()
                .url(Objects.requireNonNull(plugin.getConfig().getString("url")))
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                plugin.getLogger().severe(e.getMessage());
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                // Get Response
                String body = Objects.requireNonNull(response.body()).string();

                // Get the URL of the resource pack
                try {
                    Gson gson = new Gson();
                    Release release = gson.fromJson(body, Release.class);

                    for (Assets assets : release.assets) {
                        if (Objects.equals(assets.name, plugin.getConfig().get("file"))) {
                            if (!Objects.equals(plugin.texture, assets.browser_download_url)) {
                                reload(assets.browser_download_url);
                                callback.accept(assets.browser_download_url);
                                return;
                            }
                        }
                    }
                } catch (Exception e) {
                    plugin.getLogger().severe(e.getMessage());
                }

                callback.accept(plugin.texture); // If no new texture URL is found, use the existing one
            }
        });
    }

    @SuppressWarnings("deprecation")
    public void reload(String url) {
        plugin.texture = url;
        plugin.sha1 = null;

        if (plugin.first) {
            for (Player player : Bukkit.getServer().getOnlinePlayers()) {
                player.sendMessage(
                        Component.text(prefix).append(
                                Component.text(ChatColor.GREEN + "新しいリソースパックがリリースされました！")
                        ));
            }
        }

        plugin.first = true;
    }

    @SuppressWarnings("deprecation")
    public void apply(Player player) {
        // Get the latest URL
        getLatest(url -> {
            if (url == null) {
                player.sendMessage(
                        Component.text(prefix).append(
                                Component.text(ChatColor.RED + "リソースパックのURLが取得できませんでした。 / Resource pack URL could not be retrieved.")
                        )
                );
                return;
            }

            // Get SHA1 for a file
            if (plugin.sha1 == null) {
                Request request = new Request.Builder()
                        .url(url)
                        .build();

                client.newCall(request).enqueue(new Callback() {
                    @Override
                    public void onFailure(@NotNull Call call, @NotNull IOException e) {
                        plugin.getLogger().severe(e.getMessage());
                        player.sendMessage(
                                Component.text(prefix).append(
                                        Component.text(ChatColor.RED + "リソースパックのダウンロードに失敗しました。 / Resource pack download failed.")
                                )
                        );
                    }

                    @Override
                    public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                        if (!response.isSuccessful()) {
                            player.sendMessage(
                                    Component.text(prefix).append(
                                            Component.text(ChatColor.RED + "リソースパックのダウンロードに失敗しました。 / Resource pack download failed.")
                                    )
                            );

                            return;
                        }

                        Path temp = Files.createTempFile("resource-pack", ".zip");
                        try (BufferedSink sink = Okio.buffer(Okio.sink(temp))) {
                            long totalBytes = Objects.requireNonNull(response.body()).contentLength();
                            long readBytes = 0;
                            long bytesRead;

                            while ((bytesRead = response.body().source().read(sink.buffer(), 8192)) != -1) {
                                readBytes += bytesRead;
                                int percent = (int) (readBytes * 100.0 / totalBytes);
                                player.sendActionBar(Component.text("リソースパックをダウンロード中... (" + percent + "%)"));
                            }
                        }

                        plugin.sha1 = DigestUtils.sha1Hex(Files.readAllBytes(temp));

                        player.sendMessage(
                                Component.text(prefix).append(
                                        Component.text(ChatColor.GREEN + "リソースパックの適応中...")
                                )
                        );
                        player.setResourcePack(url, plugin.sha1);
                        player.sendMessage(
                                Component.text(prefix).append(
                                        Component.text(ChatColor.GREEN + "適応しました。")
                                                .hoverEvent(
                                                        HoverEvent.showText(
                                                                Component.text("SHA-1: " + plugin.sha1)
                                                        )
                                                )
                                )
                        );
                    }
                });
            } else {
                // SHA-1 already calculated, apply resource pack directly
                player.sendMessage(
                        Component.text(prefix).append(
                                Component.text(ChatColor.GREEN + "リソースパックの適応中...")
                        )
                );
                player.setResourcePack(url, plugin.sha1);
                player.sendMessage(
                        Component.text(prefix).append(
                                Component.text(ChatColor.GREEN + "適応しました。")
                                        .hoverEvent(
                                                HoverEvent.showText(
                                                        Component.text("SHA-1: " + plugin.sha1)
                                                )
                                        )
                        )
                );
            }
        });
    }
}
