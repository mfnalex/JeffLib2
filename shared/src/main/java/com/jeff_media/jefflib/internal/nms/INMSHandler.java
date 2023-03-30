package com.jeff_media.jefflib;

import org.bukkit.Location;
import org.bukkit.NamespacedKey;

/**
 * Represents the NMS handler that all NMS modules implement
 */
public interface INMSHandler {

    NamespacedKey getBiomeNamespacedKey(Location location);

}
