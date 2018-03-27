package com.recruit.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import org.springframework.stereotype.Service;

import com.recruit.domain.Bnoble;
import com.recruit.domain.CUserVO;
import com.recruit.domain.CodeVO;
import com.recruit.domain.JobGroupVO;
import com.recruit.domain.PUserVO;
import com.recruit.domain.RecruitVO;
import com.recruit.domain.RegionVO;
import com.recruit.domain.ResumeVO;
import com.recruit.domain.SpanelVO;
import com.recruit.persistence.CUserDAO;
import com.recruit.persistence.CodeDAO;
import com.recruit.persistence.PUserDAO;
import com.recruit.persistence.SearchDAO;

@Service
public class SearchServiceImpl implements SearchService {

	int[] empArr = { 20, 26 }; // emp 20~26
	int[] expArr = { 1, 8 }; // exp 1~8
	int[] eduArr = { 9, 14 }; // edu 9~14

	@Inject
	private PUserDAO puserdao;

	@Inject
	private CUserDAO cuserdao;

	@Inject
	private CodeDAO codedao;

	@Inject
	private SearchDAO searchDAO;

	@Override
	public PUserVO selectPUser(String skeyword) throws Exception {
		return puserdao.selectPUser(skeyword);
	}

	@Override
	public List<PUserVO> selectPUserAll() throws Exception {
		return puserdao.selectPUserAll();
	}

	@Override
	public List<PUserVO> selectPUsers(String skey) throws Exception {
		return puserdao.selectPUsers(skey);
	}

	@Override
	public CUserVO selectCUser(String skeyword) throws Exception {
		return cuserdao.selectCUser(skeyword);
	}

	@Override
	public List<CUserVO> selectCUserAll() throws Exception {
		return cuserdao.selectCUserAll();
	}

	@Override
	public List<CUserVO> selectCUsers(String skey) throws Exception {
		return cuserdao.selectCUsers(skey);
	}

	@Override
	public List<CodeVO> CodeList(int tid) throws Exception {
		return codedao.CodeList(tid);
	}

	@Override
	public List<SpanelVO> selectRecruits(String skey) throws Exception {
		return convertToRecruitPanel(searchDAO.selectRecruits(skey));
	}

	@Override
	public List<SpanelVO> selectResumes(String skey) throws Exception {
		return convertToResumePanel(searchDAO.selectResumes(skey));
	}

	// 채용공고
	@Override
	public List<SpanelVO> selectRecruits_sel(List<String> sel_skeys) throws Exception {

		List<RecruitVO> list_tmp = searchDAO.selectRecruits_selJob(sel_skeys);
		list_tmp = intersection(list_tmp, searchDAO.selectRecruits_selRgn(sel_skeys));
		list_tmp = intersection(list_tmp, searchDAO.selectRecruits_selCod(sel_skeys, empArr));
		list_tmp = intersection(list_tmp, searchDAO.selectRecruits_selCod(sel_skeys, expArr));
		list_tmp = intersection(list_tmp, searchDAO.selectRecruits_selCod(sel_skeys, eduArr));

		List<SpanelVO> listSp_tmp;
		if (list_tmp.size() == 0 || list_tmp.get(0).getBno() == -1)
			listSp_tmp = new ArrayList<SpanelVO>();
		else {
			listSp_tmp = convertToRecruitPanel(list_tmp);
		}
		return listSp_tmp;
	}

	// 이력서
	@Override
	public List<SpanelVO> selectResumes_sel(List<String> sel_skeys) throws Exception {

		List<ResumeVO> list_tmp = searchDAO.selectResumes_selJob(sel_skeys);
		list_tmp = intersection(list_tmp, searchDAO.selectResumes_selRgn(sel_skeys));
		list_tmp = intersection(list_tmp, searchDAO.selectResumes_selCod(sel_skeys, empArr));
		list_tmp = intersection(list_tmp, searchDAO.selectResumes_selCod(sel_skeys, expArr));
		list_tmp = intersection(list_tmp, searchDAO.selectResumes_selCod(sel_skeys, eduArr));

		List<SpanelVO> listSp_tmp;
		if (list_tmp.size() == 0 || list_tmp.get(0).getBno() == -1)
			listSp_tmp = new ArrayList<SpanelVO>();
		else {
			listSp_tmp = convertToResumePanel(list_tmp);
		}
		return listSp_tmp;
	}

	Map<String, String> jobGroupMap = null;
	Map<String, String> region1Map = null;
	Map<String, String> region2Map = null;
	Map<String, String> codeMap = null;

	public void makeCodeMap() throws Exception {
		if (jobGroupMap == null) {
			List<JobGroupVO> jobGroupVOList = codedao.getTbljobgroup();
			jobGroupMap = new HashMap<String, String>();
			int jobGroupVOListLen = jobGroupVOList.size();
			for (int i = 0; i < jobGroupVOListLen; i++)
				jobGroupMap.put(jobGroupVOList.get(i).getId() + "", jobGroupVOList.get(i).getJobgroup());
		}

		if (region1Map == null || region2Map == null) {
			List<RegionVO> regionVO1List = codedao.getTblregion1();
			region1Map = new HashMap<String, String>();
			int regionVO1ListLen = regionVO1List.size();
			for (int i = 0; i < regionVO1ListLen; i++)
				region1Map.put(regionVO1List.get(i).getRgbid(), regionVO1List.get(i).getRgbname());

			List<RegionVO> regionVO2List = codedao.getTblregion2();
			region2Map = new HashMap<String, String>();
			int regionVO2ListLen = regionVO2List.size();
			for (int i = 0; i < regionVO2ListLen; i++) {
				region2Map.put(regionVO2List.get(i).getRgbid() + regionVO2List.get(i).getRgsid(),
						regionVO2List.get(i).getRgsname());
			}
		}

		if (codeMap == null) {
			List<CodeVO> codeVOList = codedao.getTblcode();
			codeMap = new HashMap<String, String>();
			int codeVOListLen = codeVOList.size();
			for (int i = 0; i < codeVOListLen; i++)
				codeMap.put(codeVOList.get(i).getId() + "", codeVOList.get(i).getCareer());
		}
	}

	// 채용공고 판넬 변환
	public List<SpanelVO> convertToRecruitPanel(List<RecruitVO> listRecruit) throws Exception {
		int num = listRecruit.size();
		makeCodeMap();

		List<SpanelVO> listPanel = new ArrayList<SpanelVO>();

		for (int i = 0; i < num; i++) {
			SpanelVO spanelVO = new SpanelVO();
			spanelVO.setBno(listRecruit.get(i).getBno());
			spanelVO.setCid(listRecruit.get(i).getCid());
			spanelVO.setTitle(listRecruit.get(i).getTitle());

			spanelVO.setJobgroupid(jobGroupMap.get(listRecruit.get(i).getJobgroupid()));
			if (listRecruit.get(i).getJobgroupid2() != null)
				spanelVO.setJobgroupid2(jobGroupMap.get(listRecruit.get(i).getJobgroupid2()));

			spanelVO.setRgbid(region1Map.get(listRecruit.get(i).getRgbid()));
			spanelVO.setRgsid(region2Map.get(listRecruit.get(i).getRgbid() + listRecruit.get(i).getRgsid()));
			// spanelVO.setImg(listRecruit.get(i).getImg());

			// 기업회원용
			spanelVO.setCname(cuserdao.selectCUser(listRecruit.get(i).getCid()).getCname());
			spanelVO.setEdu(codeMap.get(listRecruit.get(i).getEdu()));
			spanelVO.setExp(codeMap.get(listRecruit.get(i).getExp()));
			spanelVO.setPeriod(listRecruit.get(i).getPeriod());

			listPanel.add(spanelVO);
		}
		return listPanel;
	}

	// 이력서 판넬 변환
	public List<SpanelVO> convertToResumePanel(List<ResumeVO> listResume) throws Exception {
		int num = listResume.size();
		makeCodeMap();

		List<SpanelVO> listPanel = new ArrayList<SpanelVO>();
		for (int i = 0; i < num; i++) {
			SpanelVO spanelVO = new SpanelVO();
			spanelVO.setBno(listResume.get(i).getBno());
			spanelVO.setUserid(listResume.get(i).getUserid());
			spanelVO.setTitle(listResume.get(i).getTitle());

			if (listResume.get(i).getJobstateid() != null)
				spanelVO.setJobgroupid(jobGroupMap.get(listResume.get(i).getJobstateid()));

			if (listResume.get(i).getJobgroupid2() != null)
				spanelVO.setJobgroupid2(jobGroupMap.get(listResume.get(i).getJobgroupid2()));

			spanelVO.setRgbid(region1Map.get(listResume.get(i).getRgbid()));
			spanelVO.setRgsid(region2Map.get(listResume.get(i).getRgbid() + listResume.get(i).getRgsid()));
			spanelVO.setImg(listResume.get(i).getImg());

			// 개인회원용
			spanelVO.setPname(puserdao.selectPUser(listResume.get(i).getUserid()).getPname());

			listPanel.add(spanelVO);
		}
		return listPanel;
	}

	public <T extends Bnoble> List<T> intersection(List<T> list1, List<T> list2) {

		// list have no element.
		if (list1.size() == 0 || list2.size() == 0)
			return new ArrayList<T>();

		// criteria isn't selected.
		if (list1.get(0).getBno() == -1)
			return list2;
		else if (list2.get(0).getBno() == -1)
			return list1;

		List<T> list = new ArrayList<T>();
		for (T t : list1) {
			if (list2.contains(t))
				list.add(t);
		}
		return list;
	}

	@Override
	public List<SpanelVO> selectRecruitsAll() throws Exception {
		System.out.println("Performance: 01");
		List<RecruitVO> list_tmp = searchDAO.selectRecruitsAll();
		System.out.println("Performance: 02");
		List<SpanelVO> list_tmp2 = convertToRecruitPanel(list_tmp);
		System.out.println("Performance: 03");
		return list_tmp2;
	}

	@Override
	public List<SpanelVO> selectResumesAll() throws Exception {
		List<ResumeVO> list_tmp = searchDAO.selectResumesAll();
		return convertToResumePanel(list_tmp);
	}

	@Override
	public String codeToName(String scode) throws Exception {
		String code1 = scode.substring(0, 1);
		if ("J".equals(code1)) { // Job
			scode = codedao.codeToJobName(scode.substring(1));
		} else if ("R".equals(code1)) { // Region
			scode = codedao.codeToRegName(scode.substring(1));
		} else {
			scode = codedao.codeToCodName(scode);
		}
		return scode;
	}

	@Override
	public List<Integer> selectJobCode() throws Exception {
		return searchDAO.selectJobCode();
	}
}