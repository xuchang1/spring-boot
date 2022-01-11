/*
 * Copyright 2012-2021 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.springframework.boot.context.properties.source;

import java.util.Collections;
import java.util.stream.Stream;

import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.ConfigurablePropertyResolver;
import org.springframework.core.env.Environment;
import org.springframework.core.env.MutablePropertySources;
import org.springframework.core.env.PropertyResolver;
import org.springframework.core.env.PropertySource;
import org.springframework.core.env.PropertySource.StubPropertySource;
import org.springframework.core.env.PropertySources;
import org.springframework.core.env.PropertySourcesPropertyResolver;
import org.springframework.util.Assert;

/**
 * Provides access to {@link ConfigurationPropertySource ConfigurationPropertySources}.
 *
 * @author Phillip Webb
 * @since 2.0.0
 */
public final class ConfigurationPropertySources {

	/**
	 * The name of the {@link PropertySource} {@link #attach(Environment) adapter}.
	 */
	private static final String ATTACHED_PROPERTY_SOURCE_NAME = "configurationProperties";

	private ConfigurationPropertySources() {
	}

	/**
	 * Create a new {@link PropertyResolver} that resolves property values against an
	 * underlying set of {@link PropertySources}. Provides an
	 * {@link ConfigurationPropertySource} aware and optimized alternative to
	 * {@link PropertySourcesPropertyResolver}.
	 * @param propertySources the set of {@link PropertySource} objects to use
	 * @return a {@link ConfigurablePropertyResolver} implementation
	 * @since 2.5.0
	 */
	public static ConfigurablePropertyResolver createPropertyResolver(MutablePropertySources propertySources) {
		return new ConfigurationPropertySourcesPropertyResolver(propertySources);
	}

	/**
	 * Determines if the specific {@link PropertySource} is the
	 * {@link ConfigurationPropertySource} that was {@link #attach(Environment) attached}
	 * to the {@link Environment}.
	 * @param propertySource the property source to test
	 * @return {@code true} if this is the attached {@link ConfigurationPropertySource}
	 */
	public static boolean isAttachedConfigurationPropertySource(PropertySource<?> propertySource) {
		return ATTACHED_PROPERTY_SOURCE_NAME.equals(propertySource.getName());
	}

	/**
	 * Attach a {@link ConfigurationPropertySource} support to the specified
	 * {@link Environment}. Adapts each {@link PropertySource} managed by the environment
	 * to a {@link ConfigurationPropertySource} and allows classic
	 * {@link PropertySourcesPropertyResolver} calls to resolve using
	 * {@link ConfigurationPropertyName configuration property names}.
	 * <p>
	 * The attached resolver will dynamically track any additions or removals from the
	 * underlying {@link Environment} property sources.
	 * @param environment the source environment (must be an instance of
	 * {@link ConfigurableEnvironment})
	 * @see #get(Environment)
	 */
	// springboot准备environment过程中，会调用该方法
	public static void attach(Environment environment) {
		Assert.isInstanceOf(ConfigurableEnvironment.class, environment);
		MutablePropertySources sources = ((ConfigurableEnvironment) environment).getPropertySources();
		PropertySource<?> attached = getAttached(sources);

		// 不相等，则移除（基于下面添加逻辑可以知道，attached里面的source就是environment中的sources）
		if (attached != null && attached.getSource() != sources) {
			sources.remove(ATTACHED_PROPERTY_SOURCE_NAME);
			attached = null;
		}

		// 重新封装了一层，加到最前面（SpringConfigurationPropertySources类型，sources就是Environment中的所有属性集合）
		// ConfigurationPropertySourcesPropertySource封装了第二层
		if (attached == null) {
			sources.addFirst(new ConfigurationPropertySourcesPropertySource(ATTACHED_PROPERTY_SOURCE_NAME,
					new SpringConfigurationPropertySources(sources)));
		}
	}

	static PropertySource<?> getAttached(MutablePropertySources sources) {
		return (sources != null) ? sources.get(ATTACHED_PROPERTY_SOURCE_NAME) : null;
	}

	/**
	 * Return {@link Iterable} containing a single new {@link ConfigurationPropertySource}
	 * adapted from the given Spring {@link PropertySource}.
	 * @param source the Spring property source to adapt
	 * @return an {@link Iterable} containing a single newly adapted
	 * {@link SpringConfigurationPropertySource}
	 */
	public static Iterable<ConfigurationPropertySource> from(PropertySource<?> source) {
		return Collections.singleton(ConfigurationPropertySource.from(source));
	}

	/**
	 * Return a set of {@link ConfigurationPropertySource} instances that have previously
	 * been {@link #attach(Environment) attached} to the {@link Environment}.
	 * @param environment the source environment (must be an instance of
	 * {@link ConfigurableEnvironment})
	 * @return an iterable set of configuration property sources
	 * @throws IllegalStateException if not configuration property sources have been
	 * attached
	 */
	public static Iterable<ConfigurationPropertySource> get(Environment environment) {
		Assert.isInstanceOf(ConfigurableEnvironment.class, environment);
		MutablePropertySources sources = ((ConfigurableEnvironment) environment).getPropertySources();
		// configurationProperties 这个名称对应的 PropertySources
		ConfigurationPropertySourcesPropertySource attached = (ConfigurationPropertySourcesPropertySource) sources
				.get(ATTACHED_PROPERTY_SOURCE_NAME);
		if (attached == null) {
			// 封装了SpringConfigurationPropertySources对象
			return from(sources);
		}

		// 正常情况下，这个地方返回的就是 SpringConfigurationPropertySources封装的 environment 中的所有属性集合对象
		return attached.getSource();
	}

	/**
	 * Return {@link Iterable} containing new {@link ConfigurationPropertySource}
	 * instances adapted from the given Spring {@link PropertySource PropertySources}.
	 * <p>
	 * This method will flatten any nested property sources and will filter all
	 * {@link StubPropertySource stub property sources}. Updates to the underlying source,
	 * identified by changes in the sources returned by its iterator, will be
	 * automatically tracked. The underlying source should be thread safe, for example a
	 * {@link MutablePropertySources}
	 * @param sources the Spring property sources to adapt
	 * @return an {@link Iterable} containing newly adapted
	 * {@link SpringConfigurationPropertySource} instances
	 */
	public static Iterable<ConfigurationPropertySource> from(Iterable<PropertySource<?>> sources) {
		return new SpringConfigurationPropertySources(sources);
	}

	private static Stream<PropertySource<?>> streamPropertySources(PropertySources sources) {
		return sources.stream().flatMap(ConfigurationPropertySources::flatten)
				.filter(ConfigurationPropertySources::isIncluded);
	}

	private static Stream<PropertySource<?>> flatten(PropertySource<?> source) {
		if (source.getSource() instanceof ConfigurableEnvironment) {
			return streamPropertySources(((ConfigurableEnvironment) source.getSource()).getPropertySources());
		}
		return Stream.of(source);
	}

	private static boolean isIncluded(PropertySource<?> source) {
		return !(source instanceof StubPropertySource)
				&& !(source instanceof ConfigurationPropertySourcesPropertySource);
	}

}
