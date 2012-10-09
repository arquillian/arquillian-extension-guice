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
package org.jboss.arquillian.guice.impl.inject;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.ArrayList;
import java.util.List;

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
     * Retrieves the list of methods that were annotated with given annotation.
     *
     * @param source          the class to scan for classes
     * @param annotationClass the annotation
     * @return list of the methods of the given class that were annotated with specified annotation
     */
    static List<Method> getStaticMethodsWithAnnotation(final Class<?> source, final Class<? extends Annotation> annotationClass) {

        return AccessController.doPrivileged(new PrivilegedAction<List<Method>>() {
            public List<Method> run() {

                List<Method> foundMethods = new ArrayList<Method>();
                Class<?> nextSource = source;
                while (nextSource != Object.class) {
                    for (Method method : nextSource.getDeclaredMethods()) {
                        if (Modifier.isStatic(method.getModifiers()) && method.isAnnotationPresent(annotationClass)) {
                            if (!method.isAccessible()) {
                                method.setAccessible(true);
                            }
                            foundMethods.add(method);
                        }
                    }
                    nextSource = nextSource.getSuperclass();
                }
                return foundMethods;
            }
        });
    }
}
