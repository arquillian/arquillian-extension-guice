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
package org.jboss.arquillian.guice.api.utils;

import com.google.inject.Injector;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Binds the {@link Injector} with the currently executing thread.
 *
 * @author <a href="mailto:jmnarloch@gmail.com">Jakub Narloch</a>
 */
public final class InjectorHolder {

    /**
     * Each key value pair stores the caseloader and the associated injector instance.
     */
    private static final Map<ClassLoader, Injector> threadInjectors = new ConcurrentHashMap<ClassLoader, Injector>();

    /**
     * Binds the injector instance with currently executing thread.
     *
     * @param injector the injector instance to bind
     */
    public static void bindInjector(Injector injector) {

        threadInjectors.put(Thread.currentThread().getContextClassLoader(), injector);
    }

    /**
     * Removes any injector that has been bound with currently executing thread.
     */
    public static void removeInjector() {

        ClassLoader loader = Thread.currentThread().getContextClassLoader();

        if (loader != null) {
            threadInjectors.remove(loader);
        }
    }

    /**
     * Retrieves the injector instance that has been bound with the thread.
     *
     * @return the injector instance or null if non injector has been bound
     */
    public static Injector getInjector() {

        ClassLoader loader = Thread.currentThread().getContextClassLoader();

        if (loader != null) {
            return threadInjectors.get(loader);
        }

        return null;
    }
}
