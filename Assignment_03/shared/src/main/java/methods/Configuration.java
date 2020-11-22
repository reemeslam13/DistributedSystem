package methods;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import de.uniba.rz.entities.AMQPChannelProperty;

public class Configuration {
	Boolean result = false;
	InputStream inputStream;
	public boolean getAMQPchannelProp(AMQPChannelProperty p) throws IOException {
	try {
		Properties prop = new Properties();
		String propFileName = "config.properties";

		inputStream = getClass().getClassLoader().getResourceAsStream(propFileName);
		
		if (inputStream != null) {
			prop.load(inputStream);
		} else {
			throw new FileNotFoundException("property file '" + propFileName + "' not found in the classpath");
		}
		 result = Boolean.valueOf( prop.getProperty(p.toString()));
		 inputStream.close();
	}
	catch (Exception e) {
		return false;
		
	} 
	return  result;
}

}
