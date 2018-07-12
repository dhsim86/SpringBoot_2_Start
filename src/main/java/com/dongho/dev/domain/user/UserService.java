package com.dongho.dev.domain.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class UserService {

	@Cacheable(cacheNames = "launching")
	public String getUserName(String id) {
		log.debug("Not cached: {}", id);
		return id + "_user";
	}

	@CacheEvict(cacheNames = "launching")
	public void deleteUser(String id) {

	}
}
