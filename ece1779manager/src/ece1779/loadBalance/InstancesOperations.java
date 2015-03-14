package ece1779.loadBalance;

import java.util.ArrayList;
import java.util.List;

import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.ec2.AmazonEC2;
import com.amazonaws.services.ec2.AmazonEC2Client;
import com.amazonaws.services.ec2.model.CreateTagsRequest;
import com.amazonaws.services.ec2.model.DescribeInstancesResult;
import com.amazonaws.services.ec2.model.Instance;
import com.amazonaws.services.ec2.model.InstanceState;
import com.amazonaws.services.ec2.model.Reservation;
import com.amazonaws.services.ec2.model.RunInstancesRequest;
import com.amazonaws.services.ec2.model.RunInstancesResult;
import com.amazonaws.services.ec2.model.StartInstancesRequest;
import com.amazonaws.services.ec2.model.StartInstancesResult;
import com.amazonaws.services.ec2.model.StopInstancesRequest;
import com.amazonaws.services.ec2.model.StopInstancesResult;
import com.amazonaws.services.ec2.model.Tag;
import com.amazonaws.services.ec2.model.TerminateInstancesRequest;
import com.amazonaws.services.ec2.model.TerminateInstancesResult;

import ece1779.GlobalValues;

public class InstancesOperations {

	private AmazonEC2 ec2;

	public InstancesOperations(AWSCredentials credentials) {
		ec2 = new AmazonEC2Client(credentials);
	}

	public InstancesOperations(AmazonEC2Client ec2) {
		this.ec2 = ec2;
	}

	/**
	 * To run quantity of new instances
	 * 
	 * @param quantity
	 */
	public RunInstancesResult runInstances(int quantity)
			throws AmazonServiceException, AmazonClientException {

		String ami = GlobalValues.AMI_ID;

		RunInstancesRequest request = new RunInstancesRequest(ami, quantity,
				quantity);
		request.setKeyName("ECE1779-GROUP14");
		request.withSecurityGroups("ECE1779-GROUP14");
		RunInstancesResult result = ec2.runInstances(request);

		// set tag
		List<String> ids = new ArrayList<String>();
		Tag tag = new Tag("Name", "ECE1779-GROUP14");
		Reservation reservation = result.getReservation();
		for (Instance instance : reservation.getInstances()) {
			ids.add(instance.getInstanceId());
		}
		addTag(tag, ids);
		System.out.println("[InstancesOperations] Running new instances :"
				+ ids.toString());
		return result;

	}

	/**
	 * Create or Override tag for resources of input ids
	 * 
	 * @param tag
	 * @param id
	 */
	private void addTag(Tag tag, List<String> ids)
			throws AmazonServiceException, AmazonClientException {

		CreateTagsRequest request = new CreateTagsRequest();
		request.setResources(ids);
		request.withTags(tag);
		ec2.createTags(request);

	}

	public TerminateInstancesResult terminateInstances(List<String> ids)
			throws AmazonServiceException, AmazonClientException {
		TerminateInstancesRequest request = new TerminateInstancesRequest(ids);
		return ec2.terminateInstances(request);
	}

	public StopInstancesResult stopInstances(List<String> ids)
			throws AmazonServiceException, AmazonClientException {
		StopInstancesRequest request = new StopInstancesRequest(ids);
		System.out.println("[InstancesOperations] Stopping  instances :"
				+ ids.toString());
		return ec2.stopInstances(request);
	}

	public StartInstancesResult startInstances(List<String> ids)
			throws AmazonServiceException, AmazonClientException {
		StartInstancesRequest request = new StartInstancesRequest(ids);
		System.out.println("[InstancesOperations] Starting  instances :"
				+ ids.toString());
		return ec2.startInstances(request);
	}

	public List<Instance> getAllEC2instances() throws AmazonServiceException,
			AmazonClientException {

		DescribeInstancesResult result = ec2.describeInstances();
		List<Reservation> reservations = result.getReservations();
		List<Instance> instances = new ArrayList<Instance>();
		for (Reservation r : reservations) {
			instances.addAll(r.getInstances());
		}

		return instances;

	}

	/**
	 * 0 : running instances <br>
	 * 1 : otherwise
	 * 
	 * @param type
	 * @return
	 * @throws AmazonServiceException
	 * @throws AmazonClientException
	 */
	// to do : there's a bug here, when the instances are under terminated/
	// stopping / shutting down, they won't be start up
	public List<Instance> getSpecificInstances(int type)
			throws AmazonServiceException, AmazonClientException {
		List<Instance> allInstances = this.getAllEC2instances();
		List<Instance> result = new ArrayList<Instance>();
		for (Instance i : allInstances) {
			if (i.getInstanceId().equals(GlobalValues.MANAGER_INSTANCE_ID)) {
				System.out
						.println("[InstancesOperation] Manager Instance, skip.");
				continue;
			}
			InstanceState state = i.getState();
			System.out.println("[InstancesOperation]Getting new state :"
					+ state.getCode() + ", ID: " + i.getInstanceId());
			if (type == 0
					&& (state.getCode().equals(0) || state.getCode().equals(16))) {
				result.add(i);
			} else if (type == 1
					&& (state.getCode().equals(64) || state.getCode()
							.equals(80))) {
				result.add(i);
			}
		}
		return result;

	}

	public static void main(String[] args) {
		String accessKey = "=";
		String secretKey = "+R+iPFsZHajU7+";
		BasicAWSCredentials awsCredentials = new BasicAWSCredentials(accessKey,
				secretKey);
		InstancesOperations op = new InstancesOperations(awsCredentials);

		// run
		// op.runInstances(2);

		List<String> ids = new ArrayList<String>();
		// ids.add("i-b87ac942");
		// op.stopInstances(ids);

		// // get runnning instances
		// for (Instance instance : op.getSpecificInstances(0)) {
		// ids.add(instance.getInstanceId());
		// }
		// op.stopInstances(ids);

		System.out.println("Done");

		//
		// List<Instance> instances = op.getAllEC2instances();
		// for (Instance instance : instances) {
		// ids.add(instance.getInstanceId());
		// }
		// System.out.println(ids.toString());

		for (Instance instance : op.getSpecificInstances(1)) {
			ids.add(instance.getInstanceId());
		}
		System.out.println(ids.toString());

	}
}
