package edu.ucsb.cs56.ucsb_courses_search.model;
import java.util.ArrayList;
import java.util.List;


import edu.ucsb.cs56.ucsb_courses_search.entity.ScheduleItem;


public class WeeklyView {
    //
    public WeeklyView(){

    }

    //function to return Day in number form , Monday=1 , takes in Course object
    public int returnColLecture(ScheduleItem course){
      String time = course.getAssociatedLectureDay();
      String lec = "";
      for(int i = 0; i < time.length(); i++)
      {
        if(time.charAt(i) != ' ')
        {
          lec += time.charAt(i);
        }
      }
      //If MW return 1, if TR return 2, if MWF return 3
      if( lec.equals("MW") ){
          return 13;
      }
      else if (lec.equals("TR")){
        return 24;
      }
      else if (lec.equals("MWF")){
        return 135;
      }
      else if (lec.equals("M")){
        return 1;
      }
      else if (lec.equals("T")){
        return 2;
      }
      else if (lec.equals("W")){
        return 3;
      }
      else if (lec.equals("R")){
        return 4;
      }
      else if (lec.equals("F")){
        return 5;
      }
      else if (lec.equals("WF")){
        return 35;
      }

      return 0;
    }
    public int returnColSection(ScheduleItem course){
      String time = course.getMeetday();
      String sec = "";
      for(int i = 0; i < time.length(); i++)
      {
        if(time.charAt(i) != ' ')
        {
          sec += time.charAt(i);
        }
      }

      //Monday corresponds to Column 1 etc
      if(sec.equals("M")){
         return 1;
       }
       else if(sec.equals("T")){
         return 2;
       }
       else if (sec.equals("W")){
         return 3;
       }
       else if (sec.equals("R")){
         return 4;
       }
       else if(sec.equals("F")){
         return 5;
       }
       else if(sec.equals("TR")){
         return 24;
       }
       else if(sec.equals("WF")){
         return 35;
      }
      else if(sec.equals("MW")){
         return 13;
       }

      return 0;
    }

    public int returnLectureStartTime(ScheduleItem course){
      //TESTED
      //RETURNS ThE ROW, where 8:00AM = 1, 8:30AM = 2

      String times = course.getAssociatedLectureTime();
      //String times = "11:00 - 11:50";
      int hour= Integer.parseInt(times.substring(0,2));

      int minutes = Integer.parseInt(times.substring(3,5));
      int time = hour*60 + minutes;//time in minutes
      if(time ==480){
        return 2;
      }
      int row = (time-480)/30+2;
      return row;
    }

    public int returnSectionStartTime(ScheduleItem course){
      //TESTED
      //RETURNS ThE ROW, where 8:00AM = 2, 8:30AM = 3

      String times = course.getMeettime();
      //String times = "11:00 - 11:50";
      int hour= Integer.parseInt(times.substring(0,2));

      int minutes = Integer.parseInt(times.substring(3,5));
      int time = hour*60 + minutes;//time in minutes
      if(time == 480){
        return 2;
      }
      int row = (time-480)/30+2;
      return row;
    }

    public int returnClasslength(ScheduleItem course){

      //Course course, String LectureOrSection
      //for rowspan nte that meetime gives the section time not lecture time in form 09:00-09:50
      // for a 75 minute class, usually 12:30-1:45 or 2-3:14, the hour is difference of 1
      //for a 50 min class, the hour is the same etc; 7-7:50
      //for a long labs, there will be first a 2 hour difference such as 14-16:30 or 14-16:50, then also check the minutes for an extra
      String times = course.getMeettime();
      //String times = "11:00 - 11:50";
      // if(LectureOrSection=="lecture"){
      //     times = course.getAssociatedLectureTime();
      // }
      // else{
      //     times = course.getMeettime();
      // }

      //Put both times in minutes

      int hour1 = Integer.parseInt(times.substring(0,2));
      int hour2 = Integer.parseInt(times.substring(6,8));

      int minutes1 = Integer.parseInt(times.substring(3,5));
      int minutes2 = Integer.parseInt(times.substring(9,11));

      int time1 = hour1*60 + minutes1;
      int time2 = hour2*60 + minutes2;
      int differenceInMinutes = time2 - time1; //note that 11:00-11:50= 50 amd 17:30-21:20 = 3 hr 50 min = 230 min
      differenceInMinutes+=15; //everytime ends either 15,50 or 20 so add extra
      return differenceInMinutes/30;//rowspanr
      //return course.getMeettime();

    }

    public String returnSpanLecture(ScheduleItem course){
      //for a long labs, there will be first a 2 hour difference such as 14-16:30 or 14-16:50, then also check the minutes for an extra
      //rowspan
      //     'HELLO <br/> \
      //                 HWLLO <br/> \
      //                 MW';

      return course.getClassname() +"<br />" + course.getAssociatedLectureDay() + "<br />" + course.getAssociatedLectureTime();
      //return "stub";
    }
    public String returnSpanSection(ScheduleItem course){
      //returns course in span format
      //rowspan
      //     'HELLO <br/> \
      //                 HWLLO <br/> \
      //                 MW';

      return course.getClassname() + "<br />" + course.getLocation() + "<br />" + course.getMeettime();

      //return "stub";
    }

    public String returnStub(){
      return "stub";
    }
}