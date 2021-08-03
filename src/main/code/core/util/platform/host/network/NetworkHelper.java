package core.util.platform.host.network;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetAddress;
import java.net.UnknownHostException;

public class NetworkHelper {

    private NetworkHelper() { }

    private static final Logger LOGGER = LoggerFactory.getLogger(NetworkHelper.class);

    public static String getMachineIPAddress(){
        try {
            return InetAddress.getLocalHost().getHostAddress();
        } catch (UnknownHostException e) {
            LOGGER.debug(e.getMessage());
        }
        return getLoopBackAddress();
    }

    public static String getLoopBackAddress(){
        return InetAddress.getLoopbackAddress().getHostAddress();
    }
}
