package henrycrain.lunarsystem;

import java.util.Comparator;

/**
 * Implementation of {@code Comparator} for comparing two {@code Course} objects
 * by their names.
 *
 * @author Henry Crain</br>
 *         111066577</br>
 *         henry.crain@stonybrook.edu
 */

public class CourseNameComparator implements Comparator<Course> {

    /**
     * Compares two courses by their names. A course is "greater than" another
     * course if its department code is greater (as per
     * {@link String#compareTo(String)}) or, if departments are the same, if its
     * number is greater. Likewise, a course is "less than" another course if
     * its department code is less or, if departments are the same, its number
     * is less. A course is "equal to" another course if it is the same course.
     *
     * @param left
     *        the first course to compare
     *
     * @param right
     *        the second course to compare
     *
     * @return 1 if left is greater than right, -1 if left is less than right,
     *         or 0 if left and right are equal
     */
    @Override
    public int compare(Course left, Course right) {
        if (left.getDepartment().compareTo(right.getDepartment()) > 0) {
            return 1;
        } else if (left.getDepartment().compareTo(right.getDepartment()) < 0) {
            return -1;
        } else {
            if (left.getNumber() > right.getNumber()) {
                return 1;
            } else if (left.getNumber() < right.getNumber()) {
                return -1;
            } else {
                return 0;
            }
        }
    }
}
