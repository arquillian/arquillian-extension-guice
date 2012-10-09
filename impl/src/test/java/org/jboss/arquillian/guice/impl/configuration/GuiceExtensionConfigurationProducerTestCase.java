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
import org.jboss.arquillian.core.api.annotation.ApplicationScoped;
import org.jboss.arquillian.test.spi.context.ClassContext;
import org.jboss.arquillian.test.spi.event.suite.AfterSuite;
import org.jboss.arquillian.test.spi.event.suite.BeforeSuite;
import org.jboss.arquillian.test.test.AbstractTestTestBase;
import org.jboss.shrinkwrap.descriptor.api.Descriptors;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.util.List;

import static junit.framework.Assert.assertNull;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * Tests the {@link GuiceExtensionConfigurationProducer} class.
 *
 * @author <a href="mailto:jmnarloch@gmail.com">Jakub Narloch</a>
 */
public class GuiceExtensionConfigurationProducerTestCase extends AbstractTestTestBase {

    /**
     * Represents the instance of the tested class.
     */
    private GuiceExtensionConfigurationProducer instance;

    /**
     * Represents the arquillian descriptor.
     */
    private ArquillianDescriptor arquillianDescriptor;

    /**
     * {@inheritDoc}
     */
    @Override
    protected void addExtensions(List<Class<?>> extensions) {
        extensions.add(GuiceExtensionConfigurationProducer.class);
    }

    /**
     * Sets up the model environment.
     */
    @Before
    public void setUp() {

        // given
        getManager().getContext(ClassContext.class).activate(GuiceExtensionConfigurationProducerTestCase.class);

        instance = new GuiceExtensionConfigurationProducer();
    }

    /**
     * Tears down the model environment.
     */
    @After
    public void tearDown() {

        // cleans the context
        getManager().fire(new AfterSuite());
        getManager().getContext(ClassContext.class).deactivate();
    }

    /**
     * Tests the {@link GuiceExtensionConfigurationProducer#loadConfiguration(BeforeSuite)} method.
     */
    @Test
    public void shouldCreateConfiguration() {

        // given
        arquillianDescriptor = Descriptors.importAs(ArquillianDescriptor.class)
                .fromFile(new File("src/test/resources", "arquillian.xml"));

        bind(ApplicationScoped.class, ArquillianDescriptor.class, arquillianDescriptor);
        getManager().inject(instance);

        // when
        getManager().fire(new BeforeSuite());

        // then
        GuiceExtensionConfiguration config = getManager().resolve(GuiceExtensionConfiguration.class);
        assertNotNull("The extension configuration hasn't been created.", config);
        assertEquals("The configuration properties is invalid.", false, config.isAutoPackage());
        assertEquals("The configuration properties is invalid.", "3.0", config.getGuiceVersion());
    }

    /**
     * Tests the {@link GuiceExtensionConfigurationProducer#loadConfiguration(BeforeSuite)} method.
     */
    @Test
    public void shouldCreateConfigurationFromDefauls() {

        // given
        arquillianDescriptor = Descriptors.importAs(ArquillianDescriptor.class)
                .fromFile(new File("src/test/resources", "empty-arquillian.xml"));

        bind(ApplicationScoped.class, ArquillianDescriptor.class, arquillianDescriptor);
        getManager().inject(instance);

        // when
        getManager().fire(new BeforeSuite());

        // then
        GuiceExtensionConfiguration config = getManager().resolve(GuiceExtensionConfiguration.class);
        assertNotNull("The extension configuration hasn't been created.", config);
        assertEquals("The configuration properties is invalid.", true, config.isAutoPackage());
        assertNull("The configuration properties is invalid.", config.getGuiceVersion());
    }
}
