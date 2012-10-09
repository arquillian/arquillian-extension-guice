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
package org.jboss.arquillian.guice.impl.model;

/**
 * Represents an example domain model.
 *
 * @author <a href="mailto:jmnarloch@gmail.com">Jakub Narloch</a>
 */
public class Employee {

    /**
     * Represents the employee first name.
     */
    private String firstName;

    /**
     * Represents the employee second name.
     */
    private String lastName;

    /**
     * Creates new instance of {@link Employee} class.
     */
    public Employee() {
        // empty constructor
    }

    /**
     * Retrieves the employee first name.
     *
     * @return the employee first name
     */
    public String getFirstName() {
        return firstName;
    }

    /**
     * Sets the employee first name.
     *
     * @param firstName the employee first name
     */
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    /**
     * Sets the employee last name.
     *
     * @return the employee last name
     */
    public String getLastName() {
        return lastName;
    }

    /**
     * Sets the employee last name.
     *
     * @param lastName the employee last name
     */
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
}
