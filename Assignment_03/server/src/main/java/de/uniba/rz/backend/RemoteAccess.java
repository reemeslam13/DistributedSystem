package de.uniba.rz.backend;

import java.net.SocketException;

/**
 * Basic interface to enable remote access to the {@link TicketStore} managing the tickets internally
 *
 */
public interface RemoteAccess extends Runnable {

	/**
	 * Generic startup method which might be used to prepare the actual execution
	 * 
	 * @param ticketStore
	 * reference to the {@link TicketStore} which is used by the application
	 */
    void prepareStartup(TicketStore ticketStore) throws SocketException;

    /**
     * Triggers the graceful shutdown of the system.
     */
    void shutdown();

}
