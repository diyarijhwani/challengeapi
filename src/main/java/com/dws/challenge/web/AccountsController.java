package com.dws.challenge.web;

import java.math.BigDecimal;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dws.challenge.domain.Account;
import com.dws.challenge.domain.CreateTransferDto;
import com.dws.challenge.exception.DuplicateAccountIdException;
import com.dws.challenge.service.AccountsService;

@RestController
@RequestMapping("/v1/accounts")
public class AccountsController {

	private static Logger LOGGER = LoggerFactory.getLogger(AccountsController.class);
	private final AccountsService accountsService;

	@Autowired
	public AccountsController(AccountsService accountsService) {
		this.accountsService = accountsService;
	}

	@PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Object> createAccount(@RequestBody @Valid Account account) {
		LOGGER.info("Creating account {}", account);

		try {
			this.accountsService.createAccount(account);
		} catch (DuplicateAccountIdException daie) {
			return new ResponseEntity<>(daie.getMessage(), HttpStatus.BAD_REQUEST);
		}

		return ResponseEntity.status(HttpStatus.CREATED).body("Account is created.");
	}

	@GetMapping(path = "/{accountId}")
	public Account getAccount(@PathVariable String accountId) {
		LOGGER.info("Retrieving account for id {}", accountId);

		return accountsService.getAccount(accountId);
				

	}

	@PostMapping(path = "/account/{fromAccountId}/transfers")
	public ResponseEntity<?> transferAmount(final @PathVariable String fromAccountId,
			@RequestBody CreateTransferDto transferDto) {
		// validation checks
		if (transferDto == null || transferDto.getToAccountId() == null || transferDto.getAmount() == null) {
			return ResponseEntity.badRequest().body("BOTH TO AND FROM ACCOUNT NEEDED.");
		}
		// check balance is positive
		if (transferDto.getAmount().compareTo(new BigDecimal(0.0)) < 1) {
			return ResponseEntity.badRequest().body("TRANSFER AMOUNT SHOULD BE GREATER THAN ZERO");
		}

		try {
			accountsService.transfer(fromAccountId, transferDto.getToAccountId(), transferDto.getAmount());
		} catch (Exception ex) {
			// message is sent back to user.
			return ResponseEntity.badRequest().body("Money Transfer Action Failed Please try again.");
		}
		return ResponseEntity.ok().body("Transfer Called.");
	}
}
