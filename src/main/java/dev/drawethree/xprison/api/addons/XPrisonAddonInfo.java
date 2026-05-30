package dev.drawethree.xprison.api.addons;

import java.util.List;

public record XPrisonAddonInfo(
        String name,
        String version,
        String author,
        String description,
        String minVersion,
        int priority,
        List<String> depends,
        boolean enabled
) {}
