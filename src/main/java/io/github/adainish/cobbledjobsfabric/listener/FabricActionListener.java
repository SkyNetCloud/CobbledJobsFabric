package io.github.adainish.cobbledjobsfabric.listener;

import com.cobblemon.mod.common.entity.pokemon.PokemonEntity;
import dev.architectury.event.events.common.PlayerEvent;
import io.github.adainish.cobbledjobsfabric.enumerations.JobAction;
import io.github.adainish.cobbledjobsfabric.obj.data.Player;
import io.github.adainish.cobbledjobsfabric.storage.PlayerStorage;
import net.fabricmc.fabric.api.entity.event.v1.ServerEntityCombatEvents;
import net.fabricmc.fabric.api.event.player.PlayerBlockBreakEvents;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.block.Block;

public class FabricActionListener
{

    public FabricActionListener()
    {
        registerKilling();
        registerCrafting();
        registerMining();
        registerFishing();
    }

    public void registerFishing()
    {

//        Bobb
//        PlayerEvent.B.register((serverPlayer, constructed, inventory) -> {
//            if (constructed == null)
//                return;
//
//            if (constructed.isEmpty())
//                return;
//            try {
//                Player player = PlayerStorage.getPlayer(serverPlayer.getUUID());
//                if (player != null) {
//                    //update job data for mining
//
//                    Optional<RegistryKey<Item>> location = BuiltInRegistries.ITEM.getKey(constructed.getItem());
//                    player.updateJobData(JobAction.Craft, location.toString());
//                    player.updateCache();
//                }
//            } catch (Exception e) {
//
//            }
//        });
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
            if (world.isClientSide())
                return;
            if (blockEntity == null)
                return;
            try {
                Player player = PlayerStorage.getPlayer(serverPlayer.getUUID());
                if (player != null) {
                    //update job data for mining

                    Block block = blockEntity.getBlockState().getBlock();
                    ResourceLocation location = BuiltInRegistries.ITEM.getKey(block.asItem());
                    player.updateJobData(JobAction.Mine, location.toString());
                    player.updateCache();
                }
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
