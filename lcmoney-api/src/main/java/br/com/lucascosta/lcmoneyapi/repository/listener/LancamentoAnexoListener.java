package br.com.lucascosta.lcmoneyapi.repository.listener;

import br.com.lucascosta.lcmoneyapi.LcmoneyApiApplication;
import br.com.lucascosta.lcmoneyapi.model.Lancamento;
import br.com.lucascosta.lcmoneyapi.storage.S3;
import org.springframework.util.StringUtils;

import javax.persistence.PostLoad;

public class LancamentoAnexoListener {
    @PostLoad
    public void postLoad(Lancamento lancamento) {
        if (StringUtils.hasText(lancamento.getAnexo())) {
            S3 s3 = LcmoneyApiApplication.getBean(S3.class);
            lancamento.setUrlAnexo(s3.configurarUrl(lancamento.getAnexo()));
        }
    }
}
