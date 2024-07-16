// Class loader access from LuckPerms
// https://github.com/LuckPerms/LuckPerms/blob/1790c0ad4744d31ea3e30eb87822b4f506de449b/common/src/main/java/me/lucko/luckperms/common/plugin/classpath/URLClassLoaderAccess.java
// Copyright (c) lucko (Luck) <lucko@lucko.me>
// Copyright (c) contributors
// MIT License

package dev.xhyrom.lighteco.common.util;

import dev.xhyrom.lighteco.common.util.exception.UnableToInjectException;

import org.checkerframework.checker.nullness.qual.NonNull;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Collection;

public abstract class URLClassLoaderAccess {
    public static URLClassLoaderAccess create(URLClassLoader classLoader) {
        if (Reflection.isSupported()) {
            return new Reflection(classLoader);
        } else if (Unsafe.isSupported()) {
            return new Unsafe(classLoader);
        } else {
            throw new UnableToInjectException(
                    "LightEco is unable to inject dependencies into the plugin class loader.\n"
                            + "To fix this, please add '--add-opens java.base/java.lang=ALL-UNNAMED' to your JVM arguments."
                            + "If it still doesn't work, please report this on https://github.com/xHyroM/lighteco/issues");
        }
    }

    private final URLClassLoader classLoader;

    protected URLClassLoaderAccess(URLClassLoader classLoader) {
        this.classLoader = classLoader;
    }

    public abstract void addURL(@NonNull URL url);

    private static class Reflection extends URLClassLoaderAccess {
        private static final Method ADD_URL_METHOD;

        static {
            Method addUrlMethod;
            try {
                addUrlMethod = URLClassLoader.class.getDeclaredMethod("addURL", URL.class);
                addUrlMethod.setAccessible(true);
            } catch (Exception e) {
                addUrlMethod = null;
            }
            ADD_URL_METHOD = addUrlMethod;
        }

        private static boolean isSupported() {
            return ADD_URL_METHOD != null;
        }

        Reflection(URLClassLoader classLoader) {
            super(classLoader);
        }

        @Override
        public void addURL(@NonNull URL url) {
            try {
                ADD_URL_METHOD.invoke(super.classLoader, url);
            } catch (ReflectiveOperationException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private static class Unsafe extends URLClassLoaderAccess {
        private static final sun.misc.Unsafe UNSAFE;

        static {
            sun.misc.Unsafe unsafe;
            try {
                Field unsafeField = sun.misc.Unsafe.class.getDeclaredField("theUnsafe");
                unsafeField.setAccessible(true);
                unsafe = (sun.misc.Unsafe) unsafeField.get(null);
            } catch (Throwable t) {
                unsafe = null;
            }
            UNSAFE = unsafe;
        }

        private static boolean isSupported() {
            return UNSAFE != null;
        }

        private final Collection<URL> unopenedURLs;
        private final Collection<URL> pathURLs;

        @SuppressWarnings("unchecked")
        Unsafe(URLClassLoader classLoader) {
            super(classLoader);

            Collection<URL> unopenedURLs;
            Collection<URL> pathURLs;
            try {
                Object ucp = fetchField(URLClassLoader.class, classLoader, "ucp");
                unopenedURLs = (Collection<URL>) fetchField(ucp.getClass(), ucp, "unopenedUrls");
                pathURLs = (Collection<URL>) fetchField(ucp.getClass(), ucp, "path");
            } catch (Throwable e) {
                unopenedURLs = null;
                pathURLs = null;
            }

            this.unopenedURLs = unopenedURLs;
            this.pathURLs = pathURLs;
        }

        private static Object fetchField(
                final Class<?> clazz, final Object object, final String name)
                throws NoSuchFieldException {
            Field field = clazz.getDeclaredField(name);
            long offset = UNSAFE.objectFieldOffset(field);
            return UNSAFE.getObject(object, offset);
        }

        @Override
        public void addURL(@NonNull URL url) {
            if (this.unopenedURLs == null || this.pathURLs == null) {
                throw new NullPointerException("unopenedURLs or pathURLs");
            }

            synchronized (this.unopenedURLs) {
                this.unopenedURLs.add(url);
                this.pathURLs.add(url);
            }
        }
    }
}
