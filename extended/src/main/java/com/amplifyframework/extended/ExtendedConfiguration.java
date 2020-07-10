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
import android.content.res.Resources;
import androidx.annotation.NonNull;

import com.amplifyframework.AmplifyException;
import com.amplifyframework.core.AmplifyConfiguration;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.util.Scanner;

/**
 * Utility class for reading 3rd party Amplify plugin configurations.
 */
public final class ExtendedConfiguration {

    private ExtendedConfiguration() { }

    /**
     * Read a configuration defined by emptyConfig from the file specified
     * by configFileName.
     * @param context Application context.
     * @param configFileName Configuration file name (do not include the extension).
     * @param emptyConfig A new instance of an {@link ExtendedCategoryConfiguration} for your plugin.
     * @return An {@link ExtendedCategoryConfiguration} read from the config file.
     * @throws AmplifyException If there was a problem reading the config file.
     */
    public static ExtendedCategoryConfiguration read(@NonNull Context context, String configFileName,
                                                     ExtendedCategoryConfiguration emptyConfig)
            throws AmplifyException {
        try {
            JSONObject json = readInputJson(context, getConfigResourceId(context, configFileName), configFileName);
            return configFromJson(json, emptyConfig, configFileName);
        } catch (AmplifyException exception) {
            throw new AmplifyException("Plugin configuration file contains malformed JSON.", exception,
                    "Consider re-generating the config files.");
        }
    }

    /** See {@link AmplifyConfiguration}. */
    private static JSONObject readInputJson(Context context, int resourceId, String configFileName)
            throws AmplifyException {
        InputStream inputStream;

        try {
            inputStream = context.getResources().openRawResource(resourceId);
        } catch (Resources.NotFoundException exception) {
            throw new AmplifyException(
                    "Failed to find " + configFileName + ".",
                    exception, "Please check that it has been created."
            );
        }

        final Scanner in = new Scanner(inputStream);
        final StringBuilder sb = new StringBuilder();
        while (in.hasNextLine()) {
            sb.append(in.nextLine());
        }
        in.close();

        try {
            return new JSONObject(sb.toString());
        } catch (JSONException jsonError) {
            throw new AmplifyException(
                    "Failed to read " + configFileName + ".",
                    jsonError, "Please check that it is correctly formed."
            );
        }
    }

    private static ExtendedCategoryConfiguration configFromJson(@NonNull JSONObject json,
                                                                ExtendedCategoryConfiguration categoryConfiguration,
                                                                String configFileName) throws AmplifyException {
        try {
            String categoryJsonKey = categoryConfiguration.getExtendedCategoryType();
            if (json.has(categoryJsonKey)) {
                categoryConfiguration.populateFromJSON(json.getJSONObject(categoryJsonKey));
            }
            return categoryConfiguration;
        } catch (JSONException error) {
            throw new AmplifyException(
                    "Could not parse " + configFileName + ".json ",
                    error, "Check any modifications made to the file."
            );
        }
    }

    /** See {@link com.amplifyframework.core.AmplifyConfiguration}. */
    private static int getConfigResourceId(Context context, String configFileName) throws AmplifyException {
        try {
            return context.getResources()
                    .getIdentifier(configFileName, "raw", context.getPackageName());
        } catch (Exception exception) {
            throw new AmplifyException(
                    "Failed to read " + configFileName + ".",
                    exception, "Please check that it is correctly formed."
            );
        }
    }
}
