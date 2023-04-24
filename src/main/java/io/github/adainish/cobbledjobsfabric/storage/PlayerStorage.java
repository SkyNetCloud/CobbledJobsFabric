package io.github.adainish.cobbledjobsfabric.storage;

import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;
import io.github.adainish.cobbledjobsfabric.CobbledJobsFabric;
import io.github.adainish.cobbledjobsfabric.obj.data.Player;
import io.github.adainish.cobbledjobsfabric.util.Adapters;
import net.minecraft.server.level.ServerPlayer;

import java.io.*;
import java.util.UUID;

public class PlayerStorage
{
    public static void makePlayer(ServerPlayer player) {
        File dir = CobbledJobsFabric.getPlayerStorageDir();
        dir.mkdirs();


        Player gormottiPlayer = new Player(player.getUUID());

        File file = new File(dir, "%uuid%.json".replaceAll("%uuid%", String.valueOf(player.getUUID())));
        if (file.exists()) {
            CobbledJobsFabric.getLog().error("There was an issue generating the Player, Player already exists? Ending function");
            return;
        }

        Gson gson = Adapters.PRETTY_MAIN_GSON;
        String json = gson.toJson(gormottiPlayer);

        try {
            file.createNewFile();
            FileWriter writer = new FileWriter(file);
            writer.write(json);
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void savePlayerNoCache(Player player) {

        File dir = CobbledJobsFabric.getPlayerStorageDir();
        dir.mkdirs();

        File file = new File(dir, "%uuid%.json".replaceAll("%uuid%", String.valueOf(player.uuid)));
        Gson gson = Adapters.PRETTY_MAIN_GSON;
        JsonReader reader = null;
        try {
            reader = new JsonReader(new FileReader(file));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        if (reader == null) {
            CobbledJobsFabric.getLog().error("Something went wrong attempting to read the Player Data");
            return;
        }

        try {
            FileWriter writer = new FileWriter(file);
            writer.write(gson.toJson(player));
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void savePlayer(Player player) {

        File dir = CobbledJobsFabric.getPlayerStorageDir();
        dir.mkdirs();

        File file = new File(dir, "%uuid%.json".replaceAll("%uuid%", String.valueOf(player.uuid)));
        Gson gson = Adapters.PRETTY_MAIN_GSON;
        JsonReader reader = null;
        try {
            reader = new JsonReader(new FileReader(file));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        if (reader == null) {
            CobbledJobsFabric.getLog().error("Something went wrong attempting to read the Player Data");
            return;
        }

        try {
            FileWriter writer = new FileWriter(file);
            writer.write(gson.toJson(player));
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        player.updateCache();
    }

    public static Player getPlayer(UUID uuid) {
        File dir = CobbledJobsFabric.getPlayerStorageDir();
        dir.mkdirs();

        if (CobbledJobsFabric.dataWrapper.playerCache.containsKey(uuid))
            return CobbledJobsFabric.dataWrapper.playerCache.get(uuid);

        File dataFile = new File(dir, "%uuid%.json".replaceAll("%uuid%", String.valueOf(uuid)));
        Gson gson = Adapters.PRETTY_MAIN_GSON;
        JsonReader reader = null;
        try {
            reader = new JsonReader(new FileReader(dataFile));
        } catch (FileNotFoundException e) {
            CobbledJobsFabric.getLog().error("Something went wrong attempting to read the Player Data, new Player Perhaps?");
            return null;
        }

        return gson.fromJson(reader, Player.class);
    }
}
