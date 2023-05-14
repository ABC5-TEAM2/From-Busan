package com.frombusan.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.modernmt.ModernMT;
import com.modernmt.model.Memory;
import com.modernmt.model.Translation;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequestMapping("translate")
@RestController
public class TranslatorController {

	ModernMT mmt = new ModernMT("9F982625-12CF-5F66-37B2-D8B9A4B56415");

	@PostMapping("reviewsList")
    public ResponseEntity<List<Translation>> list(@RequestParam String text1
           , @RequestParam(value="reviewPlaces[]") List<String> reviewPlace
           , @RequestParam(value="reviewTitle[]") List<String> reviewTitle
    		,@RequestParam String lang) throws IOException {
		
		List<Translation> reviewList = mmt.translate("ko", lang, Arrays.asList(text1));
		List<Translation> reviewPlaces = mmt.translate("ko",lang,reviewPlace);
		List<Translation> reviewTitles = mmt.translate("ko",lang,reviewTitle);
		List<Translation> joined = new ArrayList<>();
		joined.addAll(reviewList);
		joined.addAll(reviewPlaces);
		joined.addAll(reviewTitles);
        return ResponseEntity.ok(joined);
    }
	
	@PostMapping("tourInfo")
    public ResponseEntity<List<Translation>> tourInfo(@RequestParam String itemcntnts
    		,@RequestParam String lang
    		,@RequestParam String hldyInfo
    		,@RequestParam String usageAmount
    		,@RequestParam String trfcInfo
    		,@RequestParam String usage_day_week_and_time
    		) throws IOException {
		
		List<Translation> itemcntnt = mmt.translate("ko", lang, Arrays.asList(itemcntnts));
		List<Translation> hldyInfos = mmt.translate("ko", lang, Arrays.asList(hldyInfo));
		List<Translation> usage_day = mmt.translate("ko", lang, Arrays.asList(usage_day_week_and_time));
		List<Translation> usageAmounts = mmt.translate("ko", lang, Arrays.asList(usageAmount));
		List<Translation> trfcInfos = mmt.translate("ko", lang, Arrays.asList(trfcInfo));
		List<Translation> joined = new ArrayList<>();
		joined.addAll(itemcntnt);
		joined.addAll(hldyInfos);
		joined.addAll(usage_day);
		joined.addAll(usageAmounts);
		joined.addAll(trfcInfos);
		
        return ResponseEntity.ok(joined);
    }
	
	@PostMapping("fesInfo")
    public ResponseEntity<List<Translation>> fesInfo(@RequestParam String itemcntnts
    		,@RequestParam String lang
    		,@RequestParam String middle_size_rm1
    		,@RequestParam String usageAmount
    		,@RequestParam String trfcInfo
    		) throws IOException {
		
		List<Translation> itemcntnt = mmt.translate("ko", lang, Arrays.asList(itemcntnts));
		List<Translation> middle_size = mmt.translate("ko", lang, Arrays.asList(middle_size_rm1));
		List<Translation> usageAmounts = mmt.translate("ko", lang, Arrays.asList(usageAmount));
		List<Translation> trfcInfos = mmt.translate("ko", lang, Arrays.asList(trfcInfo));
		List<Translation> joined = new ArrayList<>();
		joined.addAll(itemcntnt);
		joined.addAll(usageAmounts);
		joined.addAll(trfcInfos);
		joined.addAll(middle_size);
		
        return ResponseEntity.ok(joined);
    }
	
	
	@PostMapping("Lists")//명소*축제 리스트
    public ResponseEntity<List<Translation>> lists(
           @RequestParam(value="itemcntnts[]") List<String> itemcntnts
           , @RequestParam(value="main_title[]") List<String> main_title
    		,@RequestParam String lang) throws IOException {
		
		List<Translation> itemcntnt = mmt.translate("ko",lang,itemcntnts);
		List<Translation> mainTitle = mmt.translate("ko",lang,main_title);
		List<Translation> joined = new ArrayList<>();
		joined.addAll(mainTitle);
		joined.addAll(itemcntnt);
        return ResponseEntity.ok(joined);
    }
}
