package edu.ucsb.cs56.ucsb_courses_search.controller;

import edu.ucsb.cs56.ucsb_courses_search.downloaders.CoursePageToCSV;
import edu.ucsb.cs56.ucsb_courses_search.service.CurriculumService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import edu.ucsb.cs56.ucsbapi.academics.curriculums.v1.classes.CoursePage;

import java.io.IOException;

import javax.servlet.http.HttpServletResponse;

import org.springframework.web.bind.annotation.RestController;

@RestController
public class CSVDownloadController {

    @Autowired
    private CurriculumService curriculumService;

    @GetMapping("/searchCSV")
    public void downloadCSV(@RequestParam(name = "subjectArea", required = false) String subjectArea,
            @RequestParam(name = "quarter", required = false) String quarter,
            @RequestParam(name = "courseLevel", required = false) String courseLevel,
            HttpServletResponse response) throws IOException {
        response.setContentType("text/csv");
        response.setHeader("Content-Disposition", "attachment; file=courses.csv");

        String json = curriculumService.getJSON(subjectArea, quarter, courseLevel);

        CoursePage cp = CoursePage.fromJSON(json);

        CoursePageToCSV.writeSections(response.getWriter(), cp);
    }

    @GetMapping("/searchCSDeptCSV")
    public void downloadCSV(@RequestParam(name = "quarter", required = true) String quarter,
            HttpServletResponse response) throws IOException {
        response.setContentType("text/csv");
        response.setHeader("Content-Disposition", "attachment; file=courses.csv");

        String json = curriculumService.getJSON(quarter);

        CoursePage cp = CoursePage.fromJSON(json);

        CoursePageToCSV.writeSections(response.getWriter(), cp);
    }

    @GetMapping("/searchInstructorCSV")
    public void downloadCSV(@RequestParam(name = "instructor", required = true) String instructor,
        @RequestParam(name = "quarter", required = true) String quarter,
            HttpServletResponse response) throws IOException {
        response.setContentType("text/csv");
        response.setHeader("Content-Disposition", "attachment; file=courses.csv");

        String json = curriculumService.getJSON(instructor, quarter);

        CoursePage cp = CoursePage.fromJSON(json);

        CoursePageToCSV.writeSections(response.getWriter(), cp);
    }

}