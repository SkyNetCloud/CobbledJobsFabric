package io.github.adainish.cobbledjobsfabric.util;

import com.google.gson.*;
import com.mojang.serialization.JsonOps;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;


import java.lang.reflect.Type;

public class ItemStackAdapter implements JsonSerializer<ItemStack>, JsonDeserializer<ItemStack> {

    public static ItemStackAdapter INSTANCE = new ItemStackAdapter();

    public CompoundTag asNBT(){
        CompoundTag nbt = new CompoundTag();
        return (CompoundTag) NbtOps.INSTANCE.convertTo(NbtOps.INSTANCE, nbt);
    }

    public JsonElement saveToJson(ItemStack itemStack) {
        return NbtOps.INSTANCE.convertTo(JsonOps.INSTANCE, itemStack.save(new CompoundTag()));
    }

    @Override
    public ItemStack deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) {
        if (json.isJsonPrimitive()) {
            return new ItemStack(BuiltInRegistries.ITEM.get(new ResourceLocation(json.getAsString().toLowerCase())));
        } else {
            return ItemStack.of(asNBT());
        }
    }

    @Override
    public JsonElement serialize(ItemStack src, Type typeOfSrc, JsonSerializationContext context) {
        return saveToJson(src);
    }


}