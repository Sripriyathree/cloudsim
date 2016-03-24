import java.util.ArrayList;
import org.cloudbus.cloudsim.Cloudlet;
import org.cloudbus.cloudsim.UtilizationModel;
import org.cloudbus.cloudsim.UtilizationModelFull;

/**
 * CloudletCreator Creates Cloudlets as per the User Requirements.
 * @author Linda J
 *
 */
public class CloudletCreator {
	
	
	//cloudlet creator
	public ArrayList<Cloudlet> createUserCloudlet(int reqTasks,int brokerId){
		ArrayList<Cloudlet> cloudletList = new ArrayList<Cloudlet>();
		
    	//Cloudlet properties
    	int id = 0;
    	int pesNumber=1;
    	long length = 1000;
    	long fileSize = 300;
    	long outputSize = 300;
    	int leaseType1=1;
    	//int leaseType2=2;
    	UtilizationModel utilizationModel = new UtilizationModelFull();
    	   	
    	//int lease=0;
    	for(id=0;id<reqTasks;id++){
    		//if((id%2)==0){
    		
    		Cloudlet task = new Cloudlet(id, (length*(id+1)), pesNumber, fileSize, outputSize, utilizationModel, utilizationModel, utilizationModel);
    		task.setUserId(brokerId);
    		System.out.println("Task"+id+"="+(task.getCloudletLength()));
    		cloudletList.add(task);
    		
    		//}
    		
    		//if((id%2)==1)
    		//else{
        		
        		//Cloudlet task = new Cloudlet(id, (length*(id+1)), pesNumber, fileSize, outputSize, utilizationModel, utilizationModel, utilizationModel,2);
        		//task.setUserId(brokerId);
        		//System.out.println("Task"+id+"="+(task.getCloudletLength()));
        		//cloudletList.add(task);
        		
        		//}
    		
    		//add the cloudlets to the list
        	
    	}

    	System.out.println("SUCCESSFULLY Cloudletlist created :)");

		return cloudletList;
		
	}

}
