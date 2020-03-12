package edu.ucsb.cs56.ucsb_courses_search.controller;

import edu.ucsb.cs56.ucsb_courses_search.service.FinalService;

import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;


import edu.ucsb.cs56.ucsb_courses_search.service.CalendarService;
import edu.ucsb.cs56.ucsbapi.academics.curriculums.v1.classes.QuarterDeadlines;
import edu.ucsb.cs56.ucsbapi.academics.curriculums.v1.classes.FinalPage;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import edu.ucsb.cs56.ucsb_courses_search.entity.ScheduleItem;
import edu.ucsb.cs56.ucsb_courses_search.repository.ScheduleItemRepository;
import edu.ucsb.cs56.ucsb_courses_search.service.MembershipService;
import org.springframework.beans.factory.annotation.Qualifier;


import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.LinkedHashSet;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@ResponseStatus(HttpStatus.FORBIDDEN)
class AccessForbiddenException extends RuntimeException {
}

@Controller
public class CourseController {

    private Logger logger = LoggerFactory.getLogger(CourseController.class);

    @Autowired(required = false)
    private FinalService finalService;

    @Autowired
    private ScheduleItemRepository scheduleItemRepository;

    @Autowired 
    private CalendarService calendarservice;

    @Autowired
    private MembershipService membershipService;

    @Autowired
    public CourseController(ScheduleItemRepository sheduleItemRepository, MembershipService membershipService) {
        this.scheduleItemRepository = scheduleItemRepository;
	    this.membershipService = membershipService;
    }

    @GetMapping("/courseschedule")
    public String index(Model model, OAuth2AuthenticationToken token) throws AccessForbiddenException {
        
        logger.info("Inside /courseschedule controller method CourseController#index");
        logger.info("model=" + model + " token=" + token);

        String json_ = calendarservice.getJSON();
        QuarterDeadlines quarterdeadline = QuarterDeadlines.fromJSON(json_);
        quarterdeadline.format();


        if (token!=null && this.membershipService.isMember(token)) {
            String uid = token.getPrincipal().getAttributes().get("sub").toString();
            logger.info("uid="+uid);
            Iterable<ScheduleItem> myclasses = scheduleItemRepository.findByUid(uid);
            Set<FinalPage> myfinals = new LinkedHashSet<FinalPage>();
            for(ScheduleItem scheduleItem : myclasses){
                String json = finalService.getJSON(scheduleItem.getEnrollCode(), scheduleItem.getQuarter());
                logger.info(json);
                FinalPage fp = FinalPage.fromJSON(json);
                fp.setCourseName(scheduleItem.getClassname());
                myfinals.add(fp);
            }
            model.addAttribute("myfinals", myfinals);
            logger.info("scheduleItemRepository="+scheduleItemRepository);
            // logger.info("there are " + myclasses.size() + " courses that match uid: " + uid);

            //how is this getting the info to index.html
            model.addAttribute("myclasses", myclasses);
            model.addAttribute ("calendar", quarterdeadline);
        } else {
            //ArrayList<Course> emptyList = new ArrayList<Course>();
            //model.addAttribute("myclasses", emptyList);
	    //org.springframework.security.access.AccessDeniedException("403 returned");
	        throw new AccessForbiddenException();
        }
        return "courseschedule/index";
    }
    @PostMapping("/courseschedule/add")
    public String add(ScheduleItem scheduleItem, 
                        Model model,
                        @RequestParam String lecture_classname,
                        @RequestParam String lecture_enrollCode,
                        @RequestParam String lecture_uid,
                        @RequestParam String lecture_professor,
                        @RequestParam String lecture_meettime,
                        @RequestParam String lecture_meetday,
                        @RequestParam String lecture_location,
                        @RequestParam String lecture_quarter) {
        logger.info("Hello!\n");
        logger.info("ScheduleItem's uid: " + scheduleItem.getUid());
        logger.info("ScheduleItem = " + scheduleItem);                   
        scheduleItemRepository.save(scheduleItem);

        ScheduleItem primary = new ScheduleItem();
        primary.setClassname(lecture_classname);
        primary.setEnrollCode(lecture_enrollCode);
        primary.setUid(lecture_uid);
        primary.setProfessor(lecture_professor);
        primary.setMeetday(lecture_meetday);
        primary.setMeettime(lecture_meettime);
        primary.setLocation(lecture_location);
        primary.setQuarter(lecture_quarter);
        logger.info("primary = " + primary); 
        scheduleItemRepository.save(primary);

        Iterable<ScheduleItem> myclasses = scheduleItemRepository.findByUid(scheduleItem.getUid());
        Set<FinalPage> myfinals = new LinkedHashSet<FinalPage>();
        for(ScheduleItem si : myclasses){
            String json = finalService.getJSON(si.getEnrollCode(), si.getQuarter());
            logger.info(json);
            FinalPage fp = FinalPage.fromJSON(json);
            fp.setCourseName(si.getClassname());
            myfinals.add(fp);
        }
        model.addAttribute("myfinals", myfinals);

        model.addAttribute("myclasses", scheduleItemRepository.findByUid(scheduleItem.getUid()));

        String json_ = calendarservice.getJSON();
        QuarterDeadlines quarterdeadline = QuarterDeadlines.fromJSON(json_);
        quarterdeadline.format();

        model.addAttribute ("calendar", quarterdeadline);

        return "courseschedule/index";
    }


    @PostMapping("/courseschedule/addLecture")
    public String addLecture(ScheduleItem scheduleItem, Model model) {
        logger.info("Hello!\n");
        logger.info("ScheduleItem's uid: " + scheduleItem.getUid());
        logger.info("ScheduleItem = " + scheduleItem);                   
        scheduleItemRepository.save(scheduleItem);

        Iterable<ScheduleItem> myclasses = scheduleItemRepository.findByUid(scheduleItem.getUid());
        Set<FinalPage> myfinals = new LinkedHashSet<FinalPage>();
        for(ScheduleItem si : myclasses){
            String json = finalService.getJSON(si.getEnrollCode(), si.getQuarter());
            logger.info(json);
            FinalPage fp = FinalPage.fromJSON(json);
            fp.setCourseName(si.getClassname());
            myfinals.add(fp);
        }
        model.addAttribute("myfinals", myfinals);

        model.addAttribute("myclasses", scheduleItemRepository.findByUid(scheduleItem.getUid()));

        String json_ = calendarservice.getJSON();
        QuarterDeadlines quarterdeadline = QuarterDeadlines.fromJSON(json_);
        quarterdeadline.format();

        model.addAttribute ("calendar", quarterdeadline);
        
        return "courseschedule/index";
    }

}