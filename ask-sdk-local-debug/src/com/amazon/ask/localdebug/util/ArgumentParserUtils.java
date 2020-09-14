/*

     Copyright 2020 Amazon.com, Inc. or its affiliates. All Rights Reserved.

     Licensed under the Apache License, Version 2.0 (the "License"). You may not use
     this file except in compliance with the License. A copy of the License is located at

         http://aws.amazon.com/apache2.0/

     or in the "license" file accompanying this file. This file is distributed on an "AS IS" BASIS,
     WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for
     the specific language governing permissions and limitations under the License.

 */

package com.amazon.ask.localdebug.util;

import com.amazon.ask.localdebug.exception.LocalDebugSdkException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * Util for parsing command line arguments.
 */
public final class ArgumentParserUtils {
    /**
     * Logger instance of the class.
     */
    private static final Logger LOG = LoggerFactory.getLogger(ArgumentParserUtils.class);

    /**
     * Private constructor.
     */
    private ArgumentParserUtils() {
    }

    /**
     * Gets argument value.
     * @param argumentName Name of the argument for which the value needs to be discovered.
     * @param argumentList Input arguments parsed as a list.
     * @param isRequired Mark argument as required.
     * @return argument value object.
     */
    public static Object getArgumentValue(final String argumentName, final List<String> argumentList,
                                          final boolean isRequired) {
        return getArgumentValue(argumentName, argumentList, isRequired, null);
    }

    /**
     * Gets argument value.
     * @param argumentName Name of the argument for which the value needs to be discovered.
     * @param argumentList Input arguments parsed as a list.
     * @return argument value object.
     */
    public static Object getArgumentValue(final String argumentName, final List<String> argumentList) {
        return getArgumentValue(argumentName, argumentList, true, null);
    }

  /**
   * Deconstructs the user provided arguments into discrete configuration values.
   *
   * @param argumentName Argument for which the value needs to be identified.
   * @param argumentList Input arguments parsed as a list.
   * @param isRequired Mark argument as required.
   * @param defaultValue default value to be set if argument value is not available.
   * @return argument value object
   */
  public static Object getArgumentValue(
      final String argumentName,
      final List<String> argumentList,
      final boolean isRequired,
      final Object defaultValue) {
        int index = argumentList.indexOf(String.format("--%s", argumentName));
        if (index == -1) {
            if (isRequired) {
                String error = String.format("Required argument - %s not provided.", argumentName);
                LOG.error(error);
                throw new LocalDebugSdkException(error);
            }
            return defaultValue;
        }
        index++;
        try {
            return argumentList.get(index);
        } catch (Exception ex) {
            LOG.error(ex.getMessage());
            throw new LocalDebugSdkException(ex.getMessage(), ex);
        }
    }
}
