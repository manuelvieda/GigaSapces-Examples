/**
 * Manuel Vieda 
 * http://manuelvieda.com
 * Date: 27/07/2015
 */
package com.manuelvieda.gs.example.processors;

import org.openspaces.events.polling.ReceiveHandler;
import org.openspaces.events.polling.receive.ExclusiveReadReceiveOperationHandler;
import org.openspaces.events.polling.receive.ReceiveOperationHandler;

/**
 * This class provides default implementation for the Polling Processors.
 *
 * @author Manuel E. Vieda (contacto@manuelvieda.com)
 * @version 1.0
 * @since 1.0
 */
public abstract class AbstractGenericProcessor {

	/**
	 * The receive handler strategy.
	 *
	 * @return the receive operation handler method.
	 */
	@ReceiveHandler
	public ReceiveOperationHandler receiveHandler() {

		final ExclusiveReadReceiveOperationHandler receiveHandler = new ExclusiveReadReceiveOperationHandler();
		receiveHandler.setNonBlocking(true);
		receiveHandler.setNonBlockingFactor(30);
		receiveHandler.setUseMemoryOnlySearch(true);
		receiveHandler.setUseFifoGrouping(false);
		return receiveHandler;

	}

}
