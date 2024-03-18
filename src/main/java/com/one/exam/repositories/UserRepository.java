package com.one.exam.repositories;

import com.one.exam.models.User;

public interface UserRepository extends BaseRepository<User>{
	User findByEmail(String email);
}
