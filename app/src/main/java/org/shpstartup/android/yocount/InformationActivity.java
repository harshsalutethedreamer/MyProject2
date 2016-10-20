package org.shpstartup.android.yocount;

/**
 * Created by harshgupta on 08/09/16.
 */
public class InformationActivity {
    private int _id;
    private int totalcount;
    private String topicname;
    private String description;
    private String ndate;
    private int nspecial;

//    public static final InformationActivity[] informationActivity={
//      new InformationActivity(1,"SELFIE","JANUARY 2010",123453,"TIMES SELFIE TAKEN",1),
//              new InformationActivity(2,"APPLE","MARCH 2010",763,"APPLE EATEN BY ME",2),
//              new InformationActivity(3,"MOVIES","JANUARY 2015",145,"MOVIES WATCHED BY ME",3),
//            new InformationActivity(4,"ONO","JANUARY 2016",145,"YOLO LIVES Once",4),
//            new InformationActivity(5,"FUNNY","JANUARY 2010",13432,"TIMES SELFIE TAKEN",5),
//            new InformationActivity(6,"MOZO","MARCH 2010",355564,"APPLE EATEN BY ME",6),
//            new InformationActivity(7,"MOZO","MARCH 2010",355564,"APPLE EATEN BY ME",7),
//            new InformationActivity(8,"MOZO","MARCH 2010",355564,"APPLE EATEN BY ME",8)
//    };

    public InformationActivity(int _id,String topicname, String ndate, int totalcount, String description, int special){
        this._id=_id;
        this.totalcount=totalcount;
        this.topicname=topicname;
        this.description=description;
        this.ndate=ndate;
        nspecial=special;

    }

    public int getId() {
        return _id;
    }

    public Integer getTotalcount() {
        return totalcount;
    }

    public String getDescription() {
        return description;
    }

    public String getTopicname() {
        return topicname;
    }

    public String getNdate(){
        return ndate;
    }

    public int getNspecial() {
        return nspecial;
    }


    public String toString(){
        return this.topicname;
    }
}
