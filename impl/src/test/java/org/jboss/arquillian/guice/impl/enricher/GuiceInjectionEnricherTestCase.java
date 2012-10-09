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
package org.jboss.arquillian.guice.impl.enricher;

import com.google.inject.Guice;
import com.google.inject.Inject;
import com.google.inject.Injector;
import org.jboss.arquillian.core.api.annotation.ApplicationScoped;
import org.jboss.arquillian.guice.api.annotation.GuiceConfiguration;
import org.jboss.arquillian.guice.impl.model.EmployeeModule;
import org.jboss.arquillian.guice.impl.model.EmployeeService;
import org.jboss.arquillian.test.spi.context.ClassContext;
import org.jboss.arquillian.test.spi.event.suite.AfterSuite;
import org.jboss.arquillian.test.spi.event.suite.BeforeSuite;
import org.jboss.arquillian.test.test.AbstractTestTestBase;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static junit.framework.Assert.assertNull;
import static org.junit.Assert.assertNotNull;

/**
 * Tests the {@link GuiceInjectionEnricher} class.
 *
 * @author <a href="mailto:jmnarloch@gmail.com">Jakub Narloch</a>
 */
public class GuiceInjectionEnricherTestCase extends AbstractTestTestBase {

    /**
     * Represents the instance of the tested class.
     */
    private GuiceInjectionEnricher instance;

    /**
     * Represents the test class.
     */
    private TestClass testClass;

    /**
     * {@inheritDoc}
     */
    @Override
    protected void addExtensions(List<Class<?>> extensions) {
        extensions.add(GuiceInjectionEnricher.class);
    }

    /**
     * Sets up the model environment.
     */
    @Before
    public void setUp() {

        // given
        getManager().getContext(ClassContext.class).activate(TestClass.class);
        getManager().fire(new BeforeSuite());

        instance = new GuiceInjectionEnricher();
        testClass = new TestClass();
        Injector injector = Guice.createInjector(new EmployeeModule());

        bind(ApplicationScoped.class, Injector.class, injector);
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
    }

    /**
     * Tests the {@link GuiceInjectionEnricher#enrich(Object)} method.
     */
    @Test
    public void shouldInjectDependency() {

        // when
        instance.enrich(testClass);

        // then
        assertNotNull("The employee service has not been injected.", testClass.getEmployeeService());
    }

    /**
     * Tests the {@link GuiceInjectionEnricher#enrich(Object)} method.
     */
    @Test
    public void shouldNotInjectDependency() {

        // when
        instance.enrich(testClass);

        // then
        assertNull("The employee service has been injected.", testClass.getNullEmployeeService());
    }

    /**
     * A sample unit test with a custom guice configuration.
     */
    @GuiceConfiguration(EmployeeModule.class)
    private static class TestClass {

        /**
         * The injected {@link EmployeeService}.
         */
        @Inject
        EmployeeService employeeService;

        /**
         * Not annotated field.
         */
        EmployeeService nullEmployeeService;

        /**
         * Dummy test method.
         */
        @Test
        public void test() {

            // empty test
        }

        /**
         * Retrieves the employee service.
         *
         * @return the employee service
         */
        public EmployeeService getEmployeeService() {
            return employeeService;
        }

        /**
         * Retrieves the "null" employee service.
         *
         * @return the "null" employee service
         */
        public EmployeeService getNullEmployeeService() {
            return nullEmployeeService;
        }
    }
}
