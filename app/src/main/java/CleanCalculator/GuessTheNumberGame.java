package CleanCalculator;

import java.io.File;
import java.util.Arrays;
import java.util.Scanner;

interface FileReader {
    String read(String filename);
}

class JavaUtilFileReader implements FileReader {
    public String read(String filename) {
        var file = new File(filename);
        Scanner scanner;
        try {
            scanner = new Scanner(file);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        var result = new StringBuilder();
        while (scanner.hasNextLine()) {
            result.append(scanner.nextLine());
            result.append("\n");
        }
        scanner.close();
        return result.toString();
    }
}

interface FileParser<T> {
    T parse(String fileContent, String separator);
}

class IntegerFileParser implements FileParser<int[]> {
    public int[] parse(String fileContent, String separator) {
        var rawNumbers = fileContent.split(separator);
        return Arrays.stream(rawNumbers).mapToInt(Integer::parseInt).toArray();
    }
}

interface Calculator {
    int add(int a, int b);

    int subtract(int a, int b);

    int multiply(int a, int b);
}

class BasicCalculator implements Calculator {
    public int add(int a, int b) {
        return a + b;
    }

    public int subtract(int a, int b) {
        return a - b;
    }

    public int multiply(int a, int b) {
        return a * b;
    }
}

interface Printer {
    void println(String message);
    void println(int message);
}

class JavaUtilPrinter implements Printer {
    public void println(String message) {
        System.out.println(message);
    }

    public void println(int message) {
        System.out.println(message);
    }
}

interface CalculatorLogger {
    void start();
    void applyingOperation(String operation);
    void parsedValue(int value);
    void currentOperationOnValueOnLine(String operation, int value, int line);
    void separator();
    void total(int total);
    void end();
}

interface CleanCalculator {
    void makeArithmeticOperationOnFile(String filename, String operation);
}

class MyCleanCalculator implements CleanCalculator {
    private final FileReader fileReader;
    private final FileParser<int[]> fileParser;
    private final Calculator calculator;
    private final Printer printer;
    private final CalculatorLogger calculatorLogger;

    public MyCleanCalculator(
            FileReader fileReader,
            FileParser<int[]> fileParser,
            Calculator calculator,
            Printer printer,
            CalculatorLogger calculatorLogger
            ) {
        this.fileReader = fileReader;
        this.fileParser = fileParser;
        this.calculator = calculator;
        this.printer = printer;
        this.calculatorLogger = calculatorLogger;
    }

    public void makeArithmeticOperationOnFile(String filename, String operation) {
        calculatorLogger.start();

        var fileContent = fileReader.read(filename);
        var numbers = fileParser.parse(fileContent, "\n");

        var result = 0;
        var index = 0;
        for (var number : numbers) {
            switch (operation) {
                case "add":
                    result = calculator.add(result, number);
                    break;
                case "subtract":
                    result = calculator.subtract(result, number);
                    break;
                case "multiply":
                    result = calculator.multiply(result, number);
                    break;
                default:
                    throw new RuntimeException("Unknown operation");
            }
            if (index == 0) {
                printer.println(String.format(" %d", number));
            } else {
                printer.println(String.format("%s%d (= %d)", operation, number, result));
            }
            index++;
        }

        calculatorLogger.separator();
        calculatorLogger.total(result);

        calculatorLogger.end();
    }
}