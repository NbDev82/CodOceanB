package com.example.codoceanb.infras.app;

import com.example.codoceanb.auth.entity.User;
import com.example.codoceanb.auth.repository.UserRepos;
import com.example.codoceanb.comment.repository.CommentRepository;
import com.example.codoceanb.discuss.repository.DiscussRepository;
import com.example.codoceanb.notification.entity.Notification;
import com.example.codoceanb.notification.repository.NotificationRepository;
import com.example.codoceanb.report.respository.ReportRepository;
import com.example.codoceanb.report.respository.ViolationTypeRepository;
import com.example.codoceanb.submitcode.library.entity.LibrariesSupport;
import com.example.codoceanb.submitcode.library.repository.LibraryRepository;
import com.example.codoceanb.submitcode.parameter.entity.Parameter;
import com.example.codoceanb.submitcode.parameter.repository.ParameterRepository;
import com.example.codoceanb.submitcode.problem.entity.Problem;
import com.example.codoceanb.submitcode.problem.entity.ProblemHint;
import com.example.codoceanb.submitcode.problem.entity.ProblemApproach;
import com.example.codoceanb.submitcode.problem.repository.ProblemApproachRepository;
import com.example.codoceanb.submitcode.problem.repository.ProblemHintRepository;
import com.example.codoceanb.submitcode.problem.repository.ProblemRepository;
import com.example.codoceanb.submitcode.submission.entity.Submission;
import com.example.codoceanb.submitcode.testcase.entity.TestCase;
import com.example.codoceanb.submitcode.testcase.repository.TestCaseRepository;
import jakarta.transaction.Transactional;
import jakarta.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;

@Component
@Transactional
public class initialData {
        @Autowired
        private ProblemRepository problemRepository;
        @Autowired
        private ProblemHintRepository problemHintRepository;
        @Autowired
        private ProblemApproachRepository problemApproachRepository;
        @Autowired
        private LibraryRepository libraryRepository;
        @Autowired
        private ParameterRepository parameterRepository;
        @Autowired
        private TestCaseRepository testCaseRepository;
        @Autowired
        private UserRepos userRepos;
        @Autowired
        private DiscussRepository discussRepository;
        @Autowired
        private ReportRepository reportRepository;
        @Autowired
        private CommentRepository commentRepository;
        @Autowired
        private ViolationTypeRepository violationTypeRepository;
        @Autowired
        private NotificationRepository notificationRepository;
        @Autowired
        private PasswordEncoder passwordEncoder;

        @Bean
        public void init() {
                deleteAll();

                List<User> savedUsers = initUsers();
                if (savedUsers.isEmpty()) {
                        return;
                }

                List<Problem> savedProblems = new ArrayList<>();
                savedProblems.addAll(initProblems1(savedUsers.get(0)));
                savedProblems.addAll(initProblems2(savedUsers.get(2)));
                savedProblems.addAll(initProblems3(savedUsers.get(4)));
                savedProblems.addAll(initProblems4(savedUsers.get(6)));
                savedProblems.addAll(initProblems5(savedUsers.get(7)));
                savedProblems.addAll(initProblems6(savedUsers.get(5)));
                savedProblems.addAll(initProblems7(savedUsers.get(8)));

                if (savedProblems.isEmpty()) {
                        return;
                }

                List<UUID> savedProblemIds = savedProblems.stream()
                                .map(Problem::getId)
                                .toList();

                List<UUID> savedUserIds = new ArrayList<>(savedUsers.stream()
                                .map(User::getId)
                                .toList());
                UUID ownerId = savedUserIds.get(0);
                savedUserIds.removeIf(userId -> userId.equals(ownerId));
        }

        private void deleteAll() {
                problemApproachRepository.deleteAll();
                problemHintRepository.deleteAll();
                notificationRepository.deleteAll();
                commentRepository.deleteAll();
                violationTypeRepository.deleteAll();
                reportRepository.deleteAll();
                discussRepository.deleteAll();
                userRepos.deleteAll();
                libraryRepository.deleteAll();
                testCaseRepository.deleteAll();
                parameterRepository.deleteAll();
                problemRepository.deleteAll();
        }

        private void createNotification(User user, String content) {
                Notification notification = Notification.builder()
                                .content(content)
                                .receivedTime(LocalDateTime.now())
                                .isRead(false)
                                .isDelete(false)
                                .type(Notification.EType.PERSONAL)
                                .recipient(user)
                                .build();
                notificationRepository.save(notification);
        }

        private List<User> initUsers() {
                List<User> savedUsers = new ArrayList();

                User user = User.builder()
                                .fullName("Nguyễn Trường An")
                                .phoneNumber("0396779183")
                                .dateOfBirth(LocalDateTime.of(2003, 12, 28, 0, 0))
                                .email("nguyentruongan281203@gmail.com")
                                .password(passwordEncoder.encode("123456An@"))
                                .createdAt(LocalDateTime.now())
                                .updatedAt(LocalDateTime.now())
                                .isFirstLogin(false)
                                .isActive(true)
                                .role(User.ERole.USER)
                                .urlImage("https://res.cloudinary.com/du5medjhm/image/upload/v1730996001/avatar-40_d7hhex.png")
                                .build();

                createNotification(user, "Welcome to the platform 1!");
                createNotification(user, "Welcome to the platform 2!");
                createNotification(user, "Welcome to the platform 3!");
                createNotification(user, "Welcome to the platform 4!");
                createNotification(user, "Welcome to the platform 5!");

                User user1 = User.builder()
                                .fullName("Nguyễn Văn Hoàng")
                                .phoneNumber("03967789182")
                                .dateOfBirth(LocalDateTime.of(2003, 1, 1, 0, 0))
                                .email("hoangbeo@gmail.com")
                                .password(passwordEncoder.encode("123456Hoang@"))
                                .createdAt(LocalDateTime.now())
                                .isFirstLogin(true)
                                .updatedAt(LocalDateTime.now())
                                .isActive(true)
                                .urlImage("https://res.cloudinary.com/du5medjhm/image/upload/v1730996001/avatar-40_d7hhex.png")
                                .role(User.ERole.ADMIN) // Đã sửa lại role là user
                                .build();

                User user2 = User.builder()
                                .fullName("Trần Văn An")
                                .phoneNumber("03967789152")
                                .dateOfBirth(LocalDateTime.of(1985, 5, 15, 0, 0))
                                .email("vanan@gmail.com")
                                .password(passwordEncoder.encode("123456An@"))
                                .createdAt(LocalDateTime.now())
                                .updatedAt(LocalDateTime.now())
                                .isFirstLogin(true)
                                .isActive(true)
                                .urlImage("https://res.cloudinary.com/du5medjhm/image/upload/v1730996001/avatar-40_d7hhex.png")
                                .role(User.ERole.MODERATOR) // Đã sửa lại role là user
                                .build();

                User user3 = User.builder()
                                .fullName("Bob Smith")
                                .phoneNumber("555987654")
                                .dateOfBirth(LocalDateTime.of(1978, 9, 20, 0, 0))
                                .email("bob@example.com")
                                .password(passwordEncoder.encode("password456"))
                                .createdAt(LocalDateTime.now())
                                .updatedAt(LocalDateTime.now())
                                .isFirstLogin(true)
                                .isActive(true)
                                .urlImage("https://res.cloudinary.com/du5medjhm/image/upload/v1730996001/avatar-40_d7hhex.png")
                                .role(User.ERole.ADMIN) // Đã sửa lại role là user
                                .build();

                User user4 = User.builder()
                                .fullName("Emma Johnson")
                                .phoneNumber("555456789")
                                .dateOfBirth(LocalDateTime.of(1992, 3, 10, 0, 0))
                                .email("emma@example.com")
                                .password(passwordEncoder.encode("password789"))
                                .createdAt(LocalDateTime.now())
                                .updatedAt(LocalDateTime.now())
                                .isFirstLogin(true)
                                .isActive(true)
                                .urlImage("https://res.cloudinary.com/du5medjhm/image/upload/v1730996001/avatar-40_d7hhex.png")
                                .role(User.ERole.USER_VIP) // Đã sửa lại role là user
                                .build();

                User user5 = User.builder()
                                .fullName("John Smith")
                                .phoneNumber("555123456")
                                .dateOfBirth(LocalDateTime.of(1990, 5, 20, 0, 0))
                                .email("john@example.com")
                                .password(passwordEncoder.encode("password123"))
                                .createdAt(LocalDateTime.now())
                                .updatedAt(LocalDateTime.now())
                                .isFirstLogin(true)
                                .isActive(true)
                                .urlImage("https://res.cloudinary.com/du5medjhm/image/upload/v1730996001/avatar-40_d7hhex.png")
                                .role(User.ERole.USER)
                                .build();

                User user6 = User.builder()
                                .fullName("Sophia Lee")
                                .phoneNumber("555987654")
                                .dateOfBirth(LocalDateTime.of(1988, 8, 25, 0, 0))
                                .email("sophia@example.com")
                                .password(passwordEncoder.encode("password456"))
                                .createdAt(LocalDateTime.now())
                                .updatedAt(LocalDateTime.now())
                                .isFirstLogin(true)
                                .isActive(true)
                                .urlImage("https://res.cloudinary.com/du5medjhm/image/upload/v1730996001/avatar-40_d7hhex.png")
                                .role(User.ERole.USER)
                                .build();

                User user7 = User.builder()
                                .fullName("James Brown")
                                .phoneNumber("555789123")
                                .dateOfBirth(LocalDateTime.of(1995, 1, 15, 0, 0))
                                .email("james@example.com")
                                .password(passwordEncoder.encode("password101"))
                                .createdAt(LocalDateTime.now())
                                .updatedAt(LocalDateTime.now())
                                .isFirstLogin(true)
                                .isActive(true)
                                .urlImage("https://res.cloudinary.com/du5medjhm/image/upload/v1730996001/avatar-40_d7hhex.png")
                                .role(User.ERole.USER)
                                .build();

                User user8 = User.builder()
                                .fullName("Olivia Martinez")
                                .phoneNumber("555654321")
                                .dateOfBirth(LocalDateTime.of(1997, 7, 30, 0, 0))
                                .email("olivia@example.com")
                                .password(passwordEncoder.encode("password202"))
                                .createdAt(LocalDateTime.now())
                                .updatedAt(LocalDateTime.now())
                                .isFirstLogin(true)
                                .isActive(true)
                                .urlImage("https://res.cloudinary.com/du5medjhm/image/upload/v1730996001/avatar-40_d7hhex.png")
                                .role(User.ERole.USER)
                                .build();

                savedUsers.add(userRepos.save(user));
                savedUsers.add(userRepos.save(user1));
                savedUsers.add(userRepos.save(user2));
                savedUsers.add(userRepos.save(user3));
                savedUsers.add(userRepos.save(user4));
                savedUsers.add(userRepos.save(user5));
                savedUsers.add(userRepos.save(user6));
                savedUsers.add(userRepos.save(user7));
                savedUsers.add(userRepos.save(user8));

                return savedUsers;
        }

        private List<Problem> initProblems1(@NotNull User owner) {
                List<Problem> savedProblems = new ArrayList<>();
                savedProblems.add(buildProblem(owner));
                savedProblems.add(buildProblem1(owner));
                savedProblems.add(buildProblem2(owner));
                savedProblems.add(buildProblem3(owner));
                savedProblems.add(buildProblem4(owner));
                savedProblems.add(buildProblem5(owner));
                return savedProblems;
        }

        private List<Problem> initProblems2(@NotNull User owner) {
                List<Problem> savedProblems = new ArrayList<>();
                savedProblems.add(buildProblem6(owner));
                savedProblems.add(buildProblem7(owner));
                savedProblems.add(buildProblem8(owner));
                savedProblems.add(buildProblem9(owner));
                return savedProblems;
        }

        private List<Problem> initProblems3(@NotNull User owner) {
                List<Problem> savedProblems = new ArrayList<>();
                savedProblems.add(buildProblem10(owner));
                savedProblems.add(buildProblem14(owner));

                return savedProblems;
        }

        private List<Problem> initProblems4(@NotNull User owner) {
                List<Problem> savedProblems = new ArrayList<>();
                savedProblems.add(buildProblem15(owner));
                savedProblems.add(buildProblem17(owner));

                return savedProblems;
        }

        private List<Problem> initProblems5(@NotNull User owner) {
                List<Problem> savedProblems = new ArrayList<>();

                savedProblems.add(buildProblem21(owner));
                savedProblems.add(buildProblem22(owner));
                savedProblems.add(buildProblem23(owner));
                savedProblems.add(buildProblem24(owner));
                savedProblems.add(buildProblem25(owner));

                return savedProblems;
        }

        private List<Problem> initProblems6(@NotNull User owner) {
                List<Problem> savedProblems = new ArrayList<>();

                savedProblems.add(buildProblem26(owner));
                savedProblems.add(buildProblem27(owner));
                // savedProblems.add(buildProblem29(owner));

                return savedProblems;
        }

        private List<Problem> initProblems7(@NotNull User owner) {
                List<Problem> savedProblems = new ArrayList<>();

                // savedProblems.add(buildProblem31(owner));
                savedProblems.add(buildProblem32(owner));
                savedProblems.add(buildProblem33(owner));
                savedProblems.add(buildProblem34(owner));

                return savedProblems;
        }

        public Problem buildProblem(User user) {
                List<Problem.ETopic> topics = new ArrayList<>();
                topics.add(Problem.ETopic.MATH);

                Problem problem = Problem.builder()
                                .owner(user)
                                .title("Palindrome Number")
                                .createdAt(LocalDateTime.now())
                                .updatedAt(LocalDateTime.now())
                                .description("Given an integer x, return true if x is a \n" +
                                                "palindrome\n" +
                                                ", and false otherwise.")
                                .difficulty(Problem.EDifficulty.EASY)
                                .topics(topics)
                                .functionName("isPalindrome")
                                .isDeleted(false)
                                .outputDataType("boolean")
                                .description("<h1 style=\"font-size: 2em; margin: 0.67em 0; font-weight: bold;text-align: center;\">Palindrome Number</h1>\r\n"
                                                + //
                                                "\r\n" + //
                                                "---\r\n" + //
                                                "\r\n" + //
                                                "## 1. Description\r\n" + //
                                                "\r\n" + //
                                                "<!-- Provide a clear and concise description of the problem. \r\n" + //
                                                "    - What is the problem about?\r\n" + //
                                                "    - What are the constraints?\r\n" + //
                                                "    - What is the expected input and output?\r\n" + //
                                                "    - Any special conditions or edge cases to consider?\r\n" + //
                                                "-->\r\n" + //
                                                "\r\n" + //
                                                "\r\n" + //
                                                "Given an integer `x`, return `true` if `x` is a palindrome, and `false` otherwise. A palindrome is a number that reads the same backward as forward.\r\n"
                                                + //
                                                "\r\n" + //
                                                "Constraints:\r\n" + //
                                                "- The input integer can be positive, negative, or zero.\r\n" + //
                                                "- If `x` is negative or ends with 0 (and is not 0 itself), it cannot be a palindrome.\r\n"
                                                + //
                                                "\r\n" + //
                                                "Expected Input and Output:\r\n" + //
                                                "- Input: An integer `x`.\r\n" + //
                                                "- Output: A boolean value indicating whether `x` is a palindrome.\r\n"
                                                + //
                                                "\r\n" + //
                                                "Special Conditions:\r\n" + //
                                                "- Consider edge cases such as negative numbers and numbers ending with zero.\r\n"
                                                + //
                                                "\r\n" + //
                                                "![Palindrome Number Illustration](https://media.geeksforgeeks.org/wp-content/uploads/20230912123351/palindrome-number-in-c.png)\r\n"
                                                + //
                                                "\r\n" + //
                                                "\r\n" + //
                                                "---\r\n" + //
                                                "\r\n" + //
                                                "## 2. Examples\r\n" + //
                                                "\r\n" + //
                                                "---\r\n" + //
                                                "\r\n" + //
                                                "### Example 1: Basic Usage\r\n" + //
                                                "| Input | Output |\r\n" + //
                                                "| ----- | ------ |\r\n" + //
                                                "| 121   | true   |\r\n" + //
                                                "\r\n" + //
                                                "---\r\n" + //
                                                "\r\n" + //
                                                "### Example 2: Advanced Scenario\r\n" + //
                                                "| Input | Output |\r\n" + //
                                                "| ----- | ------ |\r\n" + //
                                                "| -121  | false  |\r\n" + //
                                                "\r\n")
                                .correctAnswer("import java.util.ArrayList;\n" +
                                                "import java.util.List;\n" +
                                                "\n" +
                                                "public class Solution {\n" +
                                                "    public static boolean isPalindrome (int x) {\n" +
                                                "        // If x is negative or if x ends with 0 (and is not 0 itself), it cannot be a palindrome\n"
                                                +
                                                "    if (x < 0 || (x % 10 == 0 && x != 0)) {\n" +
                                                "        return false;\n" +
                                                "    }\n" +
                                                "\n" +
                                                "    int reversedHalf = 0;\n" +
                                                "    while (x > reversedHalf) {\n" +
                                                "        // Build the reversed half of the number\n" +
                                                "        reversedHalf = reversedHalf * 10 + x % 10;\n" +
                                                "        x /= 10;\n" +
                                                "    }\n" +
                                                "\n" +
                                                "    // If the length of the number is odd, we get rid of the middle digit by dividing reversedHalf by 10\n"
                                                +
                                                "    return x == reversedHalf || x == reversedHalf / 10;\n" +
                                                "    }\n" +
                                                "}")
                                .build();
                problem = problemRepository.save(problem);
                ProblemHint hint = ProblemHint.builder()
                                .isLocked(false)
                                .overView("## Overview\n\nTo determine if a number is a palindrome, we need to check if it reads the same forwards and backwards.")
                                .pseudoCode("## Pseudo Code\n\n```plaintext\nfunction isPalindrome(x):\n    if x < 0 or (x mod 10 = 0 and x ≠ 0) then\n        return false\n    end if\n    \n    reversedHalf = 0\n    while x > reversedHalf do\n        reversedHalf = reversedHalf * 10 + x mod 10\n        x = x div 10\n    end while\n    \n    return x = reversedHalf or x = reversedHalf div 10\n```")
                                .problem(problem)
                                .build();
                ProblemHint savedHint = problemHintRepository.save(hint);

                problem.setProblemHint(hint);
                problem = problemRepository.save(problem);

                // Build and save approach
                ProblemApproach approach1 = ProblemApproach.builder()
                                .intuition("## Intuition\n\nTo check if a number is a palindrome, we can compare the first half with the second half of the number. Instead of reversing the entire number and comparing, we only need to reverse the second half and compare it with the first half, which reduces the processing time by half.")
                                .algorithm("## Algorithm\n\n1. **Handle special cases:**\n   - Negative numbers cannot be palindromes\n   - Numbers ending with 0 (except for the number 0 itself) cannot be palindromes\n\n2. **Iterate through each digit of the second half:**\n   - Extract the last digit using modulo 10\n   - Add the digit to the reversed number\n   - Remove the last digit from the original number\n\n3. **Compare the first half with the reversed number:**\n   - Handle the case of odd number of digits by dividing the reversed number by 10")
                                .implementation("## Implementation\n\n```java\npublic boolean isPalindrome(int x) {\n    if (x < 0 || (x % 10 == 0 && x != 0)) {\n        return false;\n    }\n    int reversedHalf = 0;\n    while (x > reversedHalf) {\n        reversedHalf = reversedHalf * 10 + x % 10;\n        x /= 10;\n    }\n    return x == reversedHalf || x == reversedHalf / 10;\n}\n```")
                                .language(Submission.ELanguage.JAVA)
                                .problemHint(savedHint)
                                .build();

                ProblemApproach approach2 = ProblemApproach.builder()
                                .intuition("## Intuition\n\nConvert the number to a string and check if the string is symmetrical. This method is easy to understand and implement but requires additional memory to store the string.")
                                .algorithm("## Algorithm\n\n1. **Convert** the integer to a string.\n" +
                                                "2. **Use** two pointers (start and end):\n" +
                                                "   - The start pointer moves from left to right.\n" +
                                                "   - The end pointer moves from right to left.\n" +
                                                "3. **Compare** the characters at the positions of the two pointers.\n" +
                                                "4. If all pairs of characters are the same, it is a palindrome.")
                                .implementation("## Implementation\n\n```java\npublic boolean isPalindrome(int x) {\n" +
                                                "    String str = String.valueOf(x);\n" +
                                                "    int left = 0;\n" +
                                                "    int right = str.length() - 1;\n" +
                                                "    while (left < right) {\n" +
                                                "        if (str.charAt(left) != str.charAt(right)) {\n" +
                                                "            return false;\n" +
                                                "        }\n" +
                                                "        left++;\n" +
                                                "        right--;\n" +
                                                "    }\n" +
                                                "    return true;\n" +
                                                "}\n```")
                                .language(Submission.ELanguage.JAVA)
                                .problemHint(savedHint)
                                .build();

                ProblemApproach approach3 = ProblemApproach.builder()
                                .intuition("## Intuition\n\nReverse the entire number and compare it with the original number. This method is simple but may encounter overflow issues with large numbers.")
                                .algorithm("## Algorithm\n\n1. **Store** the original number in a temporary variable.\n" +
                                           "2. **Create** a variable to store the reversed number.\n" +
                                           "3. **Iterate** through each digit of the original number:\n" +
                                           "   - **Extract** the last digit.\n" +
                                           "   - **Add** it to the reversed number.\n" +
                                           "4. **Compare** the reversed number with the original number.")
                                .implementation("## Implementation\n\n```java\npublic boolean isPalindrome(int x) {\n" +
                                                "    if (x < 0) return false;\n" +
                                                "    int temp = x;\n" +
                                                "    int reversed = 0;\n" +
                                                "    while (temp > 0) {\n" +
                                                "        int digit = temp % 10;\n" +
                                                "        reversed = reversed * 10 + digit;\n" +
                                                "        temp /= 10;\n" +
                                                "    }\n" +
                                                "    return x == reversed;\n" +
                                                "}\n```")
                                .language(Submission.ELanguage.JAVA)
                                .problemHint(savedHint)
                                .build();
                problemApproachRepository.save(approach1);
                problemApproachRepository.save(approach2);
                problemApproachRepository.save(approach3);

                List<ProblemApproach> approaches = new ArrayList<>();
                approaches.add(approach1);
                approaches.add(approach2);
                approaches.add(approach3);
                savedHint.setApproaches(approaches);
                problemHintRepository.save(savedHint);

                buildTestCase(problem);
                buildLibrary(problem);
                return problem;
        }

        private void buildLibrary(Problem problem) {
                LibrariesSupport librariesSupport1 = LibrariesSupport.builder()
                                .name("java.util.ArrayList")
                                .problem(problem)
                                .build();
                libraryRepository.save(librariesSupport1);

                LibrariesSupport librariesSupport2 = LibrariesSupport.builder()
                                .name("java.util.List")
                                .problem(problem)
                                .build();
                libraryRepository.save(librariesSupport2);
        }

        private void buildTestCase(Problem problem) {
                TestCase testCase = TestCase.builder()
                                .outputData("true")
                                .isPublic(true)
                                .problem(problem)
                                .build();
                buildParameters(testCase, "525");
                testCaseRepository.save(testCase);

                TestCase testCase1 = TestCase.builder()
                                .outputData("false")
                                .problem(problem)
                                .build();
                buildParameters(testCase1, "523");
                testCaseRepository.save(testCase1);
        }

        private void buildParameters(TestCase testCase, String x) {
                Parameter parameter1 = Parameter.builder()
                                .inputDataType("int")
                                .name("x")
                                .inputData(x)
                                .index(1)
                                .testCase(testCase)
                                .build();
                parameterRepository.save(parameter1);

        }

        public Problem buildProblem1(User user) {
                List<Problem.ETopic> topics = new ArrayList<>();
                topics.add(Problem.ETopic.RECURSION);

                Problem problem = Problem.builder()
                                .owner(user)
                                .title("Missing Number")
                                .createdAt(LocalDateTime.now())
                                .updatedAt(LocalDateTime.now())
                                .description(
                                                "<h1 style=\"font-size: 2em; margin: 0.67em 0; font-weight: bold;text-align: center;\">Missing Number</h1>\r\n"
                                                                + //
                                                                "\r\n" + //
                                                                "---\r\n" + //
                                                                "\r\n" + //
                                                                "## 1. Description\r\n" + //
                                                                "\r\n" + //
                                                                "<!-- Provide a clear and concise description of the problem. \r\n"
                                                                + //
                                                                "    - What is the problem about?\r\n" + //
                                                                "    - What are the constraints?\r\n" + //
                                                                "    - What is the expected input and output?\r\n" + //
                                                                "    - Any special conditions or edge cases to consider?\r\n"
                                                                + //
                                                                "-->\r\n" + //
                                                                "\r\n" + //
                                                                "\r\n" + //
                                                                "Given an array `nums` containing `n` distinct numbers in the range `[0, n]`, return the only number in the range that is missing from the array.\n"
                                                                +
                                                                "\n" +
                                                                "Constraints:\n" +
                                                                "- `n == nums.length`\n" +
                                                                "- `1 <= n <= 10^4`\n" +
                                                                "- `0 <= nums[i] <= n`\n" +
                                                                "- All the numbers of `nums` are unique.\n" +
                                                                "\n" +
                                                                "Follow up: Could you implement a solution using only `O(1)` extra space complexity and `O(n)` runtime complexity?\n"
                                                                +
                                                                "---\n" +
                                                                "## 2. Examples\n" +
                                                                "---\n" +
                                                                "### Example 1: Basic Usage\n" +
                                                                "| Input          | Output          |\n" +
                                                                "| -------------- | --------------- |\n" +
                                                                "| nums = [3,0,1] | 2               |\n" +
                                                                "---\n" +
                                                                "### Example 2: Advanced Scenario\n" +
                                                                "| Input          | Output          |\n" +
                                                                "| -------------- | --------------- |\n" +
                                                                "| nums = [0,1]   | 2               |\n" +
                                                                "---\n" +
                                                                "### Example 3: Advanced Scenario\n" +
                                                                "| Input                      | Output |\n" +
                                                                "| ---------------------------| -------|\n" +
                                                                "| nums = [9,6,4,2,3,5,7,0,1] | 8       |\n")
                                .difficulty(Problem.EDifficulty.EASY)
                                .topics(topics)
                                .functionName("missingNumber")
                                .isDeleted(false)
                                .outputDataType("int")
                                .correctAnswer("import java.util.ArrayList;\n" +
                                                "import java.util.List;\n" +
                                                "\n" +
                                                "public class Solution {\n" +
                                                "    public static int missingNumber (int[] nums) {\n" +
                                                "       int n = nums.length;\n" +
                                                "    \n" +
                                                "        // Calculate the expected sum of numbers from 0 to n\n" +
                                                "        int expectedSum = n * (n + 1) / 2;\n" +
                                                "        \n" +
                                                "        // Calculate the actual sum of the numbers in the array\n" +
                                                "        int actualSum = 0;\n" +
                                                "        for (int num : nums) {\n" +
                                                "            actualSum += num;\n" +
                                                "        }\n" +
                                                "        \n" +
                                                "        // The missing number is the difference between expectedSum and actualSum\n"
                                                +
                                                "        return expectedSum - actualSum;\n" +
                                                "\n" +
                                                "    }\n" +
                                                "}")
                                .build();
                problem = problemRepository.save(problem);

                ProblemHint hint = ProblemHint.builder()
                                .isLocked(false)
                                .overView("<h1 style=\"font-size: 2em; margin: 0.67em 0; font-weight: bold;text-align: center;\">Overview</h1>\r\n"
                                                + //
                                                "\r\n" + //
                                                "---\r\n" + //
                                                "\r\n" + //
                                                "## 1. Problem Understanding\r\n" + //
                                                "<!-- Provide a brief overview of the problem. \r\n" + //
                                                "    - What is the problem about?\r\n" + //
                                                "    - What are the key points to understand?\r\n" + //
                                                "-->\r\n" + //
                                                "To find the missing number in an array containing numbers from 0 to n, calculate the expected sum of numbers from 0 to n and subtract the actual sum of the array.\r\n"
                                                + //
                                                "\r\n" + //
                                                "---\r\n" + //
                                                "\r\n" + //
                                                "## 2. Key Concepts\r\n" + //
                                                "<!-- List and explain the key concepts or algorithms needed to solve the problem. \r\n"
                                                + //
                                                "    - What data structures are involved?\r\n" + //
                                                "    - What algorithms or techniques are required?\r\n" + //
                                                "-->\r\n" + //
                                                "The key concept involves using arithmetic to determine the missing number by leveraging the formula for the sum of the first n natural numbers.")
                                .pseudoCode("~~~plaintext\r\n" + //
                                                "// Default Pseudo Code Template\r\n" + //
                                                "// Problem Title: Missing Number\r\n" + //
                                                "\r\n" + //
                                                "// Steps to Solve the Problem:\r\n" + //
                                                "// 1. Calculate the length of the array, n\r\n" + //
                                                "// 2. Calculate the expected sum of numbers from 0 to n using the formula: n * (n + 1) / 2\r\n"
                                                + //
                                                "// 3. Initialize actualSum to 0\r\n" + //
                                                "// 4. Iterate over each number in the array and add it to actualSum\r\n"
                                                + //
                                                "// 5. Subtract actualSum from expectedSum to find the missing number\r\n"
                                                + //
                                                "// 6. Return the result\r\n" + //
                                                "\r\n" + //
                                                "function missingNumber(nums):\r\n" + //
                                                "    n = length of nums\r\n" + //
                                                "    expectedSum = n * (n + 1) / 2\r\n" + //
                                                "    actualSum = 0\r\n" + //
                                                "    for num in nums do\r\n" + //
                                                "        actualSum += num\r\n" + //
                                                "    end for\r\n" + //
                                                "    return expectedSum - actualSum\r\n" + //
                                                "~~~")
                        .problem(problem)
                        .build();

                ProblemHint savedHint = problemHintRepository.save(hint);

                problem.setProblemHint(hint);
                problem = problemRepository.save(problem);

                ProblemApproach approach1 = ProblemApproach.builder()
                                .intuition("## Intuition\r\n" + //
                                                "The missing number can be found by calculating the difference between the expected sum of numbers from 0 to n and the actual sum of the array.")
                                .algorithm("## Algorithm\r\n" + //
                                                "1. Calculate the expected sum of numbers from 0 to n using the formula: n * (n + 1) / 2.\r\n"
                                                + //
                                                "2. Calculate the actual sum of the numbers in the array.\r\n" + //
                                                "3. The missing number is the difference between the expected sum and the actual sum.")
                                .implementation("## Implementation\r\n" + //
                                                "~~~java\r\n" + //
                                                "public static int missingNumber(int[] nums) {\r\n" + //
                                                "    int n = nums.length;\r\n" + //
                                                "    int expectedSum = n * (n + 1) / 2;\r\n" + //
                                                "    int actualSum = 0;\r\n" + //
                                                "    for (int num : nums) {\r\n" + //
                                                "        actualSum += num;\r\n" + //
                                                "    }\r\n" + //
                                                "    return expectedSum - actualSum;\r\n" + //
                                                "}")
                                .language(Submission.ELanguage.JAVA)
                                .problemHint(savedHint)
                                .build();

                problemApproachRepository.save(approach1);

                List<ProblemApproach> approaches = new ArrayList<>();
                approaches.add(approach1);
                savedHint.setApproaches(approaches);
                problemHintRepository.save(savedHint);
                problemApproachRepository.save(approach1);

                buildTestCase1(problem);
                buildLibrary1(problem);
                return problemRepository.save(problem);
        }

        private void buildLibrary1(Problem problem) {
                LibrariesSupport librariesSupport1 = LibrariesSupport.builder()
                                .name("java.util.ArrayList")
                                .problem(problem)
                                .build();
                libraryRepository.save(librariesSupport1);

                LibrariesSupport librariesSupport2 = LibrariesSupport.builder()
                                .name("java.util.List")
                                .problem(problem)
                                .build();
                libraryRepository.save(librariesSupport2);
        }

        private void buildTestCase1(Problem problem) {
                TestCase testCase = TestCase.builder()
                                .outputData("2")
                                .isPublic(true)
                                .problem(problem)
                                .build();
                buildParameters1(testCase, "{3, 0, 1}");
                testCaseRepository.save(testCase);

                TestCase testCase1 = TestCase.builder()
                                .outputData("8")
                                .problem(problem)
                                .build();
                buildParameters1(testCase1, "{9, 6, 4, 2, 3, 5, 7, 0, 1}");
                testCaseRepository.save(testCase1);
        }

        private void buildParameters1(TestCase testCase, String x) {
                Parameter parameter1 = Parameter.builder()
                                .inputDataType("int[]")
                                .name("nums")
                                .inputData(x)
                                .index(1)
                                .testCase(testCase)
                                .build();
                parameterRepository.save(parameter1);

        }

        public Problem buildProblem2(User user) {
                List<Problem.ETopic> topics = new ArrayList<>();
                topics.add(Problem.ETopic.MATH);
                topics.add(Problem.ETopic.ARRAY);

                Problem problem = Problem.builder()
                                .owner(user)
                                .title("Simple Array Sum")
                                .createdAt(LocalDateTime.now())
                                .updatedAt(LocalDateTime.now())
                                .description(
                                                "<h1 style=\"font-size: 2em; margin: 0.67em 0; font-weight: bold;text-align: center;\">Simple Array Sum</h1>\r\n"
                                                                + //
                                                                "\r\n" + //
                                                                "---\r\n" + //
                                                                "\r\n" + //
                                                                "## 1. Description\r\n" + //
                                                                "\r\n" + //
                                                                "<!-- Provide a clear and concise description of the problem. \r\n"
                                                                + //
                                                                "    - What is the problem about?\r\n" + //
                                                                "    - What are the constraints?\r\n" + //
                                                                "    - What is the expected input and output?\r\n" + //
                                                                "    - Any special conditions or edge cases to consider?\r\n"
                                                                + //
                                                                "-->\r\n" + //
                                                                "\r\n" + //
                                                                "\r\n" + //
                                                                "Write a function to calculate the sum of all elements in an integer array.\n"
                                                                +
                                                                "\n" +
                                                                "Use the provided function signature:\n" +
                                                                "\n" +
                                                                "int arraySum(int[] arr)\n" +
                                                                "\n" +
                                                                "Constraints:\n" +
                                                                "- The length of the array is between 1 and 1000.\n" +
                                                                "- Each element of the array is between -1000 and 1000.\n"
                                                                +
                                                                "---\n" +
                                                                "## 2. Examples\n" +
                                                                "---\n" +
                                                                "### Example 1: Basic Usage\n" +
                                                                "| Input          | Output          |\n" +
                                                                "| -------------- | --------------- |\n" +
                                                                "| arr = [1, 2, 3, 4, 5] | 15 |\n" +
                                                                "---\n" +
                                                                "### Example 2: Advanced Scenario\n" +
                                                                "| Input          | Output          |\n" +
                                                                "| -------------- | --------------- |\n" +
                                                                "| arr = [-1, -2, -3, -4, -5] | -15 |\n" +
                                                                "---\n" +
                                                                "> **Note:** Ensure that the function handles both positive and negative integers correctly.\n")
                                .difficulty(Problem.EDifficulty.EASY)
                                .topics(topics)
                                .functionName("arraySum")
                                .isDeleted(false)
                                .outputDataType("int")
                                .correctAnswer("\n" +
                                                "public class Solution {\n" +
                                                "    public static int arraySum (int[] arr) {\n" +
                                                "        int sum = 0;\n" +
                                                "    for (int num : arr) {\n" +
                                                "        sum += num;\n" +
                                                "    }\n" +
                                                "    return sum;\n" +
                                                "    }\n" +
                                                "}")
                                .build();
                problem = problemRepository.save(problem);

                ProblemHint hint = ProblemHint.builder()
                                .isLocked(false)
                                .overView("## Overview\r\n" + //
                                                "To calculate the sum of an array, iterate through each element and accumulate the total sum.")
                                .pseudoCode("~~~plaintext\r\n" + //
                                                "function arraySum(arr):\r\n" + //
                                                "    sum = 0\r\n" + //
                                                "    for num in arr do\n" +
                                                "        sum += num\r\n" + //
                                                "    end for\r\n" + //
                                                "    return sum\r\n" + //
                                                "~~~")
                        .problem(problem)
                        .build();
                ProblemHint savedHint = problemHintRepository.save(hint);

                problem.setProblemHint(hint);
                problem = problemRepository.save(problem);

                ProblemApproach approach1 = ProblemApproach.builder()
                                .intuition("## Intuition\r\n" + //
                                                "The sum of an array can be found by iterating through each element and adding it to a running total.")
                                .algorithm("## Algorithm\r\n" + //
                                                "1. Initialize a variable `sum` to 0.\r\n" + //
                                                "2. Iterate through each element in the array.\r\n" + //
                                                "3. Add each element to `sum`.\r\n" + //
                                                "4. Return the value of `sum`.")
                                .implementation("## Implementation\r\n" + //
                                                "~~~java\r\n" + //
                                                "public static int arraySum(int[] arr) {\r\n" + //
                                                "    int sum = 0;\r\n" + //
                                                "    for (int num : arr) {\r\n" + //
                                                "        sum += num;\r\n" + //
                                                "    }\r\n" + //
                                                "    return sum;\r\n" + //
                                                "}\r\n" + //
                                                "~~~")
                                .language(Submission.ELanguage.JAVA)
                                .problemHint(savedHint)
                                .build();

                problemApproachRepository.save(approach1);

                List<ProblemApproach> approaches = new ArrayList<>();
                approaches.add(approach1);
                savedHint.setApproaches(approaches);
                problemHintRepository.save(savedHint);
                buildTestCase2(problem);
                return problemRepository.save(problem);
        }

        private void buildTestCase2(Problem problem) {
                TestCase testCase = TestCase.builder()
                                .outputData("15")
                                .problem(problem)
                                .isPublic(true)
                                .build();
                buildParameters2(testCase, "{1, 2, 3, 4, 5}");

                testCaseRepository.save(testCase);
        }

        private void buildParameters2(TestCase testCase, String arr) {
                Parameter parameter1 = Parameter.builder()
                                .inputDataType("int[]")
                                .name("arr")
                                .inputData(arr)
                                .index(1)
                                .testCase(testCase)
                                .build();

                parameterRepository.save(parameter1);
        }

        public Problem buildProblem3(User user) {
                List<Problem.ETopic> topics = new ArrayList<>();
                topics.add(Problem.ETopic.ARRAY);

                Problem problem = Problem.builder()
                                .owner(user)
                                .title("Search in Rotated Sorted Array")
                                .createdAt(LocalDateTime.now())
                                .updatedAt(LocalDateTime.now())
                                .description(
                                                "<h1 style=\"font-size: 2em; margin: 0.67em 0; font-weight: bold;text-align: center;\">Search in Rotated Sorted Array</h1>\r\n"
                                                                + //
                                                                "\r\n" + //
                                                                "---\r\n" + //
                                                                "\r\n" + //
                                                                "## 1. Description\r\n" + //
                                                                "\r\n" + //
                                                                "<!-- Provide a clear and concise description of the problem. \r\n"
                                                                + //
                                                                "    - What is the problem about?\r\n" + //
                                                                "    - What are the constraints?\r\n" + //
                                                                "    - What is the expected input and output?\r\n" + //
                                                                "    - Any special conditions or edge cases to consider?\r\n"
                                                                + //
                                                                "-->\r\n" + //
                                                                "\r\n" + //
                                                                "\r\n" + //
                                                                "You are given an integer array `nums` sorted in ascending order (with distinct values), and an integer `target`.\n"
                                                                +
                                                                "\n" +
                                                                "Suppose that `nums` is rotated at some pivot unknown to you beforehand (i.e., `[0,1,2,4,5,6,7]` might become `[4,5,6,7,0,1,2]`).\n"
                                                                +
                                                                "\n" +
                                                                "If `target` is found in the array return its index, otherwise, return `-1`.\n"
                                                                +
                                                                "\n" +
                                                                "Constraints:\n" +
                                                                "- `1 <= nums.length <= 5000`\n" +
                                                                "- `-10^4 <= nums[i] <= 10^4`\n" +
                                                                "- All values of `nums` are unique.\n" +
                                                                "- `nums` is guaranteed to be rotated at some pivot.\n"
                                                                +
                                                                "- `-10^4 <= target <= 10^4`\n" +
                                                                "---\n" +
                                                                "## 2. Examples\n" +
                                                                "---\n" +
                                                                "### Example 1: Basic Usage\n" +
                                                                "| Input          | Output          |\n" +
                                                                "| -------------- | --------------- |\n" +
                                                                "| nums = [4,5,6,7,0,1,2], target = 0 | 4 |\n" +
                                                                "---\n" +
                                                                "### Example 2: Advanced Scenario\n" +
                                                                "| Input          | Output          |\n" +
                                                                "| -------------- | --------------- |\n" +
                                                                "| nums = [4,5,6,7,0,1,2], target = 3 | -1 |\n" +
                                                                "---\n" +
                                                                "### Example 3: Advanced Scenario\n" +
                                                                "| Input          | Output          |\n" +
                                                                "| -------------- | --------------- |\n" +
                                                                "| nums = [1], target = 0 | -1 |\n" +
                                                                "---\n" +
                                                                "> **Note:** Ensure that the function handles both positive and negative integers correctly.\n")
                                .difficulty(Problem.EDifficulty.NORMAL)
                                .topics(topics)
                                .functionName("search")
                                .isDeleted(false)
                                .outputDataType("int")
                                .correctAnswer("import java.util.Arrays;\n" +
                                                "\n" +
                                                "public class Solution {\n" +
                                                "    public static int search (int[] nums, int target) {\n" +
                                                "         int left = 0;\n" +
                                                "        int right = nums.length - 1;\n" +
                                                "\n" +
                                                "        while (left <= right) {\n" +
                                                "            int mid = left + (right - left) / 2;\n" +
                                                "\n" +
                                                "            // Check if mid element is the target\n" +
                                                "            if (nums[mid] == target) {\n" +
                                                "                return mid;\n" +
                                                "            }\n" +
                                                "\n" +
                                                "            // Determine which side is sorted\n" +
                                                "            if (nums[left] <= nums[mid]) {\n" +
                                                "                // Left side is sorted\n" +
                                                "                if (nums[left] <= target && target < nums[mid]) {\n" +
                                                "                    right = mid - 1; // Target is in the left sorted portion\n"
                                                +
                                                "                } else {\n" +
                                                "                    left = mid + 1; // Target is in the right unsorted portion\n"
                                                +
                                                "                }\n" +
                                                "            } else {\n" +
                                                "                // Right side is sorted\n" +
                                                "                if (nums[mid] < target && target <= nums[right]) {\n" +
                                                "                    left = mid + 1; // Target is in the right sorted portion\n"
                                                +
                                                "                } else {\n" +
                                                "                    right = mid - 1; // Target is in the left unsorted portion\n"
                                                +
                                                "                }\n" +
                                                "            }\n" +
                                                "        }\n" +
                                                "\n" +
                                                "        // Target not found\n" +
                                                "        return -1;\n" +
                                                "    }\n" +
                                                "}")
                                .build();
                problem = problemRepository.save(problem);
                ProblemHint hint = ProblemHint.builder()
                                .isLocked(false)
                                .overView("## Overview\r\n" + //
                                                "To find the target in a rotated sorted array, use a modified binary search to determine which side of the array is sorted.")
                                .pseudoCode("~~~plaintext\r\n" + //
                                                "function search(nums, target):\r\n" + //
                                                "    left = 0\r\n" + //
                                                "    right = length of nums - 1\r\n" + //
                                                "    while left <= right do\r\n" + //
                                                "        mid = left + (right - left) / 2\r\n" + //
                                                "        if nums[mid] == target then\r\n" + //
                                                "            return mid\r\n" + //
                                                "        if nums[left] <= nums[mid] then\r\n" + //
                                                "            if nums[left] <= target < nums[mid] then\r\n" + //
                                                "                right = mid - 1\r\n" + //
                                                "            else\r\n" + //
                                                "                left = mid + 1\r\n" + //
                                                "        else\r\n" + //
                                                "            if nums[mid] < target <= nums[right] then\r\n" + //
                                                "                left = mid + 1\r\n" + //
                                                "            else\r\n" + //
                                                "                right = mid - 1\r\n" + //
                                                "    return -1\r\n" + //
                                                "~~~")
                        .problem(problem)
                        .build();
                ProblemHint savedHint = problemHintRepository.save(hint);

                problem.setProblemHint(hint);
                problem = problemRepository.save(problem);

                ProblemApproach approach1 = ProblemApproach.builder()
                                .intuition("## Intuition\r\n" + //
                                                "Use binary search to efficiently find the target by determining which half of the array is sorted.")
                                .algorithm("## Algorithm\r\n" + //
                                                "1. Initialize two pointers, left and right, to the start and end of the array.\r\n"
                                                + //
                                                "2. While left is less than or equal to right:\r\n" + //
                                                "   - Calculate the mid index.\r\n" + //
                                                "   - If the mid element is the target, return mid.\r\n" + //
                                                "   - Determine which side of the array is sorted:\r\n" + //
                                                "     - If the left side is sorted, check if the target is within this range.\r\n"
                                                + //
                                                "     - If the right side is sorted, check if the target is within this range.\r\n"
                                                + //
                                                "3. Adjust the left or right pointer based on the sorted side.\r\n" + //
                                                "4. If the target is not found, return -1.")
                                .implementation("~~~java\r\n" + //
                                                "public static int search(int[] nums, int target) {\r\n" + //
                                                "    int left = 0;\r\n" + //
                                                "    int right = nums.length - 1;\r\n" + //
                                                "    while (left <= right) {\r\n" + //
                                                "        int mid = left + (right - left) / 2;\r\n" + //
                                                "        if (nums[mid] == target) {\r\n" + //
                                                "            return mid;\r\n" + //
                                                "        }\r\n" + //
                                                "        if (nums[left] <= nums[mid]) {\r\n" + //
                                                "            if (nums[left] <= target && target < nums[mid]) {\r\n" + //
                                                "                right = mid - 1;\r\n" + //
                                                "            } else {\r\n" + //
                                                "                left = mid + 1;\r\n" + //
                                                "            }\r\n" + //
                                                "        } else {\r\n" + //
                                                "            if (nums[mid] < target && target <= nums[right]) {\r\n" + //
                                                "                left = mid + 1;\r\n" + //
                                                "            } else {\r\n" + //
                                                "                right = mid - 1;\r\n" + //
                                                "            }\r\n" + //
                                                "        }\r\n" + //
                                                "    }\r\n" + //
                                                "    return -1;\r\n" + //
                                                "}\r\n" + //
                                                "~~~")
                                .language(Submission.ELanguage.JAVA)
                                .problemHint(savedHint)
                                .build();

                problemApproachRepository.save(approach1);

                List<ProblemApproach> approaches = new ArrayList<>();
                approaches.add(approach1);
                savedHint.setApproaches(approaches);
                problemHintRepository.save(savedHint);
                problemApproachRepository.save(approach1);

                buildTestCase3(problem);
                buildLibrary3(problem);
                return problemRepository.save(problem);
        }

        private void buildLibrary3(Problem problem) {
                LibrariesSupport librariesSupport1 = LibrariesSupport.builder()
                                .name("java.util.Arrays")
                                .problem(problem)
                                .build();
                libraryRepository.save(librariesSupport1);
        }

        private void buildTestCase3(Problem problem) {
                TestCase testCase = TestCase.builder()
                                .outputData("4")
                                .problem(problem)
                                .build();
                buildParameters3(testCase, "{4, 5, 6, 7, 0, 1, 2}", "0");
                testCaseRepository.save(testCase);

                TestCase testCase1 = TestCase.builder()
                                .outputData("-1")
                                .problem(problem)
                                .build();
                buildParameters3(testCase1, "{4, 5, 6, 7, 0, 1, 2}", "3");
                testCaseRepository.save(testCase1);

                TestCase testCase2 = TestCase.builder()
                                .outputData("-1")
                                .problem(problem)
                                .isPublic(true)
                                .build();
                buildParameters3(testCase2, "{1}", "0");
                testCaseRepository.save(testCase2);
        }

        private void buildParameters3(TestCase testCase, String nums, String target) {
                Parameter parameter1 = Parameter.builder()
                                .inputDataType("int[]")
                                .name("nums")
                                .inputData(nums)
                                .index(1)
                                .testCase(testCase)
                                .build();
                parameterRepository.save(parameter1);

                Parameter parameter2 = Parameter.builder()
                                .inputDataType("int")
                                .name("target")
                                .inputData(target)
                                .index(2)
                                .testCase(testCase)
                                .build();
                parameterRepository.save(parameter2);
        }

        public Problem buildProblem4(User user) {
                List<Problem.ETopic> topics = new ArrayList<>();
                topics.add(Problem.ETopic.STRING);

                Problem problem = Problem.builder()
                                .owner(user)
                                .title("Longest Palindromic Subsequence")
                                .createdAt(LocalDateTime.now())
                                .updatedAt(LocalDateTime.now())
                                .description("<h1 style=\"font-size: 2em; margin: 0.67em 0; font-weight: bold;text-align: center;\">Longest Palindromic Subsequence</h1>\r\n"
                                                + //
                                                "\r\n" + //
                                                "---\r\n" + //
                                                "\r\n" + //
                                                "## 1. Description\r\n" + //
                                                "\r\n" + //
                                                "<!-- Provide a clear and concise description of the problem. \r\n" + //
                                                "    - What is the problem about?\r\n" + //
                                                "    - What are the constraints?\r\n" + //
                                                "    - What is the expected input and output?\r\n" + //
                                                "    - Any special conditions or edge cases to consider?\r\n" + //
                                                "-->\r\n" + //
                                                "\r\n" + //
                                                "Given a string `s`, find the longest palindromic subsequence's length in `s`.\r\n"
                                                + //
                                                "\r\n" + //
                                                "A subsequence is a sequence that can be derived from another sequence by deleting some or no elements without changing the order of the remaining elements.\r\n"
                                                + //
                                                "\r\n" + //
                                                "Constraints:\r\n" + //
                                                "- `1 <= s.length <= 1000`\r\n" + //
                                                "- `s` consists only of lowercase English letters.\r\n" + //
                                                "\r\n" + //
                                                "![algodailyrandomassets](https://storage.googleapis.com/algodailyrandomassets/curriculum/easy-strings/length-longest-palindromic-subsequence/brute-force-solution.png)\r\n"
                                                + //
                                                "\r\n" + //
                                                "---\r\n" + //
                                                "\r\n" + //
                                                "## 2. Examples\r\n" + //
                                                "\r\n" + //
                                                "---\r\n" + //
                                                "\r\n" + //
                                                "### Example 1: Basic Usage\r\n" + //
                                                "| Input          | Output          |\r\n" + //
                                                "| -------------- | --------------- |\r\n" + //
                                                "| s = \"bbbab\"    | 4               |\r\n" + //
                                                "\r\n" + //
                                                "> **Explanation:** One possible longest palindromic subsequence is \"bbbb\".\r\n"
                                                + //
                                                "\r\n" + //
                                                "---\r\n" + //
                                                "\r\n" + //
                                                "### Example 2: Advanced Scenario\r\n" + //
                                                "| Input          | Output          |\r\n" + //
                                                "| -------------- | --------------- |\r\n" + //
                                                "| s = \"cbbd\"     | 2               |\r\n" + //
                                                "\r\n" + //
                                                "> **Explanation:** One possible longest palindromic subsequence is \"bb\".\r\n"
                                                + //
                                                "\r\n")
                                .difficulty(Problem.EDifficulty.NORMAL)
                                .topics(topics)
                                .functionName("longestPalindromeSubseq")
                                .isDeleted(false)
                                .outputDataType("int")
                                .correctAnswer("import java.lang.StringBuilder;\n" +
                                                "\n" +
                                                "public class Solution {\n" +
                                                "    public static int longestPalindromeSubseq (String s) {\n" +
                                                "            int n = s.length();\n" +
                                                "        int[][] dp = new int[n][n];\n" +
                                                "\n" +
                                                "        // Each character is a palindrome of length 1\n" +
                                                "        for (int i = 0; i < n; i++) {\n" +
                                                "            dp[i][i] = 1;\n" +
                                                "        }\n" +
                                                "\n" +
                                                "        // Build the dp table\n" +
                                                "        for (int length = 2; length <= n; length++) { // length of the substring\n"
                                                +
                                                "            for (int i = 0; i < n - length + 1; i++) {\n" +
                                                "                int j = i + length - 1; // end index of substring\n" +
                                                "                if (s.charAt(i) == s.charAt(j)) {\n" +
                                                "                    dp[i][j] = dp[i + 1][j - 1] + 2; // characters match\n"
                                                +
                                                "                } else {\n" +
                                                "                    dp[i][j] = Math.max(dp[i + 1][j], dp[i][j - 1]); // characters don't match\n"
                                                +
                                                "                }\n" +
                                                "            }\n" +
                                                "        }\n" +
                                                "\n" +
                                                "        return dp[0][n - 1]; // The length of the longest palindromic subsequence\n"
                                                +
                                                "    }\n" +
                                                "}")
                                .build();
                problem = problemRepository.save(problem);

                ProblemHint hint = ProblemHint.builder()
                                .isLocked(false)
                                .overView("## Overview\n\nTo find the longest palindromic subsequence, use dynamic programming to build a table that stores the length of the longest palindromic subsequence for each substring.")
                                .pseudoCode("## Pseudo Code\n\n```plaintext\nfunction longestPalindromeSubseq(s):\n    n = length of s\n    dp = 2D array of size n x n\n    for i from 0 to n-1 do\n        dp[i][i] = 1\n    for length from 2 to n do\n        for i from 0 to n-length do\n            j = i + length - 1\n            if s[i] == s[j] then\n                dp[i][j] = dp[i+1][j-1] + 2\n            else\n                dp[i][j] = max(dp[i+1][j], dp[i][j-1])\n    return dp[0][n-1]\n```\n")
                        .problem(problem)
                        .build();
                ProblemHint savedHint = problemHintRepository.save(hint);

                problem.setProblemHint(hint);
                problem = problemRepository.save(problem);

                ProblemApproach approach1 = ProblemApproach.builder()
                                .intuition("## Intuition\n\nThe longest palindromic subsequence can be found by using dynamic programming to compare characters from both ends of the string and building up the solution.")
                                .algorithm("## Algorithm\n\n1. Initialize a 2D array `dp` where `dp[i][j]` represents the length of the longest palindromic subsequence in the substring `s[i...j]`.\n\n"
                                                +
                                                "2. Set `dp[i][i]` to 1 for all `i` since each character is a palindrome of length 1.\n\n"
                                                +
                                                "3. For each substring length from 2 to n, iterate over all possible starting indices `i`:\n\n"
                                                +
                                                "   - Calculate the ending index `j` as `i + length - 1`.\n\n" +
                                                "   - If `s[i]` equals `s[j]`, set `dp[i][j]` to `dp[i+1][j-1] + 2`.\n\n"
                                                +
                                                "   - Otherwise, set `dp[i][j]` to `max(dp[i+1][j], dp[i][j-1])`.\n\n" +
                                                "4. The result is stored in `dp[0][n-1]`.")
                                .implementation("## Implementation\n\n```java\npublic static int longestPalindromeSubseq(String s) {\n"
                                                +
                                                "    int n = s.length();\n" +
                                                "    int[][] dp = new int[n][n];\n" +
                                                "    for (int i = 0; i < n; i++) {\n" +
                                                "        dp[i][i] = 1;\n" +
                                                "    }\n" +
                                                "    for (int length = 2; length <= n; length++) {\n" +
                                                "        for (int i = 0; i < n - length + 1; i++) {\n" +
                                                "            int j = i + length - 1;\n" +
                                                "            if (s.charAt(i) == s.charAt(j)) {\n" +
                                                "                dp[i][j] = dp[i + 1][j - 1] + 2;\n" +
                                                "            } else {\n" +
                                                "                dp[i][j] = Math.max(dp[i + 1][j], dp[i][j - 1]);\n" +
                                                "            }\n" +
                                                "        }\n" +
                                                "    }\n" +
                                                "    return dp[0][n - 1];\n" +
                                                "}\n```")
                                .language(Submission.ELanguage.JAVA)
                                .problemHint(savedHint)
                                .build();

                problemApproachRepository.save(approach1);

                List<ProblemApproach> approaches = new ArrayList<>();
                approaches.add(approach1);
                savedHint.setApproaches(approaches);
                problemHintRepository.save(savedHint);
                problemApproachRepository.save(approach1);

                buildTestCase4(problem);
                buildLibrary4(problem);
                return problemRepository.save(problem);
        }

        private void buildLibrary4(Problem problem) {
                LibrariesSupport librariesSupport1 = LibrariesSupport.builder()
                                .name("java.lang.StringBuilder")
                                .problem(problem)
                                .build();
                libraryRepository.save(librariesSupport1);
        }

        private void buildTestCase4(Problem problem) {
                TestCase testCase = TestCase.builder()
                                .outputData("4")
                                .isPublic(true)
                                .problem(problem)
                                .build();
                buildParameters4(testCase, "\"bbbab\"");
                testCaseRepository.save(testCase);

                TestCase testCase1 = TestCase.builder()
                                .outputData("2")
                                .problem(problem)
                                .build();
                buildParameters4(testCase1, "\"cbbd\"");
                testCaseRepository.save(testCase1);
        }

        private void buildParameters4(TestCase testCase, String s) {
                Parameter parameter1 = Parameter.builder()
                                .inputDataType("String")
                                .name("s")
                                .inputData(s)
                                .index(1)
                                .testCase(testCase)
                                .build();
                parameterRepository.save(parameter1);
        }

        public Problem buildProblem5(User user) {
                List<Problem.ETopic> topics = new ArrayList<>();
                topics.add(Problem.ETopic.DYNAMIC_PROGRAMMING);
                topics.add(Problem.ETopic.MATH);

                Problem problem = Problem.builder()
                                .owner(user)
                                .title("Unique Paths")
                                .createdAt(LocalDateTime.now())
                                .updatedAt(LocalDateTime.now())
                                .description(
                                                "<h1 style=\"font-size: 2em; margin: 0.67em 0; font-weight: bold;text-align: center;\">Unique Paths</h1>\r\n"
                                                                + //
                                                                "\r\n" + //
                                                                "---\r\n" + //
                                                                "\r\n" + //
                                                                "## 1. Description\r\n" + //
                                                                "\r\n" + //
                                                                "<!-- Provide a clear and concise description of the problem. \r\n"
                                                                + //
                                                                "    - What is the problem about?\r\n" + //
                                                                "    - What are the constraints?\r\n" + //
                                                                "    - What is the expected input and output?\r\n" + //
                                                                "    - Any special conditions or edge cases to consider?\r\n"
                                                                + //
                                                                "-->\r\n" + //
                                                                "\r\n" + //
                                                                "\r\n" + //
                                                                "A robot is located at the top-left corner of a m x n grid (marked 'Start' in the diagram below).\r\n"
                                                                + //
                                                                "\r\n" + //
                                                                "The robot can only move either down or right at any point in time. The robot is trying to reach the bottom-right corner of the grid (marked 'Finish' in the diagram below).\r\n"
                                                                + //
                                                                "\r\n" + //
                                                                "How many possible unique paths are there?\r\n" + //
                                                                "\r\n" + //
                                                                "**Constraints:**\r\n" + //
                                                                "- 1 <= m, n <= 100\r\n" + //
                                                                "- It's guaranteed that the answer will be less than or equal to 2 * 10^9.\r\n"
                                                                + //
                                                                "![alt text](https://assets.leetcode.com/uploads/2018/10/22/robot_maze.png)\r\n"
                                                                + //
                                                                "---\r\n" + //
                                                                "\r\n" + //
                                                                "## 2. Examples\r\n" + //
                                                                "\r\n" + //
                                                                "---\r\n" + //
                                                                "\r\n" + //
                                                                "### Example 1: Basic Usage\r\n" + //
                                                                "\r\n" + //
                                                                "| Input        | Output |\r\n" + //
                                                                "|--------------|--------|\r\n" + //
                                                                "| m = 3, n = 7 | 28     |\r\n" + //
                                                                "\r\n" + //
                                                                ">**Explanation:** From the top-left corner, there are a total of 28 ways to reach the bottom-right corner.\r\n"
                                                                + //
                                                                "\r\n" + //
                                                                "---\r\n" + //
                                                                "\r\n" + //
                                                                "### Example 2: Advanced Scenario\r\n" + //
                                                                "\r\n" + //
                                                                "| Input        | Output |\r\n" + //
                                                                "|--------------|--------|\r\n" + //
                                                                "| m = 3, n = 2 | 3      |\r\n" + //
                                                                "\r\n" + //
                                                                ">**Explanation:** From the top-left corner, there are a total of 3 ways to reach the bottom-right corner:\r\n"
                                                                + //
                                                                "1. Right -> Down -> Down\r\n" + //
                                                                "2. Down -> Down -> Right\r\n" + //
                                                                "3. Down -> Right -> Down\r\n" + //
                                                                "\r\n" + //
                                                                "---\r\n" + //
                                                                "\r\n" + //
                                                                ">**Note:** Ensure that the function handles both small and large grid sizes efficiently.")
                                .difficulty(Problem.EDifficulty.EASY)
                                .topics(topics)
                                .functionName("uniquePaths")
                                .isDeleted(false)
                                .outputDataType("int")
                                .correctAnswer("public class Solution {\n" +
                                                "    public static int uniquePaths(int m, int n) {\n" +
                                                "        int[][] dp = new int[m][n];\n" +
                                                "\n" +
                                                "        for (int i = 0; i < m; i++) {\n" +
                                                "            dp[i][0] = 1;\n" +
                                                "        }\n" +
                                                "        for (int j = 0; j < n; j++) {\n" +
                                                "            dp[0][j] = 1;\n" +
                                                "        }\n" +
                                                "\n" +
                                                "        for (int i = 1; i < m; i++) {\n" +
                                                "            for (int j = 1; j < n; j++) {\n" +
                                                "                dp[i][j] = dp[i - 1][j] + dp[i][j - 1];\n" +
                                                "            }\n" +
                                                "        }\n" +
                                                "\n" +
                                                "        return dp[m - 1][n - 1];\n" +
                                                "    }\n" +
                                                "}")
                                .build();
                problem = problemRepository.save(problem);

                ProblemHint hint = ProblemHint.builder()
                                .isLocked(false)
                                .overView("## Overview\n\nTo find the number of unique paths, use dynamic programming to build a table that stores the number of ways to reach each cell.")
                                .pseudoCode("## Pseudo Code\n\n```plaintext\nfunction uniquePaths(m, n):\n    dp = 2D array of size m x n\n    for i from 0 to m-1 do\n        dp[i][0] = 1\n    for j from 0 to n-1 do\n        dp[0][j] = 1\n    for i from 1 to m-1 do\n        for j from 1 to n-1 do\n            dp[i][j] = dp[i-1][j] + dp[i][j-1]\n    return dp[m-1][n-1]\n```")
                        .problem(problem)
                        .build();
                ProblemHint savedHint = problemHintRepository.save(hint);

                problem.setProblemHint(hint);
                problem = problemRepository.save(problem);

                ProblemApproach approach1 = ProblemApproach.builder()
                                .intuition("## Intuition\n\nThe number of unique paths to reach a cell is the sum of the unique paths to reach the cell directly above and the cell directly to the left.")
                                .algorithm("## Algorithm\n\n1. Initialize a 2D array `dp` where `dp[i][j]` represents the number of unique paths to reach cell (i, j).\n\n"
                                                +
                                                "2. Set `dp[i][0]` to 1 for all `i` and `dp[0][j]` to 1 for all `j` since there's only one way to reach any cell in the first row or column.\n\n"
                                                +
                                                "3. For each cell (i, j) from (1, 1) to (m-1, n-1), set `dp[i][j]` to `dp[i-1][j] + dp[i][j-1]`.\n\n"
                                                +
                                                "4. The result is stored in `dp[m-1][n-1]`.")
                                .implementation("## Implementation\n\n```java\npublic static int uniquePaths(int m, int n) {\n"
                                                +
                                                "    int[][] dp = new int[m][n];\n" +
                                                "    for (int i = 0; i < m; i++) {\n" +
                                                "        dp[i][0] = 1;\n" +
                                                "    }\n" +
                                                "    for (int j = 0; j < n; j++) {\n" +
                                                "        dp[0][j] = 1;\n" +
                                                "    }\n" +
                                                "    for (int i = 1; i < m; i++) {\n" +
                                                "        for (int j = 1; j < n; j++) {\n" +
                                                "            dp[i][j] = dp[i - 1][j] + dp[i][j - 1];\n" +
                                                "        }\n" +
                                                "    }\n" +
                                                "    return dp[m - 1][n - 1];\n" +
                                                "}\n```")
                                .language(Submission.ELanguage.JAVA)
                                .problemHint(savedHint)
                                .build();

                problemApproachRepository.save(approach1);

                List<ProblemApproach> approaches = new ArrayList<>();
                approaches.add(approach1);
                savedHint.setApproaches(approaches);
                problemHintRepository.save(savedHint);
                problemApproachRepository.save(approach1);
                buildTestCase5(problem);
                return problemRepository.save(problem);
        }

        private void buildTestCase5(Problem problem) {
                TestCase testCase1 = TestCase.builder()
                                .outputData("28")
                                .isPublic(true)
                                .problem(problem)
                                .build();
                buildParameters5(testCase1, "3", "7");
                testCaseRepository.save(testCase1);

                TestCase testCase2 = TestCase.builder()
                                .outputData("3")
                                .problem(problem)
                                .build();
                buildParameters5(testCase2, "3", "2");
                testCaseRepository.save(testCase2);
        }

        private void buildParameters5(TestCase testCase, String m, String n) {
                Parameter parameter1 = Parameter.builder()
                                .inputDataType("int")
                                .name("m")
                                .inputData(m)
                                .index(1)
                                .testCase(testCase)
                                .build();

                Parameter parameter2 = Parameter.builder()
                                .inputDataType("int")
                                .name("n")
                                .inputData(n)
                                .index(2)
                                .testCase(testCase)
                                .build();

                parameterRepository.save(parameter1);
                parameterRepository.save(parameter2);
        }

        public Problem buildProblem6(User user) {
                List<Problem.ETopic> topics = new ArrayList<>();
                topics.add(Problem.ETopic.DYNAMIC_PROGRAMMING);

                Problem problem = Problem.builder()
                                .owner(user)
                                .title("Climbing Stairs")
                                .createdAt(LocalDateTime.now())
                                .updatedAt(LocalDateTime.now())
                                .description(
                                                "<h1 style=\"font-size: 2em; margin: 0.67em 0; font-weight: bold;text-align: center;\">Climbing Stairs</h1>\r\n"
                                                                + //
                                                                "\r\n" + //
                                                                "---\r\n" + //
                                                                "\r\n" + //
                                                                "## 1. Description\r\n" + //
                                                                "\r\n" + //
                                                                "<!-- Provide a clear and concise description of the problem. \r\n"
                                                                + //
                                                                "    - What is the problem about?\r\n" + //
                                                                "    - What are the constraints?\r\n" + //
                                                                "    - What is the expected input and output?\r\n" + //
                                                                "    - Any special conditions or edge cases to consider?\r\n"
                                                                + //
                                                                "-->\r\n" + //
                                                                "\r\n" + //
                                                                "\r\n" + //
                                                                "You are climbing a staircase. It takes n steps to reach the top.\n"
                                                                +
                                                                "Each time you can either climb 1 or 2 steps. In how many distinct ways can you climb to the top?\n"
                                                                +
                                                                "\r\n" + //
                                                                "![alt text](https://th.bing.com/th?id=OIP.-eRi5b6uLnSNPDYTK8h1RgAAAA&w=132&h=200&c=12&rs=1&qlt=99&o=6&dpr=1.3&pid=23.1)\n"
                                                                +
                                                                "---\n" +
                                                                "## 2. Examples\n" +
                                                                "---\n" +
                                                                "### Example 1: Basic Usage\n" +
                                                                "| Input          | Output          |\n" +
                                                                "| -------------- | --------------- |\n" +
                                                                "| n = 2          | 2               |\n" +
                                                                "\n" +
                                                                "> **Explanation:** There are two ways to climb to the top.\n"
                                                                +
                                                                "1. 1 step + 1 step\n" +
                                                                "2. 2 steps\n" +
                                                                "---\n" +
                                                                "### Example 2: Advanced Scenario\n" +
                                                                "| Input          | Output          |\n" +
                                                                "| -------------- | --------------- |\n" +
                                                                "| n = 3          | 3               |\n" +
                                                                "\n" +
                                                                "> **Explanation:** There are three ways to climb to the top.\n"
                                                                +
                                                                "1. 1 step + 1 step + 1 step\n" +
                                                                "2. 1 step + 2 steps\n" +
                                                                "3. 2 steps + 1 step\n" +
                                                                "---\n" +
                                                                "> **Note:** Ensure that the function handles both small and large values of n efficiently.\n")
                                .difficulty(Problem.EDifficulty.EASY)
                                .topics(topics)
                                .functionName("climbStairs")
                                .isDeleted(false)
                                .outputDataType("int")
                                .correctAnswer("public class Solution {\n" +
                                                "    public static int climbStairs(int n) {\n" +
                                                "        if (n <= 2) return n;\n" +
                                                "        int first = 1, second = 2;\n" +
                                                "        for (int i = 3; i <= n; i++) {\n" +
                                                "            int third = first + second;\n" +
                                                "            first = second;\n" +
                                                "            second = third;\n" +
                                                "        }\n" +
                                                "        return second;\n" +
                                                "    }\n" +
                                                "}")
                                .build();
                problem = problemRepository.save(problem);
                ProblemHint hint = ProblemHint.builder()
                                .isLocked(false)
                                .overView("## Overview\n\nTo find the number of distinct ways to climb to the top, use dynamic programming to store the number of ways to reach each step.")
                                .pseudoCode("## Pseudo Code\n\n```plaintext\nfunction climbStairs(n):\n    if n <= 2 then return n\n    first = 1, second = 2\n    for i from 3 to n do\n        third = first + second\n        first = second\n        second = third\n    return second\n```")
                        .problem(problem)
                        .build();
                ProblemHint savedHint = problemHintRepository.save(hint);

                problem.setProblemHint(hint);
                problem = problemRepository.save(problem);

                ProblemApproach approach1 = ProblemApproach.builder()
                                .intuition("## Intuition\n\nThe number of ways to reach the nth step is the sum of the ways to reach the (n-1)th and (n-2)th steps.")
                                .algorithm("## Algorithm\n\n1. If n is less than or equal to 2, return n.\n" +
                                                "2. Initialize two variables, first and second, to represent the number of ways to reach the first and second steps.\n"
                                                +
                                                "3. Iterate from the third step to the nth step:\n" +
                                                "   - Calculate the number of ways to reach the current step as the sum of the ways to reach the previous two steps.\n"
                                                +
                                                "   - Update the first and second variables.\n" +
                                                "4. Return the value of the second variable, which represents the number of ways to reach the nth step.")
                                .implementation("## Implementation\n\n```java\npublic static int climbStairs(int n) {\n"
                                                +
                                                "    if (n <= 2) return n;\n" +
                                                "    int first = 1, second = 2;\n" +
                                                "    for (int i = 3; i <= n; i++) {\n" +
                                                "        int third = first + second;\n" +
                                                "        first = second;\n" +
                                                "        second = third;\n" +
                                                "    }\n" +
                                                "    return second;\n" +
                                                "}\n```")
                                .language(Submission.ELanguage.JAVA)
                                .problemHint(savedHint)
                                .build();

                problemApproachRepository.save(approach1);

                List<ProblemApproach> approaches = new ArrayList<>();
                approaches.add(approach1);
                savedHint.setApproaches(approaches);
                problemHintRepository.save(savedHint);
                problemApproachRepository.save(approach1);
                buildTestCase6(problem);
                buildLibrary6(problem);
                return problemRepository.save(problem);
        }

        private void buildLibrary6(Problem problem) {
                LibrariesSupport librariesSupport = LibrariesSupport.builder()
                                .name("java.util.*") // Không cần thư viện đặc biệt, nhưng giả sử cần sử dụng một số
                                // utils
                                .problem(problem)
                                .build();
                libraryRepository.save(librariesSupport);
        }

        private void buildTestCase6(Problem problem) {
                TestCase testCase1 = TestCase.builder()
                                .outputData("2")
                                .isPublic(true)
                                .problem(problem)
                                .build();
                buildParameters6(testCase1, "2");
                testCaseRepository.save(testCase1);

                TestCase testCase2 = TestCase.builder()
                                .outputData("3")
                                .problem(problem)
                                .build();
                buildParameters6(testCase2, "3");
                testCaseRepository.save(testCase2);

                TestCase testCase3 = TestCase.builder()
                                .outputData("8")
                                .problem(problem)
                                .build();
                buildParameters6(testCase3, "5");
                testCaseRepository.save(testCase3);
        }

        private void buildParameters6(TestCase testCase, String n) {
                Parameter parameter = Parameter.builder()
                                .inputDataType("int")
                                .name("n")
                                .inputData(n)
                                .index(1)
                                .testCase(testCase)
                                .build();
                parameterRepository.save(parameter);
        }

        public Problem buildProblem7(User user) {
                List<Problem.ETopic> topics = new ArrayList<>();
                topics.add(Problem.ETopic.ARRAY);

                Problem problem = Problem.builder()
                                .owner(user)
                                .title("Valid Parentheses")
                                .createdAt(LocalDateTime.now())
                                .updatedAt(LocalDateTime.now())
                                .description(
                                                "<h1 style=\"font-size: 2em; margin: 0.67em 0; font-weight: bold;text-align: center;\">Valid Parentheses</h1>\r\n"
                                                                + //
                                                                "\r\n" + //
                                                                "---\r\n" + //
                                                                "\r\n" + //
                                                                "## 1. Description\r\n" + //
                                                                "\r\n" + //
                                                                "<!-- Provide a clear and concise description of the problem. \r\n"
                                                                + //
                                                                "    - What is the problem about?\r\n" + //
                                                                "    - What are the constraints?\r\n" + //
                                                                "    - What is the expected input and output?\r\n" + //
                                                                "    - Any special conditions or edge cases to consider?\r\n"
                                                                + //
                                                                "-->\r\n" + //
                                                                "\r\n" + //
                                                                "\r\n" + //
                                                                "Given a string `s` containing just the characters '(', ')', '{', '}', '[' and ']', determine if the input string is valid.\n"
                                                                +
                                                                "\n" +
                                                                "An input string is valid if:\n" +
                                                                "- Open brackets must be closed by the same type of brackets.\n"
                                                                +
                                                                "- Open brackets must be closed in the correct order.\n"
                                                                +
                                                                "![alt text](https://miro.medium.com/max/1400/1*oOtPtE0jeKG3lyLb-HjHhQ.png)\r\n"
                                                                + //
                                                                "\n" +

                                                                "---\n" +
                                                                "## 2. Examples\n" +
                                                                "---\n" +
                                                                "### Example 1: Basic Usage\n" +
                                                                "| Input          | Output          |\n" +
                                                                "| -------------- | --------------- |\n" +
                                                                "| s = \"()\"       | true            |\n" +
                                                                "\n" +
                                                                "> **Explanation:** The string \"()\" is valid because each open bracket is closed by the same type of bracket in the correct order.\n"
                                                                +
                                                                "---\n" +
                                                                "### Example 2: Advanced Scenario\n" +
                                                                "| Input          | Output          |\n" +
                                                                "| -------------- | --------------- |\n" +
                                                                "| s = \"()[]{}\"   | true            |\n" +
                                                                "\n" +
                                                                "> **Explanation:** The string \"()[]{}\" is valid because each open bracket is closed by the same type of bracket in the correct order.\n"
                                                                +
                                                                "---\n" +
                                                                "### Example 3: Advanced Scenario\n" +
                                                                "| Input          | Output          |\n" +
                                                                "| -------------- | --------------- |\n" +
                                                                "| s = \"(]\"       | false           |\n" +
                                                                "\n" +
                                                                "> **Explanation:** The string \"(]\" is not valid because the open bracket '(' is not closed by the same type of bracket.\n"
                                                                +
                                                                "---\n" +
                                                                "> **Note:** Ensure that the function handles both small and large values of `s` efficiently.\n")
                                .difficulty(Problem.EDifficulty.EASY)
                                .topics(topics)
                                .functionName("isValid")
                                .isDeleted(false)
                                .outputDataType("boolean")
                                .correctAnswer("import java.util.Stack;\n" +
                                                "\n" +
                                                "public class Solution {\n" +
                                                "    public static boolean isValid(String s) {\n" +
                                                "        Stack<Character> stack = new Stack<>();\n" +
                                                "        for (char c : s.toCharArray()) {\n" +
                                                "            if (c == '(' || c == '[' || c == '{') {\n" +
                                                "                stack.push(c);\n" +
                                                "            } else {\n" +
                                                "                if (stack.isEmpty()) return false;\n" +
                                                "                char top = stack.pop();\n" +
                                                "                if ((c == ')' && top != '(') || \n" +
                                                "                    (c == ']' && top != '[') || \n" +
                                                "                    (c == '}' && top != '{')) {\n" +
                                                "                    return false;\n" +
                                                "                }\n" +
                                                "            }\n" +
                                                "        }\n" +
                                                "        return stack.isEmpty();\n" +
                                                "    }\n" +
                                                "}")
                                .build();
                problem = problemRepository.save(problem);
                ProblemHint hint = ProblemHint.builder()
                                .isLocked(false)
                                .overView("## Overview\n\nTo determine if the parentheses are valid, use a stack to track open brackets and ensure they are closed in the correct order.")
                                .pseudoCode("## Pseudo Code\n\n```plaintext\nfunction isValid(s):\n    stack = empty stack\n    for each character c in s do\n        if c is an opening bracket then\n            push c onto stack\n        else if stack is empty or top of stack does not match c then\n            return false\n        else\n            pop from stack\n    return stack is empty\n```")
                        .problem(problem)
                        .build();
                ProblemHint savedHint = problemHintRepository.save(hint);

                problem.setProblemHint(hint);
                problem = problemRepository.save(problem);

                ProblemApproach approach1 = ProblemApproach.builder()
                                .intuition("## Intuition\n\nUse a stack to keep track of opening brackets and ensure each closing bracket matches the most recent opening bracket.")
                                .algorithm("## Algorithm\n\n1. Initialize an empty stack.\n" +
                                                "2. Iterate over each character in the string:\n" +
                                                "   - If the character is an opening bracket, push it onto the stack.\n"
                                                +
                                                "   - If the character is a closing bracket, check if the stack is empty or the top of the stack does not match the bracket type.\n"
                                                +
                                                "   - If it does not match, return false.\n" +
                                                "   - Otherwise, pop the top of the stack.\n" +
                                                "3. After processing all characters, if the stack is empty, return true; otherwise, return false.")
                                .implementation("## Implementation\n\n```java\npublic static boolean isValid(String s) {\n"
                                                +
                                                "    Stack<Character> stack = new Stack<>();\n" +
                                                "    for (char c : s.toCharArray()) {\n" +
                                                "        if (c == '(' || c == '[' || c == '{') {\n" +
                                                "            stack.push(c);\n" +
                                                "        } else {\n" +
                                                "            if (stack.isEmpty()) return false;\n" +
                                                "            char top = stack.pop();\n" +
                                                "            if ((c == ')' && top != '(') || \n" +
                                                "                (c == ']' && top != '[') || \n" +
                                                "                (c == '}' && top != '{')) {\n" +
                                                "                return false;\n" +
                                                "            }\n" +
                                                "        }\n" +
                                                "    }\n" +
                                                "    return stack.isEmpty();\n" +
                                                "}\n```")
                                .language(Submission.ELanguage.JAVA)
                                .problemHint(savedHint)
                                .build();

                problemApproachRepository.save(approach1);

                List<ProblemApproach> approaches = new ArrayList<>();
                approaches.add(approach1);
                savedHint.setApproaches(approaches);
                problemHintRepository.save(savedHint);
                problemApproachRepository.save(approach1);

                buildTestCase7(problem);
                buildLibrary7(problem);
                return problemRepository.save(problem);
        }

        private void buildLibrary7(Problem problem) {
                LibrariesSupport librariesSupport = LibrariesSupport.builder()
                                .name("java.util.Stack")
                                .problem(problem)
                                .build();
                libraryRepository.save(librariesSupport);
        }

        private void buildTestCase7(Problem problem) {
                TestCase testCase1 = TestCase.builder()
                                .outputData("true")
                                .isPublic(true)
                                .problem(problem)
                                .build();
                buildParameters7(testCase1, "\"()\"");
                testCaseRepository.save(testCase1);

                TestCase testCase2 = TestCase.builder()
                                .outputData("true")
                                .problem(problem)
                                .build();
                buildParameters7(testCase2, "\"()[]{}\"");
                testCaseRepository.save(testCase2);

                TestCase testCase3 = TestCase.builder()
                                .outputData("false")
                                .problem(problem)
                                .build();
                buildParameters7(testCase3, "\"(]\"");
                testCaseRepository.save(testCase3);

                // Additional test cases
                TestCase testCase4 = TestCase.builder()
                                .outputData("false")
                                .problem(problem)
                                .build();
                buildParameters7(testCase4, "\"([)]\"");
                testCaseRepository.save(testCase4);

                TestCase testCase5 = TestCase.builder()
                                .outputData("true")
                                .problem(problem)
                                .build();
                buildParameters7(testCase5, "\"{[]}\"");
                testCaseRepository.save(testCase5);
        }

        private void buildParameters7(TestCase testCase, String s) {
                Parameter parameter = Parameter.builder()
                                .inputDataType("String")
                                .name("s")
                                .inputData(s)
                                .index(1)
                                .testCase(testCase)
                                .build();
                parameterRepository.save(parameter);
        }

        public Problem buildProblem8(User user) {
                List<Problem.ETopic> topics = new ArrayList<>();
                topics.add(Problem.ETopic.STRING);

                Problem problem = Problem.builder()
                                .owner(user)
                                .title("Check if a String is a Subsequence of Another")
                                .createdAt(LocalDateTime.now())
                                .updatedAt(LocalDateTime.now())
                                .description(
                                                "<h1 style=\"font-size: 2em; margin: 0.67em 0; font-weight: bold;text-align: center;\">Check if a String is a Subsequence of Another</h1>\r\n"
                                                                + //
                                                                "\r\n" + //
                                                                "---\r\n" + //
                                                                "\r\n" + //
                                                                "## 1. Description\r\n" + //
                                                                "\r\n" + //
                                                                "<!-- Provide a clear and concise description of the problem. \r\n"
                                                                + //
                                                                "    - What is the problem about?\r\n" + //
                                                                "    - What are the constraints?\r\n" + //
                                                                "    - What is the expected input and output?\r\n" + //
                                                                "    - Any special conditions or edge cases to consider?\r\n"
                                                                + //
                                                                "-->\r\n" + //
                                                                "\r\n" + //
                                                                "\r\n" + //
                                                                "Given two strings `s` and `t`, return `true` if `s` is a subsequence of `t`, or `false` otherwise.\n"
                                                                +
                                                                "\n" +
                                                                "A subsequence of a string is a new string generated from the original string with some characters (can be none) deleted without changing the relative order of the remaining characters.\n"
                                                                +
                                                                "---\n" +
                                                                "## 2. Examples\n" +
                                                                "---\n" +
                                                                "### Example 1: Basic Usage\n" +
                                                                "| Input          | Output          |\n" +
                                                                "| -------------- | --------------- |\n" +
                                                                "| s = \"abc\", t = \"ahbgdc\" | true |\n" +
                                                                "\n" +
                                                                "> **Explanation:** The string \"abc\" is a subsequence of \"ahbgdc\".\n"
                                                                +
                                                                "---\n" +
                                                                "### Example 2: Advanced Scenario\n" +
                                                                "| Input          | Output          |\n" +
                                                                "| -------------- | --------------- |\n" +
                                                                "| s = \"axc\", t = \"ahbgdc\" | false |\n" +
                                                                "\n" +
                                                                "> **Explanation:** The string \"axc\" is not a subsequence of \"ahbgdc\".\n"
                                                                +
                                                                "---\n" +
                                                                "> **Note:**\n" +
                                                                "Constraints:\n" +
                                                                "- `1 <= s.length, t.length <= 10^4`\n" +
                                                                "- `s` and `t` consist only of lowercase English letters.\n")
                                .difficulty(Problem.EDifficulty.NORMAL)
                                .topics(topics)
                                .functionName("isSubsequence")
                                .isDeleted(false)
                                .outputDataType("boolean")
                                .correctAnswer("public class Solution {\n" +
                                                "    public static boolean isSubsequence(String s, String t) {\n" +
                                                "        int i = 0, j = 0;\n" +
                                                "        while (i < s.length() && j < t.length()) {\n" +
                                                "            if (s.charAt(i) == t.charAt(j)) {\n" +
                                                "                i++;\n" +
                                                "            }\n" +
                                                "            j++;\n" +
                                                "        }\n" +
                                                "        return i == s.length();\n" +
                                                "    }\n" +
                                                "}")
                                .build();
                problem = problemRepository.save(problem);
                ProblemHint hint = ProblemHint.builder()
                                .isLocked(false)
                                .overView("## Overview\n\nTo determine if one string is a subsequence of another, iterate through both strings and check if all characters of the first string appear in the second string in order.")
                                .pseudoCode("## Pseudo Code\n\n```plaintext\nfunction isSubsequence(s, t):\n    i = 0, j = 0\n    while i < length of s and j < length of t do\n        if s[i] == t[j] then\n            increment i\n        increment j\n    return i == length of s\n```")
                        .problem(problem)
                        .build();
                ProblemHint savedHint = problemHintRepository.save(hint);

                problem.setProblemHint(hint);
                problem = problemRepository.save(problem);

                ProblemApproach approach1 = ProblemApproach.builder()
                                .intuition("## Intuition\n\nThe problem can be solved by using two pointers to iterate through both strings and checking if all characters of the first string appear in the second string in order.")
                                .algorithm("## Algorithm\n\n1. Initialize two pointers, i and j, to 0.\n" +
                                                "2. Iterate through both strings:\n" +
                                                "   - If the characters at the current positions of both strings match, increment the pointer for the first string.\n"
                                                +
                                                "   - Always increment the pointer for the second string.\n" +
                                                "3. If the pointer for the first string reaches its length, return true; otherwise, return false.")
                                .implementation("## Implementation\n\n```java\npublic static boolean isSubsequence(String s, String t) {\n"
                                                +
                                                "    int i = 0, j = 0;\n" +
                                                "    while (i < s.length() && j < t.length()) {\n" +
                                                "        if (s.charAt(i) == t.charAt(j)) {\n" +
                                                "            i++;\n" +
                                                "        }\n" +
                                                "        j++;\n" +
                                                "    }\n" +
                                                "    return i == s.length();\n" +
                                                "}\n```")
                                .language(Submission.ELanguage.JAVA)
                                .problemHint(savedHint)
                                .build();

                problemApproachRepository.save(approach1);

                List<ProblemApproach> approaches = new ArrayList<>();
                approaches.add(approach1);
                savedHint.setApproaches(approaches);
                problemHintRepository.save(savedHint);
                problemApproachRepository.save(approach1);

                buildTestCase8(problem);
                return problemRepository.save(problem);
        }

        private void buildTestCase8(Problem problem) {
                TestCase testCase1 = TestCase.builder()
                                .outputData("true")
                                .isPublic(true)
                                .problem(problem)
                                .build();
                buildParameters8(testCase1, "\"abc\"", "\"ahbgdc\"");
                testCaseRepository.save(testCase1);

                TestCase testCase2 = TestCase.builder()
                                .outputData("false")
                                .problem(problem)
                                .build();
                buildParameters8(testCase2, "\"axc\"", "\"ahbgdc\"");
                testCaseRepository.save(testCase2);

                TestCase testCase3 = TestCase.builder()
                                .outputData("true")
                                .problem(problem)
                                .build();
                buildParameters8(testCase3, "\"\"", "\"anything\"");
                testCaseRepository.save(testCase3);
        }

        private void buildParameters8(TestCase testCase, String s, String t) {
                Parameter parameter1 = Parameter.builder()
                                .inputDataType("String")
                                .name("s")
                                .inputData(s)
                                .index(1)
                                .testCase(testCase)
                                .build();

                Parameter parameter2 = Parameter.builder()
                                .inputDataType("String")
                                .name("t")
                                .inputData(t)
                                .index(2)
                                .testCase(testCase)
                                .build();

                parameterRepository.save(parameter1);
                parameterRepository.save(parameter2);
        }

        public Problem buildProblem9(User user) {
                List<Problem.ETopic> topics = new ArrayList<>();
                topics.add(Problem.ETopic.ARRAY);

                Problem problem = Problem.builder()
                                .owner(user)
                                .title("Find the Maximum Product of Three Numbers")
                                .createdAt(LocalDateTime.now())
                                .updatedAt(LocalDateTime.now())
                                .description(
                                                "<h1 style=\"font-size: 2em; margin: 0.67em 0; font-weight: bold;text-align: center;\">Find the Maximum Product of Three Numbers</h1>\r\n"
                                                                + //
                                                                "\r\n" + //
                                                                "---\r\n" + //
                                                                "\r\n" + //
                                                                "## 1. Description\r\n" + //
                                                                "\r\n" + //
                                                                "<!-- Provide a clear and concise description of the problem. \r\n"
                                                                + //
                                                                "    - What is the problem about?\r\n" + //
                                                                "    - What are the constraints?\r\n" + //
                                                                "    - What is the expected input and output?\r\n" + //
                                                                "    - Any special conditions or edge cases to consider?\r\n"
                                                                + //
                                                                "-->\r\n" + //
                                                                "\r\n" + //
                                                                "\r\n" + //
                                                                "Given an integer array `nums`, find three numbers whose product is maximum and return the maximum product.\n"
                                                                +
                                                                "\n" +
                                                                "Constraints:\n" +
                                                                "- `3 <= nums.length <= 10^4`\n" +
                                                                "- `-1000 <= nums[i] <= 1000`\n" +
                                                                "---\n" +
                                                                "## 2. Examples\n" +
                                                                "---\n" +
                                                                "### Example 1: Basic Usage\n" +
                                                                "| Input          | Output          |\n" +
                                                                "| -------------- | --------------- |\n" +
                                                                "| nums = [1,2,3] | 6               |\n" +
                                                                "\n" +
                                                                "> **Explanation:** The maximum product is obtained by multiplying 1, 2, and 3.\n"
                                                                +
                                                                "---\n" +
                                                                "### Example 2: Advanced Scenario\n" +
                                                                "| Input          | Output          |\n" +
                                                                "| -------------- | --------------- |\n" +
                                                                "| nums = [1,2,3,4] | 24            |\n" +
                                                                "\n" +
                                                                "> **Explanation:** The maximum product is obtained by multiplying 2, 3, and 4.\n"
                                                                +
                                                                "---\n" +
                                                                "### Example 3: Advanced Scenario\n" +
                                                                "| Input          | Output          |\n" +
                                                                "| -------------- | --------------- |\n" +
                                                                "| nums = [-1,-2,-3] | -6           |\n" +
                                                                "\n" +
                                                                "> **Explanation:** The maximum product is obtained by multiplying -1, -2, and -3.\n"
                                                                +
                                                                "---\n" +
                                                                "> **Note:** Ensure that the function handles both positive and negative integers correctly.\n")
                                .difficulty(Problem.EDifficulty.NORMAL)
                                .topics(topics)
                                .functionName("maximumProduct")
                                .isDeleted(false)
                                .outputDataType("int")
                                .correctAnswer("import java.util.Arrays;\n" +
                                                "\n" +
                                                "public class Solution {\n" +
                                                "    public static int maximumProduct(int[] nums) {\n" +
                                                "        Arrays.sort(nums);\n" +
                                                "        int n = nums.length;\n" +
                                                "        return Math.max(nums[0] * nums[1] * nums[n - 1], nums[n - 1] * nums[n - 2] * nums[n - 3]);\n"
                                                +
                                                "    }\n" +
                                                "}")
                                .build();

                problem = problemRepository.save(problem);
                ProblemHint hint = ProblemHint.builder()
                                .isLocked(false)
                                .overView("## Overview\n\nTo find the maximum product of three numbers, consider both the largest three numbers and the two smallest numbers with the largest number.")
                                .pseudoCode("## Pseudo Code\n\n```plaintext\nfunction maximumProduct(nums):\n    sort nums\n    n = length of nums\n    return max(nums[0] * nums[1] * nums[n-1], nums[n-1] * nums[n-2] * nums[n-3])\n```")
                        .problem(problem)
                        .build();
                ProblemHint savedHint = problemHintRepository.save(hint);

                problem.setProblemHint(hint);
                problem = problemRepository.save(problem);

                ProblemApproach approach1 = ProblemApproach.builder()
                                .intuition("## Intuition\n\nThe maximum product of three numbers can be obtained by either the product of the three largest numbers or the product of the two smallest numbers and the largest number.")
                                .algorithm("## Algorithm\n\n1. Sort the array.\n" +
                                                "2. Calculate the product of the three largest numbers.\n" +
                                                "3. Calculate the product of the two smallest numbers and the largest number.\n"
                                                +
                                                "4. Return the maximum of the two products.")
                                .implementation("## Implementation\n\n```java\npublic static int maximumProduct(int[] nums) {\n"
                                                +
                                                "    Arrays.sort(nums);\n" +
                                                "    int n = nums.length;\n" +
                                                "    return Math.max(nums[0] * nums[1] * nums[n - 1], nums[n - 1] * nums[n - 2] * nums[n - 3]);\n"
                                                +
                                                "}\n```")
                                .language(Submission.ELanguage.JAVA)
                                .problemHint(savedHint)
                                .build();

                problemApproachRepository.save(approach1);

                List<ProblemApproach> approaches = new ArrayList<>();
                approaches.add(approach1);
                savedHint.setApproaches(approaches);
                problemHintRepository.save(savedHint);
                problemApproachRepository.save(approach1);

                buildTestCase9(problem);
                buildLibrary9(problem);
                return problemRepository.save(problem);
        }

        private void buildLibrary9(Problem problem) {
                LibrariesSupport librariesSupport = LibrariesSupport.builder()
                                .name("java.util.Arrays")
                                .problem(problem)
                                .build();
                libraryRepository.save(librariesSupport);
        }

        private void buildTestCase9(Problem problem) {
                TestCase testCase1 = TestCase.builder()
                                .outputData("6")
                                .isPublic(true)
                                .problem(problem)
                                .build();
                buildParameters9(testCase1, "{1,2,3}");
                testCaseRepository.save(testCase1);

                TestCase testCase2 = TestCase.builder()
                                .outputData("24")
                                .problem(problem)
                                .build();
                buildParameters9(testCase2, "{1,2,3,4}");
                testCaseRepository.save(testCase2);

                TestCase testCase3 = TestCase.builder()
                                .outputData("-6")
                                .problem(problem)
                                .build();
                buildParameters9(testCase3, "{-1,-2,-3}");
                testCaseRepository.save(testCase3);
        }

        private void buildParameters9(TestCase testCase, String nums) {
                Parameter parameter = Parameter.builder()
                                .inputDataType("int[]")
                                .name("nums")
                                .inputData(nums)
                                .index(1)
                                .testCase(testCase)
                                .build();
                parameterRepository.save(parameter);
        }

        public Problem buildProblem10(User user) {
                List<Problem.ETopic> topics = new ArrayList<>();
                topics.add(Problem.ETopic.MATH);

                Problem problem = Problem.builder()
                                .owner(user)
                                .title("Count Primes Below N")
                                .createdAt(LocalDateTime.now())
                                .updatedAt(LocalDateTime.now())
                                .description(
                                                "<h1 style=\"font-size: 2em; margin: 0.67em 0; font-weight: bold;text-align: center;\">Count Primes Below N</h1>\r\n"
                                                                + //
                                                                "\r\n" + //
                                                                "---\r\n" + //
                                                                "## 1. Description\r\n" + //
                                                                "<!-- Provide a clear and concise description of the problem. \r\n"
                                                                + //
                                                                "    - What is the problem about?\r\n" + //
                                                                "    - What are the constraints?\r\n" + //
                                                                "    - What is the expected input and output?\r\n" + //
                                                                "    - Any special conditions or edge cases to consider?\r\n"
                                                                + //
                                                                "-->\r\n" + //
                                                                "Given an integer n, return the number of prime numbers that are strictly less than n.\r\n"
                                                                + //
                                                                "\r\n" + //
                                                                "Constraints:\r\n" + //
                                                                "- 0 <= n <= 5 * 10^6\r\n" + //
                                                                "\r\n" + //
                                                                "---\r\n" + //
                                                                "## 2. Examples\r\n" + //
                                                                "---\r\n" + //
                                                                "### Example 1: Basic Usage\r\n" + //
                                                                "| Input          | Output          |\r\n" + //
                                                                "| -------------- | --------------- |\r\n" + //
                                                                "| n = 10         | 4               |\r\n" + //
                                                                "\r\n" + //
                                                                "> **Explanation:** There are 4 prime numbers less than 10: 2, 3, 5, 7.\r\n"
                                                                + //
                                                                "\r\n" + //
                                                                "---\r\n" + //
                                                                "### Example 2: Advanced Scenario\r\n" + //
                                                                "| Input          | Output          |\r\n" + //
                                                                "| -------------- | --------------- |\r\n" + //
                                                                "| n = 0          | 0               |\r\n" + //
                                                                "\r\n" + //
                                                                "> **Explanation:** There are no prime numbers less than 0.\r\n"
                                                                + //
                                                                "\r\n" + //
                                                                "---\r\n" + //
                                                                "### Example 3: Advanced Scenario\r\n" + //
                                                                "| Input          | Output          |\r\n" + //
                                                                "| -------------- | --------------- |\r\n" + //
                                                                "| n = 1          | 0               |\r\n" + //
                                                                "\r\n" + //
                                                                "> **Explanation:** There are no prime numbers less than 1.\r\n"
                                                                + //
                                                                "\r\n" + //
                                                                "---\r\n" + //
                                                                "\r\n" + //
                                                                "> **Note:** Ensure that the function handles both small and large values of n efficiently.")
                                .difficulty(Problem.EDifficulty.NORMAL)
                                .topics(topics)
                                .functionName("countPrimes")
                                .isDeleted(false)
                                .outputDataType("int")
                                .correctAnswer("public class Solution {\n" +
                                                "    public static int countPrimes(int n) {\n" +
                                                "        if (n <= 2) return 0;\n" +
                                                "        boolean[] isPrime = new boolean[n];\n" +
                                                "        Arrays.fill(isPrime, true);\n" +
                                                "        isPrime[0] = isPrime[1] = false;\n" +
                                                "\n" +
                                                "        for (int i = 2; i * i < n; i++) {\n" +
                                                "            if (isPrime[i]) {\n" +
                                                "                for (int j = i * i; j < n; j += i) {\n" +
                                                "                    isPrime[j] = false;\n" +
                                                "                }\n" +
                                                "            }\n" +
                                                "        }\n" +
                                                "\n" +
                                                "        int count = 0;\n" +
                                                "        for (boolean prime : isPrime) {\n" +
                                                "            if (prime) count++;\n" +
                                                "        }\n" +
                                                "        return count;\n" +
                                                "    }\n" +
                                                "}")
                                .build();
                problem = problemRepository.save(problem);

                ProblemHint hint = ProblemHint.builder()
                                .isLocked(false)
                                .overView("## Overview\n\nTo count the number of primes below n, use the Sieve of Eratosthenes algorithm to mark non-prime numbers.")
                                .pseudoCode("## Pseudo Code\n\n```plaintext\nfunction countPrimes(n):\n    if n <= 2 then return 0\n    isPrime = array of boolean of size n, initialized to true\n    isPrime[0] = isPrime[1] = false\n    for i from 2 to sqrt(n) do\n        if isPrime[i] then\n            for j from i*i to n, step i do\n                isPrime[j] = false\n    count = 0\n    for each prime in isPrime do\n        if prime then count++\n    return count\n```")
                        .problem(problem)
                        .build();
                ProblemHint savedHint = problemHintRepository.save(hint);

                problem.setProblemHint(hint);
                problem = problemRepository.save(problem);

                ProblemApproach approach1 = ProblemApproach.builder()
                                .intuition("## Intuition\n\nThe Sieve of Eratosthenes efficiently finds all prime numbers up to a given limit by iteratively marking the multiples of each prime starting from 2.")
                                .algorithm("## Algorithm\n\n1. Initialize a boolean array `isPrime` of size n, set all entries to true.\n"
                                                +
                                                "2. Set `isPrime[0]` and `isPrime[1]` to false since 0 and 1 are not prime.\n"
                                                +
                                                "3. For each number i from 2 to sqrt(n):\n" +
                                                "   - If `isPrime[i]` is true, mark all multiples of i as false starting from i*i.\n"
                                                +
                                                "4. Count the number of true values in `isPrime` array, which represents the number of primes less than n.")
                                .implementation("## Implementation\n\n```java\npublic static int countPrimes(int n) {\n"
                                                +
                                                "    if (n <= 2) return 0;\n" +
                                                "    boolean[] isPrime = new boolean[n];\n" +
                                                "    Arrays.fill(isPrime, true);\n" +
                                                "    isPrime[0] = isPrime[1] = false;\n" +
                                                "    for (int i = 2; i * i < n; i++) {\n" +
                                                "        if (isPrime[i]) {\n" +
                                                "            for (int j = i * i; j < n; j += i) {\n" +
                                                "                isPrime[j] = false;\n" +
                                                "            }\n" +
                                                "        }\n" +
                                                "    }\n" +
                                                "    int count = 0;\n" +
                                                "    for (boolean prime : isPrime) {\n" +
                                                "        if (prime) count++;\n" +
                                                "    }\n" +
                                                "    return count;\n" +
                                                "}\n```")
                                .language(Submission.ELanguage.JAVA)
                                .problemHint(savedHint)
                                .build();

                problemApproachRepository.save(approach1);

                List<ProblemApproach> approaches = new ArrayList<>();
                approaches.add(approach1);
                savedHint.setApproaches(approaches);
                problemHintRepository.save(savedHint);
                problemApproachRepository.save(approach1);

                buildTestCase10(problem);
                buildLibrary10(problem);
                return problemRepository.save(problem);
        }

        private void buildLibrary10(Problem problem) {
                LibrariesSupport librariesSupport = LibrariesSupport.builder()
                                .name("java.util.Arrays")
                                .problem(problem)
                                .build();
                libraryRepository.save(librariesSupport);
        }

        private void buildTestCase10(Problem problem) {
                TestCase testCase1 = TestCase.builder()
                                .outputData("4")
                                .isPublic(true)
                                .problem(problem)
                                .build();
                buildParameters10(testCase1, "10");
                testCaseRepository.save(testCase1);

                TestCase testCase2 = TestCase.builder()
                                .outputData("0")
                                .problem(problem)
                                .build();
                buildParameters10(testCase2, "1");
                testCaseRepository.save(testCase2);

                TestCase testCase3 = TestCase.builder()
                                .outputData("0")
                                .problem(problem)
                                .build();
                buildParameters10(testCase3, "0");
                testCaseRepository.save(testCase3);
        }

        private void buildParameters10(TestCase testCase, String n) {
                Parameter parameter = Parameter.builder()
                                .inputDataType("int")
                                .name("n")
                                .inputData(n)
                                .index(1)
                                .testCase(testCase)
                                .build();
                parameterRepository.save(parameter);
        }

        public Problem buildProblem14(User user) {
                List<Problem.ETopic> topics = new ArrayList<>();
                topics.add(Problem.ETopic.ARRAY);

                Problem problem = Problem.builder()
                                .owner(user)
                                .title("Find the Maximum Number in an Array")
                                .createdAt(LocalDateTime.now())
                                .updatedAt(LocalDateTime.now())
                                .description(
                                                "<h1 style=\"font-size: 2em; margin: 0.67em 0; font-weight: bold;text-align: center;\">Find the Maximum Number in an Array</h1>\r\n"
                                                                + //
                                                                "\r\n" + //
                                                                "---\r\n" + //
                                                                "\r\n" + //
                                                                "## 1. Description\r\n" + //
                                                                "\r\n" + //
                                                                "<!-- Provide a clear and concise description of the problem. \r\n"
                                                                + //
                                                                "    - What is the problem about?\r\n" + //
                                                                "    - What are the constraints?\r\n" + //
                                                                "    - What is the expected input and output?\r\n" + //
                                                                "    - Any special conditions or edge cases to consider?\r\n"
                                                                + //
                                                                "-->\r\n" + //
                                                                "\r\n" + //
                                                                "\r\n" + //
                                                                "Given an integer array `nums`, return the maximum number in the array. \r\n"
                                                                + //

                                                                "---\n" +
                                                                "## 2. Examples\n" +
                                                                "---\n" +
                                                                "### Example 1: Basic Usage\n" +
                                                                "| Input          | Output          |\n" +
                                                                "| -------------- | --------------- |\n" +
                                                                "| nums = [1, 2, 3, 4, 5] | 5 |\n" +
                                                                "\n" +
                                                                "> **Explanation:** The maximum number in the array is 5.\n"
                                                                +
                                                                "---\n" +
                                                                "### Example 2: Advanced Scenario\n" +
                                                                "| Input          | Output          |\n" +
                                                                "| -------------- | --------------- |\n" +
                                                                "| nums = [-1, -2, -3, -4] | -1 |\n" +
                                                                "\n" +
                                                                "> **Explanation:** The maximum number in the array is -1.\n"
                                                                +
                                                                "---\n" +
                                                                "> **Note:**\n" +
                                                                "> Constraints:\n" +
                                                                "> - `1 <= nums.length <= 100`\n" +
                                                                "> - `-10^4 <= nums[i] <= 10^4`\n")
                                .difficulty(Problem.EDifficulty.EASY)
                                .topics(topics)
                                .functionName("findMax")
                                .isDeleted(false)
                                .outputDataType("int")
                                .correctAnswer("public class Solution {\n" +
                                                "    public int findMax(int[] nums) {\n" +
                                                "        int max = Integer.MIN_VALUE;\n" +
                                                "        for (int num : nums) {\n" +
                                                "            max = Math.max(max, num);\n" +
                                                "        }\n" +
                                                "        return max;\n" +
                                                "    }\n" +
                                                "}")
                                .build();
                problem = problemRepository.save(problem);
                ProblemHint hint = ProblemHint.builder()
                                .isLocked(false)
                                .overView("## Overview\n\nTo find the maximum number in an array, iterate through the array and keep track of the largest number encountered.")
                                .pseudoCode("## Pseudo Code\n\n```plaintext\nfunction findMax(nums):\n    max = -infinity\n    for num in nums do\n        if num > max then\n            max = num\n    return max\n```")
                        .problem(problem)
                        .build();
                ProblemHint savedHint = problemHintRepository.save(hint);

                problem.setProblemHint(hint);
                problem = problemRepository.save(problem);

                ProblemApproach approach1 = ProblemApproach.builder()
                                .intuition("## Intuition\n\nThe maximum number in an array can be found by iterating through the array and updating the maximum value whenever a larger number is found.")
                                .algorithm("## Algorithm\n\n1. Initialize a variable `max` to a very small number (e.g., Integer.MIN_VALUE).\n"
                                                +
                                                "2. Iterate through each number in the array:\n" +
                                                "   - If the current number is greater than `max`, update `max`.\n" +
                                                "3. After the loop, `max` will contain the largest number in the array.")
                                .implementation("## Implementation\n\n```java\npublic int findMax(int[] nums) {\n" +
                                                "    int max = Integer.MIN_VALUE;\n" +
                                                "    for (int num : nums) {\n" +
                                                "        max = Math.max(max, num);\n" +
                                                "    }\n" +
                                                "    return max;\n" +
                                                "}\n```")
                                .language(Submission.ELanguage.JAVA)
                                .problemHint(savedHint)
                                .build();

                problemApproachRepository.save(approach1);

                List<ProblemApproach> approaches = new ArrayList<>();
                approaches.add(approach1);
                savedHint.setApproaches(approaches);
                problemHintRepository.save(savedHint);
                problemApproachRepository.save(approach1);

                buildTestCase14(problem);
                return problemRepository.save(problem);
        }

        private void buildTestCase14(Problem problem) {
                TestCase testCase1 = TestCase.builder()
                                .outputData("5")
                                .isPublic(true)
                                .problem(problem)
                                .build();
                buildParameters14(testCase1, "{1, 2, 3, 4, 5}");
                testCaseRepository.save(testCase1);

                TestCase testCase2 = TestCase.builder()
                                .outputData("-1")
                                .problem(problem)
                                .build();
                buildParameters14(testCase2, "{-1, -2, -3, -4}");
                testCaseRepository.save(testCase2);
        }

        private void buildParameters14(TestCase testCase, String inputData) {
                Parameter parameter = Parameter.builder()
                                .inputDataType("int[]")
                                .name("nums")
                                .inputData(inputData)
                                .index(1)
                                .testCase(testCase)
                                .build();
                parameterRepository.save(parameter);
        }

        public Problem buildProblem15(User user) {
                List<Problem.ETopic> topics = new ArrayList<>();
                topics.add(Problem.ETopic.ARRAY);

                Problem problem = Problem.builder()
                                .owner(user)
                                .title("Find the Sum of Elements in an Array")
                                .createdAt(LocalDateTime.now())
                                .updatedAt(LocalDateTime.now())
                                .description(
                                                "<h1 style=\"font-size: 2em; margin: 0.67em 0; font-weight: bold;text-align: center;\">Find the Sum of Elements in an Array</h1>\r\n"
                                                                + //
                                                                "\r\n" + //
                                                                "---\r\n" + //
                                                                "\r\n" + //
                                                                "## 1. Description\r\n" + //
                                                                "\r\n" + //
                                                                "<!-- Provide a clear and concise description of the problem. \r\n"
                                                                + //
                                                                "    - What is the problem about?\r\n" + //
                                                                "    - What are the constraints?\r\n" + //
                                                                "    - What is the expected input and output?\r\n" + //
                                                                "    - Any special conditions or edge cases to consider?\r\n"
                                                                + //
                                                                "-->\r\n" + //
                                                                "\r\n" + //
                                                                "\r\n" + //

                                                                "Given an integer array `nums`, return the sum of all the elements in the array.\n"
                                                                +
                                                                "---\n" +
                                                                "## 2. Examples\n" +
                                                                "---\n" +
                                                                "### Example 1: Basic Usage\n" +
                                                                "| Input          | Output          |\n" +
                                                                "| -------------- | --------------- |\n" +
                                                                "| nums = [1, 2, 3, 4, 5] | 15 |\n" +
                                                                "\n" +
                                                                "> **Explanation:** The sum of the elements is 1 + 2 + 3 + 4 + 5 = 15.\n"
                                                                +
                                                                "---\n" +
                                                                "### Example 2: Advanced Scenario\n" +
                                                                "| Input          | Output          |\n" +
                                                                "| -------------- | --------------- |\n" +
                                                                "| nums = [-1, -2, -3, -4] | -10 |\n" +
                                                                "\n" +
                                                                "> **Explanation:** The sum of the elements is -1 + -2 + -3 + -4 = -10.\n"
                                                                +
                                                                "---\n" +
                                                                "> **Note:**\n" +
                                                                "Constraints:\n" +
                                                                "- `1 <= nums.length <= 100`\n" +
                                                                "- `-10^4 <= nums[i] <= 10^4`\n")
                                .difficulty(Problem.EDifficulty.EASY)
                                .topics(topics)
                                .functionName("findSum")
                                .isDeleted(false)
                                .outputDataType("int")
                                .correctAnswer("public class Solution {\n" +
                                                "    public int findSum(int[] nums) {\n" +
                                                "        int sum = 0;\n" +
                                                "        for (int num : nums) {\n" +
                                                "            sum += num;\n" +
                                                "        }\n" +
                                                "        return sum;\n" +
                                                "    }\n" +
                                                "}")
                                .build();

                problem = problemRepository.save(problem);
                ProblemHint hint = ProblemHint.builder()
                                .isLocked(false)
                                .overView("## Overview\n\nTo find the sum of elements in an array, iterate through the array and accumulate the sum of all elements.")
                                .pseudoCode("## Pseudo Code\n\n```plaintext\nfunction findSum(nums):\n    sum = 0\n    for num in nums do\n        sum += num\n    return sum\n```")
                        .problem(problem)
                        .build();
                ProblemHint savedHint = problemHintRepository.save(hint);

                problem.setProblemHint(hint);
                problem = problemRepository.save(problem);

                ProblemApproach approach1 = ProblemApproach.builder()
                                .intuition("## Intuition\n\nThe sum of elements in an array can be found by iterating through the array and adding each element to a running total.")
                                .algorithm("## Algorithm\n\n1. Initialize a variable `sum` to 0.\n" +
                                                "2. Iterate through each number in the array:\n" +
                                                "   - Add the current number to `sum`.\n" +
                                                "3. After the loop, `sum` will contain the total sum of the array elements.")
                                .implementation("## Implementation\n\n```java\npublic int findSum(int[] nums) {\n" +
                                                "    int sum = 0;\n" +
                                                "    for (int num : nums) {\n" +
                                                "        sum += num;\n" +
                                                "    }\n" +
                                                "    return sum;\n" +
                                                "}\n```")
                                .language(Submission.ELanguage.JAVA)
                                .problemHint(savedHint)
                                .build();

                problemApproachRepository.save(approach1);

                List<ProblemApproach> approaches = new ArrayList<>();
                approaches.add(approach1);
                savedHint.setApproaches(approaches);
                problemHintRepository.save(savedHint);
                problemApproachRepository.save(approach1);

                buildTestCase15(problem);
                return problemRepository.save(problem);
        }

        private void buildTestCase15(Problem problem) {
                TestCase testCase1 = TestCase.builder()
                                .outputData("15")
                                .isPublic(true)
                                .problem(problem)
                                .build();
                buildParameters15(testCase1, "{1, 2, 3, 4, 5}");
                testCaseRepository.save(testCase1);

                TestCase testCase2 = TestCase.builder()
                                .outputData("-10")
                                .problem(problem)
                                .build();
                buildParameters15(testCase2, "{-1, -2, -3, -4}");
                testCaseRepository.save(testCase2);
        }

        private void buildParameters15(TestCase testCase, String inputData) {
                Parameter parameter = Parameter.builder()
                                .inputDataType("int[]")
                                .name("nums")
                                .inputData(inputData)
                                .index(1)
                                .testCase(testCase)
                                .build();
                parameterRepository.save(parameter);
        }

        public Problem buildProblem17(User user) {
                List<Problem.ETopic> topics = new ArrayList<>();
                topics.add(Problem.ETopic.STRING);

                Problem problem = Problem.builder()
                                .owner(user)
                                .title("Check if a String is a Palindrome")
                                .createdAt(LocalDateTime.now())
                                .updatedAt(LocalDateTime.now())
                                .description(
                                                "<h1 style=\"font-size: 2em; margin: 0.67em 0; font-weight: bold;text-align: center;\">Check if a String is a Palindrome</h1>\r\n"
                                                                + //
                                                                "\r\n" + //
                                                                "---\r\n" + //
                                                                "\r\n" + //
                                                                "## 1. Description\r\n" + //
                                                                "\r\n" + //
                                                                "<!-- Provide a clear and concise description of the problem. \r\n"
                                                                + //
                                                                "    - What is the problem about?\r\n" + //
                                                                "    - What are the constraints?\r\n" + //
                                                                "    - What is the expected input and output?\r\n" + //
                                                                "    - Any special conditions or edge cases to consider?\r\n"
                                                                + //
                                                                "-->\r\n" + //
                                                                "\r\n" + //
                                                                "\r\n" + //
                                                                "Given a string s, return true if it is a palindrome, and false otherwise.\n"
                                                                +
                                                                "A string is a palindrome if it reads the same forward and backward.\n"
                                                                +
                                                                "---\n" +
                                                                "## 2. Examples\n" +
                                                                "---\n" +
                                                                "### Example 1: Basic Usage\n" +
                                                                "| Input          | Output          |\n" +
                                                                "| -------------- | --------------- |\n" +
                                                                "| s = \"racecar\"  | true            |\n" +
                                                                "\n" +
                                                                "> **Explanation:** The string \"racecar\" is the same forward and backward.\n"
                                                                +
                                                                "---\n" +
                                                                "### Example 2: Advanced Scenario\n" +
                                                                "| Input          | Output          |\n" +
                                                                "| -------------- | --------------- |\n" +
                                                                "| s = \"hello\"    | false           |\n" +
                                                                "\n" +
                                                                "> **Explanation:** The string \"hello\" is not the same forward and backward.\n"
                                                                +
                                                                "---\n" +
                                                                "> **Note:**\n" +
                                                                "Constraints:\n" +
                                                                "- `1 <= s.length <= 1000`\n" +
                                                                "- `s` consists of only lowercase English letters.\n")
                                .difficulty(Problem.EDifficulty.EASY)
                                .topics(topics)
                                .functionName("isPalindrome")
                                .isDeleted(false)
                                .outputDataType("boolean")
                                .correctAnswer("public class Solution {\n" +
                                                "    public boolean isPalindrome(String s) {\n" +
                                                "        int left = 0, right = s.length() - 1;\n" +
                                                "        while (left < right) {\n" +
                                                "            if (s.charAt(left) != s.charAt(right)) {\n" +
                                                "                return false;\n" +
                                                "            }\n" +
                                                "            left++;\n" +
                                                "            right--;\n" +
                                                "        }\n" +
                                                "        return true;\n" +
                                                "    }\n" +
                                                "}")
                                .build();

                problem = problemRepository.save(problem);
                ProblemHint hint = ProblemHint.builder()
                                .isLocked(false)
                                .overView("## Overview\n\nTo check if a string is a palindrome, use two pointers to compare characters from the start and end of the string moving towards the center.")
                                .pseudoCode("## Pseudo Code\n\n```plaintext\nfunction isPalindrome(s):\n    left = 0, right = length of s - 1\n    while left < right do\n        if s[left] != s[right] then\n            return false\n        increment left\n        decrement right\n    return true\n```")
                        .problem(problem)
                        .build();
                ProblemHint savedHint = problemHintRepository.save(hint);

                problem.setProblemHint(hint);
                problem = problemRepository.save(problem);

                ProblemApproach approach1 = ProblemApproach.builder()
                                .intuition("## Intuition\n\nA palindrome reads the same forwards and backwards, so we can use two pointers to check characters from both ends.")
                                .algorithm("## Algorithm\n\n1. Initialize two pointers, one at the start and one at the end of the string.\n"
                                                +
                                                "2. Compare the characters at these pointers.\n" +
                                                "3. If they are the same, move the pointers towards the center.\n" +
                                                "4. If any characters differ, the string is not a palindrome.\n" +
                                                "5. If all characters match, the string is a palindrome.")
                                .implementation("## Implementation\n\n```java\npublic boolean isPalindrome(String s) {\n"
                                                +
                                                "    int left = 0, right = s.length() - 1;\n" +
                                                "    while (left < right) {\n" +
                                                "        if (s.charAt(left) != s.charAt(right)) {\n" +
                                                "            return false;\n" +
                                                "        }\n" +
                                                "        left++;\n" +
                                                "        right--;\n" +
                                                "    }\n" +
                                                "    return true;\n" +
                                                "}\n```")
                                .language(Submission.ELanguage.JAVA)
                                .problemHint(savedHint)
                                .build();

                problemApproachRepository.save(approach1);

                List<ProblemApproach> approaches = new ArrayList<>();
                approaches.add(approach1);
                savedHint.setApproaches(approaches);
                problemHintRepository.save(savedHint);
                problemApproachRepository.save(approach1);

                buildTestCase17(problem);

                return problemRepository.save(problem);
        }

        private void buildTestCase17(Problem problem) {
                TestCase testCase1 = TestCase.builder()
                                .outputData("true")
                                .isPublic(true)
                                .problem(problem)
                                .build();
                buildParameters17(testCase1, "\"racecar\"");
                testCaseRepository.save(testCase1);

                TestCase testCase2 = TestCase.builder()
                                .outputData("false")
                                .problem(problem)
                                .build();
                buildParameters17(testCase2, "\"hello\"");
                testCaseRepository.save(testCase2);
        }

        private void buildParameters17(TestCase testCase, String inputData) {
                Parameter parameter = Parameter.builder()
                                .inputDataType("String")
                                .name("s")
                                .inputData(inputData)
                                .index(1)
                                .testCase(testCase)
                                .build();
                parameterRepository.save(parameter);
        }

        public Problem buildProblem21(User user) {
                List<Problem.ETopic> topics = new ArrayList<>();
                topics.add(Problem.ETopic.ARRAY);

                Problem problem = Problem.builder()
                                .owner(user)
                                .title("Find Minimum in Rotated Sorted Array")
                                .createdAt(LocalDateTime.now())
                                .updatedAt(LocalDateTime.now())
                                .description(
                                                "<h1 style=\"font-size: 2em; margin: 0.67em 0; font-weight: bold;text-align: center;\">Find Minimum in Rotated Sorted Array</h1>\r\n"
                                                                + //
                                                                "\r\n" + //
                                                                "---\r\n" + //
                                                                "## 1. Description\r\n" + //
                                                                "<!-- Provide a clear and concise description of the problem. \r\n"
                                                                + //
                                                                "    - What is the problem about?\r\n" + //
                                                                "    - What are the constraints?\r\n" + //
                                                                "    - What is the expected input and output?\r\n" + //
                                                                "    - Any special conditions or edge cases to consider?\r\n"
                                                                + //
                                                                "-->\r\n" + //
                                                                "Suppose an array of length n sorted in ascending order is rotated between 1 and n times. For example, the array `nums = [0,1,2,4,5,6,7]` might become `[4,5,6,7,0,1,2]` if it was rotated 4 times.\r\n"
                                                                + //
                                                                "\r\n" + //
                                                                "Given the sorted rotated array `nums` of unique elements, return the minimum element of this array.\r\n"
                                                                + //
                                                                "\r\n" + //
                                                                "Constraints:\r\n" + //
                                                                "- `n == nums.length`\r\n" + //
                                                                "- `1 <= n <= 5000`\r\n" + //
                                                                "- `-5000 <= nums[i] <= 5000`\r\n" + //
                                                                "- All the integers of `nums` are unique.\r\n" + //
                                                                "- `nums` is guaranteed to be rotated at least once.\r\n"
                                                                + //
                                                                "\r\n" + //
                                                                "---\r\n" + //
                                                                "## 2. Examples\r\n" + //
                                                                "---\r\n" + //
                                                                "### Example 1: Basic Usage\r\n" + //
                                                                "| Input          | Output          |\r\n" + //
                                                                "| -------------- | --------------- |\r\n" + //
                                                                "| nums = [3,4,5,1,2] | 1 |\r\n" + //
                                                                "\r\n" + //
                                                                "> **Explanation:** The original array was `[1,2,3,4,5]` rotated 3 times.\r\n"
                                                                + //
                                                                "\r\n" + //
                                                                "---\r\n" + //
                                                                "### Example 2: Advanced Scenario\r\n" + //
                                                                "| Input          | Output          |\r\n" + //
                                                                "| -------------- | --------------- |\r\n" + //
                                                                "| nums = [4,5,6,7,0,1,2] | 0 |\r\n" + //
                                                                "\r\n" + //
                                                                "> **Explanation:** The original array was `[0,1,2,4,5,6,7]` rotated 4 times.\r\n"
                                                                + //
                                                                "\r\n" + //
                                                                "---\r\n" + //
                                                                "### Example 3: Advanced Scenario\r\n" + //
                                                                "| Input          | Output          |\r\n" + //
                                                                "| -------------- | --------------- |\r\n" + //
                                                                "| nums = [11,13,15,17] | 11 |\r\n" + //
                                                                "\r\n" + //
                                                                "> **Explanation:** The original array was `[11,13,15,17]` and it was rotated 0 times.\r\n"
                                                                + //
                                                                "\r\n" + //
                                                                "---\r\n" + //
                                                                "\r\n")
                                .difficulty(Problem.EDifficulty.EASY)
                                .topics(topics)
                                .functionName("findMin")
                                .isDeleted(false)
                                .outputDataType("int")
                                .correctAnswer("public class Solution {\n" +
                                                "    public int findMin(int[] nums) {\n" +
                                                "        int left = 0, right = nums.length - 1;\n" +
                                                "        while (left < right) {\n" +
                                                "            int mid = left + (right - left) / 2;\n" +
                                                "            if (nums[mid] > nums[right]) {\n" +
                                                "                left = mid + 1;\n" +
                                                "            } else {\n" +
                                                "                right = mid;\n" +
                                                "            }\n" +
                                                "        }\n" +
                                                "        return nums[left];\n" +
                                                "    }\n" +
                                                "}")
                                .build();
                problem = problemRepository.save(problem);
                ProblemHint hint = ProblemHint.builder()
                                .isLocked(false)
                                .overView("## Overview\n\nTo find the minimum in a rotated sorted array, use binary search to efficiently locate the pivot point where the rotation occurs.")
                                .pseudoCode("## Pseudo Code\n\n```plaintext\nfunction findMin(nums):\n    left = 0, right = length of nums - 1\n    while left < right do\n        mid = left + (right - left) / 2\n        if nums[mid] > nums[right] then\n            left = mid + 1\n        else\n            right = mid\n    return nums[left]\n```")
                        .problem(problem)
                                .build();
                ProblemHint savedHint = problemHintRepository.save(hint);

                problem.setProblemHint(hint);
                problem = problemRepository.save(problem);

                ProblemApproach approach1 = ProblemApproach.builder()
                                .intuition("## Intuition\n\nThe minimum element in a rotated sorted array can be found by identifying the point of rotation using binary search.")
                                .algorithm("## Algorithm\n\n1. Initialize two pointers, `left` and `right`, at the start and end of the array.\n"
                                                +
                                                "2. Use binary search to find the pivot point where the array is rotated.\n"
                                                +
                                                "3. If the middle element is greater than the rightmost element, the minimum is in the right half.\n"
                                                +
                                                "4. Otherwise, the minimum is in the left half.\n" +
                                                "5. Continue until `left` meets `right`, which will be the index of the minimum element.")
                                .implementation("## Implementation\n\n```java\npublic int findMin(int[] nums) {\n" +
                                                "    int left = 0, right = nums.length - 1;\n" +
                                                "    while (left < right) {\n" +
                                                "        int mid = left + (right - left) / 2;\n" +
                                                "        if (nums[mid] > nums[right]) {\n" +
                                                "            left = mid + 1;\n" +
                                                "        } else {\n" +
                                                "            right = mid;\n" +
                                                "        }\n" +
                                                "    }\n" +
                                                "    return nums[left];\n" +
                                                "}\n```")
                                .language(Submission.ELanguage.JAVA)
                                .problemHint(savedHint)
                                .build();

                problemApproachRepository.save(approach1);

                List<ProblemApproach> approaches = new ArrayList<>();
                approaches.add(approach1);
                savedHint.setApproaches(approaches);
                problemHintRepository.save(savedHint);
                problemApproachRepository.save(approach1);

                buildTestCase21(problem);
                return problemRepository.save(problem);
        }

        private void buildTestCase21(Problem problem) {
                TestCase testCase1 = TestCase.builder()
                                .outputData("1")
                                .isPublic(true)
                                .problem(problem)
                                .build();
                buildParameters21(testCase1, "{3,4,5,1,2}");
                testCaseRepository.save(testCase1);

                TestCase testCase2 = TestCase.builder()
                                .outputData("0")
                                .problem(problem)
                                .build();
                buildParameters21(testCase2, "{4,5,6,7,0,1,2}");
                testCaseRepository.save(testCase2);

                TestCase testCase3 = TestCase.builder()
                                .outputData("11")
                                .problem(problem)
                                .build();
                buildParameters21(testCase3, "{11,13,15,17}");
                testCaseRepository.save(testCase3);
        }

        private void buildParameters21(TestCase testCase, String nums) {
                Parameter parameter1 = Parameter.builder()
                                .inputDataType("int[]")
                                .name("nums")
                                .inputData(nums)
                                .index(1)
                                .testCase(testCase)
                                .build();

                parameterRepository.save(parameter1);
        }

        public Problem buildProblem22(User user) {
                List<Problem.ETopic> topics = new ArrayList<>();
                topics.add(Problem.ETopic.MATH);

                Problem problem = Problem.builder()
                                .owner(user)
                                .title("Fibonacci Number")
                                .createdAt(LocalDateTime.now())
                                .updatedAt(LocalDateTime.now())
                                .description(
                                                "<h1 style=\"font-size: 2em; margin: 0.67em 0; font-weight: bold;text-align: center;\">Fibonacci Number</h1>\r\n"
                                                                + //
                                                                "\r\n" + //
                                                                "---\r\n" + //
                                                                "## 1. Description\r\n" + //
                                                                "<!-- Provide a clear and concise description of the problem. \r\n"
                                                                + //
                                                                "    - What is the problem about?\r\n" + //
                                                                "    - What are the constraints?\r\n" + //
                                                                "    - What is the expected input and output?\r\n" + //
                                                                "    - Any special conditions or edge cases to consider?\r\n"
                                                                + //
                                                                "-->\r\n" + //
                                                                "The Fibonacci numbers, commonly denoted F(n), form a sequence of numbers such that each number is the sum of the two preceding ones, starting from 0 and 1. That is,\r\n"
                                                                + //
                                                                "\r\n" + //
                                                                "F(0) = 0, F(1) = 1\r\n" + //
                                                                "F(n) = F(n - 1) + F(n - 2), for n > 1.\r\n" + //
                                                                "\r\n" + //
                                                                "Given n, calculate F(n).\r\n" + //
                                                                "\r\n" + //
                                                                "Constraints:\r\n" + //
                                                                "- 0 <= n <= 30\r\n" + //
                                                                "\r\n" + //
                                                                "![alt text](https://agilebox.app/wp-content/uploads/2023/05/Fibonacci-Sequence-e1685094237832-600x383.png)\r\n"
                                                                +
                                                                "---\r\n" + //
                                                                "## 2. Examples\r\n" + //
                                                                "---\r\n" + //
                                                                "### Example 1: Basic Usage\r\n" + //
                                                                "| Input          | Output          |\r\n" + //
                                                                "| -------------- | --------------- |\r\n" + //
                                                                "| n = 2          | 1               |\r\n" + //
                                                                "\r\n" + //
                                                                "> **Explanation:** F(2) = F(1) + F(0) = 1 + 0 = 1.\r\n"
                                                                + //
                                                                "\r\n" + //
                                                                "---\r\n" + //
                                                                "### Example 2: Advanced Scenario\r\n" + //
                                                                "| Input          | Output          |\r\n" + //
                                                                "| -------------- | --------------- |\r\n" + //
                                                                "| n = 3          | 2               |\r\n" + //
                                                                "\r\n" + //
                                                                "> **Explanation:** F(3) = F(2) + F(1) = 1 + 1 = 2.\r\n"
                                                                + //
                                                                "\r\n" + //
                                                                "---\r\n" + //
                                                                "### Example 3: Advanced Scenario\r\n" + //
                                                                "| Input          | Output          |\r\n" + //
                                                                "| -------------- | --------------- |\r\n" + //
                                                                "| n = 4          | 3               |\r\n" + //
                                                                "\r\n" + //
                                                                "> **Explanation:** F(4) = F(3) + F(2) = 2 + 1 = 3.\r\n"
                                                                + //
                                                                "\r\n" + //
                                                                "---\r\n" + //
                                                                "\r\n" + //
                                                                "> **Note:**")
                                .difficulty(Problem.EDifficulty.EASY)
                                .topics(topics)
                                .functionName("fib")
                                .isDeleted(false)
                                .outputDataType("int")
                                .correctAnswer("public class Solution {\n" +
                                                "    public int fib(int n) {\n" +
                                                "        if (n <= 1) return n;\n" +
                                                "        int a = 0, b = 1;\n" +
                                                "        for (int i = 2; i <= n; i++) {\n" +
                                                "            int temp = a + b;\n" +
                                                "            a = b;\n" +
                                                "            b = temp;\n" +
                                                "        }\n" +
                                                "        return b;\n" +
                                                "    }\n" +
                                                "}")
                                .build();

                problem = problemRepository.save(problem);
                ProblemHint hint = ProblemHint.builder()
                                .isLocked(false)
                                .overView("## Overview\n\nTo calculate the Fibonacci number, use an iterative approach to sum the two preceding numbers.")
                                .pseudoCode("## Pseudo Code\n\n```plaintext\nfunction fib(n):\n    if n <= 1 then return n\n    a = 0, b = 1\n    for i from 2 to n do\n        temp = a + b\n        a = b\n        b = temp\n    return b\n```")
                        .problem(problem)
                        .build();
                ProblemHint savedHint = problemHintRepository.save(hint);

                problem.setProblemHint(hint);
                problem = problemRepository.save(problem);

                ProblemApproach approach1 = ProblemApproach.builder()
                                .intuition("## Intuition\n\nThe Fibonacci sequence can be efficiently calculated using an iterative approach to avoid the overhead of recursion.")
                                .algorithm("## Algorithm\n\n1. Initialize two variables, `a` and `b`, to represent the first two Fibonacci numbers.\n"
                                                +
                                                "2. Iterate from 2 to n, updating `a` and `b` to the next Fibonacci numbers.\n"
                                                +
                                                "3. Return `b`, which will be the nth Fibonacci number.")
                                .implementation("## Implementation\n\n```java\npublic int fib(int n) {\n" +
                                                "    if (n <= 1) return n;\n" +
                                                "    int a = 0, b = 1;\n" +
                                                "    for (int i = 2; i <= n; i++) {\n" +
                                                "        int temp = a + b;\n" +
                                                "        a = b;\n" +
                                                "        b = temp;\n" +
                                                "    }\n" +
                                                "    return b;\n" +
                                                "}\n```")
                                .language(Submission.ELanguage.JAVA)
                                .problemHint(savedHint)
                                .build();

                problemApproachRepository.save(approach1);

                List<ProblemApproach> approaches = new ArrayList<>();
                approaches.add(approach1);
                savedHint.setApproaches(approaches);
                problemHintRepository.save(savedHint);
                problemApproachRepository.save(approach1);

                buildTestCase22(problem);

                return problemRepository.save(problem);
        }

        private void buildTestCase22(Problem problem) {
                TestCase testCase1 = TestCase.builder()
                                .outputData("1")
                                .isPublic(true)
                                .problem(problem)
                                .build();
                buildParameters22(testCase1, "2");
                testCaseRepository.save(testCase1);

                TestCase testCase2 = TestCase.builder()
                                .outputData("2")
                                .problem(problem)
                                .build();
                buildParameters22(testCase2, "3");
                testCaseRepository.save(testCase2);

                TestCase testCase3 = TestCase.builder()
                                .outputData("3")
                                .problem(problem)
                                .build();
                buildParameters22(testCase3, "4");
                testCaseRepository.save(testCase3);
        }

        private void buildParameters22(TestCase testCase, String n) {
                Parameter parameter1 = Parameter.builder()
                                .inputDataType("int")
                                .name("n")
                                .inputData(n)
                                .index(1)
                                .testCase(testCase)
                                .build();

                parameterRepository.save(parameter1);
        }

        public Problem buildProblem23(User user) {
                List<Problem.ETopic> topics = new ArrayList<>();
                topics.add(Problem.ETopic.ARRAY);

                Problem problem = Problem.builder()
                                .owner(user)
                                .title("Count Even Numbers")
                                .createdAt(LocalDateTime.now())
                                .updatedAt(LocalDateTime.now())
                                .description(
                                                "<h1 style=\"font-size: 2em; margin: 0.67em 0; font-weight: bold;text-align: center;\">Count Even Numbers</h1>\r\n"
                                                                + //
                                                                "\r\n" + //
                                                                "---\r\n" + //
                                                                "\r\n" + //
                                                                "## 1. Description\r\n" + //
                                                                "\r\n" + //
                                                                "<!-- Provide a clear and concise description of the problem. \r\n"
                                                                + //
                                                                "    - What is the problem about?\r\n" + //
                                                                "    - What are the constraints?\r\n" + //
                                                                "    - What is the expected input and output?\r\n" + //
                                                                "    - Any special conditions or edge cases to consider?\r\n"
                                                                + //
                                                                "-->\r\n" + //
                                                                "\r\n" + //
                                                                "\r\n" + //
                                                                "Given an array of integers, return the number of even numbers in the array.\n"
                                                                +
                                                                "\n" +
                                                                "Constraints:\n" +
                                                                "- `1 <= nums.length <= 100`\n" +
                                                                "- `1 <= nums[i] <= 1000`\n" +
                                                                "---\n" +
                                                                "## 2. Examples\n" +
                                                                "---\n" +
                                                                "### Example 1: Basic Usage\n" +
                                                                "| Input          | Output          |\n" +
                                                                "| -------------- | --------------- |\n" +
                                                                "| nums = [1, 2, 3, 4] | 2 |\n" +
                                                                "\n" +
                                                                "> **Explanation:** There are two even numbers in the array: 2 and 4.\n"
                                                                +
                                                                "---\n" +
                                                                "### Example 2: Advanced Scenario\n" +
                                                                "| Input          | Output          |\n" +
                                                                "| -------------- | --------------- |\n" +
                                                                "| nums = [1, 3, 5, 7] | 0 |\n" +
                                                                "\n" +
                                                                "> **Explanation:** There are no even numbers in the array.\n"
                                                                +
                                                                "---\n" +
                                                                "### Example 3: Advanced Scenario\n" +
                                                                "| Input          | Output          |\n" +
                                                                "| -------------- | --------------- |\n" +
                                                                "| nums = [2, 4, 6, 8, 10] | 5 |\n" +
                                                                "\n" +
                                                                "> **Explanation:** All numbers in the array are even.\n")
                                .difficulty(Problem.EDifficulty.EASY)
                                .topics(topics)
                                .functionName("countEven")
                                .isDeleted(false)
                                .outputDataType("int")
                                .correctAnswer("public class Solution {\n" +
                                                "    public int countEven(int[] nums) {\n" +
                                                "        int count = 0;\n" +
                                                "        for (int num : nums) {\n" +
                                                "            if (num % 2 == 0) {\n" +
                                                "                count++;\n" +
                                                "            }\n" +
                                                "        }\n" +
                                                "        return count;\n" +
                                                "    }\n" +
                                                "}")
                                .build();

                problem = problemRepository.save(problem);
                ProblemHint hint = ProblemHint.builder()
                                .isLocked(false)
                                .overView("## Overview\n\nTo count the even numbers in an array, iterate through the array and increment a counter each time an even number is found.")
                                .pseudoCode("## Pseudo Code\n\n```plaintext\nfunction countEven(nums):\n    count = 0\n    for num in nums do\n        if num % 2 == 0 then\n            count++\n    return count\n```")
                        .problem(problem)
                        .build();
                ProblemHint savedHint = problemHintRepository.save(hint);

                problem.setProblemHint(hint);
                problem = problemRepository.save(problem);

                ProblemApproach approach1 = ProblemApproach.builder()
                                .intuition("## Intuition\n\nThe number of even numbers in an array can be found by iterating through the array and checking each number for evenness.")
                                .algorithm("## Algorithm\n\n1. Initialize a counter to zero.\n2. Iterate through each number in the array:\n   - If the number is even (i.e., divisible by 2), increment the counter.\n3. Return the counter as the result.")
                                .implementation("## Implementation\n\n```java\npublic int countEven(int[] nums) {\n    int count = 0;\n    for (int num : nums) {\n        if (num % 2 == 0) {\n            count++;\n        }\n    }\n    return count;\n}\n```")
                                .language(Submission.ELanguage.JAVA)
                                .problemHint(savedHint)
                                .build();

                problemApproachRepository.save(approach1);

                List<ProblemApproach> approaches = new ArrayList<>();
                approaches.add(approach1);
                savedHint.setApproaches(approaches);
                problemHintRepository.save(savedHint);
                problemApproachRepository.save(approach1);

                buildTestCase23(problem);

                return problemRepository.save(problem);
        }

        private void buildTestCase23(Problem problem) {
                TestCase testCase1 = TestCase.builder()
                                .outputData("2")
                                .isPublic(true)
                                .problem(problem)
                                .build();
                buildParameters23(testCase1, "{1, 2, 3, 4}");
                testCaseRepository.save(testCase1);

                TestCase testCase2 = TestCase.builder()
                                .outputData("0")
                                .problem(problem)
                                .build();
                buildParameters23(testCase2, "{1, 3, 5, 7}");
                testCaseRepository.save(testCase2);

                TestCase testCase3 = TestCase.builder()
                                .outputData("5")
                                .problem(problem)
                                .build();
                buildParameters23(testCase3, "{2, 4, 6, 8, 10}");
                testCaseRepository.save(testCase3);
        }

        private void buildParameters23(TestCase testCase, String nums) {
                Parameter parameter1 = Parameter.builder()
                                .inputDataType("int[]")
                                .name("nums")
                                .inputData(nums)
                                .index(1)
                                .testCase(testCase)
                                .build();

                parameterRepository.save(parameter1);
        }

        public Problem buildProblem24(User user) {
                List<Problem.ETopic> topics = new ArrayList<>();
                topics.add(Problem.ETopic.MATH);

                Problem problem = Problem.builder()
                                .owner(user)
                                .title("Check Prime Number")
                                .createdAt(LocalDateTime.now())
                                .updatedAt(LocalDateTime.now())
                                .description(
                                                "<h1 style=\"font-size: 2em; margin: 0.67em 0; font-weight: bold;text-align: center;\">Check Prime Number</h1>\r\n"
                                                                + //
                                                                "\r\n" + //
                                                                "---\r\n" + //
                                                                "## 1. Description\r\n" + //
                                                                "<!-- Provide a clear and concise description of the problem. \r\n"
                                                                + //
                                                                "    - What is the problem about?\r\n" + //
                                                                "    - What are the constraints?\r\n" + //
                                                                "    - What is the expected input and output?\r\n" + //
                                                                "    - Any special conditions or edge cases to consider?\r\n"
                                                                + //
                                                                "-->\r\n" + //
                                                                "Given an integer n, return true if it is a prime number, and false otherwise.\r\n"
                                                                + //
                                                                "\r\n" + //
                                                                "A prime number is a natural number greater than 1 that cannot be formed by multiplying two smaller natural numbers.\r\n"
                                                                + //
                                                                "\r\n" + //
                                                                "Constraints:\r\n" + //
                                                                "- 1 <= n <= 1000\r\n" + //
                                                                "\r\n" + //
                                                                "---\r\n" + //
                                                                "## 2. Examples\r\n" + //
                                                                "---\r\n" + //
                                                                "### Example 1: Basic Usage\r\n" + //
                                                                "| Input          | Output          |\r\n" + //
                                                                "| -------------- | --------------- |\r\n" + //
                                                                "| n = 5          | true            |\r\n" + //
                                                                "\r\n" + //
                                                                "> **Explanation:** 5 is a prime number.\r\n" + //
                                                                "\r\n" + //
                                                                "---\r\n" + //
                                                                "### Example 2: Advanced Scenario\r\n" + //
                                                                "| Input          | Output          |\r\n" + //
                                                                "| -------------- | --------------- |\r\n" + //
                                                                "| n = 4          | false           |\r\n" + //
                                                                "\r\n" + //
                                                                "> **Explanation:** 4 is not a prime number because it can be formed by multiplying 2 * 2.\r\n"
                                                                + //
                                                                "\r\n" + //
                                                                "---\r\n" + //
                                                                "### Example 3: Advanced Scenario\r\n" + //
                                                                "| Input          | Output          |\r\n" + //
                                                                "| -------------- | --------------- |\r\n" + //
                                                                "| n = 1          | false           |\r\n" + //
                                                                "\r\n" + //
                                                                "> **Explanation:** 1 is not a prime number.\r\n" + //
                                                                "\r\n")
                                .difficulty(Problem.EDifficulty.EASY)
                                .topics(topics)
                                .functionName("isPrime")
                                .isDeleted(false)
                                .outputDataType("boolean")
                                .correctAnswer("public class Solution {\n" +
                                                "    public boolean isPrime(int n) {\n" +
                                                "        if (n <= 1) return false;\n" +
                                                "        for (int i = 2; i * i <= n; i++) {\n" +
                                                "            if (n % i == 0) {\n" +
                                                "                return false;\n" +
                                                "            }\n" +
                                                "        }\n" +
                                                "        return true;\n" +
                                                "    }\n" +
                                                "}")
                                .build();

                problem = problemRepository.save(problem);
                ProblemHint hint = ProblemHint.builder()
                                .isLocked(false)
                                .overView("## Overview\n\nTo check if a number is prime, iterate from 2 to the square root of the number and check for divisibility.")
                                .pseudoCode("## Pseudo Code\n\n```plaintext\nfunction isPrime(n):\n    if n <= 1 then return false\n    for i from 2 to sqrt(n) do\n        if n % i == 0 then\n            return false\n    return true\n```")
                        .problem(problem)
                        .build();
                ProblemHint savedHint = problemHintRepository.save(hint);

                problem.setProblemHint(hint);
                problem = problemRepository.save(problem);

                ProblemApproach approach1 = ProblemApproach.builder()
                                .intuition("## Intuition\n\nA number is prime if it is not divisible by any number other than 1 and itself. We only need to check divisibility up to the square root of the number.")
                                .algorithm("## Algorithm\n\n1. If the number is less than or equal to 1, it is not prime.\n2. Iterate from 2 to the square root of the number.\n3. If the number is divisible by any of these, it is not prime.\n4. If no divisors are found, the number is prime.")
                                .implementation("## Implementation\n\n```java\npublic boolean isPrime(int n) {\n    if (n <= 1) return false;\n    for (int i = 2; i * i <= n; i++) {\n        if (n % i == 0) {\n            return false;\n        }\n    }\n    return true;\n}\n```")
                                .language(Submission.ELanguage.JAVA)
                                .problemHint(savedHint)
                                .build();

                problemApproachRepository.save(approach1);

                List<ProblemApproach> approaches = new ArrayList<>();
                approaches.add(approach1);
                savedHint.setApproaches(approaches);
                problemHintRepository.save(savedHint);
                problemApproachRepository.save(approach1);

                buildTestCase24(problem);

                return problemRepository.save(problem);
        }

        private void buildTestCase24(Problem problem) {
                TestCase testCase1 = TestCase.builder()
                                .outputData("true")
                                .isPublic(true)
                                .problem(problem)
                                .build();
                buildParameters24(testCase1, "5");
                testCaseRepository.save(testCase1);

                TestCase testCase2 = TestCase.builder()
                                .outputData("false")
                                .problem(problem)
                                .build();
                buildParameters24(testCase2, "4");
                testCaseRepository.save(testCase2);

                TestCase testCase3 = TestCase.builder()
                                .outputData("false")
                                .problem(problem)
                                .build();
                buildParameters24(testCase3, "1");
                testCaseRepository.save(testCase3);
        }

        private void buildParameters24(TestCase testCase, String n) {
                Parameter parameter1 = Parameter.builder()
                                .inputDataType("int")
                                .name("n")
                                .inputData(n)
                                .index(1)
                                .testCase(testCase)
                                .build();

                parameterRepository.save(parameter1);
        }

        public Problem buildProblem25(User user) {
                List<Problem.ETopic> topics = new ArrayList<>();
                topics.add(Problem.ETopic.MATH);

                Problem problem = Problem.builder()
                                .owner(user)
                                .title("Count Prime Numbers")
                                .createdAt(LocalDateTime.now())
                                .updatedAt(LocalDateTime.now())
                                .description(
                                                "<h1 style=\"font-size: 2em; margin: 0.67em 0; font-weight: bold;text-align: center;\">Count Prime Numbers</h1>\r\n"
                                                                + //
                                                                "\r\n" + //
                                                                "---\r\n" + //
                                                                "## 1. Description\r\n" + //
                                                                "<!-- Provide a clear and concise description of the problem. \r\n"
                                                                + //
                                                                "    - What is the problem about?\r\n" + //
                                                                "    - What are the constraints?\r\n" + //
                                                                "    - What is the expected input and output?\r\n" + //
                                                                "    - Any special conditions or edge cases to consider?\r\n"
                                                                + //
                                                                "-->\r\n" + //
                                                                "Given an integer n, return the number of prime numbers that are less than or equal to n.\r\n"
                                                                + //
                                                                "\r\n" + //
                                                                "A prime number is a natural number greater than 1 that cannot be formed by multiplying two smaller natural numbers.\r\n"
                                                                + //
                                                                "\r\n" + //
                                                                "Constraints:\r\n" + //
                                                                "- 1 <= n <= 1000\r\n" + //
                                                                "\r\n" + //
                                                                "---\r\n" + //
                                                                "## 2. Examples\r\n" + //
                                                                "---\r\n" + //
                                                                "### Example 1: Basic Usage\r\n" + //
                                                                "| Input          | Output          |\r\n" + //
                                                                "| -------------- | --------------- |\r\n" + //
                                                                "| n = 10         | 4               |\r\n" + //
                                                                "\r\n" + //
                                                                "> **Explanation:** The prime numbers less than or equal to 10 are [2, 3, 5, 7].\r\n"
                                                                + //
                                                                "\r\n" + //
                                                                "---\r\n" + //
                                                                "### Example 2: Advanced Scenario\r\n" + //
                                                                "| Input          | Output          |\r\n" + //
                                                                "| -------------- | --------------- |\r\n" + //
                                                                "| n = 30         | 10              |\r\n" + //
                                                                "\r\n" + //
                                                                "> **Explanation:** The prime numbers less than or equal to 30 are [2, 3, 5, 7, 11, 13, 17, 19, 23, 29].\r\n"
                                                                + //
                                                                "\r\n")
                                .difficulty(Problem.EDifficulty.EASY)
                                .topics(topics)
                                .functionName("countPrimes")
                                .isDeleted(false)
                                .outputDataType("int")
                                .correctAnswer("public class Solution {\n" +
                                                "    public int countPrimes(int n) {\n" +
                                                "        if (n <= 2) return 0;\n" +
                                                "        boolean[] isPrime = new boolean[n];\n" +
                                                "        Arrays.fill(isPrime, true);\n" +
                                                "        isPrime[0] = isPrime[1] = false;\n" +
                                                "        for (int i = 2; i * i < n; i++) {\n" +
                                                "            if (isPrime[i]) {\n" +
                                                "                for (int j = i * i; j < n; j += i) {\n" +
                                                "                    isPrime[j] = false;\n" +
                                                "                }\n" +
                                                "            }\n" +
                                                "        }\n" +
                                                "        int count = 0;\n" +
                                                "        for (boolean prime : isPrime) {\n" +
                                                "            if (prime) count++;\n" +
                                                "        }\n" +
                                                "        return count;\n" +
                                                "    }\n" +
                                                "}")
                                .build();

                problem = problemRepository.save(problem);
                ProblemHint hint = ProblemHint.builder()
                                .isLocked(false)
                                .overView("## Overview\n\nTo count the number of prime numbers less than or equal to n, use the Sieve of Eratosthenes algorithm to efficiently mark non-prime numbers.")
                                .pseudoCode("## Pseudo Code\n\n```plaintext\nfunction countPrimes(n):\n    if n <= 2 then return 0\n    create a boolean array isPrime of size n, initialized to true\n    set isPrime[0] and isPrime[1] to false\n    for i from 2 to sqrt(n) do\n        if isPrime[i] then\n            for j from i*i to n, incrementing by i do\n                set isPrime[j] to false\n    count = 0\n    for each value in isPrime do\n        if value is true then increment count\n    return count\n```")
                        .problem(problem)
                        .build();
                ProblemHint savedHint = problemHintRepository.save(hint);

                problem.setProblemHint(hint);
                problem = problemRepository.save(problem);

                ProblemApproach approach1 = ProblemApproach.builder()
                                .intuition("### Intuition\n\nThe Sieve of Eratosthenes is an efficient algorithm to find all prime numbers up to a given limit by iteratively marking the multiples of each prime number starting from 2.")
                                .algorithm("### Algorithm\n\n1. **Initialize** a boolean array `isPrime` of size `n`, with all values set to `true`.\n"
                                                +
                                                "2. **Set** `isPrime[0]` and `isPrime[1]` to `false`, as 0 and 1 are not prime numbers.\n"
                                                +
                                                "3. **For each** number `i` starting from 2, if `isPrime[i]` is `true`, mark all multiples of `i` as `false`.\n"
                                                +
                                                "4. **Count** the number of `true` values in `isPrime`, which represent prime numbers.\n"
                                                +
                                                "5. **Return** the count.")
                                .implementation("### Implementation\n\n```java\npublic int countPrimes(int n) {\n" +
                                                "    if (n <= 2) return 0;\n" +
                                                "    boolean[] isPrime = new boolean[n];\n" +
                                                "    Arrays.fill(isPrime, true);\n" +
                                                "    isPrime[0] = isPrime[1] = false;\n" +
                                                "    for (int i = 2; i * i < n; i++) {\n" +
                                                "        if (isPrime[i]) {\n" +
                                                "            for (int j = i * i; j < n; j += i) {\n" +
                                                "                isPrime[j] = false;\n" +
                                                "            }\n" +
                                                "        }\n" +
                                                "    }\n" +
                                                "    int count = 0;\n" +
                                                "    for (boolean prime : isPrime) {\n" +
                                                "        if (prime) count++;\n" +
                                                "    }\n" +
                                                "    return count;\n" +
                                                "}\n```")
                                .language(Submission.ELanguage.JAVA)
                                .problemHint(savedHint)
                                .build();

                problemApproachRepository.save(approach1);

                List<ProblemApproach> approaches = new ArrayList<>();
                approaches.add(approach1);
                savedHint.setApproaches(approaches);
                problemHintRepository.save(savedHint);
                problemApproachRepository.save(approach1);

                buildTestCase25(problem);
                return problemRepository.save(problem);
        }

        private void buildTestCase25(Problem problem) {
                TestCase testCase1 = TestCase.builder()
                                .outputData("4")
                                .isPublic(true)
                                .problem(problem)
                                .build();
                buildParameters25(testCase1, "10");
                testCaseRepository.save(testCase1);

                TestCase testCase2 = TestCase.builder()
                                .outputData("10")
                                .problem(problem)
                                .build();
                buildParameters25(testCase2, "30");
                testCaseRepository.save(testCase2);

                TestCase testCase3 = TestCase.builder()
                                .outputData("0")
                                .problem(problem)
                                .build();
                buildParameters25(testCase3, "1");
                testCaseRepository.save(testCase3);
        }

        private void buildParameters25(TestCase testCase, String n) {
                Parameter parameter1 = Parameter.builder()
                                .inputDataType("int")
                                .name("n")
                                .inputData(n)
                                .index(1)
                                .testCase(testCase)
                                .build();

                parameterRepository.save(parameter1);
        }

        public Problem buildProblem26(User user) {
                List<Problem.ETopic> topics = new ArrayList<>();
                topics.add(Problem.ETopic.MATH);

                Problem problem = Problem.builder()
                                .owner(user)
                                .title("Check Perfect Number")
                                .createdAt(LocalDateTime.now())
                                .updatedAt(LocalDateTime.now())
                                .description(
                                                "<h1 style=\"font-size: 2em; margin: 0.67em 0; font-weight: bold;text-align: center;\">Check Perfect Number</h1>\r\n"
                                                                + //
                                                                "\r\n" + //
                                                                "---\r\n" + //
                                                                "\r\n" + //
                                                                "## 1. Description\r\n" + //
                                                                "\r\n" + //
                                                                "<!-- Provide a clear and concise description of the problem. \r\n"
                                                                + //
                                                                "    - What is the problem about?\r\n" + //
                                                                "    - What are the constraints?\r\n" + //
                                                                "    - What is the expected input and output?\r\n" + //
                                                                "    - Any special conditions or edge cases to consider?\r\n"
                                                                + //
                                                                "-->\r\n" + //
                                                                "\r\n" + //
                                                                "\r\n" + //
                                                                "Given an integer n, return true if it is a perfect number, and false otherwise.\r\n"
                                                                + //
                                                                "\r\n" + //
                                                                "A perfect number is a positive integer that is equal to the sum of its proper positive divisors, excluding the number itself.\r\n"
                                                                + //
                                                                "\r\n" + //
                                                                "Constraints:\r\n" + //
                                                                "- 1 <= n <= 10^8\r\n" + //
                                                                "\r\n" + //
                                                                "---\r\n" + //
                                                                "## 2. Examples\r\n" + //
                                                                "---\r\n" + //
                                                                "### Example 1: Basic Usage\r\n" + //
                                                                "| Input          | Output          |\r\n" + //
                                                                "| -------------- | --------------- |\r\n" + //
                                                                "| n = 28         | true            |\r\n" + //
                                                                "\r\n" + //
                                                                "> **Explanation:** 28 is a perfect number because 1 + 2 + 4 + 7 + 14 = 28.\r\n"
                                                                + //
                                                                "\r\n" + //
                                                                "---\r\n" + //
                                                                "### Example 2: Advanced Scenario\r\n" + //
                                                                "| Input          | Output          |\r\n" + //
                                                                "| -------------- | --------------- |\r\n" + //
                                                                "| n = 6          | true            |\r\n" + //
                                                                "\r\n" + //
                                                                "> **Explanation:** 6 is a perfect number because 1 + 2 + 3 = 6.\r\n"
                                                                + //
                                                                "\r\n" + //
                                                                "---\r\n" + //
                                                                "### Example 3: Advanced Scenario\r\n" + //
                                                                "| Input          | Output          |\r\n" + //
                                                                "| -------------- | --------------- |\r\n" + //
                                                                "| n = 12         | false           |\r\n" + //
                                                                "\r\n" + //
                                                                "> **Explanation:** 12 is not a perfect number because 1 + 2 + 3 + 4 + 6 = 16.\r\n"
                                                                + //
                                                                "\r\n")
                                .difficulty(Problem.EDifficulty.NORMAL)
                                .topics(topics)
                                .functionName("isPerfectNumber")
                                .isDeleted(false)
                                .outputDataType("boolean")
                                .correctAnswer("public class Solution {\n" +
                                                "    public boolean isPerfectNumber(int n) {\n" +
                                                "        if (n <= 1) return false;\n" +
                                                "        int sum = 1;\n" +
                                                "        for (int i = 2; i * i <= n; i++) {\n" +
                                                "            if (n % i == 0) {\n" +
                                                "                sum += i;\n" +
                                                "                if (i != n / i) sum += n / i;\n" +
                                                "            }\n" +
                                                "        }\n" +
                                                "        return sum == n;\n" +
                                                "    }\n" +
                                                "}")
                                .build();
                problem = problemRepository.save(problem);
                ProblemHint hint = ProblemHint.builder()
                                .isLocked(false)
                                .overView("## Overview\n\nTo check if a number is perfect, calculate the sum of its divisors and compare it to the number itself.")
                                .pseudoCode("## Pseudo Code\n\n```plaintext\nfunction isPerfectNumber(n):\n    if n <= 1 then return false\n    sum = 1\n    for i from 2 to sqrt(n) do\n        if n % i == 0 then\n            sum += i\n            if i != n / i then\n                sum += n / i\n    return sum == n\n```")
                        .problem(problem)
                        .build();
                ProblemHint savedHint = problemHintRepository.save(hint);

                problem.setProblemHint(hint);
                problem = problemRepository.save(problem);

                ProblemApproach approach1 = ProblemApproach.builder()
                                .intuition("## Intuition\n\nA perfect number is equal to the sum of its proper divisors. We can find these divisors by iterating up to the square root of the number.")
                                .algorithm("## Algorithm\n\n1. **Initialize** sum to 1 (since 1 is a divisor of every number).\n"
                                                +
                                                "2. **Iterate** from 2 to the square root of n:\n" +
                                                "   - If i is a divisor of n, **add** i and n/i to the sum.\n" +
                                                "3. If the sum **equals** n, then n is a perfect number.")
                                .implementation("## Implementation\n\n```java\npublic boolean isPerfectNumber(int n) {\n"
                                                +
                                                "    if (n <= 1) return false;\n" +
                                                "    int sum = 1;\n" +
                                                "    for (int i = 2; i * i <= n; i++) {\n" +
                                                "        if (n % i == 0) {\n" +
                                                "            sum += i;\n" +
                                                "            if (i != n / i) sum += n / i;\n" +
                                                "        }\n" +
                                                "    }\n" +
                                                "    return sum == n;\n" +
                                                "}\n```")
                                .language(Submission.ELanguage.JAVA)
                                .problemHint(savedHint)
                                .build();

                problemApproachRepository.save(approach1);

                List<ProblemApproach> approaches = new ArrayList<>();
                approaches.add(approach1);
                savedHint.setApproaches(approaches);
                problemHintRepository.save(savedHint);
                problemApproachRepository.save(approach1);

                buildTestCase26(problem);
                return problemRepository.save(problem);
        }

        private void buildTestCase26(Problem problem) {
                TestCase testCase1 = TestCase.builder()
                                .outputData("true")
                                .isPublic(true)
                                .problem(problem)
                                .build();
                buildParameters26(testCase1, "28");
                testCaseRepository.save(testCase1);

                TestCase testCase2 = TestCase.builder()
                                .outputData("true")
                                .problem(problem)
                                .build();
                buildParameters26(testCase2, "6");
                testCaseRepository.save(testCase2);

                TestCase testCase3 = TestCase.builder()
                                .outputData("false")
                                .problem(problem)
                                .build();
                buildParameters26(testCase3, "12");
                testCaseRepository.save(testCase3);
        }

        private void buildParameters26(TestCase testCase, String n) {
                Parameter parameter1 = Parameter.builder()
                                .inputDataType("int")
                                .name("n")
                                .inputData(n)
                                .index(1)
                                .testCase(testCase)
                                .build();

                parameterRepository.save(parameter1);
        }

        public Problem buildProblem27(User user) {
                List<Problem.ETopic> topics = new ArrayList<>();
                topics.add(Problem.ETopic.STRING);

                Problem problem = Problem.builder()
                                .owner(user)
                                .title("Add Two Large Numbers")
                                .createdAt(LocalDateTime.now())
                                .updatedAt(LocalDateTime.now())
                                .description(
                                                "<h1 style=\"font-size: 2em; margin: 0.67em 0; font-weight: bold;text-align: center;\">Add Two Large Numbers</h1>\r\n"
                                                                + //
                                                                "\r\n" + //
                                                                "---\r\n" + //
                                                                "\r\n" + //
                                                                "## 1. Description\r\n" + //
                                                                "\r\n" + //
                                                                "<!-- Provide a clear and concise description of the problem. \r\n"
                                                                + //
                                                                "    - What is the problem about?\r\n" + //
                                                                "    - What are the constraints?\r\n" + //
                                                                "    - What is the expected input and output?\r\n" + //
                                                                "    - Any special conditions or edge cases to consider?\r\n"
                                                                + //
                                                                "-->\r\n" + //
                                                                "\r\n" + //
                                                                "\r\n" + //
                                                                "Given two non-negative integers num1 and num2 represented as strings, return the sum of num1 and num2 as a string.\n"
                                                                +
                                                                "\n" +
                                                                "Constraints:\n" +
                                                                "- `1 <= num1.length, num2.length <= 10^4`\n" +
                                                                "- `num1` and `num2` consist only of digits.\n" +
                                                                "- `num1` and `num2` do not contain any leading zeros except the number 0 itself.\n"
                                                                +
                                                                "---\n" +
                                                                "## 2. Examples\n" +
                                                                "---\n" +
                                                                "### Example 1: Basic Usage\n" +
                                                                "| Input          | Output          |\n" +
                                                                "| -------------- | --------------- |\n" +
                                                                "| num1 = \"123\", num2 = \"456\" | \"579\" |\n" +
                                                                "\n" +
                                                                "> **Explanation:** The sum of 123 and 456 is 579.\n"
                                                                +
                                                                "---\n" +
                                                                "### Example 2: Advanced Scenario\n" +
                                                                "| Input          | Output          |\n" +
                                                                "| -------------- | --------------- |\n" +
                                                                "| num1 = \"999\", num2 = \"1\" | \"1000\" |\n" +
                                                                "\n" +
                                                                "> **Explanation:** The sum of 999 and 1 is 1000.\n")
                                .difficulty(Problem.EDifficulty.NORMAL)
                                .topics(topics)
                                .functionName("addLargeNumbers")
                                .isDeleted(false)
                                .outputDataType("String")
                                .correctAnswer("public class Solution {\n" +
                                                "    public String addLargeNumbers(String num1, String num2) {\n" +
                                                "        StringBuilder result = new StringBuilder();\n" +
                                                "        int carry = 0, i = num1.length() - 1, j = num2.length() - 1;\n"
                                                +
                                                "        while (i >= 0 || j >= 0 || carry != 0) {\n" +
                                                "            int x = (i >= 0) ? num1.charAt(i) - '0' : 0;\n" +
                                                "            int y = (j >= 0) ? num2.charAt(j) - '0' : 0;\n" +
                                                "            int sum = x + y + carry;\n" +
                                                "            result.append(sum % 10);\n" +
                                                "            carry = sum / 10;\n" +
                                                "            i--;\n" +
                                                "            j--;\n" +
                                                "        }\n" +
                                                "        return result.reverse().toString();\n" +
                                                "    }\n" +
                                                "}")
                                .build();
                problem = problemRepository.save(problem);
                ProblemHint hint = ProblemHint.builder()
                                .isLocked(false)
                                .overView("## Overview\n\nTo add two large numbers represented as strings, process each digit from right to left, keeping track of the carry.")
                                .pseudoCode("## Pseudo Code\n\n```plaintext\nfunction addLargeNumbers(num1, num2):\n    initialize result as an empty string\n    initialize carry as 0\n    set i to the last index of num1, j to the last index of num2\n    while i >= 0 or j >= 0 or carry != 0 do\n        x = digit at index i of num1 if i >= 0 else 0\n        y = digit at index j of num2 if j >= 0 else 0\n        sum = x + y + carry\n        append sum % 10 to result\n        carry = sum / 10\n        decrement i and j\n    return reverse of result\n```")
                        .problem(problem)
                        .build();
                ProblemHint savedHint = problemHintRepository.save(hint);

                problem.setProblemHint(hint);
                problem = problemRepository.save(problem);

                ProblemApproach approach1 = ProblemApproach.builder()
                                .intuition("## Intuition\n\nAdding two numbers digit by digit from the least significant to the most significant digit allows us to handle the carry easily.")
                                .algorithm("## Algorithm\n\n1. Initialize a `StringBuilder` for the result and a `carry` variable.\n2. Iterate over the digits of both numbers from right to left.\n3. For each pair of digits, calculate the sum and update the carry.\n4. Append the last digit of the sum to the result.\n5. After the loop, if there's a carry left, append it to the result.\n6. Reverse the result and return it as a string.")
                                .implementation("## Implementation\n\n```java\npublic String addLargeNumbers(String num1, String num2) {\n    StringBuilder result = new StringBuilder();\n    int carry = 0, i = num1.length() - 1, j = num2.length() - 1;\n    while (i >= 0 || j >= 0 || carry != 0) {\n        int x = (i >= 0) ? num1.charAt(i) - '0' : 0;\n        int y = (j >= 0) ? num2.charAt(j) - '0' : 0;\n        int sum = x + y + carry;\n        result.append(sum % 10);\n        carry = sum / 10;\n        i--;\n        j--;\n    }\n    return result.reverse().toString();\n}\n```")
                                .language(Submission.ELanguage.JAVA)
                                .problemHint(savedHint)
                                .build();

                problemApproachRepository.save(approach1);

                List<ProblemApproach> approaches = new ArrayList<>();
                approaches.add(approach1);
                savedHint.setApproaches(approaches);
                problemHintRepository.save(savedHint);
                problemApproachRepository.save(approach1);

                buildTestCase27(problem);
                return problemRepository.save(problem);
        }

        private void buildTestCase27(Problem problem) {
                TestCase testCase1 = TestCase.builder()
                                .outputData("\"579\"")
                                .isPublic(true)
                                .problem(problem)
                                .build();
                buildParameters27(testCase1, "\"123\"", "\"456\"");
                testCaseRepository.save(testCase1);

                TestCase testCase2 = TestCase.builder()
                                .outputData("\"1000\"")
                                .problem(problem)
                                .build();
                buildParameters27(testCase2, "\"999\"", "\"1\"");
                testCaseRepository.save(testCase2);
        }

        private void buildParameters27(TestCase testCase, String num1, String num2) {
                Parameter parameter1 = Parameter.builder()
                                .inputDataType("String")
                                .name("num1")
                                .inputData(num1)
                                .index(1)
                                .testCase(testCase)
                                .build();

                Parameter parameter2 = Parameter.builder()
                                .inputDataType("String")
                                .name("num2")
                                .inputData(num2)
                                .index(2)
                                .testCase(testCase)
                                .build();

                parameterRepository.save(parameter1);
                parameterRepository.save(parameter2);
        }

        public Problem buildProblem32(User user) {
                List<Problem.ETopic> topics = new ArrayList<>();
                topics.add(Problem.ETopic.GRAPH);

                Problem problem = Problem.builder()
                                .owner(user)
                                .title("Minimum Spanning Tree")
                                .createdAt(LocalDateTime.now())
                                .updatedAt(LocalDateTime.now())
                                .description(
                                                "<h1 style=\"font-size: 2em; margin: 0.67em 0; font-weight: bold;text-align: center;\">Minimum Spanning Tree</h1>\r\n"
                                                                + //
                                                                "\r\n" + //
                                                                "---\r\n" + //
                                                                "\r\n" + //
                                                                "## 1. Description\r\n" + //
                                                                "\r\n" + //
                                                                "<!-- Provide a clear and concise description of the problem. \r\n"
                                                                + //
                                                                "    - What is the problem about?\r\n" + //
                                                                "    - What are the constraints?\r\n" + //
                                                                "    - What is the expected input and output?\r\n" + //
                                                                "    - Any special conditions or edge cases to consider?\r\n"
                                                                + //
                                                                "-->\r\n" + //
                                                                "\r\n" + //
                                                                "\r\n" + //
                                                                "Given a connected, undirected graph with weighted edges, find the minimum spanning tree (MST) of the graph using Kruskal's algorithm.\n"
                                                                +
                                                                "\n" +
                                                                "Constraints:\n" +
                                                                "- `1 <= number of vertices <= 1000`\n" +
                                                                "- `1 <= weight of edges <= 1000`\n" +
                                                                "---\n" +
                                                                "## 2. Examples\n" +
                                                                "---\n" +
                                                                "### Example 1: Basic Usage\n" +
                                                                "| Input          | Output          |\n" +
                                                                "| -------------- | --------------- |\n" +
                                                                "| edges = [(0, 1, 10), (0, 2, 6), (0, 3, 5), (1, 3, 15), (2, 3, 4)], vertices = 4 | 19 |\n"
                                                                +
                                                                "\n" +
                                                                "> **Explanation:** The MST includes edges (2, 3, 4), (0, 3, 5), and (0, 1, 10) with total weight 19.\n"
                                                                +
                                                                "---\n" +
                                                                "### Example 2: Advanced Scenario\n" +
                                                                "| Input          | Output          |\n" +
                                                                "| -------------- | --------------- |\n" +
                                                                "| edges = [(0, 1, 1), (1, 2, 2), (2, 3, 1), (3, 0, 2), (0, 2, 2), (1, 3, 3)], vertices = 4 | 4 |\n"
                                                                +
                                                                "\n" +
                                                                "> **Explanation:** The MST includes edges (0, 1, 1), (2, 3, 1), and (1, 2, 2) with total weight 4.\n")
                                .difficulty(Problem.EDifficulty.HARD)
                                .topics(topics)
                                .functionName("kruskalMST")
                                .isDeleted(false)
                                .outputDataType("int")
                                .correctAnswer("import java.util.*;\n" +
                                                "\n" +
                                                "public class Solution {\n" +
                                                "    class Edge implements Comparable<Edge> {\n" +
                                                "        int src, dest, weight;\n" +
                                                "        public int compareTo(Edge compareEdge) {\n" +
                                                "            return this.weight - compareEdge.weight;\n" +
                                                "        }\n" +
                                                "    };\n" +
                                                "\n" +
                                                "    class Subset {\n" +
                                                "        int parent, rank;\n" +
                                                "    };\n" +
                                                "\n" +
                                                "    int find(Subset[] subsets, int i) {\n" +
                                                "        if (subsets[i].parent != i)\n" +
                                                "            subsets[i].parent = find(subsets, subsets[i].parent);\n" +
                                                "        return subsets[i].parent;\n" +
                                                "    }\n" +
                                                "\n" +
                                                "    void union(Subset[] subsets, int x, int y) {\n" +
                                                "        int xroot = find(subsets, x);\n" +
                                                "        int yroot = find(subsets, y);\n" +
                                                "        if (subsets[xroot].rank < subsets[yroot].rank)\n" +
                                                "            subsets[xroot].parent = yroot;\n" +
                                                "        else if (subsets[xroot].rank > subsets[yroot].rank)\n" +
                                                "            subsets[yroot].parent = xroot;\n" +
                                                "        else {\n" +
                                                "            subsets[yroot].parent = xroot;\n" +
                                                "            subsets[xroot].rank++;\n" +
                                                "        }\n" +
                                                "    }\n" +
                                                "\n" +
                                                "    public int kruskalMST(int[][] edges, int V) {\n" +
                                                "        Edge[] edgeList = new Edge[edges.length];\n" +
                                                "        for (int i = 0; i < edges.length; i++) {\n" +
                                                "            edgeList[i] = new Edge();\n" +
                                                "            edgeList[i].src = edges[i][0];\n" +
                                                "            edgeList[i].dest = edges[i][1];\n" +
                                                "            edgeList[i].weight = edges[i][2];\n" +
                                                "        }\n" +
                                                "        Arrays.sort(edgeList);\n" +
                                                "        Subset[] subsets = new Subset[V];\n" +
                                                "        for (int v = 0; v < V; ++v) {\n" +
                                                "            subsets[v] = new Subset();\n" +
                                                "            subsets[v].parent = v;\n" +
                                                "            subsets[v].rank = 0;\n" +
                                                "        }\n" +
                                                "        int result = 0;\n" +
                                                "        int e = 0;\n" +
                                                "        for (int i = 0; e < V - 1; i++) {\n" +
                                                "            Edge nextEdge = edgeList[i];\n" +
                                                "            int x = find(subsets, nextEdge.src);\n" +
                                                "            int y = find(subsets, nextEdge.dest);\n" +
                                                "            if (x != y) {\n" +
                                                "                result += nextEdge.weight;\n" +
                                                "                union(subsets, x, y);\n" +
                                                "                e++;\n" +
                                                "            }\n" +
                                                "        }\n" +
                                                "        return result;\n" +
                                                "    }\n" +
                                                "}")
                                .build();

                problem = problemRepository.save(problem);
                ProblemHint hint = ProblemHint.builder()
                                .isLocked(true)
                                .overView("## Overview\n\nTo find the minimum spanning tree, sort all edges by weight and use a union-find structure to ensure no cycles are formed.")
                                .pseudoCode("## Pseudo Code\n\n```plaintext\nfunction kruskalMST(edges, V):\n    sort edges by weight\n    initialize subsets for each vertex\n    result = 0\n    for each edge in sorted edges do\n        if adding edge does not form a cycle then\n            add edge to result\n            union the sets of the two vertices\n    return result\n```")
                        .problem(problem)
                        .build();
                ProblemHint savedHint = problemHintRepository.save(hint);

                problem.setProblemHint(hint);
                problem = problemRepository.save(problem);

                ProblemApproach approach1 = ProblemApproach.builder()
                                .intuition("## Intuition\n\nKruskal's algorithm finds the MST by adding edges in increasing order of weight, ensuring no cycles are formed.")
                                .algorithm("## Algorithm\n\n1. **Sort** all edges in non-decreasing order of their weight.\n"
                                                +
                                                "2. **Initialize** a union-find structure to keep track of connected components.\n"
                                                +
                                                "3. **Iterate** over the sorted edges and add them to the MST if they do not form a cycle.\n"
                                                +
                                                "4. **Use** union-find to manage the connected components.\n" +
                                                "5. **Stop** when the MST contains V-1 edges.")
                                .implementation("## Implementation\n\n```java\npublic int kruskalMST(int[][] edges, int V) {\n"
                                                +
                                                "    Edge[] edgeList = new Edge[edges.length];\n" +
                                                "    for (int i = 0; i < edges.length; i++) {\n" +
                                                "        edgeList[i] = new Edge();\n" +
                                                "        edgeList[i].src = edges[i][0];\n" +
                                                "        edgeList[i].dest = edges[i][1];\n" +
                                                "        edgeList[i].weight = edges[i][2];\n" +
                                                "    }\n" +
                                                "    Arrays.sort(edgeList);\n" +
                                                "    Subset[] subsets = new Subset[V];\n" +
                                                "    for (int v = 0; v < V; ++v) {\n" +
                                                "        subsets[v] = new Subset();\n" +
                                                "        subsets[v].parent = v;\n" +
                                                "        subsets[v].rank = 0;\n" +
                                                "    }\n" +
                                                "    int result = 0;\n" +
                                                "    int e = 0;\n" +
                                                "    for (int i = 0; e < V - 1; i++) {\n" +
                                                "        Edge nextEdge = edgeList[i];\n" +
                                                "        int x = find(subsets, nextEdge.src);\n" +
                                                "        int y = find(subsets, nextEdge.dest);\n" +
                                                "        if (x != y) {\n" +
                                                "            result += nextEdge.weight;\n" +
                                                "            union(subsets, x, y);\n" +
                                                "            e++;\n" +
                                                "        }\n" +
                                                "    }\n" +
                                                "    return result;\n" +
                                                "}\n```")
                                .language(Submission.ELanguage.JAVA)
                                .problemHint(savedHint)
                                .build();

                problemApproachRepository.save(approach1);

                List<ProblemApproach> approaches = new ArrayList<>();
                approaches.add(approach1);
                savedHint.setApproaches(approaches);
                problemHintRepository.save(savedHint);
                problemApproachRepository.save(approach1);

                buildTestCase32(problem);
                return problemRepository.save(problem);
        }

        private void buildTestCase32(Problem problem) {
                TestCase testCase1 = TestCase.builder()
                                .outputData("19")
                                .isPublic(true)
                                .problem(problem)
                                .build();
                buildParameters32(testCase1, "{{0, 1, 10}, {0, 2, 6}, {0, 3, 5}, {1, 3, 15}, {2, 3, 4}}", "4");
                testCaseRepository.save(testCase1);

                TestCase testCase2 = TestCase.builder()
                                .outputData("4")
                                .problem(problem)
                                .build();
                buildParameters32(testCase2, "{{0, 1, 1}, {1, 2, 2}, {2, 3, 1}, {3, 0, 2}, {0, 2, 2}, {1, 3, 3}}", "4");
                testCaseRepository.save(testCase2);
        }

        private void buildParameters32(TestCase testCase, String edges, String vertices) {
                Parameter parameter1 = Parameter.builder()
                                .inputDataType("int[][]")
                                .name("edges")
                                .inputData(edges)
                                .index(1)
                                .testCase(testCase)
                                .build();

                Parameter parameter2 = Parameter.builder()
                                .inputDataType("int")
                                .name("vertices")
                                .inputData(vertices)
                                .index(2)
                                .testCase(testCase)
                                .build();

                parameterRepository.save(parameter1);
                parameterRepository.save(parameter2);
        }

        public Problem buildProblem33(User user) {
                List<Problem.ETopic> topics = new ArrayList<>();
                topics.add(Problem.ETopic.MATH);

                Problem problem = Problem.builder()
                                .owner(user)
                                .title("Triangle Validity Check")
                                .createdAt(LocalDateTime.now())
                                .updatedAt(LocalDateTime.now())
                                .description(
                                                "<h1 style=\"font-size: 2em; margin: 0.67em 0; font-weight: bold;text-align: center;\">Triangle Validity Check</h1>\r\n"
                                                                + //
                                                                "\r\n" + //
                                                                "---\r\n" + //
                                                                "\r\n" + //
                                                                "## 1. Description\r\n" + //
                                                                "\r\n" + //
                                                                "<!-- Provide a clear and concise description of the problem. \r\n"
                                                                + //
                                                                "    - What is the problem about?\r\n" + //
                                                                "    - What are the constraints?\r\n" + //
                                                                "    - What is the expected input and output?\r\n" + //
                                                                "    - Any special conditions or edge cases to consider?\r\n"
                                                                + //
                                                                "-->\r\n" + //
                                                                "\r\n" + //
                                                                "\r\n" + //
                                                                "Given three integers a, b, and c representing the lengths of the sides of a triangle, return true if they can form a valid triangle, and false otherwise.\n"
                                                                +
                                                                "\n" +
                                                                "Constraints:\n" +
                                                                "- `1 <= a, b, c <= 1000`\n" +
                                                                "---\n" +
                                                                "## 2. Examples\n" +
                                                                "---\n" +
                                                                "### Example 1: Basic Usage\n" +
                                                                "| Input          | Output          |\n" +
                                                                "| -------------- | --------------- |\n" +
                                                                "| a = 3, b = 4, c = 5 | true |\n" +
                                                                "\n" +
                                                                "> **Explanation:** The sides 3, 4, and 5 can form a valid triangle.\n"
                                                                +
                                                                "---\n" +
                                                                "### Example 2: Advanced Scenario\n" +
                                                                "| Input          | Output          |\n" +
                                                                "| -------------- | --------------- |\n" +
                                                                "| a = 1, b = 2, c = 3 | false |\n" +
                                                                "\n" +
                                                                "> **Explanation:** The sides 1, 2, and 3 cannot form a valid triangle.\n")
                                .difficulty(Problem.EDifficulty.EASY)
                                .topics(topics)
                                .functionName("isValidTriangle")
                                .isDeleted(false)
                                .outputDataType("boolean")
                                .correctAnswer("public class Solution {\n" +
                                                "    public boolean isValidTriangle(int a, int b, int c) {\n" +
                                                "        return (a + b > c) && (a + c > b) && (b + c > a);\n" +
                                                "    }\n" +
                                                "}")
                                .build();

                problem = problemRepository.save(problem);
                ProblemHint hint = ProblemHint.builder()
                                .isLocked(false)
                                .overView("## Overview\n\nTo check if three sides can form a triangle, ensure that the sum of any two sides is greater than the third side.")
                                .pseudoCode("## Pseudo Code\n\n```plaintext\nfunction isValidTriangle(a, b, c):\n    return (a + b > c) and (a + c > b) and (b + c > a)\n```")
                        .problem(problem)
                        .build();
                ProblemHint savedHint = problemHintRepository.save(hint);

                problem.setProblemHint(hint);
                problem = problemRepository.save(problem);

                ProblemApproach approach1 = ProblemApproach.builder()
                                .intuition("## Intuition\n\nA valid triangle requires that the sum of any two sides must be greater than the third side.")
                                .algorithm("## Algorithm\n\n1. Check if the sum of any two sides is greater than the third side.\n"
                                                +
                                                "2. If all conditions are satisfied, the sides can form a triangle.")
                                .implementation("## Implementation\n\n```java\npublic boolean isValidTriangle(int a, int b, int c) {\n"
                                                +
                                                "    return (a + b > c) && (a + c > b) && (b + c > a);\n" +
                                                "}\n```")
                                .language(Submission.ELanguage.JAVA)
                                .problemHint(savedHint)
                                .build();

                problemApproachRepository.save(approach1);

                List<ProblemApproach> approaches = new ArrayList<>();
                approaches.add(approach1);
                savedHint.setApproaches(approaches);
                problemHintRepository.save(savedHint);
                problemApproachRepository.save(approach1);

                buildTestCase33(problem);
                return problemRepository.save(problem);
        }

        private void buildTestCase33(Problem problem) {
                TestCase testCase1 = TestCase.builder()
                                .outputData("true")
                                .isPublic(true)
                                .problem(problem)
                                .build();
                buildParameters33(testCase1, "3", "4", "5");
                testCaseRepository.save(testCase1);

                TestCase testCase2 = TestCase.builder()
                                .outputData("false")
                                .problem(problem)
                                .build();
                buildParameters33(testCase2, "1", "2", "3");
                testCaseRepository.save(testCase2);
        }

        private void buildParameters33(TestCase testCase, String a, String b, String c) {
                Parameter parameter1 = Parameter.builder()
                                .inputDataType("int")
                                .name("a")
                                .inputData(a)
                                .index(1)
                                .testCase(testCase)
                                .build();

                Parameter parameter2 = Parameter.builder()
                                .inputDataType("int")
                                .name("b")
                                .inputData(b)
                                .index(2)
                                .testCase(testCase)
                                .build();

                Parameter parameter3 = Parameter.builder()
                                .inputDataType("int")
                                .name("c")
                                .inputData(c)
                                .index(3)
                                .testCase(testCase)
                                .build();

                parameterRepository.save(parameter1);
                parameterRepository.save(parameter2);
                parameterRepository.save(parameter3);
        }

        public Problem buildProblem34(User user) {
                List<Problem.ETopic> topics = new ArrayList<>();
                topics.add(Problem.ETopic.GRAPH);

                Problem problem = Problem.builder()
                                .owner(user)
                                .title("Shortest Path in Maze")
                                .createdAt(LocalDateTime.now())
                                .updatedAt(LocalDateTime.now())
                                .description(
                                                "<h1 style=\"font-size: 2em; margin: 0.67em 0; font-weight: bold;text-align: center;\">Shortest Path in Maze</h1>\r\n"
                                                                + //
                                                                "\r\n" + //
                                                                "---\r\n" + //
                                                                "\r\n" + //
                                                                "## 1. Description\r\n" + //
                                                                "\r\n" + //
                                                                "<!-- Provide a clear and concise description of the problem. \r\n"
                                                                + //
                                                                "    - What is the problem about?\r\n" + //
                                                                "    - What are the constraints?\r\n" + //
                                                                "    - What is the expected input and output?\r\n" + //
                                                                "    - Any special conditions or edge cases to consider?\r\n"
                                                                + //
                                                                "-->\r\n" + //
                                                                "\r\n" + //
                                                                "\r\n" + //
                                                                "Given a maze represented as a 2D grid of integers where 0 represents an open path and 1 represents a wall, find the shortest path from the top-left corner to the bottom-right corner. Return the length of the path, or -1 if no path exists.\n"
                                                                +
                                                                "\n" +
                                                                "Constraints:\n" +
                                                                "- `1 <= grid.length, grid[0].length <= 100`\n" +
                                                                "- `grid[i][j]` is either 0 or 1.\n" +
                                                                "---\n" +
                                                                "## 2. Examples\n" +
                                                                "---\n" +
                                                                "### Example 1: Basic Usage\n" +
                                                                "| Input          | Output          |\n" +
                                                                "| -------------- | --------------- |\n" +
                                                                "| grid = [[0, 1], [0, 0]] | 2 |\n" +
                                                                "\n" +
                                                                "> **Explanation:** The shortest path is from (0, 0) to (1, 1) with length 2.\n"
                                                                +
                                                                "---\n" +
                                                                "### Example 2: Advanced Scenario\n" +
                                                                "| Input          | Output          |\n" +
                                                                "| -------------- | --------------- |\n" +
                                                                "| grid = [[0, 1, 0], [1, 0, 0], [0, 0, 0]] | 4 |\n" +
                                                                "\n" +
                                                                "> **Explanation:** The shortest path is from (0, 0) to (2, 2) with length 4.\n")
                                .difficulty(Problem.EDifficulty.HARD)
                                .topics(topics)
                                .functionName("shortestPathInMaze")
                                .isDeleted(false)
                                .outputDataType("int")
                                .correctAnswer("import java.util.*;\n" +
                                                "\n" +
                                                "public class Solution {\n" +
                                                "    public int shortestPathInMaze(int[][] grid) {\n" +
                                                "        int m = grid.length, n = grid[0].length;\n" +
                                                "        if (grid[0][0] == 1 || grid[m-1][n-1] == 1) return -1;\n" +
                                                "        int[][] directions = {{0, 1}, {1, 0}, {0, -1}, {-1, 0}};\n" +
                                                "        Queue<int[]> queue = new LinkedList<>();\n" +
                                                "        queue.offer(new int[]{0, 0});\n" +
                                                "        grid[0][0] = 1; // mark as visited\n" +
                                                "        int pathLength = 1;\n" +
                                                "        while (!queue.isEmpty()) {\n" +
                                                "            int size = queue.size();\n" +
                                                "            for (int i = 0; i < size; i++) {\n" +
                                                "                int[] current = queue.poll();\n" +
                                                "                int x = current[0], y = current[1];\n" +
                                                "                if (x == m - 1 && y == n - 1) return pathLength;\n" +
                                                "                for (int[] dir : directions) {\n" +
                                                "                    int newX = x + dir[0], newY = y + dir[1];\n" +
                                                "                    if (newX >= 0 && newX < m && newY >= 0 && newY < n && grid[newX][newY] == 0) {\n"
                                                +
                                                "                        queue.offer(new int[]{newX, newY});\n" +
                                                "                        grid[newX][newY] = 1; // mark as visited\n" +
                                                "                    }\n" +
                                                "                }\n" +
                                                "            }\n" +
                                                "            pathLength++;\n" +
                                                "        }\n" +
                                                "        return -1;\n" +
                                                "    }\n" +
                                                "}")
                                .build();

                problem = problemRepository.save(problem);
                ProblemHint hint = ProblemHint.builder()
                                .isLocked(true)
                                .overView("**To find the shortest path in a maze, use Breadth-First Search (BFS) to explore all possible paths from the start to the end.**")
                                .pseudoCode("```pseudo\n" +
                                                "function shortestPathInMaze(grid):\n" +
                                                "    if grid[0][0] == 1 or grid[end][end] == 1 then return -1\n" +
                                                "    initialize queue with start position\n" +
                                                "    mark start as visited\n" +
                                                "    while queue is not empty do\n" +
                                                "        for each position in queue do\n" +
                                                "            if position is end then return path length\n" +
                                                "            for each direction do\n" +
                                                "                if new position is valid and not visited then\n" +
                                                "                    add new position to queue\n" +
                                                "                    mark new position as visited\n" +
                                                "    return -1\n" +
                                                "```")
                        .problem(problem)
                        .build();
                ProblemHint savedHint = problemHintRepository.save(hint);

                problem.setProblemHint(hint);
                problem = problemRepository.save(problem);

                ProblemApproach approach1 = ProblemApproach.builder()
                                .intuition("### Intuition\n\nBreadth-First Search (BFS) is ideal for finding the shortest path in an unweighted grid because it explores all nodes at the present depth level before moving on to nodes at the next depth level.")
                                .algorithm("### Algorithm\n\n1. **Initialize** a queue and add the starting position.\n"
                                                +
                                                "2. **Mark** the starting position as visited.\n" +
                                                "3. **While** the queue is not empty, process each position:\n" +
                                                "   - If the position is the end, **return** the path length.\n" +
                                                "   - For each possible direction, **check** if the new position is valid and not visited.\n"
                                                +
                                                "   - **Add** the new position to the queue and mark it as visited.\n" +
                                                "4. If the end is not reached, **return** -1.")
                                .implementation("### Implementation\n\n```java\npublic int shortestPathInMaze(int[][] grid) {\n"
                                                +
                                                "    int m = grid.length, n = grid[0].length;\n" +
                                                "    if (grid[0][0] == 1 || grid[m-1][n-1] == 1) return -1;\n" +
                                                "    int[][] directions = {{0, 1}, {1, 0}, {0, -1}, {-1, 0}};\n" +
                                                "    Queue<int[]> queue = new LinkedList<>();\n" +
                                                "    queue.offer(new int[]{0, 0});\n" +
                                                "    grid[0][0] = 1; // mark as visited\n" +
                                                "    int pathLength = 1;\n" +
                                                "    while (!queue.isEmpty()) {\n" +
                                                "        int size = queue.size();\n" +
                                                "        for (int i = 0; i < size; i++) {\n" +
                                                "            int[] current = queue.poll();\n" +
                                                "            int x = current[0], y = current[1];\n" +
                                                "            if (x == m - 1 && y == n - 1) return pathLength;\n" +
                                                "            for (int[] dir : directions) {\n" +
                                                "                int newX = x + dir[0], newY = y + dir[1];\n" +
                                                "                if (newX >= 0 && newX < m && newY >= 0 && newY < n && grid[newX][newY] == 0) {\n"
                                                +
                                                "                    queue.offer(new int[]{newX, newY});\n" +
                                                "                    grid[newX][newY] = 1; // mark as visited\n" +
                                                "                }\n" +
                                                "            }\n" +
                                                "        }\n" +
                                                "        pathLength++;\n" +
                                                "    }\n" +
                                                "    return -1;\n" +
                                                "}\n```")
                                .language(Submission.ELanguage.JAVA)
                                .problemHint(savedHint)
                                .build();

                problemApproachRepository.save(approach1);

                List<ProblemApproach> approaches = new ArrayList<>();
                approaches.add(approach1);
                savedHint.setApproaches(approaches);
                problemHintRepository.save(savedHint);
                problemApproachRepository.save(approach1);

                buildTestCase34(problem);
                return problemRepository.save(problem);
        }

        private void buildTestCase34(Problem problem) {
                TestCase testCase1 = TestCase.builder()
                                .outputData("2")
                                .isPublic(true)
                                .problem(problem)
                                .build();
                buildParameters34(testCase1, "{{0, 1}, {0, 0}}");
                testCaseRepository.save(testCase1);

                TestCase testCase2 = TestCase.builder()
                                .outputData("4")
                                .problem(problem)
                                .build();
                buildParameters34(testCase2, "{{0, 1, 0}, {1, 0, 0}, {0, 0, 0}}");
                testCaseRepository.save(testCase2);
        }

        private void buildParameters34(TestCase testCase, String grid) {
                Parameter parameter1 = Parameter.builder()
                                .inputDataType("int[][]")
                                .name("grid")
                                .inputData(grid)
                                .index(1)
                                .testCase(testCase)
                                .build();

                parameterRepository.save(parameter1);
        }
}