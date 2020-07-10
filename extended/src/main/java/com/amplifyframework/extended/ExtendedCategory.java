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

import androidx.annotation.NonNull;

import com.amplifyframework.core.category.Category;
import com.amplifyframework.core.category.CategoryType;
import com.amplifyframework.core.plugin.Plugin;

/**
 * Extends the {@link Category} class.
 * @param <P> Type of {@link Plugin} this category is for.
 */
public abstract class ExtendedCategory<P extends Plugin<?>> extends Category<P> implements ExtendedCategoryTypeable {

    /**
     * {@inheritDoc}
     */
    @NonNull
    @Override
    public CategoryType getCategoryType() {
        // TODO: change this arbitrary decision
        return CategoryType.API;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public abstract String getExtendedCategoryType();

}
