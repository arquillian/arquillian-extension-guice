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
package org.jboss.arquillian.guice.impl.enricher;

import com.google.inject.Injector;
import org.jboss.arquillian.core.api.Instance;
import org.jboss.arquillian.core.api.annotation.Inject;
import org.jboss.arquillian.guice.impl.GuiceExtensionConsts;
import org.jboss.arquillian.test.spi.TestEnricher;

import java.lang.reflect.Method;
import java.util.logging.Logger;

/**
 * A Google Guice model enricher, injects guice dependencies into the model class.
 *
 * @author <a href="mailto:jmnarloch@gmail.com">Jakub Narloch</a>
 */
public class GuiceInjectionEnricher implements TestEnricher {

    /**
     * Logger used by this class.
     */
    private static final Logger log = Logger.getLogger(GuiceInjectionEnricher.class.getName());

    /**
     * Instance of Guice {@link Injector}.
     */
    @Inject
    private Instance<Injector> injectorInstance;

    /**
     * {@inheritDoc}
     */
    public void enrich(Object testCase) {

        if (SecurityActions.isClassPresent(GuiceExtensionConsts.INJECTOR) && isInjectorExists(testCase)) {
            injectClass(testCase);
        }
    }

    /**
     * Returns whether the Guice injector exists for a given class.
     *
     * @param testCase the model case
     *
     * @return true if the Guice injector exists for a given class, false otherwise
     */
    private boolean isInjectorExists(Object testCase) {
        return injectorInstance.get() != null;
    }

    /**
     * {@inheritDoc}
     */
    public Object[] resolve(Method method) {

        return new Object[method.getParameterTypes().length];
    }

    /**
     * Injects dependencies into model class.
     *
     * @param testCase the model class
     */
    private void injectClass(Object testCase) {

        // retrieves the injectorInstance
        Injector injector = getInjectorInstance();

        if (injector != null) {

            // injects the dependencies into model model class
            injector.injectMembers(testCase);
            log.fine("Injecting dependencies into guice model " + testCase.getClass().getSimpleName());
        }
    }

    /**
     * Retrieves the {@link Injector} instance
     *
     * @return hte {@link Injector} instance
     */
    private Injector getInjectorInstance() {

        return injectorInstance.get();
    }
}
