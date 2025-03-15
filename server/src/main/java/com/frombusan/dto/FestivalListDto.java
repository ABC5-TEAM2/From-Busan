package com.frombusan.dto;

import com.frombusan.model.festival.Festival;
import com.frombusan.util.PageNavigator;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class FestivalListDto {
    private List<Festival> festivalList;
    private PageNavigator navi;

    public static final int countPerPage = 9;
    public static final int pagePerGroup = 5;
}
