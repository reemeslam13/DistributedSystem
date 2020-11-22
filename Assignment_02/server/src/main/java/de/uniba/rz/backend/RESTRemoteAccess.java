package de.uniba.rz.backend;

import java.net.SocketException;
import java.net.URI;
import java.util.Properties;

import javax.ws.rs.core.UriBuilder;

import org.glassfish.jersey.jdkhttp.JdkHttpServerFactory;
import org.glassfish.jersey.server.ResourceConfig;


public class RESTRemoteAccess implements RemoteAccess {
	private static Properties properties = Configuration.loadProperties();

	@Override
	public void run() {
		// TODO Auto-generated method stub
		
	  
	        String serverUri = properties.getProperty("serverUri");

	        URI baseUri = UriBuilder.fromUri(serverUri).build();
	        ResourceConfig config = ResourceConfig.forApplicationClass(RESTApi.class);
	        JdkHttpServerFactory.createHttpServer(baseUri, config);
	        System.out.println("Server ready to serve your JAX-RS requests...");
	    }
	

	@Override
	public void prepareStartup(TicketStore ticketStore) throws SocketException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void shutdown() {
		// TODO Auto-generated method stub
		
	}
	

}
