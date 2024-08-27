package me.batizhao.radius.server;

import org.mindrot.jbcrypt.BCrypt;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

/**
 * @author batizhao
 */
public class DatabaseConnector {

    private static final Logger logger = LoggerFactory.getLogger(DatabaseConnector.class);

    /**
     * 获取数据库连接对象
     *
     * @return 返回数据库连接对象
     * @throws Exception 抛出异常，当无法获取数据库连接时
     */
    public static Connection getConnection() throws Exception {
        ConfigManager configManager = ConfigManager.getInstance();
        return DriverManager.getConnection(configManager.getProperty("db.url"),
                configManager.getProperty("db.username"),
                configManager.getProperty("db.password"));
    }

    /**
     * 验证用户是否存在并密码是否匹配
     *
     * @param username 用户名
     * @param password 密码
     * @return 如果用户名和密码匹配则返回true，否则返回false
     */
    public static boolean authenticateUser(String username, String password) {
        String query = "SELECT password FROM ims_user WHERE username = ?";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                String storedHash = rs.getString("password");
                return BCrypt.checkpw(password, storedHash);
            }
        } catch (Exception e) {
            logger.error("Error while authenticating user.", e);
        }
        return false;
    }

}
