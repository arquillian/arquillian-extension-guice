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
package org.jboss.arquillian.guice.api.servlet;

import com.google.inject.Injector;
import com.google.inject.servlet.GuiceFilter;
import org.jboss.arquillian.guice.api.utils.InjectorHolder;

import javax.servlet.FilterConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import java.lang.ref.WeakReference;

/**
 * A custom implementation of the standard {@link GuiceFilter} that additionally bind the created {@link Injector} with
 * current executing thread, allowing to retrieve the injector from directly from the test.
 * <p />
 * The implementation uses the fact that the injector is being passed between the {@link
 * com.google.inject.servlet.GuiceServletContextListener} and {@link GuiceFilter} as a attribute stored in {@link
 * ServletContext}. This class does not alter the default behaviour of the {@link GuiceFilter} (which by the for most
 * of the attributes and methods defines the package scope so could not be easily modified). Since this class inherits
 * from the {@link GuiceFilter} it's the only filter that is required to setup the Guice in servlet environment and in
 * the same time enable the Arquillian tests.
 * <p />
 * The implementation delegates to {@link InjectorHolder} for storing the Injector instance and binding it with the
 * executing thread.
 *
 * @author <a href="mailto:jmnarloch@gmail.com">Jakub Narloch</a>
 * @see GuiceFilter
 * @see InjectorHolder
 */
public class ArquillianGuiceFilter extends GuiceFilter {

    /**
     * Stores the servlet context.
     */
    private WeakReference<ServletContext> servletContext = new WeakReference<ServletContext>(null);

    /**
     * {@inheritDoc}
     */
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

        // retrieves the servlet context
        servletContext = new WeakReference<ServletContext>(filterConfig.getServletContext());

        // stores the injector
        bindInjector();

        // invokes the base method
        super.init(filterConfig);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void destroy() {

        // removes the injector
        removeInjector();

        // calls the base method
        super.destroy();
    }

    /**
     * Binds the injector in the {@link InjectorHolder} with currently executing thread.
     */
    private void bindInjector() {

        Injector injector = getInjector();

        if (injector != null) {

            InjectorHolder.bindInjector(injector);
        }
    }

    /**
     * Removes the injector from the {@link InjectorHolder}.
     */
    private void removeInjector() {

        InjectorHolder.removeInjector();
    }

    /**
     * Retrieves the servlet context.
     *
     * @return the servlet context
     */
    private ServletContext getServletContext() {

        return servletContext.get();
    }

    /**
     * Retrieves the injector from the servlet context attribute.
     *
     * @return the injector retrieved from servlet context attribute
     */
    private Injector getInjector() {

        ServletContext servletContext = getServletContext();

        if (servletContext != null) {

            return (Injector) servletContext.getAttribute(Injector.class.getName());
        }

        return null;
    }
}
