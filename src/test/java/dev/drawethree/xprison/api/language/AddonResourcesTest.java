package dev.drawethree.xprison.api.language;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.function.Function;
import java.util.jar.JarEntry;
import java.util.jar.JarOutputStream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

class AddonResourcesTest {

    private static final String MARKER = "dev/drawethree/xprison/api/language/AddonResourcesTest$Marker.class";

    static final class Marker {
    }

    @TempDir
    File folder;

    private File addonJar() throws Exception {
        File jar = new File(this.folder, "addon.jar");
        try (OutputStream out = Files.newOutputStream(jar.toPath()); JarOutputStream jarOut = new JarOutputStream(out);
             InputStream marker = AddonResourcesTest.class.getClassLoader().getResourceAsStream(MARKER)) {
            jarOut.putNextEntry(new JarEntry(MARKER));
            jarOut.write(marker.readAllBytes());
            jarOut.putNextEntry(new JarEntry("lang/de/messages.yml"));
            jarOut.write("bought: 'Gekauft'\n".getBytes(StandardCharsets.UTF_8));
        }
        return jar;
    }

    private static String text(InputStream stream) throws Exception {
        try (stream) {
            return new String(stream.readAllBytes(), StandardCharsets.UTF_8);
        }
    }

    @Test
    void readsOnlyTheAddonsOwnJar() throws Exception {
        try (URLClassLoader loader = new URLClassLoader(new URL[]{addonJar().toURI().toURL()}, AddonResourcesTest.class.getClassLoader())) {
            Class<?> fromParent = loader.loadClass(Marker.class.getName());
            assertEquals(AddonResourcesTest.class.getClassLoader(), fromParent.getClassLoader());

            try (URLClassLoader isolated = new URLClassLoader(loader.getURLs(), null)) {
                Function<String, InputStream> resources = AddonResources.of(isolated.loadClass(Marker.class.getName()));

                assertEquals("bought: 'Gekauft'\n", text(resources.apply("lang/de/messages.yml")));
                assertNull(resources.apply("dev/drawethree/xprison/api/language/AddonResources.class"));
                assertNull(resources.apply("lang/fr/messages.yml"));
            }
        }
    }

    @Test
    void readsAClassesDirectory() {
        Function<String, InputStream> resources = AddonResources.of(Marker.class);

        assertNotNull(resources.apply(MARKER));
        assertNull(resources.apply("missing.yml"));
    }
}
