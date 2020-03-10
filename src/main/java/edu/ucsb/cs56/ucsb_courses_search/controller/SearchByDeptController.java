package edu.ucsb.cs56.ucsb_courses_search.controller;

import edu.ucsb.cs56.ucsb_courses_search.service.QuarterListService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import edu.ucsb.cs56.ucsb_courses_search.service.CourseHistoryService;
import edu.ucsb.cs56.ucsb_courses_search.service.CurriculumService;
import edu.ucsb.cs56.ucsb_courses_search.service.FinalExamService;
import edu.ucsb.cs56.ucsb_courses_search.model.result.CourseListingRow;
import edu.ucsb.cs56.ucsb_courses_search.model.result.CourseOffering;
import edu.ucsb.cs56.ucsb_courses_search.model.result.YearOfCourseEnrollment;//COPY THIS
import edu.ucsb.cs56.ucsb_courses_search.model.search.SearchByDept;
import edu.ucsb.cs56.ucsbapi.academics.curriculums.v1.classes.CoursePage;
import edu.ucsb.cs56.ucsbapi.academics.curriculums.utilities.Quarter; //ALSO COPY THIS
import edu.ucsb.cs56.ucsb_courses_search.service.CourseHistoryService;
import java.util.*; //modify this so that it has access to all the arraylists and hashmaps

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/*

  This comment is for Juan so he knows what to copy into the other controllers.
  Lines:
  13
  16
  *18 (Just modify java.util.List to java.util.*)
  71-91
  95
  101-171

*/

@Controller
public class SearchByDeptController {
  //this is for setting how many years to go back in time.
  static int YEARS_OF_HISTORY = 2;

    private Logger logger = LoggerFactory.getLogger(SearchByDeptController.class);

    @Autowired
    private CurriculumService curriculumService;

    @Autowired
    private FinalExamService finalExamService;

    @Autowired
    private QuarterListService quarterListService;

    @Autowired
    private CourseHistoryService courseHistoryService;

    @GetMapping("/search/bydept")
    public String instructor(Model model, SearchByDept searchByDept) {
        model.addAttribute("searchByDept", new SearchByDept());
        model.addAttribute("quarters", quarterListService.getQuarters());
        return "search/bydept/search";
    }

    @GetMapping("/search/bydept/results")
    public String search(@RequestParam(name = "dept", required = true) String dept,
            @RequestParam(name = "quarter", required = true) String quarter,
            @RequestParam(name = "courseLevel", required = true) String courseLevel, Model model,
            SearchByDept searchByDept) {
        model.addAttribute("dept", dept);
        model.addAttribute("quarter", quarter);
        model.addAttribute("quarters", quarterListService.getQuarters());
        model.addAttribute("courseLevel", courseLevel);

        String json = curriculumService.getJSON(dept, quarter, courseLevel);
        CoursePage cp = CoursePage.fromJSON(json);

        List<CourseOffering> courseOfferings = CourseOffering.fromCoursePage(cp);

        List<CourseListingRow> rows = CourseListingRow.fromCourseOfferings(courseOfferings);
        HashMap<String, ArrayList<YearOfCourseEnrollment>> enrollmentClasses = courseHistoryService.getEnrollmentData(courseOfferings);

<<<<<<< HEAD
=======
        rows = finalExamService.assignFinalExams(rows);
>>>>>>> e4b5cd400cabdd12469ad17f1684568f6ab19183
        
        Comparator<CourseListingRow> byCourseId = (r1, r2) -> {
            return r1.getCourse().getCourseId().compareTo(r2.getCourse().getCourseId());
        };

        Collections.sort(rows, byCourseId);

        model.addAttribute("cp", cp);
        model.addAttribute("rows", rows);
        model.addAttribute("eh", enrollmentClasses); //BUT also copy this

        return "search/bydept/results";
    }

    //Copy this method
    /**
      This will extract the year's enrollment history from the database based on the quarter given

      @return returns a YearOfCourses object containing enro
    */
    public YearOfCourseEnrollment extractYearHistory(String quarter, String courseID, String presentQuarter)
    {
      String year = "";
      String[] enrollmentNums = new String[4];

      int currentQuarter = Integer.parseInt(quarter.substring(quarter.length()-1)); //get the current quarter

      String currentYear = quarter.substring(0, quarter.length()-1);
      if(currentQuarter == 4) //if this is the fall quarter then year is "current/current+1"
      {
        year = currentYear + "/" + Integer.toString(Integer.parseInt(currentYear) + 1);
      }
      else //this means any other quarter so year is "current-1/current"
      {
        year = Integer.toString(Integer.parseInt(currentYear) - 1) + "/" + currentYear;
      }
      //first set to fall quarter of the current year
      //System.out.println(quarter);
      Quarter stepQuarter = new Quarter(quarter);
      while(stepQuarter.getQ() != "F")
      {
        stepQuarter.decrement();
      }

      //now that we're at the beginning of the "year" lets go through each quarter and extract enrollment numbers
      for(int i = 0 ; i < 4; i++)
      {
        if(Integer.parseInt(presentQuarter) < Integer.parseInt(stepQuarter.getYYYYQ())) //First lets check if the quarter is in the future.
        {
          enrollmentNums[i] = "TBD";

          continue;
        }
        else
        {
          //now the quarter is present or past. lets get enrollment numbers
          //thse two lines will get the course info for the selected quarter and course id and parse it into a readable thing
          String json = curriculumService.getCourse(courseID,stepQuarter.getValue());
          CoursePage cp = CoursePage.fromJSON(json);

          if(cp.getClasses().size() <= 0) //this means there were no classes held that quarter
          {
            enrollmentNums[i] = "-";

          }
          else //now this is a valid class lets get the enrollment numbers
          {
            //if there's multiple courses, we need a way to loop through them
            int enrolled = 0;
            List<CourseOffering> courseOfferings = CourseOffering.fromCoursePage(cp);
            for(CourseOffering offer : courseOfferings)
            {
              enrolled += offer.getPrimary().getEnrolledTotal();
            }
            //now we have the enrollment numbers let's put them into a string and save
            enrollmentNums[i] = Integer.toString(enrolled);
          }
        }
        stepQuarter.increment();
      }
      //System.out.println(year + " | " + enrollmentNums[0] + " | " + enrollmentNums[1] + " | " + enrollmentNums[2] + " | " + enrollmentNums[3]);
      return new YearOfCourseEnrollment(year, enrollmentNums[0],enrollmentNums[1],enrollmentNums[2],enrollmentNums[3]);

    }

}
