package com.dws.challenge.domain;

import java.math.BigDecimal;
import java.util.Objects;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateTransferDto {
	private String toAccountId;
	@JsonSerialize
	private BigDecimal amount;

	@Override
	public int hashCode() {
		return Objects.hash(amount, toAccountId);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		CreateTransferDto other = (CreateTransferDto) obj;
		return amount.compareTo(other.amount) == 0 && Objects.equals(toAccountId, other.toAccountId);
	}

	public String getToAccountId() {
		return toAccountId;
	}

	public void setToAccountId(String toAccountId) {
		this.toAccountId = toAccountId;
	}

	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

}