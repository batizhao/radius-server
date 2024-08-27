package me.batizhao.radius.server;

import org.aaa4j.radius.core.attribute.attributes.UserName;
import org.aaa4j.radius.core.attribute.attributes.UserPassword;
import org.aaa4j.radius.core.packet.Packet;
import org.aaa4j.radius.core.packet.packets.AccessAccept;
import org.aaa4j.radius.core.packet.packets.AccessReject;
import org.aaa4j.radius.core.packet.packets.AccessRequest;
import org.aaa4j.radius.server.RadiusServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetAddress;
import java.util.Optional;

/**
 * MyRadiusHandler类实现了RadiusServer.Handler接口，用于处理RADIUS协议相关的请求。
 *
 * @author batizhao
 * @version 1.0
 * @since 1.0
 */
public class MyRadiusHandler implements RadiusServer.Handler {

    private static final Logger logger = LoggerFactory.getLogger(MyRadiusHandler.class);

    /**
     * 处理客户端请求，返回RADIUS共享密钥的字节数组。
     *
     * @param clientAddress 客户端IP地址
     * @return RADIUS共享密钥的字节数组
     */
    @Override
    public byte[] handleClient(InetAddress clientAddress) {
        String secret = ConfigManager.getInstance().getProperty("radius.server.secret");
        return secret.getBytes();
    }

    /**
     * 处理来自客户端的RADIUS数据包
     *
     * @param clientAddress 客户端IP地址
     * @param requestPacket RADIUS请求数据包
     * @return 处理后的RADIUS响应数据包，如果无法处理则返回null
     */
    @Override
    public Packet handlePacket(InetAddress clientAddress, Packet requestPacket) {
        if (requestPacket instanceof AccessRequest) {
            Optional<UserName> userNameAttribute = requestPacket.getAttribute(UserName.class);
            Optional<UserPassword> userPasswordAttribute = requestPacket.getAttribute(UserPassword.class);

            if (userNameAttribute.isPresent() && userPasswordAttribute.isPresent()) {
                String username = userNameAttribute.get().getData().getValue();
                String password = new String(userPasswordAttribute.get().getData().getValue());

                if (DatabaseConnector.authenticateUser(username, password)) {
                    logger.info("User {} authenticated successfully", username);
                    return new AccessAccept();
                } else {
                    logger.error("User {} authentication failed", username);
                    return new AccessReject();
                }
            }

            return null;
        }

        return null;
    }
}
