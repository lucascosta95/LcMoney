package br.com.lucascosta.lcmoneyapi.config.property;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@ConfigurationProperties("lcmoney")
@Component
public class LcmoneyApiProperty {

    private final Seguranca seguranca = new Seguranca();

    public Seguranca getSeguranca() {
        return seguranca;
    }

    public static class Seguranca {
        private boolean enableHttps;
        private String origemPermitida = "http://localhost:4200";

        public boolean isEnableHttps() {
            return enableHttps;
        }

        public void setEnableHttps(boolean enableHttps) {
            this.enableHttps = enableHttps;
        }

        public String getOrigemPermitida() {
            return origemPermitida;
        }

        public void setOrigemPermitida(String origemPermitida) {
            this.origemPermitida = origemPermitida;
        }
    }

}
