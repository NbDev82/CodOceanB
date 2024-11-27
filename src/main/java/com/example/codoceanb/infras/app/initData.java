package com.example.codoceanb.infras.app;

import com.example.codoceanb.auth.entity.User;
import com.example.codoceanb.auth.repository.UserRepos;
import com.example.codoceanb.submitcode.library.entity.LibrariesSupport;
import com.example.codoceanb.submitcode.library.repository.LibraryRepository;
import com.example.codoceanb.submitcode.parameter.entity.Parameter;
import com.example.codoceanb.submitcode.parameter.repository.ParameterRepository;
import com.example.codoceanb.submitcode.problem.entity.Problem;
import com.example.codoceanb.submitcode.problem.repository.ProblemRepository;
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
import java.util.UUID;

@Component
@Transactional
public class initData {
    @Autowired
    private ProblemRepository problemRepository;
    @Autowired
    private LibraryRepository libraryRepository;
    @Autowired
    private ParameterRepository parameterRepository;
    @Autowired
    private TestCaseRepository testCaseRepository;
    @Autowired
    private UserRepos userRepos;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Bean
    public void init(){
        deleteAll();


        List<User> savedUsers = initUsers();
        if (savedUsers.isEmpty()) {
            return;
        }

        List<Problem> savedProblems = new ArrayList<>();
        savedProblems.addAll(initProblems1to5(savedUsers.get(0)));
        savedProblems.addAll(initProblems6to9(savedUsers.get(2)));
        savedProblems.addAll(initProblems10to14(savedUsers.get(4)));
        savedProblems.addAll(initProblems15to20(savedUsers.get(6)));
        savedProblems.addAll(initProblems21to26(savedUsers.get(8)));

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
        userRepos.deleteAll();
        libraryRepository.deleteAll();
        testCaseRepository.deleteAll();
        parameterRepository.deleteAll();
        problemRepository.deleteAll();
    }

    private List<User> initUsers() {
        List<User> savedUsers = new ArrayList();

        User user=User.builder()
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
                .urlImage( "https://res.cloudinary.com/du5medjhm/image/upload/v1730996001/avatar-40_d7hhex.png")
                .build();

        User user1=User.builder()
                .fullName("Nguyễn Văn Hoàng")
                .phoneNumber("03967789182")
                .dateOfBirth(LocalDateTime.of(2003, 1, 1, 0, 0))
                .email("hoangbeo@gmail.com")
                .password(passwordEncoder.encode("123456Hoang@"))
                .createdAt(LocalDateTime.now())
                .isFirstLogin(true)
                .updatedAt(LocalDateTime.now())
                .isActive(true)
                .urlImage( "https://res.cloudinary.com/du5medjhm/image/upload/v1730996001/avatar-40_d7hhex.png")
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
                .urlImage( "https://res.cloudinary.com/du5medjhm/image/upload/v1730996001/avatar-40_d7hhex.png")
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
                .urlImage( "https://res.cloudinary.com/du5medjhm/image/upload/v1730996001/avatar-40_d7hhex.png")
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
                .urlImage( "https://res.cloudinary.com/du5medjhm/image/upload/v1730996001/avatar-40_d7hhex.png")
                .role(User.ERole.USER) // Đã sửa lại role là user
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
                .email("21110117@student.hcmute.edu.vn")
                .password(passwordEncoder.encode("123456An@"))
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .isFirstLogin(true)
                .isActive(true)
                .urlImage("https://res.cloudinary.com/du5medjhm/image/upload/v1730996001/avatar-40_d7hhex.png")
                .role(User.ERole.USER_VIP)
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

    private List<Problem> initProblems1to5(@NotNull User owner) {
        List<Problem> savedProblems = new ArrayList<>();
        savedProblems.add( buildProblem(owner) );
        savedProblems.add( buildProblem1(owner) );
        savedProblems.add( buildProblem2(owner) );
        savedProblems.add( buildProblem3(owner) );
        savedProblems.add( buildProblem4(owner) );
        savedProblems.add( buildProblem5(owner) );
        return savedProblems;
    }
    private List<Problem> initProblems6to9(@NotNull User owner) {
        List<Problem> savedProblems = new ArrayList<>();
        savedProblems.add( buildProblem6(owner) );
        savedProblems.add( buildProblem7(owner) );
        savedProblems.add( buildProblem8(owner) );
        savedProblems.add( buildProblem9(owner) );
        return savedProblems;
    }
    private List<Problem> initProblems10to14(@NotNull User owner) {
        List<Problem> savedProblems = new ArrayList<>();
        savedProblems.add( buildProblem10(owner) );
        savedProblems.add( buildProblem11(owner) );
        savedProblems.add( buildProblem12(owner) );
        savedProblems.add( buildProblem13(owner) );
        savedProblems.add( buildProblem14(owner) );

        return savedProblems;
    }
    private List<Problem> initProblems15to20(@NotNull User owner) {
        List<Problem> savedProblems = new ArrayList<>();
        savedProblems.add( buildProblem15(owner) );
        savedProblems.add( buildProblem16(owner) );
        savedProblems.add( buildProblem17(owner) );
        savedProblems.add( buildProblem19(owner) );
        savedProblems.add( buildProblem20(owner) );

        return savedProblems;
    }
    private List<Problem> initProblems21to26(@NotNull User owner) {
        List<Problem> savedProblems = new ArrayList<>();

        savedProblems.add( buildProblem21(owner) );
        savedProblems.add( buildProblem22(owner) );
        savedProblems.add( buildProblem23(owner) );
        savedProblems.add( buildProblem24(owner) );
        savedProblems.add( buildProblem25(owner) );
        savedProblems.add( buildProblem26(owner) );

        return savedProblems;
    }
    public Problem buildProblem(User user){
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
                .description("Given an integer x, return true if x is a \n" +
                        "palindrome\n" +
                        ", and false otherwise.")
                .correctAnswer("import java.util.ArrayList;\n" +
                        "import java.util.List;\n" +
                        "\n" +
                        "public class Solution {\n" +
                        "    public static boolean isPalindrome (int x) {\n" +
                        "        // If x is negative or if x ends with 0 (and is not 0 itself), it cannot be a palindrome\n" +
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
                        "    // If the length of the number is odd, we get rid of the middle digit by dividing reversedHalf by 10\n" +
                        "    return x == reversedHalf || x == reversedHalf / 10;\n" +
                        "    }\n" +
                        "}")
                .build();

        buildTestCase(problem);
        buildLibrary(problem);
        return problemRepository.save(problem);
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
        buildParameters(testCase,"525");
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

    public Problem buildProblem1(User user){
        List<Problem.ETopic> topics = new ArrayList<>();
        topics.add(Problem.ETopic.RECURSION);

        Problem problem = Problem.builder()
                .owner(user)
                .title("Missing Number")
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .description("Given an array nums containing n distinct numbers in the range [0, n], return the only number in the range that is missing from the array.\n" +
                        "\n" +
                        " \n" +
                        "\n" +
                        "Example 1:\n" +
                        "\n" +
                        "Input: nums = [3,0,1]\n" +
                        "Output: 2\n" +
                        "Explanation: n = 3 since there are 3 numbers, so all numbers are in the range [0,3]. 2 is the missing number in the range since it does not appear in nums.\n" +
                        "Example 2:\n" +
                        "\n" +
                        "Input: nums = [0,1]\n" +
                        "Output: 2\n" +
                        "Explanation: n = 2 since there are 2 numbers, so all numbers are in the range [0,2]. 2 is the missing number in the range since it does not appear in nums.\n" +
                        "Example 3:\n" +
                        "\n" +
                        "Input: nums = [9,6,4,2,3,5,7,0,1]\n" +
                        "Output: 8\n" +
                        "Explanation: n = 9 since there are 9 numbers, so all numbers are in the range [0,9]. 8 is the missing number in the range since it does not appear in nums.\n" +
                        " \n" +
                        "\n" +
                        "Constraints:\n" +
                        "\n" +
                        "n == nums.length\n" +
                        "1 <= n <= 104\n" +
                        "0 <= nums[i] <= n\n" +
                        "All the numbers of nums are unique.\n" +
                        " \n" +
                        "\n" +
                        "Follow up: Could you implement a solution using only O(1) extra space complexity and O(n) runtime complexity?")
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
                        "        // The missing number is the difference between expectedSum and actualSum\n" +
                        "        return expectedSum - actualSum;\n" +
                        "\n" +
                        "    }\n" +
                        "}")
                .build();

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
        buildParameters1(testCase,"{3, 0, 1}");
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
                .description("Write a function to calculate the sum of all elements in an integer array.\n" +
                        "\n" +
                        "Use the provided function signature:\n" +
                        "\n" +
                        "int arraySum(int[] arr)\n" +
                        "\n" +
                        "Example Test Case:\n" +
                        "\n" +
                        "Input: arr = [1, 2, 3, 4, 5]\n" +
                        "\n" +
                        "Output: 15\n" +
                        "\n" +
                        "Constraints:\n" +
                        "\n" +
                        "The length of the array is between 1 and 1000.\n" +
                        "Each element of the array is between -1000 and 1000.")
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


    public Problem buildProblem3(User user){
        List<Problem.ETopic> topics = new ArrayList<>();
        topics.add(Problem.ETopic.ARRAY);

        Problem problem = Problem.builder()
                .owner(user)
                .title("Search in Rotated Sorted Array")
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .description("You are given an integer array nums sorted in ascending order (with distinct values), and an integer target.\n" +
                        "\n" +
                        "Suppose that nums is rotated at some pivot unknown to you beforehand (i.e., [0,1,2,4,5,6,7] might become [4,5,6,7,0,1,2]).\n" +
                        "\n" +
                        "If target is found in the array return its index, otherwise, return -1.\n" +
                        "\n" +
                        " \n" +
                        "\n" +
                        "Example 1:\n" +
                        "\n" +
                        "Input: nums = [4,5,6,7,0,1,2], target = 0\n" +
                        "Output: 4\n" +
                        "Example 2:\n" +
                        "\n" +
                        "Input: nums = [4,5,6,7,0,1,2], target = 3\n" +
                        "Output: -1\n" +
                        "Example 3:\n" +
                        "\n" +
                        "Input: nums = [1], target = 0\n" +
                        "Output: -1\n" +
                        " \n" +
                        "\n" +
                        "Constraints:\n" +
                        "\n" +
                        "1 <= nums.length <= 5000\n" +
                        "-104 <= nums[i] <= 104\n" +
                        "All values of nums are unique.\n" +
                        "nums is guaranteed to be rotated at some pivot.\n" +
                        "-104 <= target <= 104")
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
                        "                    right = mid - 1; // Target is in the left sorted portion\n" +
                        "                } else {\n" +
                        "                    left = mid + 1; // Target is in the right unsorted portion\n" +
                        "                }\n" +
                        "            } else {\n" +
                        "                // Right side is sorted\n" +
                        "                if (nums[mid] < target && target <= nums[right]) {\n" +
                        "                    left = mid + 1; // Target is in the right sorted portion\n" +
                        "                } else {\n" +
                        "                    right = mid - 1; // Target is in the left unsorted portion\n" +
                        "                }\n" +
                        "            }\n" +
                        "        }\n" +
                        "\n" +
                        "        // Target not found\n" +
                        "        return -1;\n" +
                        "    }\n" +
                        "}")
                .build();

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
        buildParameters3(testCase,"{4, 5, 6, 7, 0, 1, 2}", "0");
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

    public Problem buildProblem4(User user){
        List<Problem.ETopic> topics = new ArrayList<>();
        topics.add(Problem.ETopic.STRING);

        Problem problem = Problem.builder()
                .owner(user)
                .title("Longest Palindromic Subsequence")
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .description("Given a string s, find the longest palindromic subsequence's length in s.\n" +
                        "\n" +
                        "A subsequence is a sequence that can be derived from another sequence by deleting some or no elements without changing the order of the remaining elements.\n" +
                        "\n" +
                        " \n" +
                        "\n" +
                        "Example 1:\n" +
                        "\n" +
                        "Input: s = \"bbbab\"\n" +
                        "Output: 4\n" +
                        "Explanation: One possible longest palindromic subsequence is \"bbbb\".\n" +
                        "Example 2:\n" +
                        "\n" +
                        "Input: s = \"cbbd\"\n" +
                        "Output: 2\n" +
                        "Explanation: One possible longest palindromic subsequence is \"bb\".\n" +
                        " \n" +
                        "\n" +
                        "Constraints:\n" +
                        "\n" +
                        "1 <= s.length <= 1000\n" +
                        "s consists only of lowercase English letters.")
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
                        "        for (int length = 2; length <= n; length++) { // length of the substring\n" +
                        "            for (int i = 0; i < n - length + 1; i++) {\n" +
                        "                int j = i + length - 1; // end index of substring\n" +
                        "                if (s.charAt(i) == s.charAt(j)) {\n" +
                        "                    dp[i][j] = dp[i + 1][j - 1] + 2; // characters match\n" +
                        "                } else {\n" +
                        "                    dp[i][j] = Math.max(dp[i + 1][j], dp[i][j - 1]); // characters don't match\n" +
                        "                }\n" +
                        "            }\n" +
                        "        }\n" +
                        "\n" +
                        "        return dp[0][n - 1]; // The length of the longest palindromic subsequence\n" +
                        "    }\n" +
                        "}")
                .build();

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
        buildParameters4(testCase,"\"bbbab\"");
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
                .description("A robot is located at the top-left corner of a m x n grid (marked 'Start' in the diagram below).\n" +
                        "\n" +
                        "The robot can only move either down or right at any point in time. The robot is trying to reach the bottom-right corner of the grid (marked 'Finish' in the diagram below).\n" +
                        "\n" +
                        "How many possible unique paths are there?\n" +
                        "\n" +
                        "Example 1:\n" +
                        "\n" +
                        "Input: m = 3, n = 7\n" +
                        "Output: 28\n" +
                        "\n" +
                        "Example 2:\n" +
                        "\n" +
                        "Input: m = 3, n = 2\n" +
                        "Output: 3\n" +
                        "Explanation: From the top-left corner, there are a total of 3 ways to reach the bottom-right corner:\n" +
                        "1. Right -> Down -> Down\n" +
                        "2. Down -> Down -> Right\n" +
                        "3. Down -> Right -> Down\n" +
                        "\n" +
                        "Constraints:\n" +
                        "1 <= m, n <= 100\n" +
                        "It's guaranteed that the answer will be less than or equal to 2 * 10^9.")
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

        buildTestCase5(problem);
        buildLibrary5(problem);
        return problemRepository.save(problem);
    }

    private void buildLibrary5(Problem problem) {
        LibrariesSupport librariesSupport = LibrariesSupport.builder()
                .name("java.lang.Math")
                .problem(problem)
                .build();
        libraryRepository.save(librariesSupport);
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
                .description("You are climbing a staircase. It takes n steps to reach the top.\n" +
                        "\n" +
                        "Each time you can either climb 1 or 2 steps. In how many distinct ways can you climb to the top?\n" +
                        "\n" +
                        "Example 1:\n" +
                        "Input: n = 2\n" +
                        "Output: 2\n" +
                        "Explanation: There are two ways to climb to the top.\n" +
                        "1. 1 step + 1 step\n" +
                        "2. 2 steps\n" +
                        "\n" +
                        "Example 2:\n" +
                        "Input: n = 3\n" +
                        "Output: 3\n" +
                        "Explanation: There are three ways to climb to the top.\n" +
                        "1. 1 step + 1 step + 1 step\n" +
                        "2. 1 step + 2 steps\n" +
                        "3. 2 steps + 1 step\n" +
                        "\n" +
                        "Constraints:\n" +
                        "1 <= n <= 45")
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

        buildTestCase6(problem);
        buildLibrary6(problem);
        return problemRepository.save(problem);
    }

    private void buildLibrary6(Problem problem) {
        LibrariesSupport librariesSupport = LibrariesSupport.builder()
                .name("java.util.*") // Không cần thư viện đặc biệt, nhưng giả sử cần sử dụng một số utils
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

    //FAILED
    public Problem buildProblem7(User user) {
        List<Problem.ETopic> topics = new ArrayList<>();
        topics.add(Problem.ETopic.ARRAY);

        Problem problem = Problem.builder()
                .owner(user)
                .title("Valid Parentheses")
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .description("Given a string s containing just the characters '(', ')', '{', '}', '[' and ']', determine if the input string is valid.\n" +
                        "\n" +
                        "An input string is valid if:\n" +
                        "1. Open brackets must be closed by the same type of brackets.\n" +
                        "2. Open brackets must be closed in the correct order.\n" +
                        "\n" +
                        "Example 1:\n" +
                        "Input: s = \"()\"\n" +
                        "Output: true\n" +
                        "\n" +
                        "Example 2:\n" +
                        "Input: s = \"()[]{}\"\n" +
                        "Output: true\n" +
                        "\n" +
                        "Example 3:\n" +
                        "Input: s = \"(]\"\n" +
                        "Output: false\n" +
                        "\n" +
                        "Constraints:\n" +
                        "1 <= s.length <= 10^4\n" +
                        "s consists of parentheses only '()[]{}'.")
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
                .description("Given two strings s and t, return true if s is a subsequence of t, or false otherwise.\n" +
                        "\n" +
                        "A subsequence of a string is a new string generated from the original string with some characters (can be none) deleted without changing the relative order of the remaining characters.\n" +
                        "\n" +
                        "Example 1:\n" +
                        "Input: s = \"abc\", t = \"ahbgdc\"\n" +
                        "Output: true\n" +
                        "\n" +
                        "Example 2:\n" +
                        "Input: s = \"axc\", t = \"ahbgdc\"\n" +
                        "Output: false\n" +
                        "\n" +
                        "Constraints:\n" +
                        "1 <= s.length, t.length <= 10^4\n" +
                        "s and t consist only of lowercase English letters.")
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
                .description("Given an integer array nums, find three numbers whose product is maximum and return the maximum product.\n" +
                        "\n" +
                        "Example 1:\n" +
                        "Input: nums = [1,2,3]\n" +
                        "Output: 6\n" +
                        "\n" +
                        "Example 2:\n" +
                        "Input: nums = [1,2,3,4]\n" +
                        "Output: 24\n" +
                        "\n" +
                        "Example 3:\n" +
                        "Input: nums = [-1,-2,-3]\n" +
                        "Output: -6\n" +
                        "\n" +
                        "Constraints:\n" +
                        "3 <= nums.length <= 10^4\n" +
                        "-1000 <= nums[i] <= 1000")
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
                        "        return Math.max(nums[0] * nums[1] * nums[n - 1], nums[n - 1] * nums[n - 2] * nums[n - 3]);\n" +
                        "    }\n" +
                        "}")
                .build();

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
                .description("Given an integer n, return the number of prime numbers that are strictly less than n.\n" +
                        "\n" +
                        "Example 1:\n" +
                        "Input: n = 10\n" +
                        "Output: 4\n" +
                        "Explanation: There are 4 prime numbers less than 10: 2, 3, 5, 7.\n" +
                        "\n" +
                        "Example 2:\n" +
                        "Input: n = 0\n" +
                        "Output: 0\n" +
                        "\n" +
                        "Example 3:\n" +
                        "Input: n = 1\n" +
                        "Output: 0\n" +
                        "\n" +
                        "Constraints:\n" +
                        "0 <= n <= 5 * 10^6")
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

    // FAILED
    public Problem buildProblem11(User user) {
        List<Problem.ETopic> topics = new ArrayList<>();
        topics.add(Problem.ETopic.STRING);

        Problem problem = Problem.builder()
                .owner(user)
                .title("Longest Common Prefix")
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .description("Write a function to find the longest common prefix string amongst an array of strings.\n" +
                        "\n" +
                        "If there is no common prefix, return an empty string \"\".\n" +
                        "\n" +
                        "Example 1:\n" +
                        "Input: strs = [\"flower\",\"flow\",\"flight\"]\n" +
                        "Output: \"fl\"\n" +
                        "\n" +
                        "Example 2:\n" +
                        "Input: strs = [\"dog\",\"racecar\",\"car\"]\n" +
                        "Output: \"\"\n" +
                        "Explanation: There is no common prefix among the input strings.\n" +
                        "\n" +
                        "Constraints:\n" +
                        "1 <= strs.length <= 200\n" +
                        "0 <= strs[i].length <= 200\n" +
                        "strs[i] consists of only lowercase English letters.")
                .difficulty(Problem.EDifficulty.EASY)
                .topics(topics)
                .functionName("longestCommonPrefix")
                .isDeleted(false)
                .outputDataType("String")
                .correctAnswer("public class Solution {\n" +
                        "    public static String longestCommonPrefix(String[] strs) {\n" +
                        "        if (strs == null || strs.length == 0) return \"\";\n" +
                        "        String prefix = strs[0];\n" +
                        "        for (int i = 1; i < strs.length; i++) {\n" +
                        "            while (strs[i].indexOf(prefix) != 0) {\n" +
                        "                prefix = prefix.substring(0, prefix.length() - 1);\n" +
                        "                if (prefix.isEmpty()) return \"\";\n" +
                        "            }\n" +
                        "        }\n" +
                        "        return prefix;\n" +
                        "    }\n" +
                        "}")
                .build();

        buildTestCase11(problem);
        buildLibrary11(problem);
        return problemRepository.save(problem);
    }

    private void buildLibrary11(Problem problem) {
        // No external libraries are required for this problem
    }

    private void buildTestCase11(Problem problem) {
        TestCase testCase1 = TestCase.builder()
                .outputData("\"fl\"")
                .isPublic(true)
                .problem(problem)
                .build();
        buildParameters11(testCase1, "{\"flower\", \"flow\", \"flight\"}");
        testCaseRepository.save(testCase1);

        TestCase testCase2 = TestCase.builder()
                .outputData("\"\"")
                .problem(problem)
                .build();
        buildParameters11(testCase2, "{\"dog\", \"racecar\", \"car\"}");
        testCaseRepository.save(testCase2);

        TestCase testCase3 = TestCase.builder()
                .outputData("\"a\"")
                .problem(problem)
                .build();
        buildParameters11(testCase3, "{\"a\"}");
        testCaseRepository.save(testCase3);
    }

    private void buildParameters11(TestCase testCase, String inputData) {
        Parameter parameter = Parameter.builder()
                .inputDataType("String[]")
                .name("strs")
                .inputData(inputData)
                .index(1)
                .testCase(testCase)
                .build();
        parameterRepository.save(parameter);
    }

    //FAILED
    public Problem buildProblem12(User user) {
        List<Problem.ETopic> topics = new ArrayList<>();
        topics.add(Problem.ETopic.SORTING);

        Problem problem = Problem.builder()
                .owner(user)
                .title("Sort an Array by Parity")
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .description("Given an integer array nums, move all the even integers at the beginning of the array followed by all the odd integers.\n" +
                        "\n" +
                        "Return any array that satisfies this condition.\n" +
                        "\n" +
                        "Example 1:\n" +
                        "Input: nums = [3,1,2,4]\n" +
                        "Output: [2,4,3,1]\n" +
                        "Explanation: The outputs [4,2,3,1], [2,4,1,3], and others are also accepted.\n" +
                        "\n" +
                        "Example 2:\n" +
                        "Input: nums = [0]\n" +
                        "Output: [0]\n" +
                        "\n" +
                        "Constraints:\n" +
                        "1 <= nums.length <= 5000\n" +
                        "0 <= nums[i] <= 5000")
                .difficulty(Problem.EDifficulty.EASY)
                .topics(topics)
                .functionName("sortArrayByParity")
                .isDeleted(false)
                .outputDataType("int[]")
                .correctAnswer("import java.util.ArrayList;\n" +
                        "import java.util.List;\n" +
                        "\n" +
                        "public class Solution {\n" +
                        "    public static int[] sortArrayByParity(int[] nums) {\n" +
                        "        int[] result = new int[nums.length];\n" +
                        "        int evenIndex = 0;\n" +
                        "        int oddIndex = nums.length - 1;\n" +
                        "\n" +
                        "        for (int num : nums) {\n" +
                        "            if (num % 2 == 0) {\n" +
                        "                result[evenIndex++] = num;\n" +
                        "            } else {\n" +
                        "                result[oddIndex--] = num;\n" +
                        "            }\n" +
                        "        }\n" +
                        "\n" +
                        "        return result;\n" +
                        "    }\n" +
                        "}")
                .build();

        buildTestCase12(problem);
        buildLibrary12(problem);
        return problemRepository.save(problem);
    }

    private void buildLibrary12(Problem problem) {
        // No external libraries are required for this problem
    }

    private void buildTestCase12(Problem problem) {
        TestCase testCase1 = TestCase.builder()
                .outputData("{2,4,3,1}")
                .isPublic(true)
                .problem(problem)
                .build();
        buildParameters12(testCase1, "{3,1,2,4}");
        testCaseRepository.save(testCase1);

        TestCase testCase2 = TestCase.builder()
                .outputData("{0}")
                .problem(problem)
                .build();
        buildParameters12(testCase2, "{0}");
        testCaseRepository.save(testCase2);

        TestCase testCase3 = TestCase.builder()
                .outputData("{4,2,3}")
                .problem(problem)
                .build();
        buildParameters12(testCase3, "{3,4,2}");
        testCaseRepository.save(testCase3);
    }

    private void buildParameters12(TestCase testCase, String inputData) {
        Parameter parameter = Parameter.builder()
                .inputDataType("int[]")
                .name("nums")
                .inputData(inputData)
                .index(1)
                .testCase(testCase)
                .build();
        parameterRepository.save(parameter);
    }

    //FAILED
    public Problem buildProblem13(User user) {
        List<Problem.ETopic> topics = new ArrayList<>();
        topics.add(Problem.ETopic.GRAPH);

        Problem problem = Problem.builder()
                .owner(user)
                .title("Number of Connected Components in an Undirected Graph")
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .description("You are given an integer n, the number of nodes in a graph, and an array edges where edges[i] = [u, v] represents an undirected edge between nodes u and v.\n" +
                        "\n" +
                        "Return the number of connected components in the graph.\n" +
                        "\n" +
                        "Example 1:\n" +
                        "Input: n = 5, edges = [[0,1],[1,2],[3,4]]\n" +
                        "Output: 2\n" +
                        "Explanation: There are two connected components in the graph: {0,1,2} and {3,4}.\n" +
                        "\n" +
                        "Example 2:\n" +
                        "Input: n = 5, edges = [[0,1],[1,2],[2,3],[3,4]]\n" +
                        "Output: 1\n" +
                        "Explanation: All nodes are connected.\n" +
                        "\n" +
                        "Constraints:\n" +
                        "1 <= n <= 2000\n" +
                        "1 <= edges.length <= 5000\n" +
                        "edges[i].length == 2\n" +
                        "0 <= edges[i][0], edges[i][1] < n")
                .difficulty(Problem.EDifficulty.NORMAL)
                .topics(topics)
                .functionName("countComponents")
                .isDeleted(false)
                .outputDataType("int")
                .correctAnswer("import java.util.ArrayList;\n" +
                        "import java.util.List;\n" +
                        "import java.util.Stack;\n" +
                        "\n" +
                        "public class Solution {\n" +
                        "    public int countComponents(int n, int[][] edges) {\n" +
                        "        List<List<Integer>> graph = new ArrayList<>();\n" +
                        "        for (int i = 0; i < n; i++) {\n" +
                        "            graph.add(new ArrayList<>());\n" +
                        "        }\n" +
                        "\n" +
                        "        // Build the graph\n" +
                        "        for (int[] edge : edges) {\n" +
                        "            graph.get(edge[0]).add(edge[1]);\n" +
                        "            graph.get(edge[1]).add(edge[0]);\n" +
                        "        }\n" +
                        "\n" +
                        "        boolean[] visited = new boolean[n];\n" +
                        "        int components = 0;\n" +
                        "\n" +
                        "        for (int i = 0; i < n; i++) {\n" +
                        "            if (!visited[i]) {\n" +
                        "                components++;\n" +
                        "                dfs(graph, i, visited);\n" +
                        "            }\n" +
                        "        }\n" +
                        "\n" +
                        "        return components;\n" +
                        "    }\n" +
                        "\n" +
                        "    private void dfs(List<List<Integer>> graph, int node, boolean[] visited) {\n" +
                        "        Stack<Integer> stack = new Stack<>();\n" +
                        "        stack.push(node);\n" +
                        "        visited[node] = true;\n" +
                        "        while (!stack.isEmpty()) {\n" +
                        "            int currentNode = stack.pop();\n" +
                        "            for (int neighbor : graph.get(currentNode)) {\n" +
                        "                if (!visited[neighbor]) {\n" +
                        "                    visited[neighbor] = true;\n" +
                        "                    stack.push(neighbor);\n" +
                        "                }\n" +
                        "            }\n" +
                        "        }\n" +
                        "    }\n" +
                        "}")
                .build();

        buildTestCase13(problem);
        buildLibrary13(problem);
        return problemRepository.save(problem);
    }

    private void buildLibrary13(Problem problem) {
        // No external libraries are required for this problem
    }

    private void buildTestCase13(Problem problem) {
        TestCase testCase1 = TestCase.builder()
                .outputData("2")
                .isPublic(true)
                .problem(problem)
                .build();
        buildParameters13(testCase1, "5, {{0,1},{1,2},{3,4}}");
        testCaseRepository.save(testCase1);

        TestCase testCase2 = TestCase.builder()
                .outputData("1")
                .problem(problem)
                .build();
        buildParameters13(testCase2, "5, {{0,1},{1,2},{2,3},{3,4}}");
        testCaseRepository.save(testCase2);
    }

    private void buildParameters13(TestCase testCase, String inputData) {
        Parameter parameter = Parameter.builder()
                .inputDataType("int[][]")
                .name("edges")
                .inputData(inputData)
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
                .description("Given an integer array nums, return the maximum number in the array.\n" +
                        "\n" +
                        "Example 1:\n" +
                        "Input: nums = [1, 2, 3, 4, 5]\n" +
                        "Output: 5\n" +
                        "Explanation: The maximum number in the array is 5.\n" +
                        "\n" +
                        "Example 2:\n" +
                        "Input: nums = [-1, -2, -3, -4]\n" +
                        "Output: -1\n" +
                        "Explanation: The maximum number in the array is -1.\n" +
                        "\n" +
                        "Constraints:\n" +
                        "1 <= nums.length <= 100\n" +
                        "-10^4 <= nums[i] <= 10^4")
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

        buildTestCase14(problem);
        buildLibrary14(problem);
        return problemRepository.save(problem);
    }

    private void buildLibrary14(Problem problem) {
        // No external libraries are required for this problem
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
                .description("Given an integer array nums, return the sum of all the elements in the array.\n" +
                        "\n" +
                        "Example 1:\n" +
                        "Input: nums = [1, 2, 3, 4, 5]\n" +
                        "Output: 15\n" +
                        "Explanation: The sum of the elements is 1 + 2 + 3 + 4 + 5 = 15.\n" +
                        "\n" +
                        "Example 2:\n" +
                        "Input: nums = [-1, -2, -3, -4]\n" +
                        "Output: -10\n" +
                        "Explanation: The sum of the elements is -1 + -2 + -3 + -4 = -10.\n" +
                        "\n" +
                        "Constraints:\n" +
                        "1 <= nums.length <= 100\n" +
                        "-10^4 <= nums[i] <= 10^4")
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

        buildTestCase15(problem);
        buildLibrary15(problem);
        return problemRepository.save(problem);
    }

    private void buildLibrary15(Problem problem) {
        // No external libraries are required for this problem
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
    public Problem buildProblem16(User user) {
        List<Problem.ETopic> topics = new ArrayList<>();
        topics.add(Problem.ETopic.ARRAY);

        Problem problem = Problem.builder()
                .owner(user)
                .title("Find the Maximum Element in an Array")
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .description("Given an integer array nums, return the maximum element in the array.\n" +
                        "\n" +
                        "Example 1:\n" +
                        "Input: nums = [1, 2, 3, 4, 5]\n" +
                        "Output: 5\n" +
                        "Explanation: The maximum element is 5.\n" +
                        "\n" +
                        "Example 2:\n" +
                        "Input: nums = [10, 9, 8, 7, 6]\n" +
                        "Output: 10\n" +
                        "Explanation: The maximum element is 10.\n" +
                        "\n" +
                        "Constraints:\n" +
                        "1 <= nums.length <= 100\n" +
                        "-10^4 <= nums[i] <= 10^4")
                .difficulty(Problem.EDifficulty.EASY)
                .topics(topics)
                .functionName("findMax")
                .isDeleted(false)
                .outputDataType("int")
                .correctAnswer("public class Solution {\n" +
                        "    public int findMax(int[] nums) {\n" +
                        "        int max = nums[0];\n" +
                        "        for (int i = 1; i < nums.length; i++) {\n" +
                        "            max = Math.max(max, nums[i]);\n" +
                        "        }\n" +
                        "        return max;\n" +
                        "    }\n" +
                        "}")
                .build();

        buildTestCase16(problem);
        buildLibrary16(problem);
        return problemRepository.save(problem);
    }

    private void buildLibrary16(Problem problem) {
        // No external libraries are required for this problem
    }

    private void buildTestCase16(Problem problem) {
        TestCase testCase1 = TestCase.builder()
                .outputData("5")
                .isPublic(true)
                .problem(problem)
                .build();
        buildParameters16(testCase1, "{1, 2, 3, 4, 5}");
        testCaseRepository.save(testCase1);

        TestCase testCase2 = TestCase.builder()
                .outputData("10")
                .problem(problem)
                .build();
        buildParameters16(testCase2, "{10, 9, 8, 7, 6}");
        testCaseRepository.save(testCase2);
    }

    private void buildParameters16(TestCase testCase, String inputData) {
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
                .description("Given a string s, return true if it is a palindrome, and false otherwise.\n" +
                        "A string is a palindrome if it reads the same forward and backward.\n" +
                        "\n" +
                        "Example 1:\n" +
                        "Input: s = \"racecar\"\n" +
                        "Output: true\n" +
                        "Explanation: The string \"racecar\" is the same forward and backward.\n" +
                        "\n" +
                        "Example 2:\n" +
                        "Input: s = \"hello\"\n" +
                        "Output: false\n" +
                        "Explanation: The string \"hello\" is not the same forward and backward.\n" +
                        "\n" +
                        "Constraints:\n" +
                        "1 <= s.length <= 1000\n" +
                        "s consists of only lowercase English letters.")
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

        buildTestCase17(problem);
        buildLibrary17(problem);
        return problemRepository.save(problem);
    }

    private void buildLibrary17(Problem problem) {
        // No external libraries are required for this problem
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

    //FAILED
    public Problem buildProblem19(User user) {
        List<Problem.ETopic> topics = new ArrayList<>();
        topics.add(Problem.ETopic.ARRAY);

        Problem problem = Problem.builder()
                .owner(user)
                .title("Median of Two Sorted Arrays")
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .description("Given two sorted arrays nums1 and nums2 of size m and n respectively, return the median of the two sorted arrays.\n" +
                        "The overall run time complexity should be O(log (min(m,n))).\n" +
                        "\n" +
                        "Example 1:\n" +
                        "Input: nums1 = [1, 3], nums2 = [2]\n" +
                        "Output: 2.00000\n" +
                        "Explanation: merged array = [1, 2, 3] and median is 2.\n" +
                        "\n" +
                        "Example 2:\n" +
                        "Input: nums1 = [1, 2], nums2 = [3, 4]\n" +
                        "Output: 2.50000\n" +
                        "Explanation: merged array = [1, 2, 3, 4] and median is (2 + 3)/2 = 2.5.\n" +
                        "\n" +
                        "Example 3:\n" +
                        "Input: nums1 = [0, 0], nums2 = [0, 0]\n" +
                        "Output: 0.00000\n" +
                        "Explanation: merged array = [0, 0, 0, 0] and median is 0.\n" +
                        "\n" +
                        "Example 4:\n" +
                        "Input: nums1 = [], nums2 = [1]\n" +
                        "Output: 1.00000\n" +
                        "Explanation: merged array = [1] and median is 1.\n" +
                        "\n" +
                        "Example 5:\n" +
                        "Input: nums1 = [2], nums2 = []\n" +
                        "Output: 2.00000\n" +
                        "Explanation: merged array = [2] and median is 2.\n" +
                        "\n" +
                        "Constraints:\n" +
                        "nums1.length == m\n" +
                        "nums2.length == n\n" +
                        "0 <= m <= 1000\n" +
                        "0 <= n <= 1000\n" +
                        "1 <= m + n <= 2000\n" +
                        "-10^6 <= nums1[i], nums2[i] <= 10^6")
                .difficulty(Problem.EDifficulty.HARD)
                .topics(topics)
                .functionName("findMedianSortedArrays")
                .isDeleted(false)
                .outputDataType("double")
                .correctAnswer("public class Solution {\n" +
                        "    public double findMedianSortedArrays(int[] nums1, int[] nums2) {\n" +
                        "        int m = nums1.length, n = nums2.length;\n" +
                        "        if (m > n) {\n" +
                        "            return findMedianSortedArrays(nums2, nums1);\n" +
                        "        }\n" +
                        "        int left = 0, right = m, medianPos = (m + n + 1) / 2;\n" +
                        "        while (left <= right) {\n" +
                        "            int partitionX = (left + right) / 2;\n" +
                        "            int partitionY = medianPos - partitionX;\n" +
                        "            int maxX = (partitionX == 0) ? Integer.MIN_VALUE : nums1[partitionX - 1];\n" +
                        "            int maxY = (partitionY == 0) ? Integer.MIN_VALUE : nums2[partitionY - 1];\n" +
                        "            int minX = (partitionX == m) ? Integer.MAX_VALUE : nums1[partitionX];\n" +
                        "            int minY = (partitionY == n) ? Integer.MAX_VALUE : nums2[partitionY];\n" +
                        "\n" +
                        "            if (maxX <= minY && maxY <= minX) {\n" +
                        "                if ((m + n) % 2 == 0) {\n" +
                        "                    return (Math.max(maxX, maxY) + Math.min(minX, minY)) / 2.0;\n" +
                        "                } else {\n" +
                        "                    return Math.max(maxX, maxY);\n" +
                        "                }\n" +
                        "            } else if (maxX > minY) {\n" +
                        "                right = partitionX - 1;\n" +
                        "            } else {\n" +
                        "                left = partitionX + 1;\n" +
                        "            }\n" +
                        "        }\n" +
                        "        throw new IllegalArgumentException(\"Input arrays are not sorted.\");\n" +
                        "    }\n" +
                        "}")
                .build();

        buildTestCase19(problem);
        buildLibrary19(problem);
        return problemRepository.save(problem);
    }

    private void buildLibrary19(Problem problem) {
        // No external libraries are required for this problem
    }

    private void buildTestCase19(Problem problem) {
        TestCase testCase1 = TestCase.builder()
                .outputData("2.00000")
                .isPublic(true)
                .problem(problem)
                .build();
        buildParameters19(testCase1, "{1, 3}", "{2}");
        testCaseRepository.save(testCase1);

        TestCase testCase2 = TestCase.builder()
                .outputData("2.50000")
                .problem(problem)
                .build();
        buildParameters19(testCase2, "{1, 2}", "{3, 4}");
        testCaseRepository.save(testCase2);

        TestCase testCase3 = TestCase.builder()
                .outputData("0.00000")
                .problem(problem)
                .build();
        buildParameters19(testCase3, "{0, 0}", "{0, 0}");
        testCaseRepository.save(testCase3);
    }

    private void buildParameters19(TestCase testCase, String nums1, String nums2) {
        Parameter parameter1 = Parameter.builder()
                .inputDataType("int[]")
                .name("nums1")
                .inputData(nums1)
                .index(1)
                .testCase(testCase)
                .build();

        Parameter parameter2 = Parameter.builder()
                .inputDataType("int[]")
                .name("nums2")
                .inputData(nums2)
                .index(2)
                .testCase(testCase)
                .build();

        parameterRepository.save(parameter1);
        parameterRepository.save(parameter2);
    }

    //FAILED
    public Problem buildProblem20(User user) {
        List<Problem.ETopic> topics = new ArrayList<>();
        topics.add(Problem.ETopic.GRAPH);

        Problem problem = Problem.builder()
                .owner(user)
                .title("Word Ladder II")
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .description("Given two words, beginWord and endWord, and a dictionary's word list, return all the shortest transformation sequences from beginWord to endWord, such that:\n" +
                        "Only one letter can be changed at a time.\n" +
                        "Each transformed word must exist in the word list. Note that beginWord is not a transformed word.\n" +
                        "\n" +
                        "Example 1:\n" +
                        "Input: beginWord = \"hit\", endWord = \"cog\", wordList = [\"hot\",\"dot\",\"dog\",\"lot\",\"log\",\"cog\"]\n" +
                        "Output: [[\"hit\",\"hot\",\"dot\",\"dog\",\"cog\"], [\"hit\",\"hot\",\"lot\",\"log\",\"cog\"]]\n" +
                        "\n" +
                        "Example 2:\n" +
                        "Input: beginWord = \"hit\", endWord = \"cog\", wordList = [\"hot\",\"dot\",\"dog\",\"lot\",\"log\"]\n" +
                        "Output: []\n" +
                        "Explanation: The endWord \"cog\" is not in wordList, therefore no possible transformation.\n" +
                        "\n" +
                        "Note:\n" +
                        "1. All words have the same length.\n" +
                        "2. All words consist of lowercase alphabetic characters.\n" +
                        "3. You may assume no duplicates in the word list.\n" +
                        "4. You must implement a solution with time complexity less than O(n^2).")
                .difficulty(Problem.EDifficulty.HARD)
                .topics(topics)
                .functionName("findLadders")
                .isDeleted(false)
                .outputDataType("List<List<String>>")
                .correctAnswer("public class Solution {\n" +
                        "    public List<List<String>> findLadders(String beginWord, String endWord, List<String> wordList) {\n" +
                        "        List<List<String>> result = new ArrayList<>();\n" +
                        "        Set<String> wordSet = new HashSet<>(wordList);\n" +
                        "        if (!wordSet.contains(endWord)) return result;\n" +
                        "        Set<String> visited = new HashSet<>();\n" +
                        "        Queue<List<String>> queue = new LinkedList<>();\n" +
                        "        queue.offer(Arrays.asList(beginWord));\n" +
                        "        visited.add(beginWord);\n" +
                        "        boolean found = false;\n" +
                        "        while (!queue.isEmpty() && !found) {\n" +
                        "            Set<String> currentLevelVisited = new HashSet<>();\n" +
                        "            int size = queue.size();\n" +
                        "            for (int i = 0; i < size; i++) {\n" +
                        "                List<String> currentPath = queue.poll();\n" +
                        "                String lastWord = currentPath.get(currentPath.size() - 1);\n" +
                        "                for (int j = 0; j < lastWord.length(); j++) {\n" +
                        "                    char[] wordArray = lastWord.toCharArray();\n" +
                        "                    for (char c = 'a'; c <= 'z'; c++) {\n" +
                        "                        wordArray[j] = c;\n" +
                        "                        String nextWord = new String(wordArray);\n" +
                        "                        if (nextWord.equals(endWord)) {\n" +
                        "                            currentPath.add(nextWord);\n" +
                        "                            result.add(new ArrayList<>(currentPath));\n" +
                        "                            found = true;\n" +
                        "                        }\n" +
                        "                        if (wordSet.contains(nextWord) && !visited.contains(nextWord)) {\n" +
                        "                            currentPath.add(nextWord);\n" +
                        "                            queue.offer(new ArrayList<>(currentPath));\n" +
                        "                            currentPath.remove(currentPath.size() - 1);\n" +
                        "                            currentLevelVisited.add(nextWord);\n" +
                        "                        }\n" +
                        "                    }\n" +
                        "                }\n" +
                        "            }\n" +
                        "            visited.addAll(currentLevelVisited);\n" +
                        "        }\n" +
                        "        return result;\n" +
                        "    }\n" +
                        "}")
                .build();

        buildTestCase20(problem);
        buildLibrary20(problem);
        return problemRepository.save(problem);
    }

    private void buildLibrary20(Problem problem) {
        // No external libraries are required for this problem
    }

    private void buildTestCase20(Problem problem) {
        TestCase testCase1 = TestCase.builder()
                .outputData("[[\"hit\",\"hot\",\"dot\",\"dog\",\"cog\"], [\"hit\",\"hot\",\"lot\",\"log\",\"cog\"]]")
                .isPublic(true)
                .problem(problem)
                .build();
        buildParameters20(testCase1, "\"hit\"", "\"cog\"", "[\"hot\",\"dot\",\"dog\",\"lot\",\"log\",\"cog\"]");
        testCaseRepository.save(testCase1);

        TestCase testCase2 = TestCase.builder()
                .outputData("[]")
                .problem(problem)
                .build();
        buildParameters20(testCase2, "\"hit\"", "\"cog\"", "[\"hot\",\"dot\",\"dog\",\"lot\",\"log\"]");
        testCaseRepository.save(testCase2);
    }

    private void buildParameters20(TestCase testCase, String beginWord, String endWord, String wordList) {
        Parameter parameter1 = Parameter.builder()
                .inputDataType("String")
                .name("beginWord")
                .inputData(beginWord)
                .index(1)
                .testCase(testCase)
                .build();

        Parameter parameter2 = Parameter.builder()
                .inputDataType("String")
                .name("endWord")
                .inputData(endWord)
                .index(2)
                .testCase(testCase)
                .build();

        Parameter parameter3 = Parameter.builder()
                .inputDataType("List<String>")
                .name("wordList")
                .inputData(wordList)
                .index(3)
                .testCase(testCase)
                .build();

        parameterRepository.save(parameter1);
        parameterRepository.save(parameter2);
        parameterRepository.save(parameter3);
    }
    public Problem buildProblem21(User user) {
        List<Problem.ETopic> topics = new ArrayList<>();
        topics.add(Problem.ETopic.ARRAY);

        Problem problem = Problem.builder()
                .owner(user)
                .title("Find Minimum in Rotated Sorted Array")
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .description("Suppose an array of length n sorted in ascending order is rotated between 1 and n times. For example, the array nums = [0,1,2,4,5,6,7] might become:\n" +
                        "[4,5,6,7,0,1,2] if it was rotated 4 times.\n" +
                        "\n" +
                        "Given the sorted rotated array nums of unique elements, return the minimum element of this array.\n" +
                        "\n" +
                        "Example 1:\n" +
                        "Input: nums = [3,4,5,1,2]\n" +
                        "Output: 1\n" +
                        "Explanation: The original array was [1,2,3,4,5] rotated 3 times.\n" +
                        "\n" +
                        "Example 2:\n" +
                        "Input: nums = [4,5,6,7,0,1,2]\n" +
                        "Output: 0\n" +
                        "Explanation: The original array was [0,1,2,4,5,6,7] rotated 4 times.\n" +
                        "\n" +
                        "Example 3:\n" +
                        "Input: nums = [11,13,15,17]\n" +
                        "Output: 11\n" +
                        "Explanation: The original array was [11,13,15,17] and it was rotated 0 times.\n" +
                        "\n" +
                        "Constraints:\n" +
                        "1. n == nums.length\n" +
                        "2. 1 <= n <= 5000\n" +
                        "3. -5000 <= nums[i] <= 5000\n" +
                        "4. All the integers of nums are unique.\n" +
                        "5. nums is guaranteed to be rotated at least once.")
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

        buildTestCase21(problem);
        buildLibrary21(problem);
        return problemRepository.save(problem);
    }

    private void buildLibrary21(Problem problem) {
        // No external libraries are required for this problem
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
                .description("The Fibonacci numbers, commonly denoted F(n), form a sequence of numbers such that each number is the sum of the two preceding ones, starting from 0 and 1. That is,\n" +
                        "\n" +
                        "F(0) = 0, F(1) = 1\n" +
                        "F(n) = F(n - 1) + F(n - 2), for n > 1.\n" +
                        "\n" +
                        "Given n, calculate F(n).\n" +
                        "\n" +
                        "Example 1:\n" +
                        "Input: n = 2\n" +
                        "Output: 1\n" +
                        "Explanation: F(2) = F(1) + F(0) = 1 + 0 = 1.\n" +
                        "\n" +
                        "Example 2:\n" +
                        "Input: n = 3\n" +
                        "Output: 2\n" +
                        "Explanation: F(3) = F(2) + F(1) = 1 + 1 = 2.\n" +
                        "\n" +
                        "Example 3:\n" +
                        "Input: n = 4\n" +
                        "Output: 3\n" +
                        "Explanation: F(4) = F(3) + F(2) = 2 + 1 = 3.\n" +
                        "\n" +
                        "Constraints:\n" +
                        "0 <= n <= 30")
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

        buildTestCase22(problem);
        buildLibrary22(problem);
        return problemRepository.save(problem);
    }

    private void buildLibrary22(Problem problem) {
        // No external libraries are required for this problem
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
                .description("Given an array of integers, return the number of even numbers in the array.\n" +
                        "\n" +
                        "Example 1:\n" +
                        "Input: nums = [1, 2, 3, 4]\n" +
                        "Output: 2\n" +
                        "Explanation: There are two even numbers in the array: 2 and 4.\n" +
                        "\n" +
                        "Example 2:\n" +
                        "Input: nums = [1, 3, 5, 7]\n" +
                        "Output: 0\n" +
                        "Explanation: There are no even numbers in the array.\n" +
                        "\n" +
                        "Example 3:\n" +
                        "Input: nums = [2, 4, 6, 8, 10]\n" +
                        "Output: 5\n" +
                        "Explanation: All numbers in the array are even.\n" +
                        "\n" +
                        "Constraints:\n" +
                        "1 <= nums.length <= 100\n" +
                        "1 <= nums[i] <= 1000")
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

        buildTestCase23(problem);
        buildLibrary23(problem);
        return problemRepository.save(problem);
    }

    private void buildLibrary23(Problem problem) {
        // No external libraries are required for this problem
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
                .description("Given an integer n, return true if it is a prime number, and false otherwise.\n" +
                        "\n" +
                        "A prime number is a natural number greater than 1 that cannot be formed by multiplying two smaller natural numbers.\n" +
                        "\n" +
                        "Example 1:\n" +
                        "Input: n = 5\n" +
                        "Output: true\n" +
                        "Explanation: 5 is a prime number.\n" +
                        "\n" +
                        "Example 2:\n" +
                        "Input: n = 4\n" +
                        "Output: false\n" +
                        "Explanation: 4 is not a prime number because it can be formed by multiplying 2 * 2.\n" +
                        "\n" +
                        "Example 3:\n" +
                        "Input: n = 1\n" +
                        "Output: false\n" +
                        "Explanation: 1 is not a prime number.\n" +
                        "\n" +
                        "Constraints:\n" +
                        "1 <= n <= 1000")
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

        buildTestCase24(problem);
        buildLibrary24(problem);
        return problemRepository.save(problem);
    }

    private void buildLibrary24(Problem problem) {
        // No external libraries are required for this problem
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
                .description("Given an integer n, return the number of prime numbers that are less than or equal to n.\n" +
                        "\n" +
                        "A prime number is a natural number greater than 1 that cannot be formed by multiplying two smaller natural numbers.\n" +
                        "\n" +
                        "Example 1:\n" +
                        "Input: n = 10\n" +
                        "Output: 4\n" +
                        "Explanation: The prime numbers less than or equal to 10 are [2, 3, 5, 7].\n" +
                        "\n" +
                        "Example 2:\n" +
                        "Input: n = 30\n" +
                        "Output: 10\n" +
                        "Explanation: The prime numbers less than or equal to 30 are [2, 3, 5, 7, 11, 13, 17, 19, 23, 29].\n" +
                        "\n" +
                        "Constraints:\n" +
                        "1 <= n <= 1000")
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

        buildTestCase25(problem);
        buildLibrary25(problem);
        return problemRepository.save(problem);
    }

    private void buildLibrary25(Problem problem) {

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

    //FAILED
    public Problem buildProblem26(User user) {
        List<Problem.ETopic> topics = new ArrayList<>();
        topics.add(Problem.ETopic.MATH);

        Problem problem = Problem.builder()
                .owner(user)
                .title("Check Triangle Validity")
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .description("Given three integers representing the sides of a triangle, determine if the three sides can form a valid triangle.\n" +
                        "\n" +
                        "A triangle is valid if the sum of the lengths of any two sides is greater than the length of the remaining side.\n" +
                        "\n" +
                        "Example 1:\n" +
                        "Input: a = 3, b = 4, c = 5\n" +
                        "Output: true\n" +
                        "Explanation: A triangle with sides 3, 4, 5 is a valid triangle.\n" +
                        "\n" +
                        "Example 2:\n" +
                        "Input: a = 1, b = 2, c = 3\n" +
                        "Output: false\n" +
                        "Explanation: A triangle with sides 1, 2, 3 is not a valid triangle.\n" +
                        "\n" +
                        "Constraints:\n" +
                        "1 <= a, b, c <= 1000")
                .difficulty(Problem.EDifficulty.EASY)
                .topics(topics)
                .functionName("isValidTriangle")
                .isDeleted(false)
                .outputDataType("boolean")
                .correctAnswer("public class Solution {\n" +
                        "    public boolean isValidTriangle(int a, int b, int c) {\n" +
                        "        return a + b > c && a + c > b && b + c > a;\n" +
                        "    }\n" +
                        "}")
                .build();

        buildTestCase26(problem);
        buildLibrary26(problem);
        return problemRepository.save(problem);
    }

    private void buildLibrary26(Problem problem) {
        // No external libraries are required for this problem
    }

    private void buildTestCase26(Problem problem) {
        TestCase testCase1 = TestCase.builder()
                .outputData("true")
                .isPublic(true)
                .problem(problem)
                .build();
        buildParameters26(testCase1, "3, 4, 5");
        testCaseRepository.save(testCase1);

        TestCase testCase2 = TestCase.builder()
                .outputData("false")
                .problem(problem)
                .build();
        buildParameters26(testCase2, "1, 2, 3");
        testCaseRepository.save(testCase2);

        TestCase testCase3 = TestCase.builder()
                .outputData("true")
                .problem(problem)
                .build();
        buildParameters26(testCase3, "5, 6, 7");
        testCaseRepository.save(testCase3);
    }

    private void buildParameters26(TestCase testCase, String sides) {
        Parameter parameter1 = Parameter.builder()
                .inputDataType("int,int,int")
                .name("a,b,c")
                .inputData(sides)
                .index(1)
                .testCase(testCase)
                .build();

        parameterRepository.save(parameter1);
    }

}