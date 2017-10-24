package ece1779.loadBalance;

import java.util.List;
import java.util.TimerTask;

import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.cloudwatch.model.Datapoint;

import ece1779.commonObjects.CloudWatcher;

public class AutoScaling extends TimerTask {

	private BasicAWSCredentials awsCredentials;
	private double expandThreshold;
	private double shrinkThreshold;
	private int growRatio;
	private int shrinkRatio;
	private WorkerPoolManagement wm;
	private CloudWatching cw;

	public AutoScaling(BasicAWSCredentials awsCredentials,
			double expandThreshold, double shrinkThreshold, int growRatio,
			int shrinkRatio) {
		this.awsCredentials = awsCredentials;
		this.expandThreshold = expandThreshold;
		this.shrinkThreshold = shrinkThreshold;
		this.growRatio = growRatio;
		this.shrinkRatio = shrinkRatio;

		wm = new WorkerPoolManagement(this.awsCredentials);
		cw = new CloudWatching(this.awsCredentials);

	}

	/**
	 * Find the average of maximum utilization.
	 */
	@Override
	public void run() {
		try {
			Double avg = getAverageOfMaximum();
			System.out.println("[AutoScaling] Current average utilization:"
					+ avg + " || expand threshold:" + expandThreshold
					+ " || shrinkThreshold:" + shrinkThreshold
					+ "|| shrinkRatio:" + shrinkRatio + "|| growRatio:"
					+ growRatio);
			if (avg >= expandThreshold) {
				// run extra instances
				this.wm.growingByRatio(growRatio);

			}

			else if (avg <= shrinkThreshold) {
				this.wm.shrinking(shrinkRatio);
			}
		} catch (Exception e) {
			System.out
					.println("[Timer Task Exception] It is maybe aws access problem, will do it next time.");
			e.printStackTrace();
		}

	}

	private double getAverageOfMaximum() throws AmazonServiceException,
			AmazonClientException {
		List<CloudWatcher> watchers = this.cw.getCPUUtilization();
		int numberOfWatchers = watchers.size();
		double avgUtilization = 0.0;
		for (CloudWatcher watcher : watchers) {
			double sum = 0.0;

			List<Datapoint> datapoints = watcher.getDatapoints();
			if (datapoints.isEmpty()) {
				continue;
			}
			for (Datapoint dp : datapoints) {
				sum += dp.getMaximum();
			}
			avgUtilization = sum / datapoints.size();
		}

		return avgUtilization / numberOfWatchers;

	}

}
