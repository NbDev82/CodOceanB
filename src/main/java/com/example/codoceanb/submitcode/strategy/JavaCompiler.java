package com.example.codoceanb.submitcode.strategy;


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
import java.lang.management.ManagementFactory;
import java.lang.management.MemoryMXBean;
import java.lang.reflect.Array;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

@Service
public class JavaCompiler implements CompilerStrategy{
    public static final int TIME_LIMITED = 2;
    private static final Logger log = LogManager.getLogger(JavaCompiler.class);
    private final ExecutorService executorService = Executors.newCachedThreadPool();

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
            CompilerResult compile = compile(code, fileLink, fileName);
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
        return builder
                .message("That is your result of your code for this problem")
                .maxTestcase(String.valueOf(testCases.size()))
                .passedTestcase(String.valueOf(passedTestCases))
                .testCaseResultDTOS(testCaseResultDTOs)
                .isAccepted(isAccepted)
                .status(isAccepted ?
                        Submission.EStatus.ACCEPTED :
                        Submission.EStatus.WRONG_ANSWER)
                .build();
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
                message = "Unexpected compilation error: {}" + compilerResult.getError();
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


    public String createRunCodeWithAllTestCase(String code, List<TestCase> testCases, String functionName, String outputDataType) {
        int firstBraceIndex = code.indexOf("{") + 1;
        int lastBraceIndex = code.length() - 1;

        String header = code.substring(0, firstBraceIndex);
        String body = code.substring(firstBraceIndex, lastBraceIndex);
        String footer = code.substring(lastBraceIndex);
        String parameterDeclarations = generateTestCaseParameterDeclarations(testCases);
        String inputDataType = testCases.get(0).getParameters().get(0).getInputDataType();

        String parameterReferences = testCases.get(0).getParameters().stream()
                .map(Parameter::getName)
                .collect(Collectors.joining(", "));

        String staticMethod = String.format(
                """
                       public static %s[] %s() {
                           %s[] result = new %s[%s.length];
                           int index = 0;
                           for(%s inputData : %s) {
                                result[index++] = %s(inputData);
                           }
                        \t return result;
                        }
                        """,
                outputDataType,
                functionName,
                outputDataType,
                outputDataType,
                parameterReferences,
                inputDataType,
                parameterReferences,
                functionName
        );
        return header + "\n" +  parameterDeclarations  + body + "\n" + staticMethod + footer;
    }

    private String generateTestCaseParameterDeclarations(List<TestCase> testCases) {
        Parameter p = testCases.get(0).getParameters().get(0);

        String parameterListDeclarations = String.format("public static %s[] %s = {$", p.getInputDataType(), p.getName());

        for (TestCase testCase : testCases) {
            List<Parameter> parameters = testCase.getParameters();
            String parameterDeclarations = parameters.stream()
                    .map(p1 -> String.format("%s", p.getInputData()))
                    .collect(Collectors.joining("\n\t"));
            parameterListDeclarations = parameterListDeclarations.replace("$", parameterDeclarations + ",$");
        }
        parameterListDeclarations = parameterListDeclarations.replace(",$", "};");
        return parameterListDeclarations;
    }

    @Override
    public String createRunCode(String code, List<Parameter> parameters, String functionName, String outputDataType) {
        int firstBraceIndex = code.indexOf("{") + 1;
        int lastBraceIndex = code.length() - 1;

        String header = code.substring(0, firstBraceIndex);
        String body = code.substring(firstBraceIndex, lastBraceIndex);
        String footer = code.substring(lastBraceIndex);

        String parameterDeclarations = generateParameterDeclarations(parameters);

        String parameterReferences = parameters.stream()
                .map(Parameter::getName)
                .collect(Collectors.joining(", "));

        String staticMethod = String.format(
                """
                       public static %s %s() {
                        \t return %s(%s);
                        }
                        """,
                outputDataType,
                functionName,
                functionName,
                parameterReferences
        );
        return header + "\n" +  parameterDeclarations  + body + "\n" + staticMethod + footer;
    }

    public String generateParameterDeclarations(List<Parameter> parameters) {
        return parameters.stream()
                .map(p -> String.format("public static %s %s = %s;\n", p.getInputDataType(), p.getName(), p.getInputData()))
                .collect(Collectors.joining("\n\t"));
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
                """
                        %s
                        public class Solution {
                        \tpublic static %s %s (%s) {
                        \t\t%s
                        \t}
                        }
                        """,
                importStatements,
                problem.getOutputDataType(),
                problem.getFunctionName(),
                listParameter,
                code);
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

    public List<TestCaseResultDTO> runWithTestCases(String outputDataType, List<TestCase> testCases, String fileLink, String functionName) {
        try {
            List<TestCaseResultDTO> resultDTOs =  new ArrayList<>();

            Class<?> cls = loadClass(fileLink);
            Method method = cls.getDeclaredMethod(functionName);

            var result = Modifier.isStatic(method.getModifiers())
                    ? method.invoke(null)
                    : method.invoke(cls.getDeclaredConstructor().newInstance());

            Class<?> returnDataType = method.getReturnType().getComponentType();
            String[] returnValueArray = new String[Array.getLength(result)];
            for (int i = 0; i < Array.getLength(result); i++) {
                returnValueArray[i] = String.valueOf(Array.get(result, i));
            }

            int i =0;

            log.info("Return value: {}", Arrays.toString(returnValueArray));
            for(String value : returnValueArray) {
                TestCase testCase = testCases.get(i++);
                TestCaseResultDTO.TestCaseResultDTOBuilder testCaseResultDTO = TestCaseResultDTO.builder();
                testCaseResultDTO
                        .input(generateParameterInput(testCase.getParameters()))
                        .expectedDatatype(outputDataType)
                        .expected(testCase.getOutputData())
                        .outputData(value)
                        .outputDatatype(returnDataType.getName());
                boolean isPassed = hasMatchingDataTypesAndOutput(returnDataType.getName(), outputDataType, value, testCase.getOutputData());
                testCaseResultDTO.isPassed(isPassed);

                testCaseResultDTO.status(
                        isPassed ?
                                Submission.EStatus.ACCEPTED :
                                Submission.EStatus.WRONG_ANSWER);
                resultDTOs.add(testCaseResultDTO.build());
            }

            return resultDTOs;
        } catch (ReflectiveOperationException | IOException e) {
            log.warn("Error executing method: {}", e.getMessage());
            return null;
        }
    }

    private String generateParameterInput(List<Parameter> parameters) {
        return parameters.stream()
                .map(p -> String.format("%s = %s\n", p.getName(), p.getInputData()))
                .collect(Collectors.joining("\n\t"));
    }

    @Override
    public void writeFile(String fileLink, String fileName, String code) {
        if (containsMaliciousCode(code)) {
            log.warn("Phát hiện mã độc hoặc lệnh can thiệp file trong code đầu vào.");
            throw new SecurityException("Mã nguồn chứa lệnh nguy hiểm không được phép.");
        }

        File file = new File(fileLink + fileName);
        try {
            // Tạo đường dẫn nếu đường dẫn không tồn tại
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
        if (code.matches("(?i).*(sudo|su)\\s+.*")) {
            return true;
        }

        return false;
    }

    @Override
    public CompilerResult compile(String code, String fileLink, String fileName) {
        javax.tools.JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();

        try (StandardJavaFileManager fileManager = compiler.getStandardFileManager(null, null, null)) {
            JavaFileObject compilationUnit = getFileObject(fileLink + fileName, fileManager);
            List<JavaFileObject> compilationUnits = Collections.singletonList(compilationUnit);

            DiagnosticCollector<JavaFileObject> diagnostics = new DiagnosticCollector<>();
            javax.tools.JavaCompiler.CompilationTask task = compiler.getTask(null, fileManager, diagnostics, null, null, compilationUnits);

            boolean compilationSuccess = task.call();

            if (!compilationSuccess) {
                final StringBuilder errorStringBuilder = getErrorStringBuilder(diagnostics);
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
        if (fileObject != null)
            return fileObject;

        fileObject = fileManager.getJavaFileForInput(StandardLocation.CLASS_PATH, fileName,
                JavaFileObject.Kind.CLASS);
        if (fileObject != null)
            return fileObject;

        return fileManager.getJavaFileObjects(fileName).iterator().next();
    }

    @Override
    public void deleteFileCompiled(String fileLink, String fileName){
        File file = new File(fileLink + fileName);
        if (file.exists()) {
            if(file.delete()) {
                log.info("File Solution.java deleted successfully.");
            }
        }
    }
}
