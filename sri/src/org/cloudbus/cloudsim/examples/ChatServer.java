package org.cloudbus.cloudsim.examples;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.RandomAccessFile;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Enumeration;
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

public class ChatServer {
	
	private class CloudSimExample extends Thread{
		
		public CloudSimExample(int i){
			i=9;
		}
		 private List<Cloudlet> cloudletList;

			/** The vmlist. */
			private List<Vm> vmlist;
			public void run(){
        try {
			// First step: Initialize the CloudSim package. It should be called
			// before creating any entities.
			int num_user = 1; // number of cloud users
			Calendar calendar = Calendar.getInstance();
			boolean trace_flag = false; // mean trace events

			// Initialize the CloudSim library
			CloudSim.init(num_user, calendar, trace_flag);

			// Second step: Create Datacenters
			// Datacenters are the resource providers in CloudSim. We need at
			// list one of them to run a CloudSim simulation
			Datacenter datacenter0 = createDatacenter("Datacenter_0");

			// Third step: Create Broker
			DatacenterBroker broker = createBroker();
			int brokerId = broker.getId();

			// Fourth step: Create one virtual machine
			vmlist = new ArrayList<Vm>();

			// VM description
			int vmid = 0;
			int mips = 1000;
			long size = 10000; // image size (MB)
			int ram = 512; // vm memory (MB)
			long bw = 1000;
			int pesNumber = 1; // number of cpus
			String vmm = "Xen"; // VMM name

			// create VM
			Vm vm = new Vm(vmid, brokerId, mips, pesNumber, ram, bw, size, vmm, new CloudletSchedulerTimeShared());

			// add the VM to the vmList
			vmlist.add(vm);

			// submit vm list to the broker
			broker.submitVmList(vmlist);

			// Fifth step: Create one Cloudlet
			cloudletList = new ArrayList<Cloudlet>();

			// Cloudlet properties
			int id = 0;
			long length = 400000;
			long fileSize = 300;
			long outputSize = 300;
			UtilizationModel utilizationModel = new UtilizationModelFull();

			Cloudlet cloudlet = new Cloudlet(id, length, pesNumber, fileSize, outputSize, utilizationModel, utilizationModel, utilizationModel);
			cloudlet.setUserId(brokerId);
			cloudlet.setVmId(vmid);

			// add the cloudlet to the list
			cloudletList.add(cloudlet);

			// submit cloudlet list to the broker
			broker.submitCloudletList(cloudletList);

			// Sixth step: Starts the simulation
			CloudSim.startSimulation();

			CloudSim.stopSimulation();

			//Final step: Print results when simulation is over
			List<Cloudlet> newList = broker.getCloudletReceivedList();
			printCloudletList(newList);

			// Print the debt of each user to each datacenter
			//datacenter0.printDebts();

			Log.printLine("CloudSimExample1 finished!");
		} catch (Exception e) {
			e.printStackTrace();
			Log.printLine("Unwanted errors happen");
		}
			}
	}

	 private class ClientHandler extends Thread {

	        /** Socket to read client messages. */
		
	        private Socket incoming; 

	        /** Creates a hander to serve the client on the given socket. */
	        public ClientHandler(Socket incoming) {
	            this.incoming = incoming;
	        }

	        /** Starts receiving and broadcasting messages. */
	        public void run() {
	            PrintWriter out = null;
	            try {
	                out = new PrintWriter(
	                        new OutputStreamWriter(incoming.getOutputStream()));
	               
	                // inform the server of this new client
	                ChatServer.this.addClient(out);

	                out.println("MOBILE CLIENT ");
	             //   out.println("Enter BYE to exit.");
	                int speed_server=deviceProfiler();
	               // int speed_server=CPUInfo();
	                //int speed_ser=900;
	              out.println(speed_server);
	             
	              // System.out.println("sent");
	                out.flush();

	                BufferedReader in 
	                    = new BufferedReader(
	                        new InputStreamReader(incoming.getInputStream())); 
	                for (;;) {
	                    String msg = in.readLine(); 
	                    if (msg == null) {
	                        break; 
	                    } else {
	                        if (msg.trim().equals("BYE")) 
	                            break; 
	                        System.out.println("Received: " + msg);
	                        // broadcast the receive message
	                        ChatServer.this.broadcast(msg);

	                	}

	                    }
	               // }
	                //incoming.close(); 
	                ChatServer.this.removeClient(out);
	            } catch (Exception e) {
	                if (out != null) {
	                    ChatServer.this.removeClient(out);
	                }
	                e.printStackTrace(); 
	            }
	        }
	    }


	private static final String USAGE = "Usage: java ChatServer";

    /** Default port number on which this server to be run. */
    private static final int PORT_NUMBER = 8009;

    /** List of print writers associated with current clients,
     * one for each. */
    private List<PrintWriter> clients;

    /** Creates a new server. */
    public ChatServer() {
        clients = new LinkedList<PrintWriter>();
    }

	private static Datacenter createDatacenter(String name) {

		// Here are the steps needed to create a PowerDatacenter:
		// 1. We need to create a list to store
		// our machine
		List<Host> hostList = new ArrayList<Host>();

		// 2. A Machine contains one or more PEs or CPUs/Cores.
		// In this example, it will have only one core.
		List<Pe> peList = new ArrayList<Pe>();

		int mips = 1000;

		// 3. Create PEs and add these into a list.
		peList.add(new Pe(0, new PeProvisionerSimple(mips))); // need to store Pe id and MIPS Rating

		// 4. Create Host with its id and list of PEs and add them to the list
		// of machines
		int hostId = 0;
		int ram = 2048; // host memory (MB)
		long storage = 1000000; // host storage
		int bw = 10000;

		hostList.add(
			new Host(
				hostId,
				new RamProvisionerSimple(ram),
				new BwProvisionerSimple(bw),
				storage,
				peList,
				new VmSchedulerTimeShared(peList)
			)
		); // This is our machine

		// 5. Create a DatacenterCharacteristics object that stores the
		// properties of a data center: architecture, OS, list of
		// Machines, allocation policy: time- or space-shared, time zone
		// and its price (G$/Pe time unit).
		String arch = "x86"; // system architecture
		String os = "Linux"; // operating system
		String vmm = "Xen";
		double time_zone = 10.0; // time zone this resource located
		double cost = 3.0; // the cost of using processing in this resource
		double costPerMem = 0.05; // the cost of using memory in this resource
		double costPerStorage = 0.001; // the cost of using storage in this
										// resource
		double costPerBw = 0.0; // the cost of using bw in this resource
		LinkedList<Storage> storageList = new LinkedList<Storage>(); // we are not adding SAN
													// devices by now

		DatacenterCharacteristics characteristics = new DatacenterCharacteristics(
				arch, os, vmm, hostList, time_zone, cost, costPerMem,
				costPerStorage, costPerBw);

		// 6. Finally, we need to create a PowerDatacenter object.
		Datacenter datacenter = null;
		try {
			datacenter = new Datacenter(name, characteristics, new VmAllocationPolicySimple(hostList), storageList, 0);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return datacenter;
	}

	// We strongly encourage users to develop their own broker policies, to
	// submit vms and cloudlets according
	// to the specific rules of the simulated scenario
	/**
	 * Creates the broker.
	 *
	 * @return the datacenter broker
	 */
	private static DatacenterBroker createBroker() {
		DatacenterBroker broker = null;
		try {
			broker = new DatacenterBroker("Broker");
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		return broker;
	}

	/**
	 * Prints the Cloudlet objects.
	 *
	 * @param list list of Cloudlets
	 */
	private static void printCloudletList(List<Cloudlet> list) {
		int size = list.size();
		Cloudlet cloudlet;

		String indent = "    ";
		Log.printLine();
		Log.printLine("========== OUTPUT ==========");
		Log.printLine("Cloudlet ID" + indent + "STATUS" + indent
				+ "Data center ID" + indent + "VM ID" + indent + "Time" + indent
				+ "Start Time" + indent + "Finish Time");

		DecimalFormat dft = new DecimalFormat("###.##");
		for (int i = 0; i < size; i++) {
			cloudlet = list.get(i);
			Log.print(indent + cloudlet.getCloudletId() + indent + indent);

			if (cloudlet.getCloudletStatus() == Cloudlet.SUCCESS) {
				Log.print("SUCCESS");

				Log.printLine(indent + indent + cloudlet.getResourceId()
						+ indent + indent + indent + cloudlet.getVmId()
						+ indent + indent
						+ dft.format(cloudlet.getActualCPUTime()) + indent
						+ indent + dft.format(cloudlet.getExecStartTime())
						+ indent + indent
						+ dft.format(cloudlet.getFinishTime()));
			}
		}
	}

	/** Starts the server. */
    public void start() {
    //	System.out.println("calling");
      //String ip=getIpAddress();
        System.out.println("CloudSim server started on port "
                + PORT_NUMBER +"!"); 
        try {
            ServerSocket s = new ServerSocket(PORT_NUMBER); 
            
            for (;;) {
                Socket incoming = s.accept();
             System.out.println(incoming.getInetAddress());
                new ClientHandler(incoming).start(); 
               
            }
          
        } catch (Exception e) {
            e.printStackTrace();
        }
        int i=9;
        new CloudSimExample(i).start();
        System.out.println("AndyChat server stopped."); 
    }

    private String getIpAddress() {
		// TODO Auto-generated method stub
    	
    		String ip = "hello";
    		try {
    			Enumeration<NetworkInterface> enumNetworkInterfaces = NetworkInterface
    					.getNetworkInterfaces();
    			while (enumNetworkInterfaces.hasMoreElements()) {
    				NetworkInterface networkInterface = enumNetworkInterfaces
    						.nextElement();
    				Enumeration<InetAddress> enumInetAddress = networkInterface
    						.getInetAddresses();
    				while (enumInetAddress.hasMoreElements()) {
    					InetAddress inetAddress = enumInetAddress
    							.nextElement();

    					if (inetAddress.isSiteLocalAddress()) {
    						ip += "Server running at : "
    								+ inetAddress.getHostAddress();
    					}
    				}
    			}

    		} catch (SocketException e) {
    			// TODO Auto-generated catch block
    			e.printStackTrace();
    			ip += "Something Wrong! " + e.toString() + "\n";
    		}
    		return ip;
    	}
   

	/** Adds a new client identified by the given print writer. */
    private void addClient(PrintWriter out) {
        synchronized(clients) {
            clients.add(out);
        }
    }

    /** Adds the client with given print writer. */
    private void removeClient(PrintWriter out) {
        synchronized(clients) {
            clients.remove(out);
        }
    }

    /** Broadcasts the given text to all clients. */
    private void broadcast(String msg) {
        for (PrintWriter out: clients) {
            out.println(msg);
            out.flush();
        }
    }
    public int deviceProfiler() throws IOException {
		// TODO Auto-generated method stub
		//Process process = Runtime.getRuntime().exec("wmic cpu get CurrentClockSpeed");
    	Process process = Runtime.getRuntime().exec("wmic cpu get CurrentClockSpeed"); 
	     // Process process = Runtime.getRuntime().exec("/proc/cpuinfo");
	    BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));                                          
	    String s;   
	    StringBuffer sb = new StringBuffer();
	    while ((s = reader.readLine()) != null) {                                
	      //System.out.println("Script output: " + s);
	       sb.append(s);
	     }
	    String str = sb.toString();
	    int i=Integer.parseInt(str.replaceAll("[\\D]", ""));
	    System.out.println("Speed of the server: " + i+"Mhz");
	    return i;
		//return 0;
	}

	public int CPUInfo() throws IOException {
		// TODO Auto-generated method stub
		String cpuMaxFreq = "freq";
	    RandomAccessFile reader = new RandomAccessFile("/sys/devices/system/cpu/cpu0/cpufreq/cpuinfo_min_freq", "r");
	    cpuMaxFreq = reader.readLine();
	    reader.close();
	    int s=Integer.parseInt(cpuMaxFreq);
	    return s;
		//return 0;
	}
}

    
    
    