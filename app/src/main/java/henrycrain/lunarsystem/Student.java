package henrycrain.lunarsystem;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * The {@code Student} class represents a student at UNGA-Upstate. This class
 * stores the student's WebID and a list of the courses they are taking.
 *
 * @author Henry Crain</br>
 *         111066577</br>
 *         henry.crain@stonybrook.edu
 */

@SuppressWarnings("WeakerAccess")
public class Student implements Serializable {

    private String webID;
    private ArrayList<Course> courses = new ArrayList<>();

    public Student(String webID) {
        this.webID = webID;
    }

    /**
     * @return the student's WebID
     */
    public String getWebID() {
        return webID;
    }

    /**
     * @param webID the student's new WebID
     */
    @SuppressWarnings("unused")
    public void setWebID(String webID) {
        this.webID = webID;
    }

    /**
     * @return an ArrayList of the courses the student is taking
     */
    public ArrayList<Course> getCourses() {
        return courses;
    }

    /**
     * @param course the course to add the student to
     */
    public void addCourse(Course course) {
        courses.add(course);
    }

    /**
     * @param course the course to remove the student from
     */
    public void removeCourse(Course course) {
        courses.remove(course);
    }
}
