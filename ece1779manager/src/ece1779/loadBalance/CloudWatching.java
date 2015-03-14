package ece1779.loadBalance;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Vector;

import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.cloudwatch.AmazonCloudWatch;
import com.amazonaws.services.cloudwatch.AmazonCloudWatchClient;
import com.amazonaws.services.cloudwatch.model.Datapoint;
import com.amazonaws.services.cloudwatch.model.Dimension;
import com.amazonaws.services.cloudwatch.model.GetMetricStatisticsRequest;
import com.amazonaws.services.cloudwatch.model.GetMetricStatisticsResult;
import com.amazonaws.services.cloudwatch.model.ListMetricsRequest;
import com.amazonaws.services.cloudwatch.model.ListMetricsResult;
import com.amazonaws.services.cloudwatch.model.Metric;
import com.amazonaws.services.ec2.model.Instance;

import ece1779.GlobalValues;
import ece1779.commonObjects.CloudWatcher;

public class CloudWatching {

	private AmazonCloudWatch cw;
	private BasicAWSCredentials awsCredentials;
	private InstancesOperations op;
	private List<String> runningInstanceIds;

	public CloudWatching(BasicAWSCredentials awsCredentials) {
		this.awsCredentials = awsCredentials;
		cw = new AmazonCloudWatchClient(awsCredentials);
		op = new InstancesOperations(this.awsCredentials);
	}

	public List<CloudWatcher> getCPUUtilization()
			throws AmazonServiceException, AmazonClientException {
		this.runningInstanceIds = getRunningInstanceIDs();

		List<CloudWatcher> statistics = new ArrayList<CloudWatcher>();

		ListMetricsRequest listMetricsRequest = new ListMetricsRequest();
		listMetricsRequest.setMetricName("CPUUtilization");
		listMetricsRequest.setNamespace("AWS/EC2");
		ListMetricsResult result = cw.listMetrics(listMetricsRequest);
		List<Metric> metrics = result.getMetrics();
		for (Metric metric : metrics) {
			CloudWatcher cloudWatcher = getStatistics(metric, cw);
			if (cloudWatcher != null) {
				statistics.add(cloudWatcher);
			}

		}
		/**
		 * to show the empty running instance
		 */
		for (String id : runningInstanceIds) {
			statistics.add(new CloudWatcher(id, null));
		}
		return statistics;
	}

	private CloudWatcher getStatistics(Metric metric, AmazonCloudWatch cw)
			throws AmazonServiceException, AmazonClientException {

		String namespace = metric.getNamespace();
		String metricName = metric.getMetricName();
		List<Dimension> dimensions = metric.getDimensions();
		GetMetricStatisticsRequest statisticsRequest = new GetMetricStatisticsRequest();
		statisticsRequest.setNamespace(namespace);
		statisticsRequest.setMetricName(metricName);
		statisticsRequest.setDimensions(dimensions);
		Date endTime = new Date();
		Date startTime = new Date();
		startTime.setTime(endTime.getTime() - 1200000);
		statisticsRequest.setStartTime(startTime);
		statisticsRequest.setEndTime(endTime);
		statisticsRequest.setPeriod(60); // it doesn't matter what is the
											// number here..
		Vector<String> statistics = new Vector<String>();
		statistics.add("Maximum");
		statisticsRequest.setStatistics(statistics);
		GetMetricStatisticsResult stats = cw
				.getMetricStatistics(statisticsRequest);
		// System.out.println("[CloudWatching] : Namespace = " + namespace
		// + " Metric = " + metricName + " Dimensions = " + dimensions);
		// System.out.println("[CloudWatching] Values = " + stats.toString());

		return parseStatistics(dimensions, stats);
	}

	private CloudWatcher parseStatistics(List<Dimension> dimensions,
			GetMetricStatisticsResult stats) throws AmazonServiceException,
			AmazonClientException {
		List<Datapoint> datapoints = stats.getDatapoints();
		String currentId = dimensions.get(0).getValue();
		if (!this.runningInstanceIds.contains(currentId)) {
			return null;
		} else if (currentId.equals(GlobalValues.MANAGER_INSTANCE_ID)) {
			return null;
		} else {
			runningInstanceIds.remove(currentId);
			return new CloudWatcher(dimensions.get(0).getValue(), datapoints);
		}
	}

	public List<Instance> getAllEC2instances() throws AmazonServiceException,
			AmazonClientException {

		return op.getAllEC2instances();
	}

	private List<String> getRunningInstanceIDs() throws AmazonServiceException,
			AmazonClientException {

		List<String> ids = new ArrayList<String>();
		for (Instance i : op.getSpecificInstances(0)) {
			ids.add(i.getInstanceId());
		}
		return ids;
	}

	public static void main(String[] args) {
		String accessKey = "AKIAJJJT4OW55CJN3NXA";
		String secretKey = "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";
		BasicAWSCredentials awsCredentials = new BasicAWSCredentials(accessKey,
				secretKey);

		CloudWatching cloud = new CloudWatching(awsCredentials);

		System.out
				.println("running numbers : " + cloud.getRunningInstanceIDs());
		List<CloudWatcher> result = cloud.getCPUUtilization();

		for (CloudWatcher w : result) {
			System.out.println("instance id :" + w.getInstanceId());
			System.out.println("namespace " + w.getNameSpace() + "||"
					+ "statistic" + w.getStatistic());
			System.out.println("datapoints:" + w.getDatapoints().toString());
		}
	}
}
