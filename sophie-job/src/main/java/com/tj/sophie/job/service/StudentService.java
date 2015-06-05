package com.tj.sophie.job.service;

import com.tj.sophie.core.AbstractService;
import com.tj.sophie.guice.Binding;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by mbp on 6/4/15.
 */
@Binding(from = IStudentService.class, to = StudentService.class)
public class StudentService extends AbstractService implements IStudentService {
    private List<Student> students;

    @Override
    protected void onInitialize() {
        List<Student> students = new ArrayList<>();
        for (int i = 0; i < 8; i++) {
            Student student = new Student();
            student.setName(Integer.toString(i));
            student.setAge(i);
            students.add(student);
        }
        this.students = students;
    }

    @Override
    public List<Student> getStudents() {
        return this.students;
    }
}
