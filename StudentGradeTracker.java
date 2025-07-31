import java.util.*;

public class StudentGradeTracker {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        ArrayList<String> StudentNames = new ArrayList<>();
        ArrayList<Integer> StudentGrades = new ArrayList<>();

        System.out.print("Enter the number of Student : ");
        int numStudents = scanner.nextInt();
        scanner.nextLine();

        for (int i = 0; i < numStudents; i++) {
            System.out.print("Enter the name of a student " + (i + 1) + " : ");
            String name = scanner.nextLine();
            StudentNames.add(name);

            System.out.print("Enter grade for " + name + " : ");
            int grade = scanner.nextInt();
            scanner.nextLine();
            StudentGrades.add(grade);
        }
        int sum = 0;
        int highest = Integer.MIN_VALUE;
        int lowest = Integer.MAX_VALUE;
        for (int grade : StudentGrades) {
            sum += grade;
            if (grade > highest)
                highest = grade;
            if (grade < lowest)
                lowest = grade;
        }
        double average = (numStudents > 0) ? (double) sum / numStudents : 0;

        System.out.println("\n ---Students Summary report---");
        for (int i = 0; i < StudentNames.size(); i++) {
            System.out.println(StudentNames.get(i) + " - Grade: " + StudentGrades.get(i));
        }
        System.out.println("Average Grade :" + average);
        System.out.println("Highest grade :" + highest);
        System.out.println("Lowest grade :" + lowest);

        scanner.close();

    }

}