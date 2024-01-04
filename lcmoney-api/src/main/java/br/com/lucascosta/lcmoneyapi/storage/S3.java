package br.com.lucascosta.lcmoneyapi.storage;

import br.com.lucascosta.lcmoneyapi.config.property.LcmoneyApiProperty;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

@Slf4j
@Component
public class S3 {

    @Autowired
    private AmazonS3 amazonS3;

    @Autowired
    private LcmoneyApiProperty property;

    public String salvarTemporariamente(MultipartFile arquivo) {
        var acl = new AccessControlList();
        acl.grantPermission(GroupGrantee.AllUsers, Permission.Read);

        var objectMetaData = new ObjectMetadata();
        objectMetaData.setContentType(arquivo.getContentType());
        objectMetaData.setContentLength(arquivo.getSize());

        var nomeUnico = gerarNomeUnico(arquivo.getOriginalFilename());
        try {
            var putObjectRequest = new PutObjectRequest(property.getS3().getBucket(), nomeUnico, arquivo.getInputStream(), objectMetaData).withAccessControlList(acl);
            putObjectRequest.setTagging(new ObjectTagging(List.of(new Tag("expirar", "true"))));
            amazonS3.putObject(putObjectRequest);

            log.info("Arquivo {} enviado ao S3", arquivo.getOriginalFilename());
            return nomeUnico;

        } catch (IOException e) {
            throw new RuntimeException("Inconsistencia ao tentar enviar arquivo ao S3", e);
        }
    }

    private String gerarNomeUnico(String originalFilename) {
        return String.format("%s_%s", UUID.randomUUID().toString(), originalFilename);
    }
}
