package com.example.cal;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    // UI elements
    private TextView inputText, outputText;

    // State variables to hold the input and output data
    private String input = "", output = "", newOutput = "";

    // Buttons for the calculator
    private Button button0, button1, button2, button3, button4, button5, button6, button7,
            button8, button9, buttonAdd, buttonMultiply, buttonSubtract, buttonDivide,
            buttonPoint, buttonPercent, buttonPower, buttonEqual, buttonClear;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize UI elements
        inputText = findViewById(R.id.input_text);
        outputText = findViewById(R.id.output_text);

        // Initialize buttons
        button0 = findViewById(R.id.btn0);
        button1 = findViewById(R.id.btn1);
        button2 = findViewById(R.id.btn2);
        button3 = findViewById(R.id.btn3);
        button4 = findViewById(R.id.btn4);
        button5 = findViewById(R.id.btn5);
        button6 = findViewById(R.id.btn6);
        button7 = findViewById(R.id.btn7);
        button8 = findViewById(R.id.btn8);
        button9 = findViewById(R.id.btn9);
        buttonAdd = findViewById(R.id.addition_btn);
        buttonMultiply = findViewById(R.id.multiply_btn);
        buttonDivide = findViewById(R.id.division_btn);
        buttonSubtract = findViewById(R.id.substraction_btn);
        buttonPoint = findViewById(R.id.btnpoint);
        buttonPower = findViewById(R.id.power_btn);
        buttonEqual = findViewById(R.id.equal_btn);
        buttonPercent = findViewById(R.id.percent_btn);
        buttonClear = findViewById(R.id.clear_btn);
    }

    // Handle button clicks
    public void onButtonClicked(View view) {
        Button button = (Button) view;
        String buttonText = button.getText().toString();

        switch (buttonText) {
            case "C":
                clearInputs();
                break;

            case "^":
            case "*":
            case "+":
            case "-":
            case "/":
                appendOperator(buttonText);
                break;

            case "=":
                evaluateExpression();
                break;

            case "%":
                calculatePercentage();
                break;

            default:
                appendToInput(buttonText);
                break;
        }
        inputText.setText(input);
    }

    // Clear input and output
    private void clearInputs() {
        input = "";
        output = "";
        newOutput = "";
        outputText.setText("");
    }

    // Append operator to input and evaluate the expression if necessary
    private void appendOperator(String operator) {
        evaluateExpression();
        input += operator;
    }

    // Evaluate the current expression
    private void evaluateExpression() {
        try {
            if (input.contains("+")) {
                processOperation("\\+", (a, b) -> a + b);
            } else if (input.contains("*")) {
                processOperation("\\*", (a, b) -> a * b);
            } else if (input.contains("/")) {
                processOperation("\\/", (a, b) -> a / b);
            } else if (input.contains("^")) {
                processOperation("\\^", Math::pow);
            } else if (input.contains("-")) {
                processSubtraction();
            }
        } catch (Exception e) {
            outputText.setText("Error");
        }
    }

    // Perform the specified operation on two numbers
    private void processOperation(String operator, Operation operation) {
        String[] numbers = input.split(operator);
        if (numbers.length == 2) {
            double result = operation.apply(Double.parseDouble(numbers[0]), Double.parseDouble(numbers[1]));
            output = Double.toString(result);
            newOutput = removeTrailingZero(output);
            outputText.setText(newOutput);
            input = newOutput;
        }
    }

    // Handle subtraction operation, including cases where the result is negative
    private void processSubtraction() {
        String[] numbers = input.split("-");
        if (numbers.length == 2) {
            double result = Double.parseDouble(numbers[0]) - Double.parseDouble(numbers[1]);
            output = Double.toString(result);
            newOutput = removeTrailingZero(output);
            outputText.setText(result < 0 ? "-" + newOutput : newOutput);
            input = result < 0 ? "-" + newOutput : newOutput;
        }
    }

    // Calculate percentage based on the current input
    private void calculatePercentage() {
        try {
            double value = Double.parseDouble(inputText.getText().toString()) / 100;
            outputText.setText(removeTrailingZero(Double.toString(value)));
        } catch (NumberFormatException e) {
            outputText.setText("Error");
        }
    }

    // Append the button's text to the input
    private void appendToInput(String text) {
        input += text;
    }

    // Remove unnecessary decimal places
    private String removeTrailingZero(String number) {
        if (number.contains(".") && number.endsWith("0")) {
            number = number.substring(0, number.indexOf('.'));
        }
        return number;
    }

    // Functional interface for basic arithmetic operations
    @FunctionalInterface
    interface Operation {
        double apply(double a, double b);
    }
}
