package amazon.s3;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * This class auto-configures a {@link AmazonS3Template} bean.
 *
 * @author kbastani
 */
@Configuration
@ConditionalOnMissingBean(AmazonS3Template.class)
@EnableConfigurationProperties(AmazonProperties.class)
public class S3AutoConfiguration {

    @Autowired
    private AmazonProperties amazonProperties;

    @Bean
    AmazonS3Template amazonS3Template() {
        return new AmazonS3Template(amazonProperties.getS3().getDefaultBucket(),
                amazonProperties.getAws().getAccessKeyId(),
                amazonProperties.getAws().getAccessKeySecret());
    }
}
