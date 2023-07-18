package dev.clxud.chatfilter;

import io.papermc.paper.event.player.AsyncChatEvent;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextReplacementConfig;
import net.kyori.adventure.text.logger.slf4j.ComponentLogger;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.io.*;
import java.net.URL;
import java.util.ArrayList;

public final class Chatfilter extends JavaPlugin implements @NotNull Listener {

    public final ArrayList<String> badWords = new ArrayList<>();


    @Override
    public void onEnable() {
        try {
            InputStream in = this.getClass().getClassLoader().getResourceAsStream("words.txt");
            BufferedReader reader = new BufferedReader(new InputStreamReader(in));
            String line = reader.readLine();
            while (line != null) {
                badWords.add(line);
                line = reader.readLine();
            }
            reader.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        getLogger().info("Chatfilter enabled!");

        //register events
        getServer().getPluginManager().registerEvents(this, this);

    }

    @Override
    public @NotNull ComponentLogger getComponentLogger() {
        return super.getComponentLogger();
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        getLogger().info("Chatfilter disabled!");
    }

    @EventHandler
    public void onChat(AsyncChatEvent event) {
        Player player = event.getPlayer();
        Component message = event.message();

        if (player.hasPermission("chatfilter.bypass")) return;

        for (String word : badWords) {
            // get length of word and replace with *

            if (message.toString().toLowerCase().contains(word)) {
                String replace = "";

                for (int i = 0; i < word.length(); i++) {
                    replace += "*";
                }
                message = message.replaceText(TextReplacementConfig.builder().matchLiteral(word).replacement(replace).build());
                event.message(message);
            }
        }




    }
}
