package br.com.lucascosta.lcmoneyapi.config;

import br.com.lucascosta.lcmoneyapi.config.property.LcmoneyApiProperty;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.BucketLifecycleConfiguration;
import com.amazonaws.services.s3.model.CreateBucketRequest;
import com.amazonaws.services.s3.model.Tag;
import com.amazonaws.services.s3.model.lifecycle.LifecycleFilter;
import com.amazonaws.services.s3.model.lifecycle.LifecycleTagPredicate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class S3Config {

    @Autowired
    private LcmoneyApiProperty property;

    @Bean
    public AmazonS3 amazonS3() {
        var credentials = new BasicAWSCredentials(property.getS3().getAccessKeyId(), property.getS3().getSecretAccessKey());
        var amazonS3 = AmazonS3ClientBuilder
                .standard()
                .withCredentials(new AWSStaticCredentialsProvider(credentials))
                .withRegion(Regions.US_EAST_1)
                .build();

        if (!amazonS3.doesBucketExistV2(property.getS3().getBucket())) {
            amazonS3.createBucket(new CreateBucketRequest(property.getS3().getBucket()));

            var configuration = new BucketLifecycleConfiguration().withRules(
                    new BucketLifecycleConfiguration.Rule()
                            .withId("Regra de expiração de arquivos temporarios")
                            .withFilter(new LifecycleFilter(new LifecycleTagPredicate(new Tag("expirar", "true"))))
                            .withExpirationInDays(1)
                            .withStatus(BucketLifecycleConfiguration.ENABLED)
            );

            amazonS3.setBucketLifecycleConfiguration(property.getS3().getBucket(), configuration);
        }

        return amazonS3;
    }
}
