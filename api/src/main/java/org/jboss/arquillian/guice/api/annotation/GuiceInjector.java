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
package org.jboss.arquillian.guice.api.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Allows to construct custom injector in the test class.
 * <p />
 * This is a marker annotation which allows to define a static factory method within the test that will be responsible
 * for creating the instance of the Guice injector at the runtime.
 *
 * The method should have fallowing signature:
 * <pre>
 * public static Injector [method_name]();
 * </pre>
 *
 * The actual return type must be assignable into a Injector type, otherwise a {@link ClassCastException} will be
 * thrown.
 *
 * @author <a href="mailto:jmnarloch@gmail.com">Jakub Narloch</a>
 */
@Documented
@Retention(RUNTIME)
@Target(METHOD)
public @interface GuiceInjector {
}
