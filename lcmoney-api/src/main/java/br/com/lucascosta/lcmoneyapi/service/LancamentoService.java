package br.com.lucascosta.lcmoneyapi.service;

import br.com.lucascosta.lcmoneyapi.mail.Mailer;
import br.com.lucascosta.lcmoneyapi.model.Lancamento;
import br.com.lucascosta.lcmoneyapi.model.Pessoa;
import br.com.lucascosta.lcmoneyapi.repository.LancamentoRepository;
import br.com.lucascosta.lcmoneyapi.repository.PessoaRepository;
import br.com.lucascosta.lcmoneyapi.repository.UsuarioRepository;
import br.com.lucascosta.lcmoneyapi.service.exception.PessoaInexistenteOuInativaException;
import lombok.extern.slf4j.Slf4j;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Locale;
import java.util.Optional;

@Slf4j
@Service
public class LancamentoService {

    @Autowired
    private PessoaRepository pessoaRepository;

    @Autowired
    private LancamentoRepository lancamentoRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private Mailer mailer;

    private static final String DESTINATARIOS = "ROLE_PESQUISAR_LANCAMENTO";

    public Lancamento salvar(Lancamento lancamento) {
        var pessoa = pessoaRepository.findById(lancamento.getPessoa().getId()).orElse(null);

        if (pessoa == null || pessoa.isInativa()) {
            throw new PessoaInexistenteOuInativaException();
        }

        return lancamentoRepository.save(lancamento);
    }

    public Lancamento atualizar(Long id, Lancamento lancamento) {
        var lancamentoSalvo = buscarLancamentoExistente(id);
        if (!lancamento.getPessoa().equals(lancamentoSalvo.getPessoa())) {
            validarPessoa(lancamento);
        }

        BeanUtils.copyProperties(lancamento, lancamentoSalvo, "id");

        return lancamentoRepository.save(lancamentoSalvo);
    }

    private void validarPessoa(Lancamento lancamento) {
        Optional<Pessoa> pessoa = null;
        if (lancamento.getPessoa().getId() != null) {
            pessoa = pessoaRepository.findById(lancamento.getPessoa().getId());
        }

        if (pessoa.isEmpty() || pessoa.get().isInativa()) {
            throw new PessoaInexistenteOuInativaException();
        }
    }

    private Lancamento buscarLancamentoExistente(Long id) {
        return lancamentoRepository.findById(id).orElseThrow(() -> new IllegalArgumentException());
    }

    public byte[] relatorioPorPessoa(LocalDate inicio, LocalDate fim) throws Exception {
        var dados = lancamentoRepository.porPessoa(inicio, fim);

        var parametros = new HashMap<String, Object>();
        parametros.put("DT_INICIO", Date.valueOf(inicio));
        parametros.put("DT_FIM", Date.valueOf(fim));
        parametros.put("REPORT_LOCALE", new Locale("pt", "BR"));

        var inputStream = this.getClass().getResourceAsStream("/relatorios/lancamentos-por-pessoa.jasper");
        var jasperPrint = JasperFillManager.fillReport(inputStream, parametros,
                new JRBeanCollectionDataSource(dados));
        return JasperExportManager.exportReportToPdf(jasperPrint);
    }

    /**
     * Agendamento de tarefa, o metodo cron() tem como parametros (" Segundo, Minuto, Hora, Dia, Mês, Dia da Semana ")
     * Logo no agendamento abaixo seria, executar esse metodo, dia 08 as 06:00 da manha de todos os meses.
     * Considerando que o vencimento é dia 05 de cada mês, o usuario será notificado 3 dias após o vencimento.
     */

    @Scheduled(cron = "0 0 6 8 * *")
    public void notificarLancamentosVencidos() {
        log.info("Preparando envio de e-mails sobre aviso de lançamentos vencidos.");

        var vencidos = lancamentoRepository.findByDataVencimentoLessThanEqualAndDataPagamentoIsNull(LocalDate.now());
        if (vencidos.isEmpty()) {
            log.info("Sem lançamentos vencidos para envio");
            return;
        }

        log.info("Existem {} lançamentos vencidos.", vencidos.size());

        var destinatarios = usuarioRepository.findByPermissoesDescricao(DESTINATARIOS);
        if (destinatarios.isEmpty()) {
            log.warn("Existem lançamentos vencidos, porém não foram encontrados destinatarios");
            return;
        }

        mailer.avisarSobreLancamentosVencidos(vencidos, destinatarios);
        log.info("Envio de e-mails concluidos!");
    }
}
