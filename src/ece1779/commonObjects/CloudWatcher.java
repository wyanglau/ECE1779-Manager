package ece1779.commonObjects;

import java.util.ArrayList;
import java.util.List;

import com.amazonaws.services.cloudwatch.model.Datapoint;

public class CloudWatcher {

	private String instanceId;
	private String nameSpace = "AWS/EC2";
	private String statistic = "Maximum";
	private String metricName = "CPUUtilization";
	private List<Datapoint> datapoints;

	public CloudWatcher(String instanceId, List<Datapoint> datapoints) {
		if(datapoints!=null){
		this.datapoints = datapoints;}
		else{
			this.datapoints=new ArrayList<Datapoint>();
		}
		this.instanceId = instanceId;
	}

	public String getNameSpace() {
		return nameSpace;
	}

	public void setNameSpace(String nameSpace) {
		this.nameSpace = nameSpace;
	}

	public String getStatistic() {
		return statistic;
	}

	public void setStatistic(String statistic) {
		this.statistic = statistic;
	}

	public List<Datapoint> getDatapoints() {
		return datapoints;
	}

	public void setDatapoints(List<Datapoint> datapoints) {
		this.datapoints = datapoints;
	}

	public String getMetricName() {
		return metricName;
	}

	public void setMetricName(String metricName) {
		this.metricName = metricName;
	}

	public String getInstanceId() {
		return instanceId;
	}

	public void setInstanceId(String instanceId) {
		this.instanceId = instanceId;
	}

}
