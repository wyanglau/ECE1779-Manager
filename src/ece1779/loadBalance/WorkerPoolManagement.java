package ece1779.loadBalance;

import java.util.ArrayList;
import java.util.List;

import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.ec2.model.Instance;
import com.amazonaws.services.ec2.model.Reservation;
import com.amazonaws.services.ec2.model.RunInstancesResult;

import ece1779.GlobalValues;

public class WorkerPoolManagement {
	private InstancesOperations op;
	private List<Instance> stoppedInstances;
	private List<Instance> runningInstances;
	private LoadBalancerOperation lbo;

	public WorkerPoolManagement(BasicAWSCredentials credentials) {
		op = new InstancesOperations(credentials);
		this.stoppedInstances = op.getSpecificInstances(1);
		this.runningInstances = op.getSpecificInstances(0);
		lbo = new LoadBalancerOperation(credentials,
				GlobalValues.LOADBALANCER_NAME);
	}

	/**
	 * entrance method of growing
	 * 
	 * @param ratio
	 * @return
	 * @throws AmazonServiceException
	 * @throws AmazonClientException
	 */
	public List<String> growingByRatio(int ratio)
			throws AmazonServiceException, AmazonClientException {

		List<String> ids = new ArrayList<String>();
		try {
			int runnings = this.runningInstances.size();
			if (runnings == 0) {
				runnings = 1;
			}
			int stoppeds = this.stoppedInstances.size();
			int n = runnings * ratio - runnings;
			if (stoppeds > 0) {
				ids.addAll(this.growingByNumber(n));
			}
			if (n > stoppeds) {
				RunInstancesResult result = op.runInstances(n - stoppeds);
				Reservation reservation = result.getReservation();
				for (Instance instance : reservation.getInstances()) {
					ids.add(instance.getInstanceId());
				}
			}
		} finally {
			if (!ids.isEmpty()) {
				lbo.register(ids);
			}
		}
		return ids;

	}

	public List<String> growingByNumber(int number)
			throws AmazonServiceException, AmazonClientException {

		List<String> ids = new ArrayList<String>();
		for (int i = 0; (i < number) && (i < stoppedInstances.size()); i++) {

			String id = stoppedInstances.get(i).getInstanceId();
			// we don't shut down the manager.
			if (id.equals(GlobalValues.MANAGER_INSTANCE_ID)) {
				continue;
			}
			ids.add(id);
		}

		op.startInstances(ids);

		if (stoppedInstances.size() < number) {
			op.runInstances(number - stoppedInstances.size());
		}
		return ids;

	}

	/**
	 * entrance method of shrinking
	 * 
	 * @param ratio
	 * @return
	 * @throws AmazonServiceException
	 * @throws AmazonClientException
	 */
	public List<String> shrinking(int ratio) throws AmazonServiceException,
			AmazonClientException {

		int runnings = runningInstances.size();
		if (runnings <= 1) {
			System.out.println("WorkerPoolManagement : Only " + runnings
					+ " instance is running, skip shrinking. ");
			return new ArrayList<String>();
		}

		int n = runnings / ratio;
		if (n < 1) {
			n = 1;
		}
		List<String> ids = shrinkByNumber(runnings - n);

		if (!ids.isEmpty()) {
			lbo.remove(ids);
		}
		return ids;
	}

	public List<String> shrinkByNumber(int number)
			throws AmazonServiceException, AmazonClientException {
		List<String> ids = new ArrayList<String>();
		for (int i = 0; (i < number) && (i < runningInstances.size()); i++) {
			String id = runningInstances.get(i).getInstanceId();
			// we don't shut down the manager.
			if (id.equals(GlobalValues.MANAGER_INSTANCE_ID)) {
				continue;
			}
			ids.add(id);
		}

		op.stopInstances(ids);
		return ids;
	}

	public void cloudWatching() {

	}

	public static void main(String[] args) {
		System.out.println(1 / 2);
		System.out.println(1 / 3);
		System.out.println(2 / 3);
		System.out.println(5 / 4);
	}

}
