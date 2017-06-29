package dk.tv2.bulldog.backend.db;

import dk.tv2.bulldog.backend.db.annotations.Cache;
import dk.tv2.bulldog.backend.db.annotations.Entity;
import dk.tv2.bulldog.backend.db.annotations.InjectConnection;
import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author migo
 */
public class EntityManager {

    /**
     *
     * @param <T>
     * @param instance
     *
     * @return Generic entity
     */
    public static <T> T create(Class<T> instance) {
        return create(instance, true);
    }

    /**
     *
     * @param <T>
     * @param instance
     *
     * @return Generic entity
     */
    public static <T> T create(Class<T> instance, boolean cache) {
        T entityManager = (T) getEntityManager(instance, cache);
        if (entityManager != null) {
            return entityManager;
        }
        return null;
    }

    /**
     *
     * @param <T>
     * @param instance
     * @param connection
     *
     * @return
     */
    public static <T> T create(Class<T> instance, Connection connection) {
        return create(instance, connection, true);
    }

    /**
     *
     * @param <T>
     * @param instance
     * @param connection
     * @param cache
     *
     * @return
     */
    public static <T> T create(Class<T> instance, Connection connection, boolean cache) {
        T entityManager = (T) getEntityManager(instance);
        if (entityManager != null) {
            Field[] fields = entityManager.getClass().getDeclaredFields();
            for (Field field : fields) {
                if (field.isAnnotationPresent(InjectConnection.class)) {
                    if (field.getType().isAssignableFrom(connection.getClass())) {
                        field.setAccessible(true);
                        try {
                            if (connection != null && !connection.isClosed()) {
                                field.set(entityManager, connection);
                            }
                        } catch (IllegalArgumentException | IllegalAccessException | SQLException ex) {
                            Logger.getLogger(EntityManager.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                }
            }
            return entityManager;
        }
        return null;
    }

    /**
     *
     * @param <T>
     * @param instance
     * @param instanceCount
     *
     * @return array Generic entity
     */
    public static <T> List<T> create(Class<T> instance, int instanceCount) {
        List<T> managers = new ArrayList<>();
        for (int index = 0; index < instanceCount; index++) {
            T manager = create(instance);
            if (manager != null) {
                managers.add(manager);
            }
        }
        return managers;
    }

    /**
     *
     * @param <T>
     * @param entity
     *
     * @return Generic Type
     */
    private static <T> T getEntityManager(Class<T> entity) {
        return getEntityManager(entity, true);
    }

    /**
     *
     * @param <T>
     * @param entity
     *
     * @return Generic Type
     */
    private static <T> T getEntityManager(Class<T> entity, boolean cache) {
        if (entity.isAnnotationPresent(Entity.class)) {
            Entity annotation = entity.getAnnotation(Entity.class);
            try {
                Object newManagerInstance = annotation.manager().newInstance();

                if (!entity.isInstance(newManagerInstance)) {
                    throw new RuntimeException("Manager " + annotation.manager() + " does not extend " + entity.getName() + " ");
                }

                if (newManagerInstance.getClass().isAnnotationPresent(Cache.class) && cache) {
                    Cache cacheAnnotation = newManagerInstance.getClass().getAnnotation(Cache.class);
                    Object newCacheInstance = cacheAnnotation.manager().newInstance();
                    if (newManagerInstance.getClass().isInstance(newCacheInstance)) {
                        newManagerInstance = newCacheInstance;
                    } else {
                        throw new RuntimeException("Cache Manager " + cacheAnnotation.manager() + " does not extend " + newManagerInstance.getClass().getName() + " ");
                    }
                }

                @SuppressWarnings("unchecked")
                T manager = entity.cast(newManagerInstance);

                return manager;
            } catch (InstantiationException | IllegalAccessException ex) {
                Logger.getLogger(EntityManager.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else {
            Logger.getLogger(EntityManager.class.getName()).log(Level.WARNING, "{0} is not a Entity, no annotation is present", entity.getName());
        }
        return null;
    }

}
