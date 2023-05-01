package br.com.lucascosta.lcmoneyapi.config.property;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@ConfigurationProperties("lcmoney")
@Component
public class LcmoneyApiProperty {

    private final Seguranca seguranca = new Seguranca();
    private final Mail mail = new Mail();

    public Seguranca getSeguranca() {
        return seguranca;
    }

    public Mail getMail() {
        return mail;
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

    public static class Mail {
        private String host;
        private Integer port;
        private String username;
        private String senha;

        public String getHost() {
            return host;
        }

        public void setHost(String host) {
            this.host = host;
        }

        public Integer getPort() {
            return port;
        }

        public void setPort(Integer port) {
            this.port = port;
        }

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getSenha() {
            return senha;
        }

        public void setSenha(String senha) {
            this.senha = senha;
        }
    }

}
