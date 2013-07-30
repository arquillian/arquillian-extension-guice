/*
 * JBoss, Home of Professional Open Source
 * Copyright 2012, Red Hat Middleware LLC, and individual contributors
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
package org.jboss.arquillian.extension.guice.testsuite;

import com.google.inject.Guice;
import com.google.inject.Inject;
import com.google.inject.Injector;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.extension.guice.testsuite.repository.EmployeeRepository;
import org.jboss.arquillian.extension.guice.testsuite.repository.impl.DefaultEmployeeRepository;
import org.jboss.arquillian.extension.guice.testsuite.service.EmployeeService;
import org.jboss.arquillian.extension.guice.testsuite.service.impl.DefaultEmployeeService;
import org.jboss.arquillian.guice.api.annotation.GuiceInjector;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * Test the {@link DefaultEmployeeRepository} class.
 *
 * @author <a href="mailto:jmnarloch@gmail.com">Jakub Narloch</a>
 */
@RunWith(Arquillian.class)
public class CustomInjectorTestCase {

    /**
     * Creates the test deployment.
     *
     * @return the test deployment
     */
    @Deployment
    public static Archive createTestArchive() {

        return ShrinkWrap.create(JavaArchive.class, "guice-test.jar")
                .addClasses(Employee.class,
                        EmployeeService.class, DefaultEmployeeService.class,
                        EmployeeRepository.class, DefaultEmployeeRepository.class,
                        EmployeeModule.class);
    }

    /**
     * Creates the Guice injector.
     *
     * @return the Guice {@link Injector}
     */
    @GuiceInjector
    public static Injector createInjector() {

        return Guice.createInjector(new EmployeeModule());
    }

    /**
     * <p>The injected {@link EmployeeService}.</p>
     */
    @Inject
    private EmployeeService employeeService;

    /**
     * <p>Tests the {@link EmployeeService#getEmployees()}</p>
     */
    @Test
    public void testGetEmployees() {

        List<Employee> result = employeeService.getEmployees();

        assertNotNull("Method returned null list as result.", result);
        assertEquals("Two employees were expected.", 2, result.size());
    }
}
