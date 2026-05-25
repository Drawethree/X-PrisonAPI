package dev.drawethree.xprison.api.addons;

public record XPrisonAddonInfo(
        String name,
        String version,
        String author,
        String description,
        String minVersion,
        boolean enabled
) {}
