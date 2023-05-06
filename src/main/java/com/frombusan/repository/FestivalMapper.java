package com.frombusan.repository;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.session.RowBounds;

import com.frombusan.model.festival.Festival;
import com.frombusan.model.festival.FestivalLikes;
import com.frombusan.model.festival.FestivalMyList;


@Mapper
public interface FestivalMapper {
	
Double findFestivalLat(Long festival_id) ;
	
	Double findFestivalLng(Long festival_id) ;
		
	Festival findFestival(Long festival_id);
	
	int getTotal(String searchText);
	
	List<Festival> findAllFestival();
	//페이징
	List<Festival> findAllFestival(String searchText,RowBounds rowBounds);
	

//좋아요 기능	
	void updateFestival(Festival updateFestival);
	
	List<String> findLikesMemberId(Long festival_id);
	
	List<Map<String,Object>> findLikesById(Long festival_id);
	
	void saveLikes(FestivalLikes festivalLikes);
	
	void deleteLike(Object like_id);
//	
	//찜하기
	
	void saveMyList(FestivalMyList festivalMyList);
	
	List<Map<String,Object>> findMyListById(Long festival_id);
	
	List<String> findMyListMemberId(Long festival_id);
	
	//테이블 다른걸로!
	void deleteMyList(Object wishboard_id);
	
//
	
	
	//
	List<Map<String, Object>> findMyListByMemberId(String member_id);

}
