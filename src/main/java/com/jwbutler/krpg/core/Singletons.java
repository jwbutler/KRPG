package com.jwbutler.krpg.core;

import javax.annotation.CheckForNull;
import javax.annotation.Nonnull;
import java.util.HashMap;
import java.util.Map;

import static com.google.common.base.Preconditions.checkState;


public class Singletons
{
    private static final Map<Class<?>, Object> _map = new HashMap<>();

    public static <T> void register(@Nonnull Class<? extends T> objectClass, @Nonnull T object)
    {
        _map.put(objectClass, object);
    }

    @Nonnull
    public static <T> T get(@Nonnull Class<T> objectClass)
    {
        @CheckForNull Object storedValue = _map.get(objectClass);
        checkState(storedValue != null);
        checkState(objectClass.isInstance(storedValue));
        return (T) storedValue;
    }
}
