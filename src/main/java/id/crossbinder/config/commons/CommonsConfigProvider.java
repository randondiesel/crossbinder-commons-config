/*
 * Copyright (c) The original author or authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */

package id.crossbinder.config.commons;

import java.io.File;
import java.io.FileReader;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Locale;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.commons.configuration2.CompositeConfiguration;
import org.apache.commons.configuration2.JSONConfiguration;
import org.apache.commons.configuration2.PropertiesConfiguration;
import org.apache.commons.configuration2.XMLConfiguration;
import org.apache.commons.configuration2.YAMLConfiguration;

import id.crossbinder.hod.ConfigurationProvider;

/**
 *
 * @author randondiesel
 *
 */

public class CommonsConfigProvider implements ConfigurationProvider {

	private static final Logger LOGGER = Logger.getLogger(CommonsConfigProvider.class.getName());

	private CompositeConfiguration rootConfig;

	public CommonsConfigProvider() {
		rootConfig = new CompositeConfiguration();
	}

	public CommonsConfigProvider location(String loc) {
		String locl = loc.toLowerCase(Locale.getDefault());
		try {
			FileReader reader = new FileReader(new File(loc));
			if(locl.endsWith(".properties")) {
				PropertiesConfiguration config = new PropertiesConfiguration();
				config.read(reader);
				rootConfig.addConfiguration(config);
				LOGGER.fine(String.format("properties configuration from %s", loc));
			}
			else if(locl.endsWith(".xml")) {
				XMLConfiguration config = new XMLConfiguration();
				config.read(reader);
				rootConfig.addConfiguration(config);
				LOGGER.fine(String.format("xml configuration from %s", loc));
			}
			else if(locl.endsWith(".json")) {
				JSONConfiguration config = new JSONConfiguration();
				config.read(reader);
				rootConfig.addConfiguration(config);
				LOGGER.fine(String.format("json configuration from %s", loc));
			}
			else if(locl.endsWith(".yml") || locl.endsWith(".yml")) {
				YAMLConfiguration config = new YAMLConfiguration();
				config.read(reader);
				rootConfig.addConfiguration(config);
				LOGGER.fine(String.format("yaml configuration from %s", loc));
			}
			reader.close();
		}
		catch(Exception exep) {
			LOGGER.log(Level.WARNING,
					String.format("unable to load configuration from %s", loc.toString()), exep);
		}
		return this;
	}

	////////////////////////////////////////////////////////////////////////////
	// Methods of interface ConfigurationProvider

	@Override
	public boolean contains(String path) {
		return rootConfig.containsKey(path);
	}

	@Override
	public Object getValue(String path, Class<?> type) {
		if(!rootConfig.containsKey(path)) {
			return null;
		}
		if(type.equals(Byte.TYPE) || type.equals(Byte.class)) {
			return rootConfig.getByte(path, null);
		}
		else if(type.equals(Short.TYPE) || type.equals(Short.class)) {
			return rootConfig.getShort(path, null);
		}
		else if(type.equals(Integer.TYPE) || type.equals(Integer.class)) {
			return rootConfig.getInteger(path, null);
		}
		else if(type.equals(Long.TYPE) || type.equals(Long.class)) {
			return rootConfig.getLong(path, null);
		}
		else if(type.equals(Float.TYPE) || type.equals(Float.class)) {
			return rootConfig.getFloat(path, null);
		}
		else if(type.equals(Double.TYPE) || type.equals(Double.class)) {
			return rootConfig.getDouble(path, null);
		}
		else if(type.equals(Boolean.TYPE) || type.equals(Boolean.class)) {
			return rootConfig.getBoolean(path, null);
		}
		else if(type.equals(String.class)) {
			return rootConfig.getString(path);
		}
		else if(type.equals(BigInteger.class)) {
			return rootConfig.getBigInteger(path);
		}
		else if(type.equals(BigDecimal.class)) {
			return rootConfig.getBigDecimal(path);
		}
		else if(type.equals(Properties.class)) {
			return rootConfig.getProperties(path);
		}
		else if(type.equals(String[].class)) {
			return rootConfig.getStringArray(path);
		}
		else if(type.equals(TimeInterval.class)) {
			String interval = rootConfig.getString(path);
			if(interval == null) {
				return null;
			}
			return new TimeInterval(interval);
		}
		return null;
	}
}
