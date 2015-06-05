package com.tj.sophie.job.handler;

import com.google.inject.Inject;
import com.tj.sophie.core.AbstractHandler;
import com.tj.sophie.core.Action;
import com.tj.sophie.core.IContext;
import com.tj.sophie.guice.Handler;
import com.tj.sophie.job.service.IStudentService;
import com.tj.sophie.job.service.Student;

import java.util.List;

/**
 * Created by mbp on 6/4/15.
 */
@Handler
public class MainHandler extends AbstractHandler {

    @Inject
    private IStudentService studentService;

    @Override
    protected void onExecute(IContext context) {
        List<Student> students = studentService.getStudents();
        context.setResult("students", students);
    }

    @Override
    protected void onInitialize() {
        this.setAction(Action.create("main", "main"));
    }
}
