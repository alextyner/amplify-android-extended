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

import com.amplifyframework.core.category.CategoryTypeable;

/**
 * Interface requiring an extra identifier for 3rd party plugins.
 */
public interface ExtendedCategoryTypeable extends CategoryTypeable {

    /**
     * Get the category type, but as a string for extra flexibility. Be consistent for best results.
     * @return A String category type.
     */
    String getExtendedCategoryType();

}
