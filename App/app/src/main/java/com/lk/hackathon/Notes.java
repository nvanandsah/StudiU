package com.lk.hackathon;

public class Notes {
    int id;
    String name;
    String url;
    String logo;
    int studentClass;
    String subject;
    int stars;

    public Notes(int id, String name, String url, String logo, int studentClass, String subject, int stars) {
        this.id = id;
        this.name = name;
        this.url = url;
        this.logo = logo;
        this.studentClass = studentClass;
        this.subject = subject;
        this.stars = stars;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public int getStudentClass() {
        return studentClass;
    }

    public void setStudentClass(int studentClass) {
        this.studentClass = studentClass;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public int getStars() {
        return stars;
    }

    public void setStars(int stars) {
        this.stars = stars;
    }
}
