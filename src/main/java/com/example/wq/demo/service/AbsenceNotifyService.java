package com.example.wq.demo.service;

import com.example.wq.demo.dto.AbsenceLessonAndStudent;
import com.example.wq.demo.executor.PriorityThreadPoolExecutor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;


import java.util.*;
import java.util.concurrent.*;

public class AbsenceNotifyService {
    private static final Logger LOGGER = LoggerFactory.getLogger(AbsenceNotifyService.class);

    

    @Autowired
    NotificationService notificationService;

    @Autowired
    VoiceCallService voiceCallService;

    /**
     * 上课缺勤提醒方法
     * @throws ExecutionException
     * @throws InterruptedException
     */
    public void absenceNotify() throws ExecutionException, InterruptedException {
        PriorityThreadPoolExecutor executor = new PriorityThreadPoolExecutor(5, 10,
                200, TimeUnit.MILLISECONDS,
                new PriorityBlockingQueue<Runnable>(100));
        //获取缺席学生列表
        List<AbsenceLessonAndStudent> absenceLessonList = getAbsenceLessonAndStudent();
        //语音提醒任务执行结果map集合
        Map<AbsenceLessonAndStudent, Future<Boolean>> resultMap = new HashMap<>();
        for (AbsenceLessonAndStudent absenceLesson : absenceLessonList) {
            //提交语音提醒任务
            AbsenceNotifyTask absenceNotifyTask = new AbsenceNotifyTask(absenceLesson,absenceLesson.getDuration());
            Future<Boolean> result = executor.submit(absenceNotifyTask);
            resultMap.put(absenceLesson, result);
        }
        executor.shutdown();
        //迭代遍历Map集合
        Set set = resultMap.entrySet();
        Iterator it = set.iterator();
        while (it.hasNext()) {
            Map.Entry entry = (Map.Entry) it.next();
            AbsenceLessonAndStudent absenceLessonAndStudent = (AbsenceLessonAndStudent) entry.getKey();
            Future<Boolean> future = (Future<Boolean>) entry.getValue();
            if (future.get() == false){
                //获得系统的时间，单位为毫秒,转换为妙
                long totalMilliSeconds = System.currentTimeMillis();
                long totalSeconds = totalMilliSeconds / 1000;
                if(absenceLessonAndStudent.getDuration() == 30 && (totalSeconds - absenceLessonAndStudent.getStartTime() > 5*60)){
                    //30分钟的课程、课程开始5分钟仍然没有成功呼出，则提交给人工服务
                   notifyAlert(absenceLessonAndStudent);
                } else if (absenceLessonAndStudent.getDuration() == 50 && (totalSeconds - absenceLessonAndStudent.getStartTime() > 10*60)){
                    //50分钟的课程、课程开始10分钟仍然没有成功呼出，则提交给人工服务
                    notifyAlert(absenceLessonAndStudent);
                }
            }
        }


    }

    /**
     * 人工语音呼叫方法
     * @param absenceLessonAndStudent
     */

    public void notifyAlert(AbsenceLessonAndStudent absenceLessonAndStudent){
        if (hasInClass(absenceLessonAndStudent.getMobilePhone(), absenceLessonAndStudent.getLessonId()) == false) {
            //人工语音服务
            NotificationService.notifyAlert(absenceLessonAndStudent.getMobilePhone(), "请及时参加课程");
        }
    }
}

/**
 * 拨打语音提醒task
 */
class AbsenceNotifyTask implements Callable<Boolean>, Comparable<AbsenceNotifyTask> {
    private Integer prority;
    private AbsenceLessonAndStudent absenceLessonAndStudent;

    public AbsenceNotifyTask(AbsenceLessonAndStudent absenceLessonAndStudent,Integer prority;) {
        this.absenceLessonAndStudent = absenceLessonAndStudent;
        this.prority = prority;
    }

    @Override
    public Boolean call() {
        try {
            //判断是否已经上课、没有则打电话
            if (hasInClass(absenceStudent.getMobilePhone(), absenceStudent.getLessonId()) == false) {
                //语音服务
                VoiceCallService.absenceNotify(mobile_phone);
            }

        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return null;
    }
    @Override
    public int compareTo(AbsenceNotifyTask o) {

        return prority.compareTo(o.prority);
    }
}