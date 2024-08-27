package me.batizhao.radius.server;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * @author batizhao
 */
public class ConfigManager {

    private static final Logger logger = LoggerFactory.getLogger(ConfigManager.class);

    private final Properties properties;

    /**
     * 私有构造函数，用于初始化ConfigManager对象。
     * 通过加载类路径下的"config.properties"文件，将配置信息存储到properties对象中。
     * 如果找不到配置文件，则记录错误日志。
     * 如果加载配置文件时发生IO异常，则记录异常信息。
     */
    private ConfigManager() {
        properties = new Properties();
        try (InputStream input = getClass().getClassLoader().getResourceAsStream("config.properties")) {
            if (input != null) {
                properties.load(input);
            } else {
                logger.error("Sorry, unable to find config.properties");
            }
        } catch (IOException e) {
            logger.error("Exception while loading config.properties", e);
        }
    }

    /**
     * 静态内部类，用于实现单例模式的懒汉式。
     * 在类加载时不会创建ConfigManager实例，只有在第一次调用getInstance方法时才会创建，从而起到懒加载的作用。
     */
    private static final class InstanceHolder {
        private static final ConfigManager INSTANCE = new ConfigManager();
    }

    /**
     * 获取ConfigManager单例对象
     *
     * @return 返回ConfigManager单例对象
     */
    public static ConfigManager getInstance() {
        return InstanceHolder.INSTANCE;
    }

    /**
     * 根据给定的键获取属性值
     *
     * @param key 属性键
     * @return 返回属性值，如果未找到则返回null
     */
    public String getProperty(String key) {
        return properties.getProperty(key);
    }

}
