package org.jboss.arquillian.extension.guice.testsuite.servlet;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.servlet.GuiceServletContextListener;
import com.google.inject.servlet.ServletModule;
import org.jboss.arquillian.extension.guice.testsuite.EmployeeModule;

/**
 * A context listener that sets up the guice injector.
 *
 * @author <a href="mailto:jmnarloch@gmail.com">Jakub Narloch</a>
 */
public class EmployeeModuleContextListener extends GuiceServletContextListener {

    /**
     * Creates the Guice injector.
     *
     * @return the Guice injector
     */
    @Override
    protected Injector getInjector() {

        return Guice.createInjector(new ServletModule(), new EmployeeModule());
    }
}
