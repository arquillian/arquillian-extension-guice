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
package org.jboss.arquillian.guice.impl.client;

import org.jboss.arquillian.container.test.spi.TestDeployment;
import org.jboss.arquillian.core.api.annotation.ApplicationScoped;
import org.jboss.arquillian.guice.impl.configuration.GuiceExtensionConfiguration;
import org.jboss.arquillian.test.spi.context.ClassContext;
import org.jboss.arquillian.test.spi.event.suite.AfterSuite;
import org.jboss.arquillian.test.spi.event.suite.BeforeSuite;
import org.jboss.arquillian.test.test.AbstractTestTestBase;
import org.jboss.shrinkwrap.api.*;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Tests the {@link GuiceProtocolArchiveProcessor} class.
 *
 * @author <a href="mailto:jmnarloch@gmail.com">Jakub Narloch</a>
 */
public class GuiceProtocolArchiveProcessorTestCase extends AbstractTestTestBase {

    /**
     * Represents the instance of the tested class.
     */
    private GuiceProtocolArchiveProcessor instance;

    /**
     * Represents the extension configuration.
     */
    private GuiceExtensionConfiguration configuration;

    /**
     * Represents the deployment archive.
     */
    private Archive deploymentArchive;

    /**
     * Represents the protocol archive.
     */
    private Archive auxiliaryArchive;

    /**
     * Represents the auxiliary archive.
     */
    private WebArchive protocolArchive;

    /**
     * {@inheritDoc}
     */
    @Override
    protected void addExtensions(List<Class<?>> extensions) {
        extensions.add(GuiceProtocolArchiveProcessor.class);
    }

    /**
     * Sets up the model environment.
     */
    @Before
    public void setUp() {

        // given
        getManager().getContext(ClassContext.class).activate(GuiceProtocolArchiveProcessorTestCase.class);
        getManager().fire(new BeforeSuite());

        configuration = new GuiceExtensionConfiguration();
        bind(ApplicationScoped.class, GuiceExtensionConfiguration.class, configuration);

        protocolArchive = ShrinkWrap.create(WebArchive.class, "protocol.war");
        auxiliaryArchive = ShrinkWrap.create(JavaArchive.class, "auxiliary.jar");

        instance = new GuiceProtocolArchiveProcessor();
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
     * Tests the {@link GuiceProtocolArchiveProcessor#process(TestDeployment, Archive)} method, when the model
     * deployment archive is a JAR archive.
     *
     * @throws Exception if any error occurs
     */
    @Test
    public void shouldAddDependenciesToJar() throws Exception {

        // given
        deploymentArchive = ShrinkWrap.create(JavaArchive.class, "deployment.jar");
        TestDeployment testDeployment = new TestDeployment(null, deploymentArchive,
                Arrays.<Archive<?>>asList(auxiliaryArchive));

        // when
        instance.process(testDeployment, protocolArchive);

        // then
        assertDependencies(protocolArchive, true);
    }

    /**
     * Tests the {@link GuiceProtocolArchiveProcessor#process(TestDeployment, Archive)} method, when the model
     * deployment archive is a JAR archive and auto packaging is disabled.
     *
     * @throws Exception if any error occurs
     */
    @Test
    public void shouldNotAddDependenciesToJar() throws Exception {

        // given
        deploymentArchive = ShrinkWrap.create(JavaArchive.class, "deployment.jar");
        TestDeployment testDeployment = new TestDeployment(null, deploymentArchive,
                Arrays.<Archive<?>>asList(auxiliaryArchive));

        // disables the auto packaging
        configuration.setAutoPackage(false);

        // when
        instance.process(testDeployment, protocolArchive);

        // then
        assertDependencies(protocolArchive, false);
    }

    /**
     * Tests the {@link GuiceProtocolArchiveProcessor#process(TestDeployment, Archive)} method, when the model
     * deployment archive is a WAR archive.
     *
     * @throws Exception if any error occurs
     */
    @Test
    public void shouldAddDependenciesToWar() throws Exception {

        // given
        deploymentArchive = ShrinkWrap.create(WebArchive.class, "deployment.war");
        TestDeployment testDeployment = new TestDeployment(null, deploymentArchive,
                Arrays.<Archive<?>>asList(auxiliaryArchive));

        // when
        instance.process(testDeployment, protocolArchive);

        // then
        assertDependencies((WebArchive) deploymentArchive, true);
    }

    /**
     * Tests the {@link GuiceProtocolArchiveProcessor#process(TestDeployment, Archive)} method, when the model
     * deployment archive is a WAR archive.
     *
     * @throws Exception if any error occurs
     */
    @Test
    public void shouldNotAddDependenciesToWar() throws Exception {

        // given
        deploymentArchive = ShrinkWrap.create(WebArchive.class, "deployment.war");
        TestDeployment testDeployment = new TestDeployment(null, deploymentArchive,
                Arrays.<Archive<?>>asList(auxiliaryArchive));

        // disables the auto packaging
        configuration.setAutoPackage(false);

        // when
        instance.process(testDeployment, protocolArchive);

        // then
        assertDependencies((WebArchive) deploymentArchive, false);
    }

    /**
     * <p>Checks if all requirement dependencies are present.</p>
     *
     * @param archive  the archive
     * @param required whether the dependencies are required
     */
    private void assertDependencies(WebArchive archive, boolean required) {

        boolean isGuicePresent = false;

        Map<ArchivePath, Node> contentMap = archive.getContent(new Filter<ArchivePath>() {
            public boolean include(ArchivePath object) {
                return object.get().startsWith("/WEB-INF/lib");
            }
        });

        for (ArchivePath key : contentMap.keySet()) {

            if (key.get().contains("/guice")) {

                isGuicePresent = true;
            }
        }

        if (required) {
            assertTrue("Required dependencies is missing: guice.", isGuicePresent);
        } else {
            assertFalse("Dependencies should not be added: guice.", isGuicePresent);
        }
    }
}
