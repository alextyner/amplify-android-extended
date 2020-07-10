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

import android.os.Build;
import androidx.annotation.Nullable;

import com.amplifyframework.core.category.Category;
import com.amplifyframework.core.plugin.Plugin;

import java.util.Objects;

/**
 * POJO for a 3rd party Amplify plugin.
 * @param <P> Plugin type.
 */
public final class AmplifyExtension<P extends Plugin<?>> {
    private ExtendedCategoryConfiguration extendedCategoryConfiguration;
    private Plugin<?> plugin;
    private ExtendedCategory<P> category;
    private String configFileName;

    /**
     * Constructor for the {@link AmplifyExtension}.
     * @param category An instance of the plugin's {@link Category}.
     * @param plugin An instance of the {@link Plugin}.
     * @param configFileName Name of the configuration file for the plugin.
     * @param extendedCategoryConfiguration An empty instance of {@link ExtendedCategoryConfiguration}.
     */
    public AmplifyExtension(ExtendedCategory<P> category, Plugin<?> plugin, String configFileName,
                            ExtendedCategoryConfiguration extendedCategoryConfiguration) {
        this.extendedCategoryConfiguration = Objects.requireNonNull(extendedCategoryConfiguration);
        this.category = Objects.requireNonNull(category);
        this.plugin = Objects.requireNonNull(plugin);
        this.configFileName = Objects.requireNonNull(configFileName);
    }

    /**
     * Get the {@link Category}.
     * @return The {@link Category}.
     */
    public ExtendedCategory<P> getCategory() {
        return category;
    }

    /**
     * Get the {@link ExtendedCategoryConfiguration}.
     * @return An {@link ExtendedCategoryConfiguration}.
     */
    public ExtendedCategoryConfiguration getExtendedCategoryConfiguration() {
        return extendedCategoryConfiguration;
    }

    /**
     * Get the {@link Plugin}.
     * @return The {@link Plugin}.
     */
    public Plugin<?> getPlugin() {
        return plugin;
    }

    /**
     * Get the config file name.
     * @return The config file name.
     */
    public String getConfigFileName() {
        return configFileName;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            return Objects.hash(plugin, category, configFileName);
        } else {
            return hashCode();
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean equals(@Nullable Object obj) {
        return super.equals(obj);
    }
}
