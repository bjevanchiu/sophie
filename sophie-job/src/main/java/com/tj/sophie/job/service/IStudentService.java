package com.tj.sophie.job.service;

import com.tj.sophie.core.IService;

import java.util.List;

/**
 * Created by mbp on 6/4/15.
 */
public interface IStudentService extends IService {
    List<Student> getStudents();
}
