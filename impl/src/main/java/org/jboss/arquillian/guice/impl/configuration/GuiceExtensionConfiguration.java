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

/**
 * Represents the configuration used by this component. The extension settings are being configured by
 * ArquillianDescriptor (arquillian.xml file).
 *
 * @author <a href="mailto:jmnarloch@gmail.com">Jakub Narloch</a>
 */
public class GuiceExtensionConfiguration {

    /**
     * Represents whether to auto package the guice artifacts.
     */
    private boolean autoPackage = true;

    /**
     * Represents the guice artifact version.
     */
    private String guiceVersion;

    /**
     * Creates new instance of {@link GuiceExtensionConfiguration} class.
     */
    public GuiceExtensionConfiguration() {
        // empty constructor
    }

    /**
     * Retrieves whether to auto package the dependencies.
     *
     * @return whether to auto package the dependencies
     */
    public boolean isAutoPackage() {
        return autoPackage;
    }

    /**
     * Sets whether to auto package the dependencies.
     *
     * @param autoPackage whether to auto package the dependencies
     */
    public void setAutoPackage(boolean autoPackage) {
        this.autoPackage = autoPackage;
    }

    /**
     * Retrieves the guice artifact version.
     *
     * @return the guice artifact version
     */
    public String getGuiceVersion() {
        return guiceVersion;
    }

    /**
     * Sets the guice artifact version.
     *
     * @param guiceVersion the guice artifact version
     */
    public void setGuiceVersion(String guiceVersion) {
        this.guiceVersion = guiceVersion;
    }
}
