package com.example.medical.db;

/**
 * Created by meanheffry on 10/27/14.
 */
public class Pill {
    private long id;
    private String lastModified;
    private String tube;
    private String name;
    private String dose;
    private String load;

    public String getName(){
        return this.name;
    }
    public void setName(String name){
        this.name = name;
    }
    public String getDose(){
        return this.dose;
    }
    public void setDose(String dose){
        this.dose = dose;
    }
    public long getId(){
        return id;
    }
    public void setId(long id){
        this.id = id;
    }
    public String getTube(){
        return this.tube;
    }
    public void setTube(String tube){
        this.tube = tube;
    }
    public void setLoad(String load) {
        this.load = load;
    }
    public String getLoad() {
        return load;
    }
    public String getLastModified() {
        return lastModified;
    }
    public void setLastModified(String lastModified) {
        this.lastModified = lastModified;
    }

    @Override
    public String toString() {
        return name; //+ ", Dosage "+dose+",with "+ load +" pills in tube"+ tube;
    }
}
