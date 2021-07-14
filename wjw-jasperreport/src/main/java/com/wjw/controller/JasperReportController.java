package com.wjw.controller;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.wjw.model.CrisisEvent;
import com.wjw.model.CrisisNote;
import com.wjw.model.CrisisTaskList;
import com.wjw.utils.JasperReportUtil;

@RestController
@RequestMapping("/jasper")
public class JasperReportController {


	@GetMapping("")
	public void getReport(@RequestParam("type") String reportType, HttpServletResponse response)
			throws Exception {
		HashMap<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("notesSubreportPath", JasperReportUtil.getJasperFileDir("notes"));
		parameters.put("taskListsSubreportPath", JasperReportUtil.getJasperFileDir("taskLists"));
		parameters.put("picPath", "https://manager-dev3.everbridge.net/statics/stylesheets-new/components/images/EVBG-logo.svg");
		parameters.put("startDate", new Date().getTime());
		parameters.put("endDate", new Date().getTime());
		parameters.put("logo", "https://i.ibb.co/rvdw2hg/logo.png");
		List<CrisisEvent> fieldsList = new ArrayList<>();
		
		for (int i = 0; i < 9; i++) {
			CrisisEvent event = new CrisisEvent();
			event.setId("eventId_" + i);
			event.setTitle("eventTitle_" + i);
			event.setEventType("eventType_" + i % 3);
			event.setEventStatus(i % 2 == 0 ? "Active" : "Closed");
			event.setDescription("eventDescription_" + 1);
			event.setNotes(new ArrayList<>());
			event.setCreatedDate(new Date().getTime());
			event.setTaskLists(new ArrayList<>());
			fieldsList.add(event);
			for (int j = 0; j < i % 3 + 1; j++) {
                CrisisNote note = new CrisisNote();
                note.setId("noteId_" + i + "_" + j);
                note.setScopeId("noteScopeId_" + event.getId());
                note.setNoteContent("noteContent_" + i + "_" + j + "_內容");
                note.setCreatedDate(new Date().getTime());
                note.setCreatedName("jingwei wei");
                event.getNotes().add(note);
            }
			for (int j = 0; j < 5; j++) {
                CrisisTaskList taskList = new CrisisTaskList();
                taskList.setId("taskListId_" + i + "_" + j);
                taskList.setScopeId("taskListScopeId_" + event.getId());
                taskList.setName("taskListName_" + i + "_" + j);
                event.getTaskLists().add(taskList);
            }
		}
		Collections.sort(fieldsList);
		String jasperPath = JasperReportUtil.getJasperFileDir("MultipleEventsReport");
		if (reportType.equals("pdf")) {
			JasperReportUtil.exportToPdf(jasperPath, parameters, fieldsList, response);
		} else if (reportType.equals("html")) {
			JasperReportUtil.exportToHtml(jasperPath, parameters, fieldsList, response);
		}
	}
}
