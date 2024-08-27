package me.batizhao.radius.server;

import org.aaa4j.radius.client.RadiusClient;
import org.aaa4j.radius.client.RadiusClientException;
import org.aaa4j.radius.client.clients.UdpRadiusClient;
import org.aaa4j.radius.core.attribute.StringData;
import org.aaa4j.radius.core.attribute.TextData;
import org.aaa4j.radius.core.attribute.attributes.UserName;
import org.aaa4j.radius.core.attribute.attributes.UserPassword;
import org.aaa4j.radius.core.packet.Packet;
import org.aaa4j.radius.core.packet.packets.AccessAccept;
import org.aaa4j.radius.core.packet.packets.AccessReject;
import org.aaa4j.radius.core.packet.packets.AccessRequest;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetSocketAddress;
import java.util.List;

import static java.nio.charset.StandardCharsets.UTF_8;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class RadiusServerTest {

    private static final Logger logger = LoggerFactory.getLogger(RadiusServerTest.class);
    private static RadiusClient radiusClient;

    @BeforeAll
    static void beforeAll() {
        logger.info("Starting RadiusServerTest");
        radiusClient = UdpRadiusClient.newBuilder()
                .secret("mysecret".getBytes(UTF_8))
                .address(new InetSocketAddress("localhost", 1821))
                .build();
    }

//    @Test
//    void givenWrongSecret_WhenSendingAccessRequest_ThenRadiusClientException() {
//        RadiusClient radiusClient2 = UdpRadiusClient.newBuilder()
//                .secret("mysecret2".getBytes(UTF_8))
//                .address(new InetSocketAddress("localhost", 1812))
//                .build();
//        assertThrows(RadiusClientException.class, () -> radiusClient2.send(new AccessRequest()));
//    }

    @Test
    void givenCorrectSecret_WhenSendingAccessRequest_ThenAccessAccept() throws RadiusClientException {
        AccessRequest accessRequest = new AccessRequest(List.of(
                new UserName(new TextData("tom")),
                new UserPassword(new StringData("123456".getBytes(UTF_8)))
        ));

        Packet responsePacket = radiusClient.send(accessRequest);
        logger.info("Received response packet: {}", responsePacket);
        assertInstanceOf(AccessAccept.class, responsePacket);
    }

    @Test
    void givenIncorrectPassword_WhenSendingAccessRequest_ThenAccessReject() throws RadiusClientException {
        AccessRequest accessRequest = new AccessRequest(List.of(
                new UserName(new TextData("tom")),
                new UserPassword(new StringData("12345678".getBytes(UTF_8)))
        ));

        Packet responsePacket = radiusClient.send(accessRequest);
        logger.info("Received response packet: {}", responsePacket);
        assertInstanceOf(AccessReject.class, responsePacket);
    }

    @AfterAll
    static void afterAll() {
        logger.info("Stopping RadiusServerTest");
    }
}
