package com.wjw.controller;

import java.util.ArrayList;
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
		List<Object> fieldsList = new ArrayList<>();
		
		for (int i = 0; i < 3; i++) {
			CrisisEvent event = new CrisisEvent();
			event.setId("id_" + i);
			event.setTitle("eventTitle_" + i);
			event.setEventType("eventType_" + i / 2);
			event.setNotes(new ArrayList<>());
			event.setTaskLists(new ArrayList<>());
			fieldsList.add(event);
			for (int j = 0; j < 4; j++) {
                CrisisNote note = new CrisisNote();
                note.setId("noteId_" + i + "_" + j);
                note.setScopeId("noteScopeId_" + event.getId());
                note.setNoteContent("noteContent_" + i + "_" + j);
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
		String jasperPath = JasperReportUtil.getJasperFileDir("events");
		if (reportType.equals("pdf")) {
			JasperReportUtil.exportToPdf(jasperPath, parameters, fieldsList, response);
		} else if (reportType.equals("html")) {
			JasperReportUtil.exportToHtml(jasperPath, parameters, fieldsList, response);
		}
	}
}
