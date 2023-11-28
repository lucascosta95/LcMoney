package br.com.lucascosta.lcmoneyapi.config.property;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Getter
@Setter
@ConfigurationProperties("lcmoney")
@Component
public class LcmoneyApiProperty {

    private final Seguranca seguranca = new Seguranca();
    private final Mail mail = new Mail();

    @Getter
    @Setter
    public static class Seguranca {
        private boolean enableHttps;
        private String origemPermitida = "http://localhost:4200";
    }

    @Getter
    @Setter
    public static class Mail {
        private String host;
        private Integer port;
        private String username;
        private String senha;
    }

}
