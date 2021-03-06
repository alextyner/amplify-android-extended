/*
 * Copyright 2020 Amazon.com, Inc. or its affiliates. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License").
 * You may not use this file except in compliance with the License.
 * A copy of the License is located at
 *
 *  http://aws.amazon.com/apache2.0
 *
 * or in the "license" file accompanying this file. This file is distributed
 * on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either
 * express or implied. See the License for the specific language governing
 * permissions and limitations under the License.
 */

package com.amplifyframework.extended;

import android.content.Context;
import android.util.Log;
import androidx.annotation.NonNull;

import com.amplifyframework.AmplifyException;
import com.amplifyframework.core.category.Category;
import com.amplifyframework.core.plugin.Plugin;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Like {@link com.amplifyframework.core.Amplify}, but for 3rd party plugins.
 *
 * Typical usage is as follows:
 * <pre>
 *     {@code
        AmplifyExtended.addCategory(new VideoCategory(), new VideoCategoryConfiguration(), "videoconfiguration");
        AmplifyExtended.addPlugin(AmplifyExtended.category("video"), new AWSVideoPlugin());
        AmplifyExtended.configure(getApplicationContext());
 *     }
 * </pre>
 */
public final class AmplifyExtended {

    private static Map<String, CategoryMetadata<? extends Plugin<?>>> categoryData = new HashMap<>();
    private static Map<String, ExtendedCategory<? extends Plugin<?>>> categories = new HashMap<>();

    // Used as a synchronization locking object. Set to true once configure() is complete.
    private static final AtomicBoolean CONFIGURATION_LOCK = new AtomicBoolean(false);

    // An executor on which categories may be initialized.
    private static final ExecutorService INITIALIZATION_POOL = Executors.newSingleThreadExecutor();

    /**
     * Dis-allows instantiation of this utility class.
     */
    private AmplifyExtended() {
        throw new UnsupportedOperationException("No instances allowed.");
    }

    /**
     * Add a new category. It should extend {@link ExtendedCategory} and have a unique identifier defined by
     * {@link ExtendedCategoryTypeable#getExtendedCategoryType()}.
     * @param category A new {@link ExtendedCategory}.
     * @param emptyConfiguration A new instance of {@link ExtendedCategoryConfiguration} specialized
     *                           for your plugin.
     * @param configurationFileName Name of the configuration file for your plugin.
     * @param <P> Type of the category's plugins.
     */
    @SuppressWarnings("unchecked")
    public static <P extends Plugin<?>> void addCategory(@NonNull ExtendedCategory<? extends Plugin<?>> category,
                                       @NonNull ExtendedCategoryConfiguration emptyConfiguration,
                                       @NonNull String configurationFileName) {
        if (categories.containsValue(category)) {
            return;
        }

        categories.put(category.getExtendedCategoryType(), category);

        categoryData.put(category.getExtendedCategoryType(), new CategoryMetadata<P>((ExtendedCategory<P>) category,
                emptyConfiguration, configurationFileName));
    }

    /**
     * Remove a category.
     * @param category An instance of {@link ExtendedCategory} with an identifier matching the category
     *                 you wish to remove.
     */
    public static void removeCategory(ExtendedCategory<? extends Plugin<?>> category) {
        categories.remove(category.getExtendedCategoryType());
    }

    /**
     * Retrieve an {@link ExtendedCategory} with the given name. It must have been added using
     * {@link AmplifyExtended#addCategory(ExtendedCategory, ExtendedCategoryConfiguration, String)}
     * @param categoryName Name of the desired category.
     * @param <C> Type of category you'd like the behaviors of.
     * @return The desired {@link ExtendedCategory}.
     */
    @SuppressWarnings("unchecked")
    public static <C extends ExtendedCategory<?>> C category(String categoryName) {
        try {
            return (C) categories.get(categoryName);
        } catch (ClassCastException noPlugin) {
            return null;
        } catch (NullPointerException noCategory) {
            return null;
        }
    }

    /**
     * Add a new 3rd party plugin.
     * @param category A new {@link Category} instance for your plugin.
     * @param plugin A new conforming instance of {@link Plugin}.
     * @param <P> Plugin type.
     * @throws AmplifyException If the plugin and category types do not match.
     */
    public static <P extends Plugin<?>> void addPlugin(@NonNull final ExtendedCategory<P> category,
                                                       @NonNull final P plugin)
            throws AmplifyException {
        category.addPlugin(plugin);
    }

    /**
     * Remove a 3rd part plugin.
     * @param category The category from which the plugin should be removed.
     * @param plugin An instance of the plugin with the same {@link Plugin#getPluginKey()} as a
     *               plugin you've added before.
     * @param <P> Plugin type.
     * @throws AmplifyException If the plugin and category types do not match.
     */
    @SuppressWarnings("unchecked")
    public static <P extends Plugin<?>> void removePlugin(@NonNull final ExtendedCategory<P> category,
                                                          @NonNull final P plugin) {
        category.removePlugin(plugin);
    }

    /**
     * This is where the configurations are actually read and parsed for each plugin.
     * @param context Application context.
     * @throws AmplifyException If something really goes wrong.
     */
    public static void configure(@NonNull Context context) throws AmplifyException {
        Objects.requireNonNull(context);

        synchronized (CONFIGURATION_LOCK) {
            if (CONFIGURATION_LOCK.get()) {
                throw new AmplifyException(
                        "The client issued a subsequent call to `AmplifyExtended.configure` after "
                                + "the first had already succeeded.",
                        "Be sure to only call AmplifyExtended.configure once"
                );
            }

            for (ExtendedCategory<?> category : categories.values()) {
                CategoryMetadata<? extends Plugin<?>> metadata = categoryData.get(category.getExtendedCategoryType());

                // Actually a subclass -- used to know the right type
                ExtendedCategoryConfiguration emptyConfig = metadata.getExtendedCategoryConfiguration();

                ExtendedCategoryConfiguration categoryConfiguration;
                try {
                    categoryConfiguration = ExtendedConfiguration.read(context,
                            metadata.getConfigFileName(), emptyConfig);
                } catch (AmplifyException exception) {
                    Log.e("AmplifyExt", String.format("Configuration file [%s] could not be read.",
                            metadata.getConfigFileName()), exception);
                    continue;
                }

                category.configure(categoryConfiguration, context);
                beginInitialization(category, context);
            }

            CONFIGURATION_LOCK.set(true);
        }
    }

    private static void beginInitialization(@NonNull Category<? extends Plugin<?>> category, @NonNull Context context) {
        INITIALIZATION_POOL.execute(() -> category.initialize(context));
    }

}
