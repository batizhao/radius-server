package me.batizhao.radius.server;

import org.aaa4j.radius.server.RadiusServer;
import org.aaa4j.radius.server.servers.UdpRadiusServer;

import java.net.InetSocketAddress;

/**
 * Server类，用于启动Radius服务器
 *
 * @author batizhao
 * @since 2024/8/20
 */
public class Server {

    public static void main(String[] args) throws Exception {
        String portStr = ConfigManager.getInstance().getProperty("radius.server.port");
        if (portStr == null) {
            throw new IllegalArgumentException("radius.server.port property is missing");
        }
        int port = Integer.parseInt(portStr);

        RadiusServer radiusServer = UdpRadiusServer.newBuilder()
                .bindAddress(new InetSocketAddress(port))
                .handler(new MyRadiusHandler())
                .build();

        radiusServer.start();
    }

}
