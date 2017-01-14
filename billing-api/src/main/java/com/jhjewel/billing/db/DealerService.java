package com.jhjewel.billing.db;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jhjewel.billing.exception.DataNotFoundException;
import com.jhjewel.billing.model.Dealer;
import com.jhjewel.billing.repository.DealerRepo;

@Service
public class DealerService {

	@Autowired
	private DealerRepo repo;

	public List<Dealer> getAllDealers() {
		return repo.findAll();
	}

	public Dealer addDealer(Dealer dealer) {
		return repo.saveAndFlush(dealer);
	}

	public void deleteDealer(int dealerId) {
		repo.delete(dealerId);
	}

	public Dealer updateDealer(Dealer dealer) throws DataNotFoundException {
		Dealer dbDealer = repo.findOne(dealer.getId());
		if (dbDealer == null) {
			throw new DataNotFoundException("Dealer not found");
		}
		return repo.saveAndFlush(dealer);
	}

	public Dealer getUser(int dealerId) {
		return repo.findOne(dealerId);
	}

}