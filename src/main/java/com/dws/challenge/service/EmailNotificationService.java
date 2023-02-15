package com.dws.challenge.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.dws.challenge.domain.Account;

public class EmailNotificationService implements NotificationService {

	private static Logger LOGGER = LoggerFactory.getLogger(EmailNotificationService.class);

	@Override
	public void notifyAboutTransfer(Account account, String transferDescription) {
		// THIS METHOD SHOULD NOT BE CHANGED - ASSUME YOUR COLLEAGUE WILL IMPLEMENT IT
		LOGGER.info("Sending notification to owner of {}: {}", account.getAccountId(), transferDescription);
	}

}
