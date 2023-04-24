package io.github.adainish.cobbledjobsfabric.tasks;

import io.github.adainish.cobbledjobsfabric.CobbledJobsFabric;
import io.github.adainish.cobbledjobsfabric.obj.data.Player;

import java.util.ArrayList;
import java.util.List;

public class PlayerDataTask implements Runnable{
    @Override
    public void run() {
        if (CobbledJobsFabric.dataWrapper.playerCache.isEmpty())
            return;

        List<Player> toUpdate = new ArrayList<>();

        CobbledJobsFabric.dataWrapper.playerCache.forEach((key, player) -> toUpdate.add(player));

        toUpdate.forEach(Player::save);
    }
}
