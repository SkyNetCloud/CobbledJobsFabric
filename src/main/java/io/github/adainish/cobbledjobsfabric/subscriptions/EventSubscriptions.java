package io.github.adainish.cobbledjobsfabric.subscriptions;

import com.cobblemon.mod.common.api.Priority;
import com.cobblemon.mod.common.api.events.CobblemonEvents;
import com.cobblemon.mod.common.platform.events.PlatformEvents;
import io.github.adainish.cobbledjobsfabric.enumerations.JobAction;
import io.github.adainish.cobbledjobsfabric.obj.data.Player;
import io.github.adainish.cobbledjobsfabric.storage.PlayerStorage;
import kotlin.Unit;

import java.util.Objects;


public class EventSubscriptions {

    public EventSubscriptions() {
        subscribeToPlayerLogin();
        subscribeToPlayerLogout();
        subscribeToCapture();
        subscribeToFainting();
        subscribeToEvolving();
        subscribeToTrade();
    }

    public void subscribeToEvolving() {
        CobblemonEvents.EVOLUTION_COMPLETE.subscribe(Priority.NORMAL, event -> {
            Player player = PlayerStorage.getPlayer(event.component1().getOwnerPlayer().getUUID());
            try {
                if (player != null) {
                    //update job data for evolving
                    player.updateJobData(JobAction.Evolve, event.component1().getSpecies().resourceIdentifier.toString());
                    player.updateCache();
                }
            } catch (Exception e) {
                return Unit.INSTANCE;
            }

            return Unit.INSTANCE;
        });

    }

    public void subscribeToFainting() {
        // Doesn't pick up on wild Fainting
        CobblemonEvents.POKEMON_FAINTED.subscribe(Priority.NORMAL, event -> {
            Player player = PlayerStorage.getPlayer(event.component1().getOwnerUUID());
                try {
                    if (player != null) {
                        if (event.getPokemon().isWild()) {
                            player.updateJobData(JobAction.Kill, event.component1().getSpecies().resourceIdentifier.toString());
                            player.updateCache();
                        }
                    }
                } catch (Exception e) {
                    return Unit.INSTANCE;
                }
            return Unit.INSTANCE;
        });

    }

    public void subscribeToTrade() {
        CobblemonEvents.TRADE_COMPLETED.subscribe(Priority.NORMAL, event -> {
            Player player = PlayerStorage.getPlayer(event.getTradeParticipant1().getUuid());
            Player player2 = PlayerStorage.getPlayer(event.getTradeParticipant2().getUuid());
            if (player != null && player2 != null) {
                player.updateJobData(JobAction.Trade, event.getTradeParticipant1Pokemon().getSpecies().resourceIdentifier.toString());
                player2.updateJobData(JobAction.Trade, event.getTradeParticipant2Pokemon().getSpecies().resourceIdentifier.toString());
                player2.updateCache();
                player.updateCache();
            }
            return Unit.INSTANCE;
        });
    }

    public void subscribeToCapture() {
        CobblemonEvents.POKEMON_CAPTURED.subscribe(Priority.NORMAL, event -> {
            Player player = PlayerStorage.getPlayer(event.getPlayer().getUUID());
            if (player != null) {
                //update job data for capturing
                player.updateJobData(JobAction.Capture, event.component1().getSpecies().resourceIdentifier.toString());
                player.updateCache();
            }
            return Unit.INSTANCE;
        });
    }
    public void subscribeToPlayerLogin() {
        PlatformEvents.SERVER_PLAYER_LOGIN.subscribe(Priority.NORMAL, e -> {

            Player player = PlayerStorage.getPlayer(e.getPlayer().getUUID());
            if (player == null) {
                PlayerStorage.makePlayer(e.getPlayer());
                player = PlayerStorage.getPlayer(e.getPlayer().getUUID());
            }

            if (player != null) {
                player.userName = e.getPlayer().getName().plainCopy().toString();
                player.syncWithJobManager();
                player.updateCache();
            }

            return Unit.INSTANCE;
        });
    }

    public void subscribeToPlayerLogout() {
        PlatformEvents.SERVER_PLAYER_LOGOUT.subscribe(Priority.NORMAL, e -> {
            Player player = PlayerStorage.getPlayer(e.getPlayer().getUUID());
            if (player != null) {
                player.save();
            }
            return Unit.INSTANCE;
        });
    }
}
