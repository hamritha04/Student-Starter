package com.university;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class RegistrationServiceTest {

    @Test
    void shouldRegisterStudentForValidCourse() {
        Student student = new Student("S101", "Anita");

        Course course = new Course(
                "CS301", "Data Structures", 4, 30, true, null);

        boolean result = new RegistrationService().register(student, course);

        assertTrue(result);
        assertTrue(student.isRegistered("CS301"));
        assertEquals(4, student.getTotalCredits());
        assertEquals(1, course.getCurrentEnrollment());
    }

    @Test
    void shouldRejectInactiveCourse() {
        Student student = new Student("S101", "Anita");

        Course course = new Course(
                "CS301", "Data Structures", 4, 30, false, null);

        boolean result = new RegistrationService().register(student, course);

        assertFalse(result);
        assertFalse(student.isRegistered("CS301"));
        assertEquals(0, student.getTotalCredits());
        assertEquals(0, course.getCurrentEnrollment());
    }

    @Test
    void shouldRejectMissingPrerequisite() {
        Student student = new Student("S101", "Anita");

        Course course = new Course(
                "CS401", "Advanced Algorithms", 4, 30, true, "CS301");

        boolean result = new RegistrationService().register(student, course);

        assertFalse(result);
        assertFalse(student.isRegistered("CS401"));
        assertEquals(0, student.getTotalCredits());
        assertEquals(0, course.getCurrentEnrollment());
    }

    @Test
    void shouldAllowRegistrationWhenPrerequisiteIsCompleted() {
        Student student = new Student("S101", "Anita");
        student.addCompletedPrerequisite("CS301");

        Course course = new Course(
                "CS401", "Advanced Algorithms", 4, 30, true, "CS301");

        boolean result = new RegistrationService().register(student, course);

        assertTrue(result);
        assertTrue(student.isRegistered("CS401"));
        assertEquals(4, student.getTotalCredits());
        assertEquals(1, course.getCurrentEnrollment());
    }

    @Test
    void shouldRejectDuplicateRegistration() {
        Student student = new Student("S101", "Anita");

        Course course = new Course(
                "CS301", "Data Structures", 4, 30, true, null);

        RegistrationService service = new RegistrationService();

        assertTrue(service.register(student, course));
        assertFalse(service.register(student, course));

        assertEquals(4, student.getTotalCredits());
        assertEquals(1, course.getCurrentEnrollment());
        assertEquals(1, student.getRegisteredCourses().size());
    }

    @Test
    void shouldRejectRegistrationWhenCreditLimitWouldBeExceeded() {
        Student student = new Student("S101", "Anita");

        // Existing credits total 24.
        student.addRegistration("CS101", 4);
        student.addRegistration("CS102", 4);
        student.addRegistration("CS201", 4);
        student.addRegistration("CS202", 4);
        student.addRegistration("CS203", 4);
        student.addRegistration("CS204", 4);

        Course course = new Course(
                "CS301", "Data Structures", 4, 30, true, null);

        boolean result = new RegistrationService().register(student, course);

        assertFalse(result);
        assertFalse(student.isRegistered("CS301"));
        assertEquals(24, student.getTotalCredits());
        assertEquals(0, course.getCurrentEnrollment());
        assertEquals(6, student.getRegisteredCourses().size());
    }

    @Test
    void shouldAllowRegistrationAtExactlyTwentyFourCredits() {
        Student student = new Student("S102", "Ravi");

        // Existing credits total 20; this course brings the total to 24.
        student.addRegistration("CS101", 4);
        student.addRegistration("CS102", 4);
        student.addRegistration("CS201", 4);
        student.addRegistration("CS202", 4);
        student.addRegistration("CS203", 4);

        Course course = new Course(
                "CS301", "Data Structures", 4, 30, true, null);

        boolean result = new RegistrationService().register(student, course);

        assertTrue(result);
        assertTrue(student.isRegistered("CS301"));
        assertEquals(24, student.getTotalCredits());
        assertEquals(1, course.getCurrentEnrollment());
        assertEquals(6, student.getRegisteredCourses().size());
    }

    @Test
    void shouldRejectCourseAtCapacityWithoutChangingState() {
        Student student = new Student("S103", "Maya");

        Course course = new Course(
                "CS302", "Networks", 3, 1, true, null);

        // Fill the only available seat.
        course.incrementEnrollment();

        boolean result = new RegistrationService().register(student, course);

        assertFalse(result);
        assertFalse(student.isRegistered("CS302"));
        assertEquals(0, student.getTotalCredits());
        assertEquals(1, course.getCurrentEnrollment());
        assertTrue(student.getRegisteredCourses().isEmpty());
    }

    @Test
    void shouldAllowRegistrationForLastAvailableSeat() {
        Student student = new Student("S104", "Arun");

        Course course = new Course(
                "CS303", "Operating Systems", 3, 2, true, null);

        // One of the two seats is already occupied.
        course.incrementEnrollment();

        boolean result = new RegistrationService().register(student, course);

        assertTrue(result);
        assertTrue(student.isRegistered("CS303"));
        assertEquals(3, student.getTotalCredits());
        assertEquals(2, course.getCurrentEnrollment());
        assertEquals(1, student.getRegisteredCourses().size());
    }
}