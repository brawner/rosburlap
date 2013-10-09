import java.util.HashMap;
import java.util.Map;

import edu.brown.robotics.rosbridge.RosbridgeClient;
import edu.brown.robotics.rosbridge.MessageHandler;

public class ROSBurlapSubscriberSource implements ROSBurlapDataSource {
	
	private RosbridgeClient rosbridgeClient;
	protected  SubscriberCallback subscriberCallback;
	protected String[] messageFields;
	protected HashMap<String, Object> currentMessage;
	
	public ROSBurlapSubscriberSource(RosbridgeClient client, 
			String topic, String type, String[] fields) throws Exception
	{
		this.rosbridgeClient = client;
		if (!this.rosbridgeClient.isConnected())
		{
			this.rosbridgeClient.Subscribe(topic,  type, subscriberCallback);
			this.messageFields = fields;
		}
		else
		{
			throw new Exception("Rosbridge Client is not connected");
		}
	}

	@Override
	public Object getData() {
		Object currentObject = this.subscriberCallback.currentMessage;
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

	public class SubscriberCallback implements MessageHandler
	{
		public HashMap<String, Object> currentMessage;
		public void messageReceived(Map message)
		{
			this.currentMessage = new HashMap<String, Object>( message);
		}
	}
}
