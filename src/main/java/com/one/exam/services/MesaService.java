package com.one.exam.services;

import org.springframework.stereotype.Service;
import com.one.exam.models.Mesa;
import com.one.exam.repositories.MesaRepository;

@Service
public class MesaService extends BaseService<Mesa>{

	public MesaService(MesaRepository repository) {
		super(repository);
	}
}
