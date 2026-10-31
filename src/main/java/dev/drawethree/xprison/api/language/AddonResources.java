package dev.drawethree.xprison.api.language;

import org.jetbrains.annotations.NotNull;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.security.CodeSource;
import java.util.function.Function;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

/**
 * Reads resources from one addon's own jar, for the {@code bundledResources} argument of
 * {@link XPrisonLanguageAPI#localize(String, String, org.bukkit.configuration.file.YamlConfiguration, Function)}
 * and {@link XPrisonLanguageAPI#localizeJson(String, String, com.google.gson.JsonObject, Function)}.
 *
 * <p>Prefer this over {@code getClassLoader()::getResourceAsStream}: an addon's class loader asks
 * X-Prison first, so a file X-Prison also ships, such as {@code config.yml} or
 * {@code lang/de/config.yml}, would be read from X-Prison's jar instead of yours.
 *
 * <pre>{@code
 * LocalizedConfig messages = XPrisonAPI.getInstance().getLanguageApi()
 *         .localize("Shop", "messages.yml", raw, AddonResources.of(MyAddon.class));
 * }</pre>
 *
 * @since 1.10
 */
public final class AddonResources {

    private AddonResources() {
        throw new UnsupportedOperationException("Cannot instantiate");
    }

    /**
     * Creates a reader for the jar (or classes directory) that {@code addonClass} was loaded from.
     * Each call opens the jar, copies the resource into memory and closes the jar again, so the
     * jar is never kept locked.
     *
     * @param addonClass any class from your addon
     * @return a reader returning the resource at a path such as {@code lang/de/messages.yml}, or
     *         {@code null} when it does not exist or cannot be read
     */
    @NotNull
    public static Function<String, InputStream> of(@NotNull Class<?> addonClass) {
        File source = sourceOf(addonClass);
        return path -> source == null ? null : read(source, path);
    }

    private static File sourceOf(Class<?> addonClass) {
        CodeSource codeSource = addonClass.getProtectionDomain().getCodeSource();
        URL location = codeSource == null ? null : codeSource.getLocation();
        if (location == null) {
            return null;
        }
        try {
            return new File(location.toURI());
        } catch (URISyntaxException | IllegalArgumentException e) {
            return null;
        }
    }

    private static InputStream read(File source, String path) {
        try {
            if (source.isDirectory()) {
                File file = new File(source, path);
                return file.isFile() ? new ByteArrayInputStream(Files.readAllBytes(file.toPath())) : null;
            }
            try (JarFile jar = new JarFile(source)) {
                JarEntry entry = jar.getJarEntry(path);
                if (entry == null || entry.isDirectory()) {
                    return null;
                }
                try (InputStream in = jar.getInputStream(entry)) {
                    return new ByteArrayInputStream(in.readAllBytes());
                }
            }
        } catch (IOException | SecurityException e) {
            return null;
        }
    }
}
