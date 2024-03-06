package io.github.adainish.cobbledjobsfabric.listener;

import com.cobblemon.mod.common.api.events.CobblemonEvents;
import com.cobblemon.mod.common.api.events.pokemon.TradeCompletedEvent;
import com.cobblemon.mod.common.entity.pokemon.PokemonEntity;
import com.cobblemon.mod.common.platform.events.PlatformEvents;
import dev.architectury.event.events.common.InteractionEvent;
import dev.architectury.event.events.common.PlayerEvent;
import io.github.adainish.cobbledjobsfabric.CobbledJobsFabric;
import io.github.adainish.cobbledjobsfabric.config.LanguageConfig;
import io.github.adainish.cobbledjobsfabric.enumerations.JobAction;
import io.github.adainish.cobbledjobsfabric.obj.data.Player;
import io.github.adainish.cobbledjobsfabric.storage.PlayerStorage;
import net.fabricmc.fabric.api.entity.event.v1.ServerEntityCombatEvents;
import net.fabricmc.fabric.api.event.player.PlayerBlockBreakEvents;


import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;

public class FabricActionListener
{

    public FabricActionListener()
    {
        registerKilling();
        registerCrafting();
        registerMining();
        registerFishing();
        registerSmelting();
    }

    public void registerFishing()
    {

    }

    public void registerSmelting() {
        PlayerEvent.SMELT_ITEM.register((playerServer, smelted) -> {
            Player player = PlayerStorage.getPlayer(playerServer.getUUID());
            try {
                if (player != null) {
                    //CobbledJobsFabric.getLog().info("Item is cooked");
                    ResourceLocation localtion = BuiltInRegistries.ITEM.getKey(smelted.getItem().asItem());
                    player.updateJobData(JobAction.Smelt, localtion.toString());
                    player.updateCache();
                }
            } catch (Exception e) {

            }
        });
    }


    public void registerCrafting()
    {
        PlayerEvent.CRAFT_ITEM.register((serverPlayer, constructed, inventory) -> {
            if (constructed == null)
                return;

            if (constructed.isEmpty())
                return;
            try {
                Player player = PlayerStorage.getPlayer(serverPlayer.getUUID());
                if (player != null) {
                    //update job data for mining
                    ResourceLocation location = BuiltInRegistries.ITEM.getKey(constructed.getItem());
                    player.updateJobData(JobAction.Craft, location.toString());
                    player.updateCache();
                }
            } catch (Exception e) {

            }
        });
    }

    public void registerMining() {
        PlayerBlockBreakEvents.AFTER.register((world, serverPlayer, pos, state, blockEntity) -> {
//            if (world.isClientSide())
//                return;
//            if (blockEntity == null)
//                return;
            Player player = PlayerStorage.getPlayer(serverPlayer.getUUID());
                try {
                    //CobbledJobsFabric.getLog().info("This is working");
                    Block block = state.getBlock();
                    ResourceLocation location = BuiltInRegistries.ITEM.getKey(Item.byBlock(block));
                    player.updateJobData(JobAction.Mine, location.toString());
                    player.updateCache();
                } catch (Exception e) {

                }
        });
    }



    public void registerKilling()
    {
        ServerEntityCombatEvents.AFTER_KILLED_OTHER_ENTITY.register((world, entity, killedEntity) -> {
            if (world.isClientSide())
                return;
            if (entity instanceof ServerPlayer)
            {
                try {
                    Player player = PlayerStorage.getPlayer(entity.getUUID());
                    if (player != null)
                    {
                        //update job data for killing
                        ResourceLocation location = null;
                        if (killedEntity instanceof PokemonEntity)
                        {
                            location = ((PokemonEntity) killedEntity).getPokemon().getSpecies().getResourceIdentifier();
                        } else {
                            location = BuiltInRegistries.ENTITY_TYPE.getKey(entity.getType());
                        }
                        player.updateJobData(JobAction.Kill, location.toString());
                        player.updateCache();
                    }
                } catch (Exception e)
                {

                }
            }
        });
    }
}
