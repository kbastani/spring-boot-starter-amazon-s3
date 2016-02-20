package amazon.s3;

import org.junit.After;
import org.junit.Test;
import org.springframework.boot.test.EnvironmentTestUtils;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Configuration;

import static junit.framework.TestCase.assertNotNull;

public class S3AutoConfigurationTest {

    private AnnotationConfigApplicationContext context;

    @After
    public void tearDown() {
        if (this.context != null) {
            this.context.close();
        }
    }

    @Test
    public void defaultAmazonS3Template() {
        load(EmptyConfiguration.class, "amazon.s3.default-bucket=test",
                "amazon.aws.access-key-id=AJGLDLSXKDFLS",
                "amazon.aws.access-key-secret=XSDFSDFLKKHASDFJALASDF");

        AmazonS3Template amazonS3Template = this.context.getBean(AmazonS3Template.class);
        assertNotNull(amazonS3Template);
    }

    @Configuration
    static class EmptyConfiguration {
    }

    private void load(Class<?> config, String... environment) {
        AnnotationConfigApplicationContext applicationContext = new AnnotationConfigApplicationContext();
        EnvironmentTestUtils.addEnvironment(applicationContext, environment);
        applicationContext.register(config);
        applicationContext.register(S3AutoConfiguration.class);
        applicationContext.refresh();
        this.context = applicationContext;
    }
}
