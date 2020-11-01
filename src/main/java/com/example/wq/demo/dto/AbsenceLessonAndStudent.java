package com.example.wq.demo.dto;

import java.util.Objects;

public class AbsenceLessonAndStudent {
    // 课程 Id
    private Long lessonId;
    // 学生手机号
    private String mobilePhone;
    // 课程开始时间
    private LocalDatetime startTime;
    // 课程时长
    private Integer duration;

    public Long getLessonId() {
        return lessonId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof AbsenceLessonAndStudent)) return false;
        AbsenceLessonAndStudent that = (AbsenceLessonAndStudent) o;
        return Objects.equals(lessonId, that.lessonId) &&
                Objects.equals(mobilePhone, that.mobilePhone) &&
                Objects.equals(startTime, that.startTime) &&
                Objects.equals(duration, that.duration);
    }

    @Override
    public int hashCode() {

        return Objects.hash(lessonId, mobilePhone, startTime, duration);
    }

    public void setLessonId(Long lessonId) {
        this.lessonId = lessonId;
    }

    @Override
    public String toString() {
        return "AbsenceLessonAndStudent{" +
                "lessonId=" + lessonId +
                ", mobilePhone='" + mobilePhone + '\'' +
                ", startTime=" + startTime +
                ", duration=" + duration +
                '}';
    }

    public String getMobilePhone() {
        return mobilePhone;
    }

    public void setMobilePhone(String mobilePhone) {
        this.mobilePhone = mobilePhone;
    }

    public LocalDatetime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalDatetime startTime) {
        this.startTime = startTime;
    }

    public Integer getDuration() {
        return duration;
    }

    public void setDuration(Integer duration) {
        this.duration = duration;
    }
}
