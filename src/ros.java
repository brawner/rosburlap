import java.util.HashMap;
import java.util.List;
import java.util.Map;

import burlap.oomdp.auxiliary.DomainGenerator;
import burlap.oomdp.core.Domain;
import edu.brown.robotics.rosbridge.*;
public class ros implements DomainGenerator {

	protected RosbridgeClient rosbridgeClient;
	protected HashMap<String, ROSBurlapDataSource> dataSources;
	protected HashMap<String, Object> data;
	
	public ros(String url, short port)
	{
		this.rosbridgeClient = new RosbridgeClient(url, port);
		this.dataSources = new HashMap<String, ROSBurlapDataSource>();
		this.data = new HashMap<String, Object>();
	}
	
	public void addSubscriberDataSource(String topic, String type, String[] fields, String id)
	{
		try {
			this.dataSources.put(id, 
					new ROSBurlapSubscriberSource(this.rosbridgeClient, topic, type, fields));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void addServiceDataSource(String service, String[] fields, String id)
	{
		this.dataSources.put(id, new ROSBurlapServiceSource(this.rosbridgeClient, service, fields));
	}
	
	public void setData()
	{
		for (Map.Entry<String, ROSBurlapDataSource> entry : this.dataSources.entrySet())
		{
			String key = entry.getKey();
			Object datum = entry.getValue().getData();
			if (datum != null)
			{
				this.data.put(key, datum);
			}
		}
	}
	
	public Object getData(String key)
	{
		if (this.data.containsKey(key))
		{
			return this.data.get(key);
		}
		return null;
	}
	
	@Override
	public Domain generateDomain() {
		// TODO Auto-generated method stub
		return null;
	}
}
