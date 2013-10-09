import java.util.HashMap;
import java.util.Map;

import edu.brown.robotics.rosbridge.RosbridgeClient;
import edu.brown.robotics.rosbridge.MessageHandler;


public class ROSBurlapServiceSource implements ROSBurlapDataSource {

	private ServiceCallback serviceCallback;
	private RosbridgeClient rosbridgeClient;
	private String service;
	private String[] messageFields;
	
	public ROSBurlapServiceSource(RosbridgeClient client, 
			String service, String[] fields)
	{
		this.rosbridgeClient = client;
		this.service = service;
		this.messageFields = fields;
	}
	@Override
	public Object getData() {
		
		this.serviceCallback.MessageReceived = false;
		this.rosbridgeClient.CallService(this.service, null, this.serviceCallback);
		
		while (!this.serviceCallback.MessageReceived)
		{
			try {
				Thread.sleep(10);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		Object currentObject = this.serviceCallback.currentMessage;
		for (int i =0 ; i < this.messageFields.length; i++)
		{
			if (currentObject != null && currentObject instanceof HashMap)
			{
				HashMap<String, Object> currentMap = 
						new HashMap<String, Object>((HashMap)currentObject);
				currentObject = currentMap.get(this.messageFields[i]);
			}
			else
			{
				return currentObject;
			}
		}
		return null;
	}
	
	public class ServiceCallback implements MessageHandler
	{
		public HashMap<String, Object> currentMessage;
		public Boolean MessageReceived;
		@Override
		public void messageReceived(Map message) {
			this.currentMessage = new HashMap<String, Object>(message);
		}
		
	}

}
