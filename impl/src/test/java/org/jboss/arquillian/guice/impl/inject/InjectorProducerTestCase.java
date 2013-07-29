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
package org.jboss.arquillian.guice.impl.inject;

import com.google.inject.Guice;
import com.google.inject.Injector;
import org.jboss.arquillian.guice.api.annotation.GuiceConfiguration;
import org.jboss.arquillian.guice.api.annotation.GuiceInjector;
import org.jboss.arquillian.guice.api.annotation.GuiceWebConfiguration;
import org.jboss.arquillian.guice.api.utils.InjectorHolder;
import org.jboss.arquillian.guice.impl.model.EmployeeModule;
import org.jboss.arquillian.test.spi.context.ClassContext;
import org.jboss.arquillian.test.spi.event.suite.AfterSuite;
import org.jboss.arquillian.test.spi.event.suite.BeforeClass;
import org.jboss.arquillian.test.spi.event.suite.BeforeSuite;
import org.jboss.arquillian.test.test.AbstractTestTestBase;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

/**
 * Tests the {@link InjectorProducer} class.
 *
 * @author <a href="mailto:jmnarloch@gmail.com">Jakub Narloch</a>
 */
public class InjectorProducerTestCase extends AbstractTestTestBase {

    /**
     * Represents the instance of the tested class.
     */
    private InjectorProducer instance;

    /**
     * {@inheritDoc}
     */
    @Override
    protected void addExtensions(List<Class<?>> extensions) {
        extensions.add(InjectorProducer.class);
    }

    /**
     * Sets up the model environment.
     */
    @Before
    public void setUp() {

        // given
        getManager().getContext(ClassContext.class).activate(TestClass.class);
        getManager().fire(new BeforeSuite());

        instance = new InjectorProducer();

        getManager().inject(instance);
    }

    /**
     * Tears down the model environment.
     */
    @After
    public void tearDown() {

        // cleans the context
        getManager().fire(new AfterSuite());
        getManager().getContext(ClassContext.class).deactivate();

        // removes any injector created by the tests
        InjectorHolder.removeInjector();
    }

    /**
     * Tests the {@link InjectorProducer#initInjector(BeforeClass)} method.
     */
    @Test
    public void shouldCreateInjector() {

        // when
        getManager().fire(new BeforeClass(TestClass.class));

        // then
        assertNotNull("The injector hasn't been created.", getManager().resolve(Injector.class));
    }

    /**
     * Tests the {@link InjectorProducer#initInjector(BeforeClass)} method.
     */
    @Test
    public void shouldResolveWebInjector() {

        // given
        Injector injector = Guice.createInjector(new EmployeeModule());
        InjectorHolder.bindInjector(injector);

        // when
        getManager().fire(new BeforeClass(TestClassWithWebConfiguration.class));

        // then
        assertNotNull("The injector hasn't been created.", getManager().resolve(Injector.class));
        assertSame("The injector was not correctly resolved.", injector, getManager().resolve(Injector.class));
    }

    /**
     * Tests the {@link InjectorProducer#initInjector(BeforeClass)} method.
     */
    @Test
    public void shouldResolveNullWebInjector() {

        // given
        // clears any injector created by the tests
        InjectorHolder.removeInjector();

        // when
        getManager().fire(new BeforeClass(TestClassWithWebConfiguration.class));

        // then
        assertNull("The injector was not correctly resolved.", getManager().resolve(Injector.class));
    }

    /**
     * Tests the {@link InjectorProducer#initInjector(BeforeClass)} method when the test class has custom injector.
     */
    @Test
    public void shouldUseCustomInjector() {

        // when
        getManager().fire(new BeforeClass(TestClassWithCustomModule.class));

        // then
        assertNotNull("The injector hasn't been created.", getManager().resolve(Injector.class));
    }

    /**
     * A sample unit test with a custom guice configuration.
     *
     * @author <a href="mailto:jmnarloch@gmail.com">Jakub Narloch</a>
     */
    @GuiceConfiguration(EmployeeModule.class)
    private static class TestClass {

        /**
         * Dummy test method.
         */
        @Test
        public void test() {

            // empty test
        }
    }

    /**
     * A sample unit test with a custom guice configuration.
     *
     * @author <a href="mailto:jmnarloch@gmail.com">Jakub Narloch</a>
     */
    @GuiceWebConfiguration
    private static class TestClassWithWebConfiguration {

        /**
         * Dummy test method.
         */
        @Test
        public void test() {

            // empty test
        }
    }

    /**
     * A sample unit test with custom guice injector factory method.
     *
     * @author <a href="mailto:jmnarloch@gmail.com">Jakub Narloch</a>
     */
    private static class TestClassWithCustomModule {

        /**
         * Creates custom guice injector.
         *
         * @return custom guice injector
         */
        @GuiceInjector
        public static Injector createInjector() {

            return Guice.createInjector(new EmployeeModule());
        }

        /**
         * Dummy test method.
         */
        @Test
        public void test() {

            // empty test
        }
    }
}
