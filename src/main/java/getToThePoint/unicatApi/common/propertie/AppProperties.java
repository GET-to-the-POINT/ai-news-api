package getToThePoint.unicatApi.common.propertie;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.core.io.Resource;

@ConfigurationProperties(prefix = "app")
public record AppProperties(Jwt jwt, Api api) {

    public record Jwt(Resource privateKey, Resource publicKey, String keyId, Cookie cookie) {
        public record Cookie(String name, String domain, String path, boolean secure, boolean httpOnly, String sameSite, int maxAge) {
        }
    }

    public record Api(String protocol, String domain, int port) {
    }
}