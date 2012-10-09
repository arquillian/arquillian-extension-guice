/*
 * JBoss, Home of Professional Open Source
 * Copyright 2013, Red Hat Middleware LLC, and individual contributors
 * by the @authors tag. See the copyright.txt in the distribution for a
 * full listing of individual contributors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.jboss.arquillian.guice.impl.configuration;

import org.jboss.arquillian.config.descriptor.api.ArquillianDescriptor;
import org.jboss.arquillian.config.descriptor.api.ExtensionDef;
import org.jboss.arquillian.core.api.Instance;
import org.jboss.arquillian.core.api.InstanceProducer;
import org.jboss.arquillian.core.api.annotation.ApplicationScoped;
import org.jboss.arquillian.core.api.annotation.Inject;
import org.jboss.arquillian.core.api.annotation.Observes;
import org.jboss.arquillian.test.spi.event.suite.BeforeSuite;

import java.util.Collections;
import java.util.Map;

/**
 * A producer responsible for creating the extension configuration.
 *
 * @author <a href="mailto:jmnarloch@gmail.com">Jakub Narloch</a>
 * @see GuiceExtensionConfiguration
 */
public class GuiceExtensionConfigurationProducer {

    /**
     * Represents the name of the guice extension configuration.
     */
    private static final String GUICE_EXTENSION = "guice";

    /**
     * Represents the name of property for enabling auto packaging the artifacts.
     */
    private static final String AUTO_PACKAGE_PROPERTY_NAME = "autoPackage";

    /**
     * Represents the name of property for setting the guice artifact version.
     */
    private static final String GUICE_VERSION_PROPERTY_NAME = "guiceVersion";

    /**
     * The arquillian descriptor.
     */
    @Inject
    private Instance<ArquillianDescriptor> descriptor;

    /**
     * The instance of transaction configuration.
     */
    @Inject
    @ApplicationScoped
    private InstanceProducer<GuiceExtensionConfiguration> configurationInstance;

    /**
     * Loads the extension configuration before the test suite is being run.
     *
     * @param beforeSuiteEvent the event fired before execution of the test suite
     */
    public void loadConfiguration(@Observes BeforeSuite beforeSuiteEvent) {

        GuiceExtensionConfiguration config = getConfiguration(descriptor.get());

        configurationInstance.set(config);
    }

    /**
     * Creates the extension configuration from the given arquillian descriptor.
     *
     * @param arquillianDescriptor the arquillian descriptor
     *
     * @return the created configuration
     */
    private GuiceExtensionConfiguration getConfiguration(ArquillianDescriptor arquillianDescriptor) {

        GuiceExtensionConfiguration config = new GuiceExtensionConfiguration();

        Map<String, String> guiceExt = getGuiceExtensionSettings(descriptor.get());
        if (!guiceExt.isEmpty()) {

            if (guiceExt.containsKey(AUTO_PACKAGE_PROPERTY_NAME)) {
                config.setAutoPackage(Boolean.parseBoolean(guiceExt.get(AUTO_PACKAGE_PROPERTY_NAME)));
            }
            config.setGuiceVersion(guiceExt.get(GUICE_VERSION_PROPERTY_NAME));
        }


        return config;
    }

    /**
     * Retrieves the extension settings from the arquillian descriptor.
     *
     * @param arquillianDescriptor the arquillian descriptor
     *
     * @return the extension settings
     */
    private Map<String, String> getGuiceExtensionSettings(ArquillianDescriptor arquillianDescriptor) {

        for (ExtensionDef extensionDef : arquillianDescriptor.getExtensions()) {

            if (GUICE_EXTENSION.equals(extensionDef.getExtensionName())) {

                return extensionDef.getExtensionProperties();
            }
        }

        return Collections.emptyMap();
    }
}