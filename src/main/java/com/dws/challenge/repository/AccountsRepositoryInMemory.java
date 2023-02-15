package com.dws.challenge.repository;

import java.math.BigDecimal;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import com.dws.challenge.domain.Account;
import com.dws.challenge.exception.DuplicateAccountIdException;

@Repository
public class AccountsRepositoryInMemory implements AccountsRepository {
	private static Logger LOGGER = LoggerFactory.getLogger(AccountsRepositoryInMemory.class);
	private final Map<String, Account> accounts = new ConcurrentHashMap<>();

	Map<String, String> newMap = new ConcurrentHashMap<String, String>();

	Map<String, BigDecimal> copy = new ConcurrentHashMap<String, BigDecimal>();

	// copy.put(accounts.get("ABC"));

	@Override
	public Account createAccount(Account account) throws DuplicateAccountIdException {
		Account previousAccount = accounts.putIfAbsent(account.getAccountId(), account);
		if (previousAccount != null) {
			throw new DuplicateAccountIdException("Account id " + account.getAccountId() + " already exists!");
		}

		return previousAccount;
	}

	@Override
	public Account getAccount(String accountId) throws DuplicateAccountIdException {
		Account presentAccount =  accounts.get(accountId);
		if (null == presentAccount ) {
			throw new DuplicateAccountIdException("Account id " + accountId + " Not found");
		}
		return accounts.get(accountId);
	}

	@Override
	public void clearAccounts() {
		accounts.clear();
	}

	@Override
	public void save(String fromAccountId, String toAccountId, BigDecimal amount) {

		BigDecimal balanceTo = (accounts.get(toAccountId).getBalance()).add(amount);
		BigDecimal balanceFrom = (accounts.get(fromAccountId).getBalance()).subtract(amount);

		Account toAcc = new Account(toAccountId, balanceTo);
		if (!(balanceFrom.compareTo(BigDecimal.ZERO) < 0)) {
			Account froAcc = new Account(fromAccountId, balanceFrom);
			accounts.put(fromAccountId, froAcc);
			accounts.put(toAccountId, toAcc);

		} else {
			LOGGER.info("Balance is insufficient::");
		}

	}

}
