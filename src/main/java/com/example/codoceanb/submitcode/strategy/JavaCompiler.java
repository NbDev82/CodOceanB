package com.example.codoceanb.submitcode.strategy;

import com.example.codoceanb.submitcode.DTO.CustomTestCaseDTO;
import com.example.codoceanb.submitcode.DTO.ParameterDTO;
import com.example.codoceanb.submitcode.DTO.ResultDTO;
import com.example.codoceanb.submitcode.DTO.TestCaseResultDTO;
import com.example.codoceanb.submitcode.ECompilerConstants;
import com.example.codoceanb.submitcode.library.entity.LibrariesSupport;
import com.example.codoceanb.submitcode.parameter.entity.Parameter;
import com.example.codoceanb.submitcode.parameter.service.ParameterService;
import com.example.codoceanb.submitcode.problem.entity.Problem;
import com.example.codoceanb.submitcode.submission.entity.Submission;
import com.example.codoceanb.submitcode.testcase.entity.TestCase;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

import javax.tools.*;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Array;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class JavaCompiler implements CompilerStrategy {
    private static final Logger log = LogManager.getLogger(JavaCompiler.class);
    private final ParameterService parameterService;

    public JavaCompiler(ParameterService parameterService) {
        this.parameterService = parameterService;
    }

    private boolean hasMatchingDataTypesAndOutput(String outputDatatype,
                                                  String expectedDatatype,
                                                  String outputData,
                                                  String expected) {
        return outputDatatype.equals(expectedDatatype) &&
                outputData.equals(expected);
    }

    @Override
    public ResultDTO run(String fileLink, String fileName, String code, Problem problem) {
        List<TestCaseResultDTO> testCaseResultDTOs;
        ResultDTO.ResultDTOBuilder builder = ResultDTO.builder();
        String functionName = problem.getFunctionName();
        List<TestCase> testCases = problem.getTestCases();

        try {
            String runCodeWithAllTestCase = createRunCodeWithAllTestCase(code, testCases, functionName, problem.getOutputDataType());
            writeFile(fileLink, fileName, runCodeWithAllTestCase);
            CompilerResult compile = compile(fileLink, fileName);

            if (compile.getCompilerConstants() != ECompilerConstants.SUCCESS) {
                return createCompilationFailureResult(compile);
            }

            testCaseResultDTOs = runWithTestCases(problem.getOutputDataType(), testCases, fileLink, functionName);
            if (testCaseResultDTOs == null) {
                throw new RuntimeException("Error running the code!");
            }
        } finally {
            deleteFileCompiled(fileLink, fileName);
        }

        long passedTestCases = testCaseResultDTOs.stream().filter(TestCaseResultDTO::isPassed).count();
        boolean isAccepted = testCases.size() == passedTestCases;
        
        testCaseResultDTOs = testCaseResultDTOs.stream()
                .filter(TestCaseResultDTO::isPublic)
                .collect(Collectors.toList());

        return builder
                .message("That is your result of your code for this problem")
                .maxTestcase(String.valueOf(testCases.size()))
                .passedTestcase(String.valueOf(passedTestCases))
                .testCaseResultDTOS(testCaseResultDTOs)
                .isAccepted(isAccepted)
                .status(isAccepted ? Submission.EStatus.ACCEPTED : Submission.EStatus.WRONG_ANSWER)
                .build();
    }

    @Override
    public ResultDTO runWithCustomTestcase(String fileLink, String fileName, List<CustomTestCaseDTO> customTestCaseDTOs, String code, Problem problem) {
        try {
            List<String> correctAnswerWithCustomTestcase = getCorrectAnswerWithCustomTestcase(fileLink, fileName, customTestCaseDTOs, problem.getCorrectAnswer(), problem);
            ResultDTO actualAnswerWithCustomTestcase = getActualAnswerWithCustomTestcase(fileLink, fileName, customTestCaseDTOs, code, problem);
            return compareResultWithCorrectAnswer(correctAnswerWithCustomTestcase, actualAnswerWithCustomTestcase);
        } finally {
            deleteFileCompiled(fileLink, fileName);
        }
    }

    private List<String> getCorrectAnswerWithCustomTestcase(String fileLink, String fileName, List<CustomTestCaseDTO> customTestCaseDTOs, String code, Problem problem) {
        List<TestCase> testCases = problem.getTestCases();
        String functionName = problem.getFunctionName();
        String runCodeWithCustomTestCase = createRunCodeWithCustomTestCase(code, testCases, functionName, problem.getOutputDataType(), customTestCaseDTOs);
        
        writeFile(fileLink, fileName, runCodeWithCustomTestCase);
        CompilerResult compile = compile(fileLink, fileName);
        
        if (compile.getCompilerConstants() != ECompilerConstants.SUCCESS) {
            throw new RuntimeException("Error compiling the code!");
        }

        List<String> testCaseResultDTOs = runWithCustomTestCases(fileLink, functionName);
        if (testCaseResultDTOs == null) {
            throw new RuntimeException("Error running the code!");
        }
        
        return testCaseResultDTOs;
    }

    private ResultDTO getActualAnswerWithCustomTestcase(String fileLink, String fileName, List<CustomTestCaseDTO> customTestCaseDTOs, String code, Problem problem) {
        List<TestCase> testCases = problem.getTestCases();
        String functionName = problem.getFunctionName();
        String runCodeWithCustomTestCase = createRunCodeWithCustomTestCase(code, testCases, functionName, problem.getOutputDataType(), customTestCaseDTOs);
        
        writeFile(fileLink, fileName, runCodeWithCustomTestCase);
        CompilerResult compile = compile(fileLink, fileName);
        
        if (compile.getCompilerConstants() != ECompilerConstants.SUCCESS) {
            throw new RuntimeException("Error compiling the code!");
        }

        List<String> result = runWithCustomTestCases(fileLink, functionName);
        if (result == null) {
            throw new RuntimeException("Error running the code!");
        }

        List<TestCaseResultDTO> testCaseResultDTOs = new ArrayList<>();
        for (int i = 0; i < result.size(); i++) {
            String output = result.get(i);
            List<ParameterDTO> input = customTestCaseDTOs.get(i).getParameterDTOs();
            TestCaseResultDTO testCaseResultDTO = TestCaseResultDTO.builder()
                    .outputDatatype(problem.getOutputDataType())
                    .outputData(output)
                    .expectedDatatype(problem.getOutputDataType())
                    .input(input)
                    .build();
            testCaseResultDTOs.add(testCaseResultDTO);
        }

        return ResultDTO.builder()
                .isAccepted(true)
                .testCaseResultDTOS(testCaseResultDTOs)
                .build();
    }

    private ResultDTO compareResultWithCorrectAnswer(List<String> correctAnswerWithCustomTestcase, ResultDTO resultDTO) {
        boolean isAccepted = true;
        List<String> results = resultDTO.getTestCaseResultDTOS().stream()
                .map(TestCaseResultDTO::getOutputData)
                .toList();

        int passedTestcase = 0;
        int maxTestcase = results.size();

        for (int i = 0; i < correctAnswerWithCustomTestcase.size(); i++) {
            TestCaseResultDTO resultDTO1 = resultDTO.getTestCaseResultDTOS().get(i);
            resultDTO1.setExpected(correctAnswerWithCustomTestcase.get(i));
            if (!correctAnswerWithCustomTestcase.get(i).equals(results.get(i))) {
                isAccepted = false;
                resultDTO1.setPassed(false);
                resultDTO.getTestCaseResultDTOS().get(i).setStatus(Submission.EStatus.WRONG_ANSWER);
            } else {
                resultDTO1.setPassed(true);
                resultDTO.getTestCaseResultDTOS().get(i).setStatus(Submission.EStatus.ACCEPTED);
                passedTestcase++;
            }

            resultDTO.getTestCaseResultDTOS().set(i,resultDTO1);
        }

        resultDTO.setMaxTestcase(String.valueOf(maxTestcase));
        resultDTO.setPassedTestcase(String.valueOf(passedTestcase));
        resultDTO.setAccepted(isAccepted);
        resultDTO.setStatus(isAccepted? Submission.EStatus.ACCEPTED : Submission.EStatus.WRONG_ANSWER);
        resultDTO.setMessage(isAccepted ? "All test cases passed!" : "Some test cases failed.");
        return resultDTO;
    }

    private ResultDTO createCompilationFailureResult(CompilerResult compilerResult) {
        String message;
        Submission.EStatus status;

        switch (compilerResult.getCompilerConstants()) {
            case ERROR:
                message = "Testcase not valid!";
                status = Submission.EStatus.COMPILE_ERROR;
                break;
            case SYNTAX_ERROR:
                message = compilerResult.getError();
                status = Submission.EStatus.COMPILE_ERROR;
                break;
            case CLASS_NOT_FOUND:
                message = "Class not found!";
                status = Submission.EStatus.COMPILE_ERROR;
                break;
            case TYPE_NOT_PRESENT:
                message = "Type not present!";
                status = Submission.EStatus.COMPILE_ERROR;
                break;
            default:
                message = "Unexpected compilation error: " + compilerResult.getError();
                log.warn(message);
                return ResultDTO.builder()
                        .status(Submission.EStatus.COMPILE_ERROR)
                        .message(message)
                        .build();
        }

        return ResultDTO.builder()
                .status(status)
                .message(message)
                .build();
    }

    private String createRunCodeWithCustomTestCase(String code,
                                                   List<TestCase> testCases,
                                                   String functionName,
                                                   String outputDataType,
                                                   List<CustomTestCaseDTO> customTestCaseDTOs) {
        int firstBraceIndex = code.indexOf("{") + 1;
        int lastBraceIndex = code.length() - 1;

        String header = code.substring(0, firstBraceIndex);
        String body = code.substring(firstBraceIndex, lastBraceIndex);
        String footer = code.substring(lastBraceIndex);
        String parameterDeclarations = generateCustomTestCaseParameterDeclarations(testCases, customTestCaseDTOs);
        String parameterReferences = testCases.get(0).getParameters().stream()
                .map(Parameter::getName)
                .collect(Collectors.joining(", "));
        String firstArrayName = parameterReferences.split(",\\s*")[0];

        String staticMethod = String.format(
                "public static %s[] %s() {\n" +
                        "    %s[] result = new %s[%s.length];\n" +
                        "    int index = 0;\n" +
                        "    for (int i = 0; i < %s.length; i++) {\n" +
                        "        result[index++] = %s(%s);\n" +
                        "    }\n" +
                        "    return result;\n" +
                        "}",
                outputDataType, functionName, outputDataType, outputDataType, firstArrayName, firstArrayName,
                functionName, parameterReferences.replaceAll(", ", "[i], ") + "[i]"
        );

        return String.join("\n", header, parameterDeclarations, body, staticMethod, footer);
    }

    public String createRunCodeWithAllTestCase(String code, List<TestCase> testCases, String functionName, String outputDataType) {
        int firstBraceIndex = code.indexOf("{") + 1;
        int lastBraceIndex = code.length() - 1;

        String header = code.substring(0, firstBraceIndex);
        String body = code.substring(firstBraceIndex, lastBraceIndex);
        String footer = code.substring(lastBraceIndex);
        String parameterDeclarations = generateTestCaseParameterDeclarations(testCases);
        String parameterReferences = testCases.get(0).getParameters().stream()
                .sorted(Comparator.comparingInt(Parameter::getIndex))
                .map(Parameter::getName)
                .collect(Collectors.joining(", "));
        String firstArrayName = parameterReferences.split(",\\s*")[0];

        String staticMethod = String.format(
                "public static %s[] %s() {\n" +
                        "    %s[] result = new %s[%s.length];\n" +
                        "    int index = 0;\n" +
                        "    for (int i = 0; i < %s.length; i++) {\n" +
                        "        result[index++] = %s(%s);\n" +
                        "    }\n" +
                        "    return result;\n" +
                        "}",
                outputDataType, functionName, outputDataType, outputDataType, firstArrayName, firstArrayName,
                functionName, parameterReferences.replaceAll(", ", "[i], ") + "[i]"
        );

        return String.join("\n", header, parameterDeclarations, body, staticMethod, footer);
    }

    private String generateCustomTestCaseParameterDeclarations(List<TestCase> testCases, List<CustomTestCaseDTO> customTestCaseDTOs) {
        Map<String, StringBuilder> parameterDeclarationsMap = new HashMap<>();
        List<Parameter> parameters1 = testCases.get(0).getParameters();

        for (Parameter parameter : parameters1) {
            StringBuilder parameterListDeclaration = new StringBuilder(String.format("public static %s[] %s = {",
                    parameter.getInputDataType(),
                    parameter.getName()));
            parameterDeclarationsMap.put(parameter.getName(), parameterListDeclaration);
        }

        for (CustomTestCaseDTO customTestCaseDTO : customTestCaseDTOs) {
            //            List<Parameter> ps = testCases.get(i).getParameters();

            List<ParameterDTO> parameters = customTestCaseDTO.getParameterDTOs();
            for (ParameterDTO parameter : parameters) {
                StringBuilder parameterListDeclaration = parameterDeclarationsMap.get(parameter.getName());
                parameterListDeclaration.append(String.format("\n\t%s", parameter.getInputData()));
                parameterListDeclaration.append(",");
            }
        }

        StringBuilder finalParameterDeclarations = new StringBuilder();
        for (StringBuilder parameterListDeclaration : parameterDeclarationsMap.values()) {
            parameterListDeclaration.deleteCharAt(parameterListDeclaration.length() - 1); // Remove the last comma
            parameterListDeclaration.append("};\n");
            finalParameterDeclarations.append(parameterListDeclaration);
        }
        return finalParameterDeclarations.toString();
    }

    private String generateTestCaseParameterDeclarations(List<TestCase> testCases) {
        Map<String, StringBuilder> parameterDeclarationsMap = new HashMap<>();
        List<Parameter> parameters1 = testCases.get(0).getParameters();

        for (Parameter parameter : parameters1) {
            StringBuilder parameterListDeclaration = new StringBuilder(String.format("public static %s[] %s = {",
                    parameter.getInputDataType(),
                    parameter.getName()));
            parameterDeclarationsMap.put(parameter.getName(), parameterListDeclaration);
        }

        for (TestCase testCase : testCases) {
            List<Parameter> parameters = testCase.getParameters();
            for (Parameter parameter : parameters) {
                StringBuilder parameterListDeclaration = parameterDeclarationsMap.get(parameter.getName());
                parameterListDeclaration.append(String.format("\n\t%s", parameter.getInputData()));
                parameterListDeclaration.append(",");
            }
        }

        StringBuilder finalParameterDeclarations = new StringBuilder();
        for (StringBuilder parameterListDeclaration : parameterDeclarationsMap.values()) {
            parameterListDeclaration.deleteCharAt(parameterListDeclaration.length() - 1); // Remove the last comma
            parameterListDeclaration.append("};\n");
            finalParameterDeclarations.append(parameterListDeclaration);
        }
        return finalParameterDeclarations.toString();
    }

    private void validateTestCase(TestCase testCase) {
        if (testCase == null) {
            throw new IllegalArgumentException("Test case cannot be null");
        }
    }

    @Override
    public String createInputCode(Problem problem, String code, TestCase testCase) {
        validateTestCase(testCase);
        StringBuilder listParameter = parameterService.createListParameter(testCase.getParameters());
        String importStatements = createImportStatements(problem);

        return String.format(
                "%s\n" +
                        "public class Solution {\n" +
                        "    public static %s %s (%s) {\n" +
                        "        %s\n" +
                        "    }\n" +
                        "}",
                importStatements, problem.getOutputDataType(), problem.getFunctionName(), listParameter, code);
    }

    private String createImportStatements(Problem problem) {
        return problem.getLibrariesSupports().stream()
                .map(LibrariesSupport::getName)
                .map(name -> "import " + name + ";\n")
                .collect(Collectors.joining());
    }

    private Class<?> loadClass(String fileLink) throws IOException, ClassNotFoundException {
        URLClassLoader classLoader = URLClassLoader.newInstance(new URL[]{new File(fileLink).toURI().toURL()});
        return Class.forName("Solution", true, classLoader);
    }

    public List<String> runWithCustomTestCases(String fileLink, String functionName) {
        try {
            Class<?> cls = loadClass(fileLink);
            Method method = cls.getDeclaredMethod(functionName);
            Object result = Modifier.isStatic(method.getModifiers())
                    ? method.invoke(null)
                    : method.invoke(cls.getDeclaredConstructor().newInstance());

            String[] returnValueArray = new String[Array.getLength(result)];
            for (int i = 0; i < Array.getLength(result); i++) {
                returnValueArray[i] = String.valueOf(Array.get(result, i));
            }

            log.info("Return value: {}", Arrays.toString(returnValueArray));

            return new ArrayList<>(Arrays.asList(returnValueArray));
        } catch (ReflectiveOperationException | IOException e) {
            log.warn("Error executing method: {}", e.getMessage());
            return null;
        }
    }

    public List<TestCaseResultDTO> runWithTestCases(String outputDataType, List<TestCase> testCases, String fileLink, String functionName) {
        try {
            List<TestCaseResultDTO> resultDTOs = new ArrayList<>();
            Class<?> cls = loadClass(fileLink);
            Method method = cls.getDeclaredMethod(functionName);
            Object result = Modifier.isStatic(method.getModifiers())
                    ? method.invoke(null)
                    : method.invoke(cls.getDeclaredConstructor().newInstance());

            Class<?> returnDataType = method.getReturnType().getComponentType();
            String[] returnValueArray = new String[Array.getLength(result)];
            for (int i = 0; i < Array.getLength(result); i++) {
                returnValueArray[i] = String.valueOf(Array.get(result, i));
            }

            log.info("Return value: {}", Arrays.toString(returnValueArray));
            for (int i = 0; i < returnValueArray.length; i++) {
                TestCase testCase = testCases.get(i);
                boolean isPassed = hasMatchingDataTypesAndOutput(returnDataType.getName(), outputDataType, returnValueArray[i], testCase.getOutputData());
                TestCaseResultDTO.TestCaseResultDTOBuilder testCaseResultDTO = TestCaseResultDTO.builder()
                        .input(generateParametersDTO(testCase.getParameters()))
                        .expectedDatatype(outputDataType)
                        .expected(testCase.getOutputData())
                        .outputData(returnValueArray[i])
                        .outputDatatype(returnDataType.getName())
                        .isPassed(isPassed)
                        .isPublic(testCase.isPublic())
                        .status(isPassed ? Submission.EStatus.ACCEPTED : Submission.EStatus.WRONG_ANSWER);
                resultDTOs.add(testCaseResultDTO.build());
            }

            return resultDTOs;
        } catch (ReflectiveOperationException | IOException e) {
            log.warn("Error executing method: {}", e.getMessage());
            return null;
        }
    }

    private List<ParameterDTO> generateParametersDTO(List<Parameter> parameters) {
        return parameters.stream()
                .map(p -> new ParameterDTO(p.getName(), p.getInputDataType(), p.getInputData()))
                .collect(Collectors.toList());
    }

    @Override
    public void writeFile(String fileLink, String fileName, String code) {
        if (containsMaliciousCode(code)) {
            log.warn("Phát hiện mã độc hoặc lệnh can thiệp file trong code đầu vào.");
            throw new SecurityException("Mã nguồn chứa lệnh nguy hiểm không được phép.");
        }

        File file = new File(fileLink + fileName);
        try {
            File parentDir = file.getParentFile();
            if (parentDir != null && !parentDir.exists()) {
                parentDir.mkdirs();
            }

            if (!file.exists()) {
                file.createNewFile();
            }
            FileWriter fileWriter = new FileWriter(file);
            fileWriter.write("");
            fileWriter.write(code);
            fileWriter.close();
            log.info("File " + fileLink + " created successfully.");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private boolean containsMaliciousCode(String code) {
        // Kiểm tra các mẫu nguy hiểm
        String[] dangerousPatterns = {
            // Các mẫu nguy hiểm có thể gây hại cho hệ thống
            
            // Dừng chương trình đột ngột
            "System\\.exit",
            
            // Thực thi lệnh hệ thống
            "Runtime\\.getRuntime\\(\\)\\.exec",
            
            // Sử dụng ProcessBuilder để thực thi lệnh hệ thống
            "ProcessBuilder",
            
            // Các thao tác với tệp tin
            "new File\\(",           // Tạo đối tượng File mới
            "\\.delete\\(\\)",       // Xóa tệp tin
            "\\.deleteOnExit\\(\\)", // Đánh dấu tệp tin để xóa khi chương trình kết thúc
            "\\.format\\(",          // Định dạng dữ liệu, có thể gây lỗi bảo mật nếu sử dụng không đúng cách
            "\\.renameTo\\(",        // Đổi tên tệp tin
            
            // Các lệnh xóa trên Linux và Windows
            "rm -rf",                // Xóa đệ quy và bắt buộc trên Linux
            "del /f /q",             // Xóa bắt buộc và im lặng trên Windows
            "rmdir /s /q",           // Xóa thư mục và nội dung của nó trên Windows
            
            // Thay đổi quyền truy cập tệp tin
            "chmod",                 // Thay đổi quyền truy cập tệp tin trên Linux
            "chown",                 // Thay đổi chủ sở hữu tệp tin trên Linux
            "setReadable",           // Đặt quyền đọc cho tệp tin
            "setWritable",           // Đặt quyền ghi cho tệp tin
            "setExecutable",         // Đặt quyền thực thi cho tệp tin
            
            // Các lớp và phương thức liên quan đến bảo mật
            "SecurityManager",       // Quản lý bảo mật của ứng dụng
            "AccessController",      // Kiểm soát truy cập vào tài nguyên hệ thống
            "doPrivileged",          // Thực hiện hành động với quyền đặc biệt
            "grant",                 // Cấp quyền truy cập
            "permission",            // Định nghĩa quyền truy cập
            "Policy",                // Chính sách bảo mật
            "setSecurityManager",    // Đặt SecurityManager mới
            
            // Các mẫu liên quan đến xóa ổ đĩa
            "format c:",             // Định dạng ổ đĩa C
            "format d:",             // Định dạng ổ đĩa D
            "format e:",             // Định dạng ổ đĩa E
            "format f:",             // Định dạng ổ đĩa F
            "diskpart",              // Công cụ quản lý đĩa trên Windows
            "clean",                 // Xóa toàn bộ dữ liệu trên đĩa
            "create partition primary", // Tạo phân vùng chính mới
            
            // Kiểm tra các đường dẫn Windows
            "C:\\\\",                // Đường dẫn gốc ổ đĩa C
            "D:\\\\",                // Đường dẫn gốc ổ đĩa D
            "E:\\\\",                // Đường dẫn gốc ổ đĩa E
            "F:\\\\",                // Đường dẫn gốc ổ đĩa F
            "Program Files",         // Thư mục chứa các chương trình cài đặt
            "Windows",               // Thư mục hệ thống Windows
            
            // Thêm các mẫu mới
            "System\\.gc\\(\\)",     // Gọi bộ thu gom rác, có thể ảnh hưởng đến hiệu suất
            "Thread\\.sleep",        // Tạm dừng luồng hiện tại, có thể gây chậm trễ
            "Runtime\\.halt",        // Dừng JVM ngay lập tức, nguy hiểm
            "Unsafe",                // Cho phép truy cập bộ nhớ trực tiếp, rất nguy hiểm
            "ClassLoader",           // Có thể được sử dụng để tải mã độc
            "URLClassLoader",        // Có thể tải các lớp từ nguồn không đáng tin cậy
            "System\\.load",         // Tải thư viện native, tiềm ẩn rủi ro bảo mật
            "System\\.loadLibrary",  // Tương tự System.load
            "native",                // Đánh dấu phương thức native, có thể gây rủi ro
            "JNI",                   // Java Native Interface, có thể gây rủi ro bảo mật
            "sun\\.misc\\.Unsafe",   // Cho phép thao tác bộ nhớ cấp thấp, rất nguy hiểm
            "java\\.lang\\.reflect", // Reflection có thể được sử dụng để phá vỡ encapsulation
            "java\\.nio\\.channels\\.FileChannel",  // Cho phép truy cập file trực tiếp, tiềm ẩn rủi ro
            "java\\.io\\.RandomAccessFile"          // Cho phép đọc/ghi file ngẫu nhiên, có thể gây rủi ro
        };

        for (String pattern : dangerousPatterns) {
            if (code.toLowerCase().matches("(?i).*\\b" + pattern + "\\b.*")) {
                return true;
            }
        }

        // Kiểm tra các lệnh xóa file trên Linux
        if (code.matches("(?i).*\\b(rm|shred)\\s+(-rf?\\s+)?[/\\w]+.*")) {
            return true;
        }

        // Kiểm tra việc truy cập các thư mục hệ thống
        if (code.matches("(?i).*(/etc/|/var/|/usr/|/root/|/bin/|/sbin/).*")) {
            return true;
        }

        // Kiểm tra các lệnh nguy hiểm khác
        return code.matches("(?i).*(sudo|su)\\s+.*");
    }

    @Override
    public CompilerResult compile(String fileLink, String fileName) {
        javax.tools.JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();

        try (StandardJavaFileManager fileManager = compiler.getStandardFileManager(null, null, null)) {
            JavaFileObject compilationUnit = getFileObject(fileLink + fileName, fileManager);
            List<JavaFileObject> compilationUnits = Collections.singletonList(compilationUnit);
            DiagnosticCollector<JavaFileObject> diagnostics = new DiagnosticCollector<>();
            javax.tools.JavaCompiler.CompilationTask task = compiler.getTask(null, fileManager, diagnostics, null, null, compilationUnits);

            boolean compilationSuccess = task.call();
            if (!compilationSuccess) {
                StringBuilder errorStringBuilder = getErrorStringBuilder(diagnostics);
                return new CompilerResult(ECompilerConstants.COMPILATION_ERROR, errorStringBuilder.toString());
            }
            return new CompilerResult(ECompilerConstants.SUCCESS);
        } catch (Exception e) {
            log.error("Compilation failed: {}", e.getMessage());
            return new CompilerResult(ECompilerConstants.COMPILATION_FAILED, e.getMessage());
        }
    }

    private static StringBuilder getErrorStringBuilder(DiagnosticCollector<JavaFileObject> diagnostics) {
        List<Diagnostic<? extends JavaFileObject>> errors = diagnostics.getDiagnostics();
        StringBuilder errorStringBuilder = new StringBuilder();
        errors.forEach(error -> {
            errorStringBuilder
                    .append("Line ")
                    .append(error.getLineNumber())
                    .append(" : ")
                    .append(error.getMessage(null))
                    .append("\n");
            log.error("Compilation error: Line {} {}", error.getLineNumber() ,error.getMessage(null));
        });
        return errorStringBuilder;
    }

    //http://www.java2s.com/example/java-api/javax/tools/javafilemanager/getjavafileforinput-3-0.html
    private JavaFileObject getFileObject(String fileName, StandardJavaFileManager fileManager) throws IOException {
        JavaFileObject fileObject = fileManager.getJavaFileForInput(StandardLocation.PLATFORM_CLASS_PATH, fileName, JavaFileObject.Kind.CLASS);
        if (fileObject != null) return fileObject;

        fileObject = fileManager.getJavaFileForInput(StandardLocation.CLASS_PATH, fileName, JavaFileObject.Kind.CLASS);
        if (fileObject != null) return fileObject;

        return fileManager.getJavaFileObjects(fileName).iterator().next();
    }

    @Override
    public void deleteFileCompiled(String fileLink, String fileName) {
        File file = new File(fileLink + fileName);
        if (file.exists() && file.delete()) {
            log.info("File {} deleted successfully.", fileName);
        }
    }
}
