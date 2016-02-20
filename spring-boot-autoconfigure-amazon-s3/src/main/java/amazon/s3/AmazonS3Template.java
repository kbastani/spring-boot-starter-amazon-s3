package amazon.s3;

import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.auth.BasicSessionCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.PutObjectResult;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.securitytoken.AWSSecurityTokenServiceClient;
import com.amazonaws.services.securitytoken.model.Credentials;
import com.amazonaws.services.securitytoken.model.GetSessionTokenRequest;
import org.springframework.stereotype.Component;

import java.io.File;
import java.util.Date;

/**
 * This class is a client for interacting with Amazon S3 bucket resources.
 *
 * @author kbastani
 */
@Component
public class AmazonS3Template {

    private String defaultBucket;
    private String accessKeyId;
    private String accessKeySecret;
    private Credentials sessionCredentials;

    /**
     * Create a new instance of the {@link AmazonS3Template} with the bucket name and access credentials
     *
     * @param defaultBucket   is the name of a default bucket from the Amazon S3 provider
     * @param accessKeyId     is the access key id credential for the specified bucket name
     * @param accessKeySecret is the access key secret for the specified bucket name
     */
    public AmazonS3Template(String defaultBucket, String accessKeyId, String accessKeySecret) {
        this.defaultBucket = defaultBucket;
        this.accessKeyId = accessKeyId;
        this.accessKeySecret = accessKeySecret;
    }

    /**
     * Save a file using authenticated session credentials
     *
     * @param key  is the name of the file to save in the bucket
     * @param file is the file that will be saved
     * @return an instance of {@link PutObjectResult} containing the result of the save operation
     */
    public PutObjectResult save(String key, File file) {
        return getAmazonS3Client().putObject(new PutObjectRequest(defaultBucket, key, file));
    }

    /**
     * Get a file using the authenticated session credentials
     *
     * @param key is the key of the file in the bucket that should be retrieved
     * @return an instance of {@link S3Object} containing the file from S3
     */
    public S3Object get(String key) {
        return getAmazonS3Client().getObject(defaultBucket, key);
    }

    /**
     * Gets an Amazon S3 client from basic session credentials
     *
     * @return an authenticated Amazon S3 client
     */
    public AmazonS3 getAmazonS3Client() {
        BasicSessionCredentials basicSessionCredentials = getBasicSessionCredentials();

        // Create a new S3 client using the basic session credentials of the service instance
        return new AmazonS3Client(basicSessionCredentials);
    }

    /**
     * Get the basic session credentials for the template's configured IAM authentication keys
     *
     * @return a {@link BasicSessionCredentials} instance with a valid authenticated session token
     */
    private BasicSessionCredentials getBasicSessionCredentials() {

        // Create a new session token if the session is expired or not initialized
        if (sessionCredentials == null || sessionCredentials.getExpiration().before(new Date()))
            sessionCredentials = getSessionCredentials();

        // Create basic session credentials using the generated session token
        return new BasicSessionCredentials(sessionCredentials.getAccessKeyId(),
                sessionCredentials.getSecretAccessKey(),
                sessionCredentials.getSessionToken());
    }

    /**
     * Creates a new session credential that is valid for 12 hours
     *
     * @return an authenticated {@link Credentials} for the new session token
     */
    private Credentials getSessionCredentials() {
        // Create a new session with the user credentials for the service instance
        AWSSecurityTokenServiceClient stsClient =
                new AWSSecurityTokenServiceClient(new BasicAWSCredentials(accessKeyId, accessKeySecret));

        // Start a new session for managing a service instance's bucket
        GetSessionTokenRequest getSessionTokenRequest =
                new GetSessionTokenRequest().withDurationSeconds(43200);

        // Get the session token for the service instance's bucket
        sessionCredentials = stsClient.getSessionToken(getSessionTokenRequest).getCredentials();

        return sessionCredentials;
    }
}
