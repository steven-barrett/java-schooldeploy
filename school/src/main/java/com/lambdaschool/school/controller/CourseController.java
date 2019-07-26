package com.lambdaschool.school.controller;

import com.lambdaschool.school.model.Course;
import com.lambdaschool.school.model.ErrorDetail;
import com.lambdaschool.school.service.CourseService;
import com.lambdaschool.school.view.CountStudentsInCourses;
import io.swagger.annotations.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;

@RestController
@RequestMapping(value = "/courses")
public class CourseController
{
    private static final Logger logger = LoggerFactory.getLogger(CourseController.class);

    @Autowired
    private CourseService courseService;

    @ApiOperation(value = "returns all Courses with Paging Ability",
            responseContainer = "List")
    @ApiImplicitParams({@ApiImplicitParam(name = "page",
            dataType = "integer",
            paramType = "query",
            value = "Results page you want to retrieve (0..N)"), @ApiImplicitParam(name = "size",
            dataType = "integer",
            paramType = "query",
            value = "Number of records per page."), @ApiImplicitParam(name = "sort",
            allowMultiple = true,
            dataType = "string",
            paramType = "query",
            value = "Sorting criteria in the format: property(,asc|desc). " + "Default sort order is ascending. " + "Multiple sort criteria are supported.")})

    @GetMapping(value = "/courses", produces = {"application/json"})
    public ResponseEntity<?> listAllCourses(HttpServletRequest request, @PageableDefault(page = 0, size = 3) Pageable pageable)
    {
        logger.info(request.getMethod() + " " + request.getRequestURI() + " accessed");

        ArrayList<Course> myCourses = courseService.findAll(pageable);
        return new ResponseEntity<>(myCourses, HttpStatus.OK);
    }

    @ApiOperation(value = "Gets the count of students in each course",
            response = CountStudentsInCourses.class)
    @ApiResponses(value = {@ApiResponse(code = 201,
            message = "Student Found",
            responseContainer = "List",
            response = CountStudentsInCourses.class), @ApiResponse(code = 404,
            message = "Could not determine count",
            response = ErrorDetail.class)})

    @GetMapping(value = "/studcount", produces = {"application/json"})
    public ResponseEntity<?> getCountStudentsInCourses(HttpServletRequest request)
    {
        logger.info(request.getMethod() + " " + request.getRequestURI() + " accessed");

        ArrayList<CountStudentsInCourses> list = courseService.getCountStudentsInCourse();
        return new ResponseEntity<>(list, HttpStatus.OK);
    }


    @ApiOperation(value = "Looks up a course by its ID",
            response = Course.class)
    @GetMapping(value = "/course/{courseId}",
            produces = {"application/json"})
    public ResponseEntity<?> findCourseById(
            @PathVariable
                    Long courseId)
    {
        Course c = courseService.findCourseById(courseId);
        return new ResponseEntity<>(c, HttpStatus.OK);
    }

    @PostMapping(value = "/course/add",
            consumes = {"application/json"},
            produces = {"application/json"})
    public ResponseEntity<?> addNewCourse(@Valid
                                          @RequestBody
                                                  Course newCourse) throws URISyntaxException
    {
        newCourse = courseService.save(newCourse);

        // set the location header for the newly created resource
        HttpHeaders responseHeaders = new HttpHeaders();

        URI newCourseUri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{courseid}").buildAndExpand(newCourse.getCourseid()).toUri();
        responseHeaders.setLocation(newCourseUri);

        return new ResponseEntity<>(null, responseHeaders, HttpStatus.CREATED);
    }

    @ApiOperation(value = "Deletes an existing course",
            response = void.class)
    @ApiResponses(value = {@ApiResponse(code = 201,
            message = "Course Deleted",
            response = void.class), @ApiResponse(code = 500,
            message = "Error deleting course",
            response = ErrorDetail.class)})
    @DeleteMapping("/courses/{courseid}")
    public ResponseEntity<?> deleteCourseById(
            @ApiParam(value = "The course id",
                    required = true,
                    example = "1")
                   @PathVariable long courseid,
                    HttpServletRequest request)
    {
        logger.info(request.getMethod() + " " + request.getRequestURI() + " accessed");

        courseService.delete(courseid);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
