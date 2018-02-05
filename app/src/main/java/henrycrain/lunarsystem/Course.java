package henrycrain.lunarsystem;

import java.io.Serializable;

/**
 * The {@code Course} class represents a course that students can take at
 * UNGA-Upstate. This class stores the course's department, number, and the
 * semester in which it is offered.
 *
 * @author Henry Crain</br>
 *         111066577</br>
 *         henry.crain@stonybrook.edu
 */

@SuppressWarnings({"WeakerAccess", "unused"})
public class Course implements Serializable {

    private String department;
    private int number;
    private String semester;

    public Course(String department, int number, String semester) {
        this.department = department;
        this.number = number;
        this.semester = semester;
    }

    /**
     * @return the three-letter code of the department offering the course
     */
    public String getDepartment() {
        return department;
    }

    /**
     * @param department the three-letter code of the department to offer the
     *                   course
     */
    public void setDepartment(String department) {
        this.department = department;
    }

    /**
     * @return the course's number
     */
    public int getNumber() {
        return number;
    }

    /**
     * @param number the number to set the course's number to
     */
    public void setNumber(int number) {
        this.number = number;
    }

    /**
     * @return the semester (F or S for fall or spring, followed by the year)
     *         the course is offered
     */
    public String getSemester() {
        return semester;
    }

    /**
     * @param semester the semester to offer the course in
     */
    public void setSemester(String semester) {
        this.semester = semester;
    }
}
