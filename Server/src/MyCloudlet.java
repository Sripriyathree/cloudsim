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

public class MyCloudlet extends Cloudlet {
	int leaseType;
	public MyCloudlet(final int cloudletId,
			final long cloudletLength,
			final int pesNumber,
			final long cloudletFileSize,
			final long cloudletOutputSize,
			final UtilizationModel utilizationModelCpu,
			final UtilizationModel utilizationModelRam,
			final UtilizationModel utilizationModelBw,int leaseType){
		super(cloudletId,
			cloudletLength,
			pesNumber,
			cloudletFileSize,
			cloudletOutputSize,
			utilizationModelCpu,
			utilizationModelRam,
			utilizationModelBw);
		this.leaseType=leaseType;
		vmId = -1;
		accumulatedBwCost = 0.0;
		costPerBw = 0.0;

		//requiredFiles = new LinkedList<String>();
	}
	public int getLeaseType(){
		return this.leaseType;
	}
	
	public boolean setReservationId(final int resId) {
		return super.setReservationId(resId);
	}
	
	public int getReservationId() {
		return super.getReservationId();
	}
	
	public boolean hasReserved(){
		return super.hasReserved();
	}
	
	public boolean setCloudletLength(final long cloudletLength){
		return super.setCloudletLength(cloudletLength);
	}
	
	public boolean setNetServiceLevel(final int netServiceLevel){
		return super.setNetServiceLevel(netServiceLevel);
	}
	
	public int getNetServiceLevel(){
		return super.getNetServiceLevel();
	}
	
	public double getWaitingTime(){
		return super.getWaitingTime();
	}
	
	public boolean setClassType(final int classType){
		return super.setClassType(classType);
	}
	
	public int getClassType() {
		return super.getClassType();
	}
	
	public boolean setNumberOfPes(final int numberOfPes){
		return super.setNumberOfPes(numberOfPes);
	}
	
	public int getNumberOfPes(){
		return super.getNumberOfPes();
	}
	
	public String getCloudletHistory() {
		return super.getCloudletHistory();
	}
	
	public long getCloudletFinishedSoFar(){
		return super.getCloudletFinishedSoFar();
	}
	
	public boolean isFinished(){
		return super.isFinished();
	}
	
	public void setCloudletFinishedSoFar(final long length) {
		super.setCloudletFinishedSoFar(length);
	}
	
	public void setUserId(final int id){
		super.setUserId(id);
	}
	
	public int getUserId(){
		return super.getUserId();
	}
	
	public int getResourceId(){
		return super.getResourceId();
	}
	
	public long getCloudletFileSize(){
		return super.getCloudletFileSize();
	}
	
	public long getCloudletOutputSize(){
		return super.getCloudletOutputSize();
	}
	
	public void setResourceParameter(final int resourceID, final double cost){
		super.setResourceParameter(resourceID,cost);
	}
	
	public void setSubmissionTime(final double clockTime){
		super.setSubmissionTime(clockTime);
	}
	
	public double getSubmissionTime(){
		return super.getSubmissionTime();
	}
	
	public void setExecStartTime(final double clockTime){
		super.setExecStartTime(clockTime);
	}
	
	public double getExecStartTime(){
		return super.getExecStartTime();
	}
	
	public void setExecParam(final double wallTime, final double actualTime){
		super.setExecParam(wallTime, actualTime);
	}
	
	public void setCloudletStatus(final int newStatus) throws Exception{
		super.setCloudletStatus(newStatus);
	}
	
	public int getCloudletStatus(){
		return super.getCloudletStatus();
	}
	
	public String getCloudletStatusString() {
		return super.getCloudletStatusString();
	}
	
	public long getCloudletLength(){
		return super.getCloudletLength();
	}
	
	public long getCloudletTotalLength(){
		return super.getCloudletTotalLength();
	}
	
	public double getFinishTime(){
		return super.getFinishTime();
	}
	
	protected void write(final String str) {
		super.write(str);
	}
	
	public int getStatus(){
		return super.getStatus();
	}
	
	public int getCloudletId() {
		return super.getCloudletId();
	}

	
	public int getVmId() {
		return super.getVmId();
	}
	
	public void setVmId(final int vmId){
		super.setVmId(vmId);
	}
	
	public double getActualCPUTime(){
		return super.getActualCPUTime();
	}
	
	public void setResourceParameter(final int resourceID, final double costPerCPU, final double costPerBw){
		super.setResourceParameter(resourceID, costPerCPU, costPerBw);
	}
	
	public double getProcessingCost(){
		return super.getProcessingCost();
	}
	
	public List<String> getRequiredFiles() {
		return super.getRequiredFiles();
	}
	
	protected void setRequiredFiles(final List<String> requiredFiles){
		super.setRequiredFiles(requiredFiles);
	}
	
	public boolean addRequiredFile(final String fileName){
		return super.addRequiredFile(fileName);
	}
	
	public boolean deleteRequiredFile(final String filename){
		return super.deleteRequiredFile(filename);
	}
	
	public boolean requiresFiles(){
		return super.requiresFiles();
	}
	
	public UtilizationModel getUtilizationModelCpu(){
		return super.getUtilizationModelCpu();
	}
	
	 
		public void setUtilizationModelCpu(final UtilizationModel utilizationModelCpu) {
			super.setUtilizationModelCpu(utilizationModelCpu);
		}

		/**
		 * Gets the utilization model ram.
		 * 
		 * @return the utilization model ram
		 */
		public UtilizationModel getUtilizationModelRam() {
			return super.getUtilizationModelRam();
		}

		/**
		 * Sets the utilization model ram.
		 * 
		 * @param utilizationModelRam the new utilization model ram
		 */
		public void setUtilizationModelRam(final UtilizationModel utilizationModelRam) {
			super.setUtilizationModelRam(utilizationModelRam);
		}
		
		public UtilizationModel getUtilizationModelBw(){
			return super.getUtilizationModelBw();
		}
		
		public void setUtilizationModelBw(final UtilizationModel utilizationModelBw){
			super.setUtilizationModelBw(utilizationModelBw);
		}
		
		public double getUtilizationOfCpu(final double time) {
			return super.getUtilizationOfCpu(time);
		}

		/**
		 * Gets the utilization of memory.
		 * 
		 * @param time the time
		 * @return the utilization of memory
		 */
		public double getUtilizationOfRam(final double time) {
			return super.getUtilizationOfRam(time);
		}

		/**
		 * Gets the utilization of bw.
		 * 
		 * @param time the time
		 * @return the utilization of bw
		 */
		public double getUtilizationOfBw(final double time) {
			return super.getUtilizationOfBw(time);
		}
	}


