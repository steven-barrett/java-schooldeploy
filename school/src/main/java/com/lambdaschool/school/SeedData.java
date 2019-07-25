package com.lambdaschool.school;

import com.lambdaschool.school.model.*;
import com.lambdaschool.school.repository.*;
import com.lambdaschool.school.service.CourseService;
import com.lambdaschool.school.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;

@Transactional
@Component
public class SeedData implements CommandLineRunner
{

    RoleRepository rolerepos;
    UserRepository userrepos;
    QuoteRepository todorepos;
    @Autowired
    private StudentService studentService;

    @Autowired
    private CourseService courseService;

    @Autowired
    private InstructorRepository instructrepos;

    public SeedData(RoleRepository rolerepos, UserRepository userrepos, QuoteRepository todorepos) {
        this.rolerepos = rolerepos;
        this.userrepos = userrepos;
        this.todorepos = todorepos;
    }

    @Override
    public void run(String[] args) throws Exception {
        Role r1 = new Role("admin");
        Role r2 = new Role("user");

        rolerepos.save(r1);
        rolerepos.save(r2);

        ArrayList<UserRoles> users = new ArrayList<>();
        users.add(new UserRoles(new User(), r2));
        User u1 = new User("barnbarn", "ILuvM4th!", users);
        u1.getQuotes().add(new Quote("Live long and prosper", u1));
        u1.getQuotes().add(new Quote("The enemy of my enemy is the enemy I kill last", u1));
        u1.getQuotes().add(new Quote("Beam me up", u1));
        userrepos.save(u1);

        ArrayList<UserRoles> admins = new ArrayList<>();
        admins.add(new UserRoles(new User(), r1));
        admins.add(new UserRoles(new User(), r2));
        User u2 = new User("admin", "password", admins);
        u2.getQuotes().add(new Quote("A creative man is motivated by the desire to achieve, not by the desire to beat others", u2));
        u2.getQuotes().add(new Quote("The question isn't who is going to let me; it's who is going to stop me.", u2));
        userrepos.save(u2);

        users = new ArrayList<>();
        users.add(new UserRoles(new User(), r2));
        User u3 = new User("Bob", "password", users);
        userrepos.save(u3);

        users = new ArrayList<>();
        users.add(new UserRoles(new User(), r2));
        User u4 = new User("Jane", "password", users);
        userrepos.save(u4);

        Instructor i1 = new Instructor("Sally");

        Instructor i2 = new Instructor("Lucy");

        Instructor i3 = new Instructor("Charlie");

        instructrepos.save(i1);
        instructrepos.save(i2);
        instructrepos.save(i3);


        Course c1 = new Course("Data Science", i1);

        Course c2 = new Course("JavaScript", i1);

        Course c3 = new Course("Node.js", i1);

        Course c4 = new Course("Java Back End", i2);

        Course c5 = new Course("Mobile IOS", i2);

        Course c6 = new Course("Mobile Android", i3);

        c1 = courseService.save(c1);
        c2 = courseService.save(c2);
        c3 = courseService.save(c3);
        c4 = courseService.save(c4);
        c5 = courseService.save(c5);
        c6 = courseService.save(c6);

        Student s1 = new Student("John");
        s1.getCourses().add(courseService.findCourseById(c1.getCourseid()));
        s1.getCourses().add(courseService.findCourseById(c4.getCourseid()));

        Student s2 = new Student("Julian");
        s2.getCourses().add(courseService.findCourseById(c2.getCourseid()));

        Student s3 = new Student("Mary");
        s3.getCourses().add(courseService.findCourseById(c3.getCourseid()));
        s3.getCourses().add(courseService.findCourseById(c1.getCourseid()));
        s3.getCourses().add(courseService.findCourseById(c6.getCourseid()));

        studentService.save(s1);
        studentService.save(s2);
        studentService.save(s3);

    }
}