package com.jeff_media.jefflib.nms.v1_19_4;

import com.jeff_media.jefflib.internal.nms.INMSHandler;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.dedicated.DedicatedServer;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.chunk.LevelChunk;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.NamespacedKey;
import org.bukkit.craftbukkit.v1_19_R3.CraftServer;
import org.bukkit.craftbukkit.v1_19_R3.CraftWorld;

import java.util.Objects;
import java.util.Optional;

/**
 * The 1.19.4 NMS handler
 */
public class NMSHandler implements INMSHandler {

    private final ResourceKey<Registry<Biome>> BIOME_REGISTRY_RESOURCE_KEY = ResourceKey.createRegistryKey(new ResourceLocation("worldgen/biome"));

    @Override
    public NamespacedKey getBiomeNamespacedKey(final Location location) {
        final ResourceLocation key = getBiomeKey(location);
        return NamespacedKey.fromString(key.getNamespace() + ":" + key.getPath());
    }

    private ResourceLocation getBiomeKey(final Location location) {
        final Registry<Biome> registry = getBiomeRegistry();
        return registry.getKey(Objects.requireNonNull(getBiomeBase(location)).value());
    }

    private Registry<Biome> getBiomeRegistry() {
        final DedicatedServer dedicatedServer = ((CraftServer) Bukkit.getServer()).getServer();
        Optional<Registry<Biome>> registry = dedicatedServer.registryAccess().registry(BIOME_REGISTRY_RESOURCE_KEY);
        if(registry.isPresent()) {
            return registry.get();
        } else {
            throw new RuntimeException("Couldn't get biome registry");
        }
    }

    private Holder<Biome> getBiomeBase(final Location location) {
        final BlockPos pos = new BlockPos(location.getBlockX(), location.getBlockY(), location.getBlockZ());
        final LevelChunk nmsChunk = ((CraftWorld) Objects.requireNonNull(location.getWorld())).getHandle().getChunkAt(pos);
        if (nmsChunk != null) {
            return nmsChunk.getNoiseBiome(pos.getX(), pos.getY(), pos.getZ());
        }
        return null;
    }
}
