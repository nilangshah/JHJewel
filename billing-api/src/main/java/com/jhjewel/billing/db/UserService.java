package com.jhjewel.billing.db;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jhjewel.billing.exception.DataNotFoundException;
import com.jhjewel.billing.model.User;
import com.jhjewel.billing.repository.UserRepo;
import com.jhjewel.util.AuthUtil;

@Service
public class UserService {

	@Autowired
	private UserRepo repo;

	public List<User> getAllUsers() {
		return repo.findAll();
	}

	public User addUser(User user) {
		String pSalt = AuthUtil.generateSalt();
		user.setPassword(AuthUtil.computeHash(user.getPassword(), pSalt));
		user.setPasswordSalt(pSalt);
		return repo.saveAndFlush(user);
	}

	public User getUserByName(String uName) {
		return repo.findByUsernameIgnoreCase(uName);
	}

	public void deleteUser(int userid) {
		repo.delete(userid);
	}

	public User updateUser(User user) throws DataNotFoundException {
		User dbUser = repo.findOne(user.getId());
		if (dbUser == null) {
			throw new DataNotFoundException("User not found");
		}
		if (user.getUsername() != null) {
			dbUser.setUsername(user.getUsername());
		}
		if (user.getPassword() != null) {
			String pSalt = AuthUtil.generateSalt();
			dbUser.setPassword(AuthUtil.computeHash(user.getPassword(), pSalt));
			dbUser.setPasswordSalt(pSalt);
		}
		return repo.saveAndFlush(dbUser);
	}

	public User getUser(int userId) {
		return repo.findOne(userId);
	}

}