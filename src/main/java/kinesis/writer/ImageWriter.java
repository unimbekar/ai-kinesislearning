/*
 * Copyright 2015 Amazon.com, Inc. or its affiliates. All Rights Reserved.
 *
 * Licensed under the Amazon Software License (the "License").
 * You may not use this file except in compliance with the License.
 * A copy of the License is located at
 *
 * http://aws.amazon.com/asl/
 *
 * or in the "license" file accompanying this file. This file is distributed
 * on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either
 * express or implied. See the License for the specific language governing
 * permissions and limitations under the License.
 */

package kinesis.writer;

import com.amazonaws.AmazonClientException;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.RegionUtils;
import com.amazonaws.services.kinesis.AmazonKinesis;
import com.amazonaws.services.kinesis.AmazonKinesisClientBuilder;
import com.amazonaws.services.kinesis.model.DescribeStreamResult;
import com.amazonaws.services.kinesis.model.PutRecordRequest;
import com.amazonaws.services.kinesis.model.Record;
import com.amazonaws.services.kinesis.model.ResourceNotFoundException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import kinesis.utils.ConfigurationUtils;
import kinesis.utils.CredentialUtils;
import kinesis.utils.ImageUtils;

import java.awt.image.BufferedImage;
import java.nio.ByteBuffer;
import java.util.Date;

/**
 * Continuously sends simulated stock trades to Kinesis
 */
public class ImageWriter {

    private static final Log LOG = LogFactory.getLog(ImageWriter.class);

    private static void checkUsage(String[] args) {
        if (args.length != 2) {
            System.err.println("Usage: " + ImageWriter.class.getSimpleName()
                    + " <stream name> <region>");
            System.exit(1);
        }
    }

    /**
     * Checks if the stream exists and is active
     *
     * @param kinesisClient Amazon Kinesis client instance
     * @param streamName    Name of stream
     */
    private static void validateStream(AmazonKinesis kinesisClient, String streamName) {
        try {
            DescribeStreamResult result = kinesisClient.describeStream(streamName);
            if (!"ACTIVE".equals(result.getStreamDescription().getStreamStatus())) {
                System.err.println("Stream " + streamName + " is not active. Please wait a few moments and try again.");
                System.exit(1);
            }
        } catch (ResourceNotFoundException e) {
            System.err.println("Stream " + streamName + " does not exist. Please create it in the console.");
            System.err.println(e);
            System.exit(1);
        } catch (Exception e) {
            System.err.println("Error found while describing the stream " + streamName);
            System.err.println(e);
            System.exit(1);
        }
    }

    /**
     * Uses the Kinesis client to send the stock trade to the given stream.
     *
     * @param bufferedImage instance representing Buffered Image
     * @param kinesisClient Amazon Kinesis client
     * @param streamName    Name of stream
     */
    private static void sendImage(BufferedImage bufferedImage, AmazonKinesis kinesisClient,
                                  String streamName) {
        byte[] bytes = ImageUtils.getImageAsByteArr(bufferedImage);
        // The bytes could be null if there is an issue with the JSON serialization by the Jackson JSON library.
        if (bytes == null) {
            LOG.warn("Could NOT Send the Image .. Found NULL");
            return;
        }

        LOG.info("Putting Image (width) --> " + bufferedImage.getWidth() + ", Length: " + bytes.length);
        PutRecordRequest putRecord = new PutRecordRequest();
        putRecord.setStreamName(streamName);

//        Record record = new Record();
//        record.setApproximateArrivalTimestamp(new Date());
//        record.setData(ByteBuffer.wrap(bytes));
//        record.setSequenceNumber();
        // TBD: Use a different Partition Key
        putRecord.setPartitionKey("partitionkey");
        putRecord.setData(ByteBuffer.wrap(bytes));

        try {
            kinesisClient.putRecord(putRecord);
        } catch (AmazonClientException ex) {
            LOG.warn("Error sending record to Amazon Kinesis.", ex);
        }
    }

    public static void main(String[] args) throws Exception {
        String streamName = System.getProperty("stream-name", "FrameStream");

        String regionName = System.getProperty("region", "us-east-1");
        Region region = RegionUtils.getRegion(regionName);
        if (region == null) {
            System.err.println(regionName + " is not a valid AWS region.");
            System.exit(1);
        }

        AmazonKinesisClientBuilder clientBuilder = AmazonKinesisClientBuilder.standard();

        clientBuilder.setRegion(regionName);
        clientBuilder.setCredentials(CredentialUtils.getCredentialsProvider());
        clientBuilder.setClientConfiguration(ConfigurationUtils.getClientConfigWithUserAgent());

        AmazonKinesis kinesisClient = clientBuilder.build();

        // Validate that the stream exists and is active
        validateStream(kinesisClient, streamName);

        // Repeatedly send stock trades with a 100 milliseconds wait in between
        ImageGenerator imageGenerator = new ImageGenerator();
        while (true) {
            BufferedImage image = imageGenerator.getRandomImage();
            sendImage(image, kinesisClient, streamName);
            Thread.sleep(100);
        }
    }

}
