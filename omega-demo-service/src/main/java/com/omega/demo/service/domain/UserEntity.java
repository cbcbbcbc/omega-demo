package com.omega.demo.service.domain;

import com.omega.demo.service.dao.UserDao;
import com.omega.demo.api.GUID;
import com.omega.demo.api.bean.User;
import com.omega.framework.task.TaskConsumer;
import com.omega.framework.task.TaskQueue;
import com.omega.framework.task.bean.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

/**
 * Created by wuzhengtao on 16/12/6.
 */

@Service
public class UserEntity {

    @Autowired
    private UserDao userDao;

    @Autowired
    private TaskQueue taskQueue;

    public User getById(String id) {
        return userDao.getById(id);
    }

    @Transactional
    public void create(User user) {
        userDao.create(user);

        Task task = new Task("sendEmail");
        task.data("userId", "2");
        taskQueue.addTask(task);
    }

    @TaskConsumer("sendEmail")
    public void sendEmail(Task task) {
        // check if already executed

        Map<String, String> dataMap = task.getDataMap();
        System.out.println("Sending email to user " + dataMap.get("userId"));
    }

}
