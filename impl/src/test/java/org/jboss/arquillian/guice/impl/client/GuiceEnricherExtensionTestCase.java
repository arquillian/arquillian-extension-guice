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

import org.jboss.arquillian.container.test.spi.client.deployment.AuxiliaryArchiveAppender;
import org.jboss.arquillian.container.test.spi.client.deployment.ProtocolArchiveProcessor;
import org.jboss.arquillian.core.spi.LoadableExtension;
import org.jboss.arquillian.guice.impl.configuration.GuiceExtensionConfiguration;
import org.jboss.arquillian.guice.impl.configuration.GuiceExtensionConfigurationProducer;
import org.jboss.arquillian.guice.impl.enricher.GuiceInjectionEnricher;
import org.jboss.arquillian.guice.impl.inject.InjectorProducer;
import org.jboss.arquillian.test.spi.TestEnricher;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;

/**
 * Tests the {@link GuiceEnricherExtension} class.
 *
 * @author <a href="mailto:jmnarloch@gmail.com">Jakub Narloch</a>
 */
@RunWith(MockitoJUnitRunner.class)
public class GuiceEnricherExtensionTestCase {

    /**
     * Represents the instance of the tested class.
     */
    private GuiceEnricherExtension instance;

    /**
     * Represents the instance of the extension builder.
     */
    @Mock
    private LoadableExtension.ExtensionBuilder extensionBuilder;

    /**
     * Sets up the model environment.
     */
    @Before
    public void setUp() {

        // given
        instance = new GuiceEnricherExtension();

        when(extensionBuilder.service(any(Class.class), any(Class.class))).thenReturn(extensionBuilder);
        when(extensionBuilder.observer(any(Class.class))).thenReturn(extensionBuilder);
    }

    /**
     * Tests the {@link GuiceEnricherExtension#register(LoadableExtension.ExtensionBuilder)} method.</p>
     */
    @Test
    public void shouldRegisterExtension() {

        // when
        instance.register(extensionBuilder);

        // then
        verify(extensionBuilder).service(AuxiliaryArchiveAppender.class, GuiceEnricherArchiveAppender.class);
        verify(extensionBuilder).service(ProtocolArchiveProcessor.class, GuiceProtocolArchiveProcessor.class);
        verify(extensionBuilder).observer(GuiceExtensionConfigurationProducer.class);
        verify(extensionBuilder).service(TestEnricher.class, GuiceInjectionEnricher.class);
        verify(extensionBuilder).observer(InjectorProducer.class);

        verifyNoMoreInteractions(extensionBuilder);
    }
}
