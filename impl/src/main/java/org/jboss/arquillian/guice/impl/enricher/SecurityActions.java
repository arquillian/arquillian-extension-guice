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

import java.security.AccessController;
import java.security.PrivilegedAction;

/**
 * Defines a set of operations that are mend to be executed within security context.
 *
 * @author <a href="mailto:jmnarloch@gmail.com">Jakub Narloch</a>
 */
final class SecurityActions {

    /**
     * Creates new instance of {@link SecurityActions}.
     *
     * Private constructor prevents from instantiation outside this class.
     */
    private SecurityActions() {
        // empty constructor
    }

    /**
     * Returns whether the given class is present in the class path.
     *
     * @param name the class name
     *
     * @return true if the class is present in the class path, false otherwise
     */
    static boolean isClassPresent(String name) {

        try {
            ClassLoader classLoader = getThreadContextClassLoader();
            classLoader.loadClass(name);
            return true;
        } catch (ClassNotFoundException e) {
            return false;
        }
    }

    /**
     * Retrieves the class loader bound to the current thread.
     *
     * @return the class loader bound to the current thread
     */
    static ClassLoader getThreadContextClassLoader() {

        return AccessController.doPrivileged(new PrivilegedAction<ClassLoader>() {

            public ClassLoader run() {
                return Thread.currentThread().getContextClassLoader();
            }
        });
    }
}
