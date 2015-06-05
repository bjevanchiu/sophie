package com.tj.sophie.job;

import com.google.gson.Gson;
import com.tj.sophie.core.Action;
import com.tj.sophie.core.Context;
import com.tj.sophie.core.IActionService;
import com.tj.sophie.core.IContext;
import com.tj.sophie.job.service.Student;

import java.util.List;

/**
 * Created by mbp on 6/4/15.
 */
public class Entry {
    public static void main(String[] args) {
        IActionService actionService = Container.getInstance().getActionService();
        IContext context = new Context("empty");
        actionService.execute(Action.create("main", "main"), context);
        List<Student> students = context.getResult("students");
        Gson gson = new Gson();
        for (Student student : students) {
            String jsonString = gson.toJson(student);
            System.out.println(jsonString);
        }
    }
}
