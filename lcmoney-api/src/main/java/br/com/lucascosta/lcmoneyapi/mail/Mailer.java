package br.com.lucascosta.lcmoneyapi.mail;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import br.com.lucascosta.lcmoneyapi.model.Lancamento;
import br.com.lucascosta.lcmoneyapi.model.Usuario;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class Mailer {

    @Autowired
    private JavaMailSender javaMailSender;

    @Autowired
    private TemplateEngine thymeleaf;

    public void avisarSobreLancamentosVencidos(List<Lancamento> vencidos, List<Usuario> destinatarios) {
        Map<String, Object> variaveis = new HashMap<>();

        variaveis.put("lancamentos", vencidos);
        List<String> emails = destinatarios.stream().map(Usuario::getEmail).collect(Collectors.toList());

        this.enviarEmail("email-remetente@seu-provedor.com.br", emails, "Lançamentos Vencidos", "mail/aviso-lancamentos-vencidos", variaveis);
    }

    public void enviarEmail(String remetente, List<String> destinatarios, String assunto, String template, Map<String, Object> variaveis) {
        Context context = new Context(new Locale("pt", "BR"));
        variaveis.forEach((key, value) -> context.setVariable(key, value));

        String mensagem = thymeleaf.process(template, context);
        enviarEmail(remetente, destinatarios, assunto, mensagem);
    }

    public void enviarEmail(String remetente, List<String> destinatarios, String assunto, String mensagem) {
        try {

            MimeMessage mimeMessage = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, "UTF-8");

            helper.setFrom(remetente);
            helper.setTo(destinatarios.toArray(new String[destinatarios.size()]));
            helper.setSubject(assunto);
            helper.setText(mensagem, true);

            javaMailSender.send(mimeMessage);

        } catch (MessagingException e) {
            throw new RuntimeException("Problemas com o envio de e-mail!", e);
        }
    }
}