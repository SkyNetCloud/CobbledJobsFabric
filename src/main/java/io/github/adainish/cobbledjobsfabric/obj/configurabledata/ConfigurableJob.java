package io.github.adainish.cobbledjobsfabric.obj.configurabledata;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ConfigurableJob
{
    public String id = "";
    public int displayOrder = 0;
    public String displayStack = "minecraft:dirt";
    public HashMap<String, JobType> jobTypes = new HashMap<>();
    public HashMap<Integer, ConfigurableLevel> levels = new HashMap<>();

    public String prettyName = "";
    public List<String> description = new ArrayList<>();

    public boolean enabled = true;

    public ConfigurableJob() {

    }

}
