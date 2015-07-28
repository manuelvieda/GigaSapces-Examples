/**
 * PayU Latam - Copyright (c) 2013 - 2015
 * http://www.payu.com.co
 * Date: 27/07/2015
 */
package com.manuelvieda.gs.example.processors;

import java.util.Calendar;

import org.openspaces.core.GigaSpace;
import org.openspaces.events.EventDriven;
import org.openspaces.events.EventTemplate;
import org.openspaces.events.TransactionalEvent;
import org.openspaces.events.adapter.SpaceDataEvent;
import org.openspaces.events.polling.Polling;
import org.springframework.beans.factory.annotation.Value;

import com.gigaspaces.client.WriteModifiers;
import com.manuelvieda.gs.example.model.Message;
import com.manuelvieda.gs.example.model.MessageState;

import net.jini.space.JavaSpace;

/**
 * Polling container that process the message in NEW state
 * 
 * @author Manuel E. Vieda (contacto@manuelvieda.com)
 * @version 1.0
 * @since 1.0
 */
@EventDriven
@Polling(
		receiveTimeout = 15000,
		concurrentConsumers = 3,
		maxConcurrentConsumers = 10,
		gigaSpace = "gigaSpace")
@TransactionalEvent(
		timeout = 60,
		transactionManager = "transactionManager")
public class ValidateMessageProcessor extends AbstractGenericProcessor {

	/**
	 * Message lease
	 */
	@Value("${message.lease:120000}")
	private long messageLease;

	/**
	 * Builds the event template.
	 *
	 * @return the event template.
	 */
	@EventTemplate
	public Message unprocessedData() {

		final Message message = new Message();
		message.setState(MessageState.IN_VALIDATION);
		return message;

	}

	/**
	 * Process the Message
	 *
	 * @param message The message to process
	 * @param gigaSpace The reference to the GigaSpace proxy
	 */
	@SpaceDataEvent
	public void eventListener(final Message message, final GigaSpace gigaSpace) {

		message.setState(MessageState.SUBMITTED);
		message.setSubmittedStateDate(Calendar.getInstance().getTime());
		gigaSpace.write(message, messageLease, JavaSpace.NO_WAIT, WriteModifiers.UPDATE_ONLY);

	}

}
