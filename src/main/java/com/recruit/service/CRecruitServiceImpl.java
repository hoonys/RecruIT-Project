package com.recruit.service;

import java.util.List;

import javax.inject.Inject;

import org.springframework.stereotype.Service;

import com.recruit.domain.CRecruitVO;
import com.recruit.domain.PersonalCriteriaVO;
import com.recruit.persistence.CRecruitDAO;

@Service
public class CRecruitServiceImpl implements CRecruitService {
	
	@Inject
	private CRecruitDAO dao;

	@Override
	public int countPaging(PersonalCriteriaVO cri)throws Exception{
		return dao.countPaging(cri);
	}
	
	@Override
	public int countPaging_ongoing(PersonalCriteriaVO cri)throws Exception{
		return dao.countPaging_ongoing(cri);
	}

	@Override
	public int countPaging_closed(PersonalCriteriaVO cri)throws Exception{
		return dao.countPaging_closed(cri);
	}
	
	@Override
	public List<CRecruitVO> selectCRList(PersonalCriteriaVO cri, String order_value)throws Exception{
		return dao.selectCRList(cri, order_value);
	};
	
	@Override
	public List<CRecruitVO> selectCRList_ongoing(PersonalCriteriaVO cri, String order_value)throws Exception{
		return dao.selectCRList_ongoing(cri, order_value);
	};
	
	@Override
	public List<CRecruitVO> selectCRList_closed(PersonalCriteriaVO cri, String order_value)throws Exception{
		return dao.selectCRList_closed(cri, order_value);
	};
	
	@Override
	public int countPaging_AP(PersonalCriteriaVO cri)throws Exception{
		return dao.countPaging_AP(cri);
	}
	
	@Override
	public int countPaging_AP_ongoing(PersonalCriteriaVO cri)throws Exception{
		return dao.countPaging_AP_ongoing(cri);
	}

	@Override
	public int countPaging_AP_closed(PersonalCriteriaVO cri)throws Exception{
		return dao.countPaging_AP_closed(cri);
	}
	
	@Override//안소연 사용
	public List<CRecruitVO> selectAPList(PersonalCriteriaVO cri, String order_value)throws Exception{
		return dao.selectAPList(cri, order_value);
	};
	
	@Override//안소연 사용
	public List<CRecruitVO> selectAPList_ongoing(PersonalCriteriaVO cri, String order_value)throws Exception{
		return dao.selectAPList_ongoing(cri, order_value);
	};
	
	@Override//안소연 사용
	public List<CRecruitVO> selectAPList_closed(PersonalCriteriaVO cri, String order_value)throws Exception{
		return dao.selectAPList_closed(cri, order_value);
	};
	
	@Override
	public CRecruitVO selectCROne(int bno)throws Exception{
		return dao.selectCROne(bno);
	};
	

}
