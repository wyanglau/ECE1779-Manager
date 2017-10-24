package ece1779.DAO;

import java.util.List;

import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.ObjectListing;
import com.amazonaws.services.s3.model.S3ObjectSummary;

import ece1779.GlobalValues;

public class MngrS3Operations {

	private AmazonS3 s3;

	public MngrS3Operations(BasicAWSCredentials awsCredentials) {
		this.s3 = new AmazonS3Client(awsCredentials);

	}

	public void deleteAllS3() throws AmazonServiceException,
			AmazonClientException {
		String bucketName = GlobalValues.BUCKET_NAME;
		ObjectListing objects = this.s3.listObjects(bucketName);

		List<S3ObjectSummary> summaries = objects.getObjectSummaries();
		for (S3ObjectSummary item : summaries) {
			String key = item.getKey();
			this.s3.deleteObject(bucketName, key);
			System.out.println("Delet obj key = " + key);
		}

		while (objects.isTruncated()) {
			objects = this.s3.listNextBatchOfObjects(objects);
			summaries = objects.getObjectSummaries();
			for (S3ObjectSummary item : summaries) {
				String key = item.getKey();
				this.s3.deleteObject(bucketName, key);
			}
		}

	}

	public static void main(String[] args) {

		String accessKey = "";
		String secretKey = "";
		BasicAWSCredentials awsCredentials = new BasicAWSCredentials(accessKey,
				secretKey);
		MngrS3Operations s3 = new MngrS3Operations(awsCredentials);

		// Images image = new Images(0, 1, null);
		// List<File> files = new ArrayList<File>();
		// save
		// for (int i = 0; i < 4; i++) {
		// File file = new File("Sources/doraemon.jpg");
		//
		// files.add(file);
		// }
		// try {
		// s3.save(files, awsCredentials, image);
		// } catch (IOException e) {
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		// }

		// //delete
		s3.deleteAllS3();
		System.out.println("Finished");
	}

}
