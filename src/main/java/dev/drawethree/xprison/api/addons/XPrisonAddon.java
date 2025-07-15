package dev.drawethree.xprison.api.addons;

/**
 * Represents a modular addon for the X-Prison core plugin.
 * <p>
 * Implementations of this interface can be loaded dynamically by X-Prison to
 * provide additional functionality, such as custom enchants, features, or integrations.
 * </p>
 * <p>
 * Every addon must define its entry class in the JAR manifest using the
 * {@code X-Prison-Addon-Class} attribute.
 * </p>
 */
public interface XPrisonAddon {

    /**
     * Called when the addon is being enabled.
     * <p>
     * This is the place to register events, commands, or hook into X-Prison APIs.
     * Called after the addon is successfully loaded and its dependencies are available.
     * </p>
     */
    void onEnable();

    /**
     * Called when the addon is being disabled.
     * <p>
     * Use this to clean up resources, unregister listeners, or perform any shutdown logic.
     * This is invoked before the addon is unloaded or the server shuts down.
     * </p>
     */
    void onDisable();
}
