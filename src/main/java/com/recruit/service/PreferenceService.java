package com.recruit.service;

import com.recruit.domain.CoordinateVO;
import com.recruit.domain.PreferenceVO;

public interface PreferenceService {
	
	public PreferenceVO selectPREFOne(String id)throws Exception;
	
	public CoordinateVO selectCodeCoordinate(int bno)throws Exception;
}
