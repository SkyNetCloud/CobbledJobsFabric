package io.github.adainish.cobbledjobsfabric.obj.configurabledata;


import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class JobType
{
    public HashMap<String, ActionKey> actionKeys = new HashMap<>();
    public static String displayStack = "minecraft:paper";
    public String prettyTitle = "";
    public List<String> description = new ArrayList<>();
    public int displayOrder = 0;
    public JobType()
    {

    }
}
