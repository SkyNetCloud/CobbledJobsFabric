package io.github.adainish.cobbledjobsfabric.util;


import com.google.gson.*;
import net.minecraft.world.item.ItemStack;


import java.lang.reflect.Modifier;


public class Adapters
{
    public static Gson PRETTY_MAIN_GSON = new GsonBuilder()
            .setPrettyPrinting()
            .disableHtmlEscaping()
            .registerTypeAdapter(ItemStack.class, ItemStackAdapter.INSTANCE)
            .excludeFieldsWithModifiers(Modifier.TRANSIENT)
            .create();


}
