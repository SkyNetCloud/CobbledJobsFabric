package io.github.adainish.cobbledjobsfabric.obj.configurabledata;

import com.cobblemon.mod.common.api.pokemon.PokemonSpecies;
import io.github.adainish.cobbledjobsfabric.enumerations.DefaultJobs;
import io.github.adainish.cobbledjobsfabric.enumerations.JobAction;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Blocks;

import java.util.HashMap;
import java.util.Objects;

import static net.minecraft.core.registries.BuiltInRegistries.*;

public class JobManager
{
    public HashMap<String, ConfigurableJob> configurableJobs = new HashMap<>();

    public JobManager()
    {

    }

    public void initDefaults()
    {
        if (configurableJobs.isEmpty())
        {
            for (DefaultJobs defaultJob:DefaultJobs.values()) {
                ConfigurableJob configurableJob = new ConfigurableJob();
                configurableJob.id = defaultJob.name().toLowerCase();
                configurableJob.prettyName = "&b" + defaultJob.name();
                configurableJob.enabled = true;
                switch (defaultJob)
                {
                    case Miner -> {
                        JobType jobType = new JobType();
                        jobType.prettyTitle = "&b" + "Mine";
                        //load default action keys
                        for (int i = 0; i < 6; i++) {
                            ActionKey actionKey = new ActionKey();
                            actionKey.baseExperience = 1;
                            actionKey.baseWorth = 1;
                            switch (i)
                            {
                                case 0 -> {
                                    ResourceLocation location = ITEM.getKey(Items.STONE.asItem());
                                    actionKey.actionKey = location.toString().toLowerCase();
                                    break;
                                }
                                case 1 -> {
                                    ResourceLocation location = ITEM.getKey(Items.DIAMOND_ORE.asItem());
                                    actionKey.actionKey = location.toString().toLowerCase();
                                    break;
                                }
                                case 2 -> {
                                    ResourceLocation location = ITEM.getKey(Items.ANDESITE.asItem());
                                    actionKey.actionKey = location.toString().toLowerCase();
                                    break;
                                }
                                case 3 -> {
                                    ResourceLocation location = ITEM.getKey(Items.COBBLESTONE.asItem());
                                    actionKey.actionKey = location.toString().toLowerCase();
                                }
                                case 4 -> {
                                    ResourceLocation location = ITEM.getKey(Items.GOLD_ORE.asItem());
                                    actionKey.actionKey = location.toString().toLowerCase();
                                }
                                case 5 -> {
                                    ResourceLocation location = ITEM.getKey(Items.IRON_ORE.asItem());
                                    actionKey.actionKey = location.toString().toLowerCase();
                                }

                            }
                            actionKey.prettyString = actionKey.actionKey
                                    .replace("cobblemon:", "")
                                    .replace("minecraft:", "")
                                    .replace("_", " ");
                            jobType.actionKeys.put(actionKey.actionKey, actionKey);
                        }
                        configurableJob.jobTypes.put(JobAction.Mine.name().toLowerCase(), jobType);
                        break;
                    }
                    case Hunter -> {
                        JobType jobType = new JobType();
                        //load default action keys
                        jobType.prettyTitle = "&bMob " + defaultJob.name();
                        //vanilla entities
                        for (int i = 0; i < 6; i++) {
                            ActionKey actionKey = new ActionKey();
                            actionKey.baseExperience = 1;
                            actionKey.baseWorth = 1;
                            switch (i)
                            {
                                case 0 -> {

                                    ResourceLocation location = ENTITY_TYPE.getKey(EntityType.BLAZE);
                                    actionKey.actionKey = location.toString().toLowerCase();
                                    break;
                                }
                                case 1 -> {
                                    ResourceLocation location = ENTITY_TYPE.getKey(EntityType.ZOMBIE);
                                    actionKey.actionKey = location.toString().toLowerCase();
                                    break;
                                }
                                case 2 -> {
                                    ResourceLocation location = ENTITY_TYPE.getKey(EntityType.SKELETON);
                                    actionKey.actionKey = location.toString().toLowerCase();
                                    break;
                                }
                                case 3 -> {
                                    ResourceLocation location = ENTITY_TYPE.getKey(EntityType.CREEPER);
                                    actionKey.actionKey = location.toString().toLowerCase();
                                }
                                case 4 -> {
                                    ResourceLocation location = ENTITY_TYPE.getKey(EntityType.ENDERMAN);
                                    actionKey.actionKey = location.toString().toLowerCase();
                                }
                                case 5 -> {
                                    ResourceLocation location = ENTITY_TYPE.getKey(EntityType.SPIDER);
                                    actionKey.actionKey = location.toString().toLowerCase();
                                }

                            }
                            jobType.actionKeys.put(actionKey.actionKey, actionKey);
                        }

                        // cobblemon
                        for (int i = 0; i < 6; i++) {
                            ActionKey actionKey = new ActionKey();
                            actionKey.baseExperience = 1;
                            actionKey.baseWorth = 1;
                            switch (i)
                            {
                                case 0 -> {
                                    ResourceLocation location = Objects.requireNonNull(PokemonSpecies.INSTANCE.getByName("charmander")).getResourceIdentifier();
                                    actionKey.actionKey = location.toString().toLowerCase();
                                    break;
                                }
                                case 1 -> {
                                    ResourceLocation location = Objects.requireNonNull(PokemonSpecies.INSTANCE.getByName("charmeleon")).getResourceIdentifier();
                                    actionKey.actionKey = location.toString().toLowerCase();
                                    break;
                                }
                                case 2 -> {
                                    ResourceLocation location = Objects.requireNonNull(PokemonSpecies.INSTANCE.getByName("charizard")).getResourceIdentifier();
                                    actionKey.actionKey = location.toString().toLowerCase();
                                    break;
                                }
                                case 3 -> {
                                    ResourceLocation location = Objects.requireNonNull(PokemonSpecies.INSTANCE.getByName("vulpix")).getResourceIdentifier();
                                    actionKey.actionKey = location.toString().toLowerCase();
                                }
                                case 4 -> {
                                    ResourceLocation location = Objects.requireNonNull(PokemonSpecies.INSTANCE.getByName("ninetales")).getResourceIdentifier();
                                    actionKey.actionKey = location.toString().toLowerCase();
                                }
                                case 5 -> {
                                    ResourceLocation location = Objects.requireNonNull(PokemonSpecies.INSTANCE.getByName("pikachu")).getResourceIdentifier();
                                    actionKey.actionKey = location.toString().toLowerCase();
                                }

                            }
                            jobType.actionKeys.put(actionKey.actionKey, actionKey);
                        }
                        configurableJob.jobTypes.put(JobAction.Kill.name().toLowerCase(), jobType);
                        break;
                    }
                    case Crafter -> {
                        JobType jobType = new JobType();
                        //load default action keys
                        jobType.prettyTitle = "&b" + defaultJob.name();
                        for (int i = 0; i < 6; i++) {
                            ActionKey actionKey = new ActionKey();
                            actionKey.baseExperience = 1;
                            actionKey.baseWorth = 1;
                            switch (i)
                            {
                                case 0 -> {
                                    ResourceLocation location = ITEM.getKey(Items.BIRCH_BOAT);
                                    actionKey.actionKey = location.toString().toLowerCase();
                                    break;
                                }
                                case 1 -> {
                                    ResourceLocation location = ITEM.getKey(Items.IRON_AXE);
                                    actionKey.actionKey = location.toString().toLowerCase();
                                    break;
                                }
                                case 2 -> {
                                    ResourceLocation location = ITEM.getKey(Items.IRON_SHOVEL);
                                    actionKey.actionKey = location.toString().toLowerCase();
                                    break;
                                }
                                case 3 -> {
                                    ResourceLocation location = ITEM.getKey(Items.IRON_SWORD);
                                    actionKey.actionKey = location.toString().toLowerCase();
                                }
                                case 4 -> {
                                    ResourceLocation location = ITEM.getKey(Items.IRON_PICKAXE);
                                    actionKey.actionKey = location.toString().toLowerCase();
                                }
                                case 5 -> {
                                    ResourceLocation location = ITEM.getKey(Items.IRON_BLOCK);
                                    actionKey.actionKey = location.toString().toLowerCase();
                                }

                            }
                            jobType.actionKeys.put(actionKey.actionKey, actionKey);
                        }
                        configurableJob.jobTypes.put(JobAction.Craft.name().toLowerCase(), jobType);
                        break;
                    }
                    case Collector -> {
                        JobType jobType = new JobType();
                        jobType.prettyTitle = "&b" + "Capture Pokemon";
                        JobType secondJobType = new JobType();
                        secondJobType.prettyTitle = "&b" + "Evolve Pokemon";
                        //load default action keys
                        for (int i = 0; i < 6; i++) {
                            ActionKey actionKey = new ActionKey();
                            actionKey.baseExperience = 1;
                            actionKey.baseWorth = 1;
                            switch (i)
                            {
                                case 0 -> {
                                    ResourceLocation location = Objects.requireNonNull(PokemonSpecies.INSTANCE.getByName("charmander")).getResourceIdentifier();
                                    actionKey.actionKey = location.toString().toLowerCase();
                                    break;
                                }
                                case 1 -> {
                                    ResourceLocation location = Objects.requireNonNull(PokemonSpecies.INSTANCE.getByName("charmeleon")).getResourceIdentifier();
                                    actionKey.actionKey = location.toString().toLowerCase();
                                    break;
                                }
                                case 2 -> {
                                    ResourceLocation location = Objects.requireNonNull(PokemonSpecies.INSTANCE.getByName("charizard")).getResourceIdentifier();
                                    actionKey.actionKey = location.toString().toLowerCase();
                                    break;
                                }
                                case 3 -> {
                                    ResourceLocation location = Objects.requireNonNull(PokemonSpecies.INSTANCE.getByName("vulpix")).getResourceIdentifier();
                                    actionKey.actionKey = location.toString().toLowerCase();
                                }
                                case 4 -> {
                                    ResourceLocation location = PokemonSpecies.INSTANCE.getByName("ninetales").getResourceIdentifier();
                                    actionKey.actionKey = location.toString().toLowerCase();
                                }
                                case 5 -> {
                                    ResourceLocation location = PokemonSpecies.INSTANCE.getByName("pikachu").getResourceIdentifier();
                                    actionKey.actionKey = location.toString().toLowerCase();
                                }

                            }
                            jobType.actionKeys.put(actionKey.actionKey, actionKey);
                        }

                        for (int i = 0; i < 6; i++) {
                            ActionKey actionKey = new ActionKey();
                            actionKey.baseExperience = 1;
                            actionKey.baseWorth = 1;
                            switch (i)
                            {
                                case 0 -> {
                                    ResourceLocation location = PokemonSpecies.INSTANCE.getByName("wartortle").getResourceIdentifier();
                                    actionKey.actionKey = location.toString().toLowerCase();
                                    break;
                                }
                                case 1 -> {
                                    ResourceLocation location = PokemonSpecies.INSTANCE.getByName("charmeleon").getResourceIdentifier();
                                    actionKey.actionKey = location.toString().toLowerCase();
                                    break;
                                }
                                case 2 -> {
                                    ResourceLocation location = PokemonSpecies.INSTANCE.getByName("ivysaur").getResourceIdentifier();
                                    actionKey.actionKey = location.toString().toLowerCase();
                                    break;
                                }
                                case 3 -> {
                                    ResourceLocation location = PokemonSpecies.INSTANCE.getByName("wartortle").getResourceIdentifier();
                                    actionKey.actionKey = location.toString().toLowerCase();
                                }
                                case 4 -> {
                                    ResourceLocation location = PokemonSpecies.INSTANCE.getByName("ninetales").getResourceIdentifier();
                                    actionKey.actionKey = location.toString().toLowerCase();
                                }
                                case 5 -> {
                                    ResourceLocation location = PokemonSpecies.INSTANCE.getByName("graveler").getResourceIdentifier();
                                    actionKey.actionKey = location.toString().toLowerCase();
                                }

                            }
                            jobType.actionKeys.put(actionKey.actionKey, actionKey);
                        }

                        configurableJob.jobTypes.put(JobAction.Capture.name().toLowerCase(), jobType);
                        configurableJob.jobTypes.put(JobAction.Evolve.name().toLowerCase(), secondJobType);
                        break;
                    }
                    case Woodcutter -> {
                        JobType jobType = new JobType();
                        jobType.prettyTitle = "&b" + "Cut Wood";
                        //load default action keys
                        for (int i = 0; i < 6; i++) {
                            ActionKey actionKey = new ActionKey();
                            actionKey.baseExperience = 1;
                            actionKey.baseWorth = 1;
                            switch (i)
                            {
                                case 0 -> {
                                    ResourceLocation location = ITEM.getKey(Items.ACACIA_LOG);
                                    actionKey.actionKey = location.toString().toLowerCase();
                                    break;
                                }
                                case 1 -> {
                                    ResourceLocation location = ITEM.getKey(Items.OAK_LOG);
                                    actionKey.actionKey = location.toString().toLowerCase();
                                    break;
                                }
                                case 2 -> {
                                    ResourceLocation location = ITEM.getKey(Items.DARK_OAK_LOG);
                                    actionKey.actionKey = location.toString().toLowerCase();
                                    break;
                                }
                                case 3 -> {
                                    ResourceLocation location = ITEM.getKey(Items.SPRUCE_LOG);
                                    actionKey.actionKey = location.toString().toLowerCase();
                                }
                                case 4 -> {
                                    ResourceLocation location = ITEM.getKey(Items.MANGROVE_LOG);
                                    actionKey.actionKey = location.toString().toLowerCase();
                                }
                                case 5 -> {
                                    ResourceLocation location = ITEM.getKey(Items.BIRCH_LOG);
                                    actionKey.actionKey = location.toString().toLowerCase();
                                }

                            }
                            jobType.actionKeys.put(actionKey.actionKey, actionKey);
                        }
                        configurableJob.jobTypes.put(JobAction.Mine.name().toLowerCase(), jobType);
                        break;
                    }
                    case Blacksmith -> {
                        JobType jobType = new JobType();
                        jobType.prettyTitle = "&e" + "Blacksmith Items";
                        for (int i = 0; i < 6; i++) {
                            ActionKey actionKey = new ActionKey();
                            actionKey.baseExperience = 1;
                            actionKey.baseWorth = 1;
                            switch (i)
                            {
                                case 0 -> {
                                    ResourceLocation location = ITEM.getKey(Items.IRON_INGOT);
                                    actionKey.actionKey = location.toString().toLowerCase();
                                    break;
                                }
                                case 1 -> {
                                    ResourceLocation location = ITEM.getKey(Items.GOLD_INGOT);
                                    actionKey.actionKey = location.toString().toLowerCase();
                                    break;
                                }
                                case 2 -> {
                                    ResourceLocation location = ITEM.getKey(Items.COPPER_INGOT);
                                    actionKey.actionKey = location.toString().toLowerCase();
                                    break;
                                }
                            }
                            jobType.actionKeys.put(actionKey.actionKey, actionKey);
                        }
                        configurableJob.jobTypes.put(JobAction.Smelt.name().toLowerCase(), jobType);
                        break;
                    }
                    case Trader -> {
                        JobType jobType = new JobType();
                        jobType.prettyTitle = "&b" + "Trading Pokemon";
                        //load default action keys
                        for (int i = 0; i < 6; i++) {
                            ActionKey actionKey = new ActionKey();
                            actionKey.baseExperience = 1;
                            actionKey.baseWorth = 1;
                            switch (i)
                            {
                                case 0 -> {
                                    ResourceLocation location = Objects.requireNonNull(PokemonSpecies.INSTANCE.getByName("charmander")).getResourceIdentifier();
                                    actionKey.actionKey = location.toString().toLowerCase();
                                    break;
                                }
                                case 1 -> {
                                    ResourceLocation location = Objects.requireNonNull(PokemonSpecies.INSTANCE.getByName("charmeleon")).getResourceIdentifier();
                                    actionKey.actionKey = location.toString().toLowerCase();
                                    break;
                                }
                                case 2 -> {
                                    ResourceLocation location = Objects.requireNonNull(PokemonSpecies.INSTANCE.getByName("charizard")).getResourceIdentifier();
                                    actionKey.actionKey = location.toString().toLowerCase();
                                    break;
                                }
                                case 3 -> {
                                    ResourceLocation location = Objects.requireNonNull(PokemonSpecies.INSTANCE.getByName("vulpix")).getResourceIdentifier();
                                    actionKey.actionKey = location.toString().toLowerCase();
                                }
                                case 4 -> {
                                    ResourceLocation location = PokemonSpecies.INSTANCE.getByName("ninetales").getResourceIdentifier();
                                    actionKey.actionKey = location.toString().toLowerCase();
                                }
                                case 5 -> {
                                    ResourceLocation location = PokemonSpecies.INSTANCE.getByName("pikachu").getResourceIdentifier();
                                    actionKey.actionKey = location.toString().toLowerCase();
                                }

                            }
                            jobType.actionKeys.put(actionKey.actionKey, actionKey);
                        }
                        configurableJob.jobTypes.put(JobAction.Trade.name().toLowerCase(), jobType);
                        break;
                    }
                }
                for (int i = 0; i < 15; i++) {
                    ConfigurableLevel configurableLevel = new ConfigurableLevel();
                    configurableLevel.requiredExperience = (i + 1) * 100;
                    configurableLevel.experienceBooster = (i + 1) * 1.15;
                    configurableLevel.moneyBooster = (i + 1) * 1.15;
                    configurableJob.levels.put(i, configurableLevel);
                }

                configurableJobs.put(configurableJob.id, configurableJob);
            }
        }
    }
}
