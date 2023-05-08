package io.github.adainish.cobbledjobsfabric.obj.data;

import ca.landonjw.gooeylibs2.api.UIManager;
import ca.landonjw.gooeylibs2.api.button.Button;
import ca.landonjw.gooeylibs2.api.button.GooeyButton;
import ca.landonjw.gooeylibs2.api.button.PlaceholderButton;
import ca.landonjw.gooeylibs2.api.button.linked.LinkType;
import ca.landonjw.gooeylibs2.api.button.linked.LinkedPageButton;
import ca.landonjw.gooeylibs2.api.helpers.PaginationHelper;
import ca.landonjw.gooeylibs2.api.page.GooeyPage;
import ca.landonjw.gooeylibs2.api.page.LinkedPage;
import ca.landonjw.gooeylibs2.api.template.types.ChestTemplate;
import com.cobblemon.mod.common.api.pokemon.PokemonSpecies;
import io.github.adainish.cobbledjobsfabric.CobbledJobsFabric;
import io.github.adainish.cobbledjobsfabric.enumerations.JobAction;
import io.github.adainish.cobbledjobsfabric.obj.configurabledata.ActionKey;
import io.github.adainish.cobbledjobsfabric.obj.configurabledata.ConfigurableJob;
import io.github.adainish.cobbledjobsfabric.obj.configurabledata.ConfigurableLevel;
import io.github.adainish.cobbledjobsfabric.obj.configurabledata.JobType;
import io.github.adainish.cobbledjobsfabric.storage.PlayerStorage;
import io.github.adainish.cobbledjobsfabric.util.EconomyUtil;
import io.github.adainish.cobbledjobsfabric.util.Util;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

public class Player {
    public UUID uuid;
    public String userName = "DEFAULT_DATA";
    public int maxJobs = 1;
    public HashMap<String, Job> jobs = new HashMap<>();

    public Player(UUID uuid) {
        this.uuid = uuid;
    }

    public void sendMessage(String message) {
        //check if online
        if (Util.isOnline(uuid)) {
            Util.send(Util.getPlayer(uuid), message);
        }
    }

    public void updateCache() {
        CobbledJobsFabric.dataWrapper.playerCache.put(uuid, this);
    }

    public void save() {
        //save to storage file
        PlayerStorage.savePlayer(this);
    }

    public void viewGUI(ServerPlayer serverPlayer)
    {
        UIManager.openUIForcefully(serverPlayer, mainJobsGUI());
    }

    public int getCurrentActiveJobCount() {
        AtomicInteger amount = new AtomicInteger();
        jobs.forEach((s, job) -> {
            ConfigurableJob configurableJob = job.getConfigurableJob();
            if (configurableJob != null) {
                if (job.hasJob && configurableJob.enabled)
                    amount.getAndIncrement();
            }
        });
        return amount.get();
    }

    public int getCurrentLevelForJob(Job job) {
        return job.level;
    }

    public ConfigurableLevel getConfigurableLevel(Job job, ConfigurableJob configurableJob) {
        if (configurableJob.levels.containsKey(getCurrentLevelForJob(job)))
            return configurableJob.levels.get(getCurrentLevelForJob(job));
        return null;
    }

    public double retrieveExperience(Job job, ConfigurableJob configurableJob, String jobTypeID, String actionKeyID) {
        double amount = configurableJob.jobTypes.get(jobTypeID).actionKeys.get(actionKeyID).baseExperience;

        ConfigurableLevel configurableLevel = getConfigurableLevel(job, configurableJob);
        if (configurableLevel != null) {
            if (configurableLevel.experienceBooster > 0)
                amount = amount * configurableLevel.experienceBooster;
        }
        return amount;
    }


    public double retrieveMoney(Job job, ConfigurableJob configurableJob, String jobTypeID, String actionKeyID) {
        double amount = configurableJob.jobTypes.get(jobTypeID).actionKeys.get(actionKeyID).baseWorth;

        ConfigurableLevel configurableLevel = getConfigurableLevel(job, configurableJob);
        if (configurableLevel != null) {
            if (configurableLevel.moneyBooster > 0)
                amount = amount * configurableLevel.moneyBooster;
        }
        return amount;
    }

    public void updateJobData(JobAction jobAction, String actionKeyID) {
        jobs.forEach((s, job) -> {
            ConfigurableJob configurableJob = job.getConfigurableJob();
            if (configurableJob != null) {
                if (configurableJob.enabled) {
                    configurableJob.jobTypes.forEach((s1, jobType) -> {
                        if (s1.equalsIgnoreCase(jobAction.name())) {
                            if (jobType.actionKeys.containsKey(actionKeyID)) {
                                double exp = retrieveExperience(job, configurableJob, s1, actionKeyID);
                                //increase exp
                                job.increaseExperience(exp);
                                //exp message
                                sendMessage("&7You gained %experience% exp.".replace("%experience%", String.valueOf(exp)));
                                double money = retrieveMoney(job, configurableJob, s1, actionKeyID);
                                //increase money
                                EconomyUtil.add(uuid, money);
                                //money message
                                sendMessage("&7Added %amount%$ to your account.".replace("%amount%", String.valueOf(money)));
                                //if can level up, level up
                                if (job.canLevelUp()) {
                                    job.increaseLevel();
                                    sendMessage("&7You leveled up your %job% to %level%.");
                                }
                            }
                        }
                    });
                }
            }
        });
    }

    public void syncWithJobManager() {
        if (jobs.isEmpty()) {
            CobbledJobsFabric.jobsConfig.jobManager.configurableJobs.forEach((s, configurableJob) -> {
                Job job = new Job();
                job.jobName = s;
                jobs.put(s, job);
            });
        } else {
            //mark old jobs as disabled
            jobs.forEach((s, job) -> {
                if (CobbledJobsFabric.jobsConfig.jobManager.configurableJobs.containsKey(s))
                    return;
                job.hasJob = false;
            });
            CobbledJobsFabric.jobsConfig.jobManager.configurableJobs.forEach((s, configurableJob) -> {
                if (jobs.containsKey(s)) {
                    jobs.get(s).jobName = s;
                } else {
                    Job job = new Job();
                    job.jobName = s;
                    jobs.put(s, job);
                }
            });
        }
    }

    public GooeyButton filler() {
        return GooeyButton.builder()
                .display(new ItemStack(Items.GRAY_STAINED_GLASS_PANE))
                .build();
    }

    public List<Button> sortedJobsButtons() {
        List<Button> buttons = new ArrayList<>();
        List<ConfigurableJob> configurableJobs = new ArrayList<>(CobbledJobsFabric.jobsConfig.jobManager.configurableJobs.values());
        configurableJobs.sort(Comparator.comparing(configurableJob -> configurableJob.displayOrder));

        for (ConfigurableJob configurableJob : configurableJobs) {
            GooeyButton button = GooeyButton.builder()
                    .title(Util.formattedString(configurableJob.prettyName))
                    .lore(Util.formattedArrayList(configurableJob.description))
                    .display(configurableJob.displayStack.copy())
                    .onClick(b -> {
                        UIManager.openUIForcefully(b.getPlayer(), selectOptionMenu(configurableJob));
                    })
                    .build();
            buttons.add(button);
        }
        return buttons;
    }

    public List<String> parsedActionKeyString(ActionKey actionKey)
    {
        List<String> strings = new ArrayList<>();

        for (String s:actionKey.description) {
            strings.add(s
                    .replace("%amount%", String.valueOf(actionKey.baseWorth))
                    .replace("%exp%", String.valueOf(actionKey.baseExperience))
            );
        }

        return strings;
    }

    public List<Button> sortedActionKeys(JobType jobType)
    {
        List<Button> buttons = new ArrayList<>();

        List<ActionKey> sortedActionKeys = new ArrayList<>(jobType.actionKeys.values());
        sortedActionKeys.sort(Comparator.comparing(actionkey -> actionkey.baseWorth));
        for (ActionKey actionKey:sortedActionKeys) {
            ResourceLocation location = ResourceLocation.tryParse(actionKey.actionKey);
            if (location == null)
                continue;
            ItemStack stack = new ItemStack(Items.AIR);
            if (PokemonSpecies.INSTANCE.getByIdentifier(location) != null)
            {
                stack = Util.returnIcon(PokemonSpecies.INSTANCE.getByIdentifier(location).create(1));
            } else {
                stack = new ItemStack(Registry.ITEM.get(location));
            }
            if (stack.isEmpty())
                continue;

            GooeyButton button = GooeyButton.builder()
                    .title(Util.formattedString(actionKey.prettyString))
                    .lore(Util.formattedArrayList(parsedActionKeyString(actionKey)))
                    .build();

            buttons.add(button);
        }
        return buttons;
    }

    public List<Button> sortedActionButtons(ConfigurableJob configurableJob) {
        List<Button> buttons = new ArrayList<>();
        List<JobType> sortedTypes = new ArrayList<>(configurableJob.jobTypes.values());
        sortedTypes.sort(Comparator.comparing(jobType -> jobType.displayOrder));
        sortedTypes.forEach((jobType) -> {
            GooeyButton button = GooeyButton.builder()
                    .title(Util.formattedString(jobType.prettyTitle))
                    .lore(Util.formattedArrayList(jobType.description))
                    .display(jobType.displayStack.copy())
                    .onClick(b -> {

                    })
                    .build();

            buttons.add(button);
        });

        return buttons;
    }

    public GooeyPage jobActionMenu(ConfigurableJob configurableJob)
    {
        ChestTemplate.Builder builder = ChestTemplate.builder(5);
        builder.fill(filler());

        ItemStack joinOrLeaveStack = new ItemStack(Items.ENDER_PEARL);


        GooeyButton joinOrLeave = GooeyButton.builder()
                .title(Util.formattedString("&a"))
                .display(new ItemStack(Items.ENDER_CHEST))
                .onClick(b -> {
                    //if join bla bla

                     //if leave bla bla
                })
                .build();

        GooeyButton back = GooeyButton.builder()
                .title(Util.formattedString("&3Go Back"))
                .display(new ItemStack(Items.ARROW))
                .build();

        builder.set(2, 3, back);
        builder.set(2, 5, joinOrLeave);
        return GooeyPage.builder().template(builder.build()).build();
    }

    public GooeyPage selectOptionMenu(ConfigurableJob configurableJob)
    {
        ChestTemplate.Builder builder = ChestTemplate.builder(5);
        builder.fill(filler());
        GooeyButton viewJobInfo = GooeyButton.builder()
                .title(Util.formattedString("&aJob Info"))
                .display(new ItemStack(Items.ENDER_CHEST))
                .onClick(b -> {

                })
                .build();

        GooeyButton joinOption = GooeyButton.builder()
                .title(Util.formattedString("&bJob Options"))
                .display(new ItemStack(Items.ENDER_PEARL))
                .onClick(b -> {
                    UIManager.openUIForcefully(b.getPlayer(), jobActionMenu(configurableJob));
                })
                .build();

        builder.set(2, 3, joinOption);
        builder.set(2, 5, viewJobInfo);
        return GooeyPage.builder().template(builder.build()).build();
    }

    public LinkedPage mainJobsGUI() {
        ChestTemplate.Builder builder = ChestTemplate.builder(5);
        builder.fill(filler());

        PlaceholderButton placeHolderButton = new PlaceholderButton();
        LinkedPageButton previous = LinkedPageButton.builder()
                .display(new ItemStack(Items.SPECTRAL_ARROW))
                .title(Util.formattedString("Previous Page"))
                .linkType(LinkType.Previous)
                .build();

        LinkedPageButton next = LinkedPageButton.builder()
                .display(new ItemStack(Items.SPECTRAL_ARROW))
                .title(Util.formattedString("Next Page"))
                .linkType(LinkType.Next)
                .build();
        builder.set(0, 3, previous)
                .set(0, 5, next)
                .rectangle(1, 1, 3, 7, placeHolderButton);

        return PaginationHelper.createPagesFromPlaceholders(builder.build(), sortedJobsButtons(), LinkedPage.builder().template(builder.build()));
    }
}
