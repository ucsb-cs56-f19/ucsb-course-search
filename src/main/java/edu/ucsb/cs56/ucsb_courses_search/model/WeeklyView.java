package edu.ucsb.cs56.ucsb_courses_search.model;
import java.util.ArrayList;
import java.util.List;

import edu.ucsb.cs56.ucsb_courses_search.entity.ScheduleItem;



public class WeeklyView {
    private int counter;
    private int rowcounter;
    private int rowspan;

    public WeeklyView(){
      this.counter=0;
      this.rowcounter=2;
      this.rowspan=1;
    }
    public String[] getWeekdays(){
      String[] days = new String[]{"Monday","Tuesday","Wednesday","Thursday","Friday"};
      return days;
    }
    public String[] getTimeRange(){
      String[] timerange = new String[30];
      int index = 0;
      for(int h = 8; h <= 22; h++){
        for(int m = 0; m <= 1; m++){
          String time = "";
          if(h > 12){
            time += (h - 12);
          } else{
            time += h;
          }
          time += ":";
          if(m == 0){
            time += "00";
          } else{
            time += "30";
          }
          if(h >= 12){
            time += " PM";
          } else{
            time += " AM";
          }
          timerange[index] = time;
          index++;
        }
      }
      return timerange;
    }

    public boolean returnCol(ScheduleItem course, int count){
      String time = course.getMeetday();
      if(count == 1)
        return time.contains("M");
      if(count == 2)
        return time.contains("T");
      if(count == 3)
        return time.contains("W");
      if(count == 4)
        return time.contains("R");
      if(count == 5)
        return time.contains("F");
      return false;

    }


    public int returnStartTime(ScheduleItem course){
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

      String times = course.getMeettime();


      int hour1 = Integer.parseInt(times.substring(0,2));
      int hour2 = Integer.parseInt(times.substring(6,8));

      int minutes1 = Integer.parseInt(times.substring(3,5));
      int minutes2 = Integer.parseInt(times.substring(9,11));

      int time1 = hour1*60 + minutes1;
      int time2 = hour2*60 + minutes2;
      int differenceInMinutes = time2 - time1; //note that 11:00-11:50= 50 amd 17:30-21:20 = 3 hr 50 min = 230 min
      differenceInMinutes+=15; //everytime ends either 15,50 or 20 so add extra
      return differenceInMinutes/30;//rowspanr


    }

    public String returnDescription(ScheduleItem course){
      //returns course in span format
      //rowspan
      //     'HELLO <br/> \
      //                 HWLLO <br/> \
      //                 MW';

      return course.getClassname() + "\n" + course.getLocation() + "\n" + course.getMeettime();


    }
    public String iterateOverArray(Iterable<ScheduleItem> myclasses){
      //ITERATE OVER
      int count = this.iterateCounter();//MONDAY = 1
      //loop through myclasses and look for a colsection = to 1
      for(ScheduleItem s:myclasses){
        this.rowspan = this.returnClasslength(s);
        if(this.returnCol(s, count)){
          int maxRow = this.returnStartTime(s)+ this.rowspan;
          if(maxRow > this.rowcounter&&this.rowcounter>=this.returnStartTime(s)){
            //this.iterateRowSpan(this.returnClasslength(s));
            if(this.returnStartTime(s)!=this.rowcounter){
              return "@@@@@@@@@@@@@@@@@@@@";
            }
            return this.returnDescription(s)+"|"+this.rowspan;
            //return "STUB"+count+" "+this.rowcounter;
          }

        }

      }
      this.iterateRowSpan(1);

      return "";


    }
    public int iterateRowSpan(int newspan){
      this.rowspan = newspan;
      return this.rowspan;

    }
    public int returnRowspan(){
      return this.rowspan;
    }

    public int iterateCounter(){
       if(this.counter==5){
        //reset the iterateCounter
        this.counter =0;
        this.rowcounter+=1;
       }
      this.counter+=1;
      return this.counter;
    }

    public String returnStub(){
      return "stub";
    }
}
