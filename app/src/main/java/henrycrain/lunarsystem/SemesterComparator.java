package henrycrain.lunarsystem;

import java.util.Comparator;

/**
 * Implementation of {@code Comparator} for comparing two {@code Course} objects
 * by the semesters in which they are offered.
 *
 * @author Henry Crain</br>
 *         111066577</br>
 *         henry.crain@stonybrook.edu
 */

public class SemesterComparator implements Comparator<Course> {

    /**
     * Compares two courses by the semester in which they are offered. A course
     * is "greater then" another course if it is chronologically later and "less
     * than" another course if it is chronologically earlier. A course is "equal
     * to" another course if they are offered at the same time.
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
        char firstLeft = Character.toUpperCase(left.getSemester().charAt(0));
        char firstRight = Character.toUpperCase(right.getSemester().charAt(0));
        int yearLeft = Integer.parseInt(left.getSemester().substring(1));
        int yearRight = Integer.parseInt(right.getSemester().substring(1));

        if (yearLeft > yearRight) {
            return 1;
        } else if (yearLeft < yearRight) {
            return -1;
        } else {
            if (firstLeft == 'F' && firstRight == 'S') {
                return 1;
            } else if (firstLeft == 'S' && firstRight == 'F') {
                return -1;
            } else {
                return 0;
            }
        }
    }
}
