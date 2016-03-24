/*import org.cloudbus.cloudsim.DatacenterBroker;

/**
 * A Broker that schedules Tasks to the VMs 
 * as per FCFS Scheduling Policy
 * @author Linda J
 *
 */
/*public class FcfsBroker extends DatacenterBroker {

	public FcfsBroker(String name) throws Exception {
		super(name);
		// TODO Auto-generated constructor stub
	}

	//scheduling function
	public void scheduleTaskstoVms(){
		int reqTasks=cloudletList.size();
		int reqVms=vmList.size();
		
		System.out.println("\n\t My Dynamic Priority Broker Schedules\n");
    	for(int i=0;i<reqTasks;i++){
    		bindCloudletToVm(i, (i%reqVms));
    		System.out.println("Task"+cloudletList.get(i).getCloudletId()+" is bound with VM"+vmList.get(i%reqVms).getId());
    	}
    	
    	System.out.println("\n");
	}
}
*/

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.LinkedList;
import java.util.List;

import org.cloudbus.cloudsim.Cloudlet;
import org.cloudbus.cloudsim.CloudletSchedulerTimeShared;
import org.cloudbus.cloudsim.Datacenter;
import org.cloudbus.cloudsim.DatacenterBroker;
import org.cloudbus.cloudsim.DatacenterCharacteristics;
import org.cloudbus.cloudsim.Host;
import org.cloudbus.cloudsim.Log;
import org.cloudbus.cloudsim.Pe;
import org.cloudbus.cloudsim.ResCloudlet;
import org.cloudbus.cloudsim.Storage;
import org.cloudbus.cloudsim.UtilizationModel;
import org.cloudbus.cloudsim.UtilizationModelFull;
import org.cloudbus.cloudsim.Vm;
import org.cloudbus.cloudsim.VmAllocationPolicySimple;
import org.cloudbus.cloudsim.VmSchedulerTimeShared;
import org.cloudbus.cloudsim.core.CloudSim;
import org.cloudbus.cloudsim.provisioners.BwProvisionerSimple;
import org.cloudbus.cloudsim.provisioners.PeProvisionerSimple;
import org.cloudbus.cloudsim.provisioners.RamProvisionerSimple;
import org.cloudbus.cloudsim.CloudletScheduler;
//import org.cloudbus.cloudsim.CloudletSchedulerTimeShared;


/**
 * A Broker that schedules Tasks to the VMs 
 * as per FCFS Scheduling Policy
 * @author Linda J
 *
 */
public class FcfsBroker extends DatacenterBroker {

	public FcfsBroker(String name) throws Exception {
		super(name);
		// TODO Auto-generated constructor stub
	}

	//scheduling function
	/*public void scheduleTaskstoVms(){
		int reqTasks=cloudletList.size();
		int reqVms=vmList.size();
		
		System.out.println("\n\tFCFS Broker Schedules\n");
    	for(int i=0;i<reqTasks;i++){
    		bindCloudletToVm(i, (i%reqVms));
    		System.out.println("Task"+cloudletList.get(i).getCloudletId()+" is bound with VM"+vmList.get(i%reqVms).getId());
    	}
    	
    	System.out.println("\n");
	}
	*/

	public void executingAHighPriorityJob(){
		int reqTasks=cloudletList.size();
		int reqVms=vmList.size();
		int currentVM=0;
		int i=1;
		int allocated[]=new int[10];
		int index=0;
		//int vmList=1;
		//int id = 0;
		//long length = 400000;
		//long fileSize = 300;
		//long outputSize = 300;
		//UtilizationModel utilizationModel = new UtilizationModelFull();
		//int pesNumber = 1; 
		//int leaseType=0;
		//Cloudlet highPriorityJob = new Cloudlet(id, length, pesNumber, fileSize, outputSize, utilizationModel, utilizationModel, utilizationModel,leaseType);
		//Cloudlet highPriorityJob=new Cloudlet();
		Cloudlet highPriorityJob=cloudletList.get(0);
		double time=2.0;
		//Cloudlet highPriorityJob=cloudletList.getById(cloudletList,1);
		//System.out.println("High priority job="+ highPriorityJob.setFinishTime(time));
		//System.out.println("High priority job="+ highPriorityJob.getCloudletId());
		//System.out.println("High priority job="+ highPriorityJob.getFinishTime());
		//System.out.println("High priority job="+ highPriorityJob.getLeaseType());
		//System.out.println("High priority job="+ highPriorityJob.getCloudletLength());
		Cloudlet suspendJob;
		System.out.println("\n\t Broker Schedules\n");
    	//for(int i=0;i<reqTasks;i++){
		for(int j=1;j<reqTasks;j++){
			//System.out.println("Inside High priority job");
			//System.out.println("job="+ cloudletList.get(j).getFinishTime());
				if(highPriorityJob.getCloudletLength() < cloudletList.get(j).getCloudletLength()){
					highPriorityJob=cloudletList.get(j);
					allocated[j]=1;
					System.out.println("High priority job");
				}
		}
		int i1=0;
		if((currentVM<reqVms)){
			bindCloudletToVm(highPriorityJob.getCloudletId(),currentVM);
			currentVM++;
			//vmList++;
			System.out.println("Task"+highPriorityJob.getCloudletId()+" is bound with VM"+currentVM);
		}
		else{
			suspendJob=selectingAJobToPreempt(highPriorityJob);
			try {
				suspendJob.setCloudletStatus(Cloudlet.PAUSED);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			int vmIId=suspendJob.getVmId();
			bindCloudletToVm(i1,vmIId);
			System.out.println("Task"+cloudletList.get(i1).getCloudletId()+" is bound with VM"+vmIId);
		}
		for(int newI=0;newI<reqTasks;newI++){
			if(allocated[newI]!=1){
				if((currentVM<reqVms)){
					bindCloudletToVm(cloudletList.get(newI).getCloudletId(),currentVM);
					currentVM++;
					//vmList++;
					System.out.println("Task"+cloudletList.get(newI).getCloudletId()+" is bound with VM"+currentVM);
				}
			}
		}
	    	
	}
		//System.out.println("Task"+cloudletList.get(i).getCloudletId()+" is bound with VM"+vmList.get(i%reqVms).getId());
	//	bindCloudletToVm(i, (i%reqVms));
    		
    	
    	//System.out.println("\n");
	

	public Cloudlet selectingAJobToPreempt(Cloudlet highPriorityJob){
		int reqTasks=cloudletList.size();
		Cloudlet[] candidateSet=new Cloudlet[10];
		int lengthOfCandidateSet=0,j=0;
		int id = 0;
		long length = 400000;
		long fileSize = 300;
		long outputSize = 300;
		UtilizationModel utilizationModel = new UtilizationModelFull();
		int pesNumber = 1; 
		int leaseType=1;
		Cloudlet suspendJob = new Cloudlet(id, length, pesNumber, fileSize, outputSize, utilizationModel, utilizationModel, utilizationModel,leaseType);
		//Cloudlet suspendJob;
		for(int i=0;i<reqTasks;i++){
			if(cloudletList.get(i).getLeaseType()==1 || cloudletList.get(i).getLeaseType()==2){
				candidateSet[j]=cloudletList.get(i);
				j++;
			}
		}
		lengthOfCandidateSet=j;
		int i=0;
		for(int k=0;k<j;k++){
			if(cloudletList.get(i).getFinishTime()< highPriorityJob.getFinishTime()){
				for(int l=k;l<j;l++){
					candidateSet[l]=candidateSet[l+1];
				}
			lengthOfCandidateSet=lengthOfCandidateSet-1;
			}
			i++;
		}
		if(lengthOfCandidateSet>1){
			suspendJob=candidateSet[0];
		}
		return suspendJob;
	}
	
	/*public boolean cloudletPause(int cloudletId) {
		boolean found = false;
		int position = 0;

		for (ResCloudlet rcl : getCloudletExecList()) {
			if (rcl.getCloudletId() == cloudletId) {
				found = true;
				break;
			}
			position++;
		}

		if (found) {
			// remove cloudlet from the exec list and put it in the paused list
			ResCloudlet rcl = getCloudletExecList().remove(position);
			if (rcl.getRemainingCloudletLength() == 0) {
				cloudletFinish(rcl);
			} else {
				rcl.setCloudletStatus(Cloudlet.PAUSED);
				getCloudletPausedList().add(rcl);
			}
			return true;
		}
		return false;
	}
	*/
}
