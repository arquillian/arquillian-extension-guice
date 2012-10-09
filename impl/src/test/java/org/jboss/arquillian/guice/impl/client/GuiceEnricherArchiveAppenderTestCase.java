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
package org.jboss.arquillian.guice.impl.client;

import org.jboss.arquillian.guice.api.annotation.GuiceConfiguration;
import org.jboss.arquillian.guice.api.annotation.GuiceInjector;
import org.jboss.arquillian.guice.impl.GuiceExtensionConsts;
import org.jboss.arquillian.guice.impl.container.GuiceEnricherRemoteExtension;
import org.jboss.arquillian.guice.impl.enricher.GuiceInjectionEnricher;
import org.jboss.arquillian.guice.impl.inject.InjectorProducer;
import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.ArchivePath;
import org.jboss.shrinkwrap.api.ArchivePaths;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 * Tests the {@link GuiceEnricherArchiveAppender} class.
 *
 * @author <a href="mailto:jmnarloch@gmail.com">Jakub Narloch</a>
 */
public class GuiceEnricherArchiveAppenderTestCase {

    /**
     * <p>Represents the list of required classes.</p>
     */
    private final static List<Class<?>> REQUIRED_CLASSES = Arrays.asList(GuiceEnricherRemoteExtension.class,
            GuiceInjectionEnricher.class, InjectorProducer.class, GuiceExtensionConsts.class, GuiceConfiguration.class,
            GuiceInjector.class);

    /**
     * Represents the instance of the tested class.
     */
    private GuiceEnricherArchiveAppender instance;

    /**
     * Sets up the model environment.
     */
    @Before
    public void setUp() {

        // given
        instance = new GuiceEnricherArchiveAppender();
    }

    /**
     * Tests the {@link GuiceEnricherArchiveAppender#buildArchive()} method.
     *
     * @throws Exception if any error occurs
     */
    @Test
    public void shouldBuildArchive() throws Exception {

        // when
        Archive archive = instance.createAuxiliaryArchive();

        // then
        assertNotNull("Method returned null.", archive);
        assertTrue("The returned archive has incorrect type.", archive instanceof JavaArchive);

        for (Class c : REQUIRED_CLASSES) {

            assertTrue("The required type is missing: " + c.getName(),
                    archive.contains(getClassResourcePath(c)));
        }
    }

    /**
     * Retrieves the resource name of the give class.
     *
     * @param c the class
     *
     * @return the resource name for the class
     */
    private static ArchivePath getClassResourcePath(Class c) {

        StringBuilder sb = new StringBuilder();
        sb.append("/");
        sb.append(c.getName().replace(".", "/"));
        sb.append(".class");

        return ArchivePaths.create(sb.toString());
    }
}
