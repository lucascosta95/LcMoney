package br.com.lucascosta.lcmoneyapi.storage;

import br.com.lucascosta.lcmoneyapi.config.property.LcmoneyApiProperty;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.AccessControlList;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.amazonaws.services.s3.model.GroupGrantee;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.ObjectTagging;
import com.amazonaws.services.s3.model.Permission;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.SetObjectTaggingRequest;
import com.amazonaws.services.s3.model.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Collections;
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

    public String configurarUrl(String objeto) {
        return String.format("http://%s.s3.amazonaws.com/%s", property.getS3().getBucket(), objeto);
    }

    public void salvar(String objeto) {
        SetObjectTaggingRequest setObjectTaggingRequest = new SetObjectTaggingRequest(property.getS3().getBucket(), objeto, new ObjectTagging(Collections.emptyList()));
        amazonS3.setObjectTagging(setObjectTaggingRequest);
    }

    public void remover(String objeto) {
        DeleteObjectRequest deleteObjectRequest = new DeleteObjectRequest(property.getS3().getBucket(), objeto);
        amazonS3.deleteObject(deleteObjectRequest);
    }

    public void substituir(String objetoAntigo, String objetoNovo) {

        if (StringUtils.hasText(objetoAntigo)) {
            this.remover(objetoAntigo);
        }

        this.salvar(objetoNovo);
    }
}
