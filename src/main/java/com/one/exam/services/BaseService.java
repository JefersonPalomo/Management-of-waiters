package com.one.exam.services;

import java.util.List;
import java.util.Optional;

import com.one.exam.repositories.BaseRepository;

public class BaseService<T> {
	
	protected BaseRepository<T> repositoryBase;
	
	public BaseService(BaseRepository<T> repositoryBase) {
		this.repositoryBase = repositoryBase;
	}
	
	public List<T> findAll(){
		return repositoryBase.findAll();
	}
	
	public T save(T object) {
		return repositoryBase.save(object);
	}

	public T findById(Long id) {
		Optional<T> optional = repositoryBase.findById(id);
		if(optional.isPresent()) {
			return optional.get();
		} else {
			return null;
		}
	}
	
	public T update (T object) {
		return save(object);
	}
	
	public void delete(Long id) {
		repositoryBase.deleteById(id);
	}
}
