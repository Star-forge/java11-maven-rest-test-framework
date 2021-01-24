/*
  (c) 2018 Mobile TeleSystems PJSC, Russia
  All Rights Reserved.

  NOTICE:  All information contained herein is, and remains
  the property of Mobile TeleSystems PJSC ("MTS" - NYSE:MBT; MOEX:MTSS)
  and its suppliers, if any. The intellectual and technical concepts
  contained herein are proprietary to Mobile TeleSystems PJSC
  and its suppliers, and are protected by trade secret or copyright law.
  Dissemination of this information or reproduction of this material
  is strictly forbidden unless prior written permission is obtained
  from Mobile TeleSystems PJSC.
 */
package config;

import exception.TestRuntimeException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
import java.net.URL;
import java.util.Properties;

/**
 * @author starforge
 */
public class PropertyLoader {
    // --------------------------------
    // ---------  REST SERVICE --------
    public static final String REST_SERVER_URL;
    public static final String REST_USERNAME;
    public static final String REST_PASSWORD;
    private static final String DEFAULT_PROPERTIES_FILE_NAME = "/autotest.properties";
    // --------------------------------------
    // -------------- AUTOTEST --------------
    private static final Logger mainLog = LoggerFactory.getLogger("main");
    private static final String PROPERTIES_FILE;
    private static final Properties properties = new Properties();

    static {
        String propFile = System.getProperty("prop");
        if (null == propFile || propFile.trim().isEmpty()) {
            propFile = DEFAULT_PROPERTIES_FILE_NAME;
        }
        PROPERTIES_FILE = propFile;
        mainLog.info("Используется файл свойств: '{}'", PROPERTIES_FILE);

        try (InputStream inputStream = getPropertiesAsStream()) {
            properties.load(inputStream);
        } catch (IOException ex) {
            throw new UncheckedIOException("Failed to load resource", ex);
        }

// ---------  REST SERVICE --------
        REST_SERVER_URL = getProperty("rest.server.url");
        REST_USERNAME = getProperty("rest.username");
        REST_PASSWORD = getProperty("rest.password");
    }

    private PropertyLoader() {
    }

    public static String getProperty(String propertyKey) {
        String prop = System.getProperty(propertyKey);
        if (null == prop || prop.trim().isEmpty()) {
            prop = properties.getProperty(propertyKey);
        }
        return prop;
    }

    private static InputStream getPropertiesAsStream() {
        if (isPropertiesAvailable()) {
            return PropertyLoader.class.getResourceAsStream(PROPERTIES_FILE);
        } else {
            mainLog.error("Failed to load resource {}", PROPERTIES_FILE);
            throw new TestRuntimeException("Failed to load resource" + PROPERTIES_FILE);
        }
    }

    private static boolean isPropertiesAvailable() {
        URL res = PropertyLoader.class.getResource(PROPERTIES_FILE);
        return null != res;
    }
}
