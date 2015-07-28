/**
 * Manuel Vieda 
 * http://manuelvieda.com
 * Date: 27/07/2015
 */
package com.manuelvieda.gs.example.feeder;

import java.util.Calendar;
import java.util.UUID;

import org.openspaces.core.GigaSpace;
import org.openspaces.core.GigaSpaceConfigurer;
import org.openspaces.core.space.UrlSpaceConfigurer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.gigaspaces.client.WriteModifiers;
import com.j_spaces.core.IJSpace;
import com.manuelvieda.gs.example.model.Message;
import com.manuelvieda.gs.example.model.MessageState;

/**
 * Feeder
 *
 * @author Manuel E. Vieda
 * @version 1.0
 * @since 1.0
 */
public class Feeder {

	/**
	 * Feeder logger
	 */
	private static final Logger LOGGER = LoggerFactory.getLogger(Feeder.class);

	/**
	 * Default Space URL
	 */
	private static final String DEFAULT_URL = "jini://*/*/space?locators=localhost";

	/**
	 * GigaSpace context for 'space' Space
	 */
	private final GigaSpace gigaSpace;

	/**
	 * Fedder main method
	 * 
	 * @param args
	 */
	public static void main(final String... args) {

		try {
			final String url = args != null && args.length == 1 ? args[0] : DEFAULT_URL;
			final Feeder feeder = new Feeder(url);

			// 100 Messages
			feeder.feed(100, "100 Messages");
			Thread.sleep(1000);

			// 1000 Messages
			feeder.feed(1000, "1000 Messages");
			Thread.sleep(1000);

			// 5000 Messages
			feeder.feed(5000, "5000 Messages");
			Thread.sleep(5000);

			// 20000 Messages
			feeder.feed(15000, "15000 Messages");

		} catch (final InterruptedException e) {

			LOGGER.error("Error sending messages. ", e);
		}

	}

	/**
	 * Feeder constructor
	 * 
	 * @param spaceUrl Space URL
	 */
	public Feeder(final String spaceUrl) {

		LOGGER.info("Stablishing connection to space with URL [{}]", spaceUrl);
		final IJSpace space = new UrlSpaceConfigurer(spaceUrl).space();
		gigaSpace = new GigaSpaceConfigurer(space).gigaSpace();

	}

	/**
	 * Create and send {@code numberOfMessages} messages with {@code content} as content
	 * 
	 * @param numberOfMessages Number of messages to send
	 * @param content The content of the messages to send
	 */
	public void feed(final int numberOfMessages, final String content) {

		LOGGER.info("Sending [{}] messages with content [{}]", numberOfMessages, content);

		for (int i = 0; i < numberOfMessages; i++) {
			final Message message = new Message();
			message.setId(UUID.randomUUID().toString());
			message.setContent(content);
			message.setState(MessageState.NEW);
			message.setNewStateDate(Calendar.getInstance().getTime());
			gigaSpace.write(message, WriteModifiers.WRITE_ONLY);

			if (LOGGER.isDebugEnabled()) {
				if (i % 100 == 0 && i != 0) {
					LOGGER.debug("  - Messages sent {}", i);
				}
			}

		}
	}

}
