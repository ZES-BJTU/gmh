package com.zes.squad.gmh.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zes.squad.gmh.mapper.JudgementMapper;
import com.zes.squad.gmh.service.JudgementService;
@Service("judgementService")
public class JudgementServiceImpl implements JudgementService{

	@Autowired
    private JudgementMapper      judgementMapper;
	@Override
	public void updateUrl(String url) {
		judgementMapper.delete();
		judgementMapper.insert(url);
		
	}

}
