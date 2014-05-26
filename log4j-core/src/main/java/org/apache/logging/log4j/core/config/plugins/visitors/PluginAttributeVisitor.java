/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements. See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.apache.logging.log4j.core.config.plugins.visitors;

import java.util.Map;

import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.config.Configuration;
import org.apache.logging.log4j.core.config.Node;
import org.apache.logging.log4j.core.config.plugins.PluginAttribute;

/**
 * PluginVisitor implementation for {@link PluginAttribute}.
 */
public class PluginAttributeVisitor extends AbstractPluginVisitor<PluginAttribute> {
    public PluginAttributeVisitor() {
        super(PluginAttribute.class);
    }

    @Override
    public Object visit(final Configuration configuration, final Node node, final LogEvent event) {
        final String name = this.annotation.value();
        final Map<String, String> attributes = node.getAttributes();
        final String rawValue = removeAttributeValue(attributes, name, this.aliases);
        final String replacedValue = this.substitutor.replace(event, rawValue);
        final Object defaultValue = findDefaultValue(event);
        final Object value = convert(replacedValue, defaultValue);
        LOGGER.debug("Attribute({}=\"{}\"", name, value);
        return value;
    }

    private Object findDefaultValue(final LogEvent event) {
        if (this.conversionType == int.class || this.conversionType == Integer.class) {
            return this.annotation.defaultIntValue();
        }
        if (this.conversionType == long.class || this.conversionType == Long.class) {
            return this.annotation.defaultLongValue();
        }
        if (this.conversionType == boolean.class || this.conversionType == Boolean.class) {
            return this.annotation.defaultBooleanValue();
        }
        if (this.conversionType == float.class || this.conversionType == Float.class) {
            return this.annotation.defaultFloatValue();
        }
        if (this.conversionType == double.class || this.conversionType == Double.class) {
            return this.annotation.defaultDoubleValue();
        }
        return this.substitutor.replace(event, this.annotation.defaultStringValue());
    }
}