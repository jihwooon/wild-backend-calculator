package com.example.demo.application;

import com.example.demo.exception.CalculatorArithmeticException;
import com.example.demo.infrastructure.Calculation;
import com.example.demo.infrastructure.CalculationRepository;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Calculator {

    private CalculationRepository calculationRepository = CalculationRepository.getInstance();

    private final Map<String, Operator> operators = new HashMap<>();

    public Calculator() {
        operators.put("+", new OperatorPlus());
        operators.put("-", new OperatorMinus());
        operators.put("*", new OperatorMultiply());
        operators.put("/", new OperatorDivide());
    }

    public int calculate(int number1, int number2,
            String operatorSymbol) {
        validateOperator(operatorSymbol);

        int result = performCalculation(number1, number2, operatorSymbol);

        Calculation calculation = saveCalculation(number1, number2,
                operatorSymbol, result);

        return calculation.getResult();
    }

    private Calculation saveCalculation(int number1, int number2,
            String operatorSymbol, int result) {
        Calculation calculation = new Calculation(number1, number2,
                operatorSymbol,
                result);

        calculationRepository.add(calculation);
        return calculation;
    }

    private int performCalculation(int number1, int number2,
            String operatorSymbol) {
        Operator operator = operators.get(operatorSymbol);

        if (operator instanceof OperatorDivide && number2 == 0) {
            throw new CalculatorArithmeticException("나누기는 0으로 나눌 수 없습니다.");
        }
        int result = operator.calculate(number1, number2);
        return result;
    }

    private void validateOperator(String operatorSymbol) {
        if (operatorSymbol == null || !operators.containsKey(operatorSymbol)) {
            throw new IllegalArgumentException(
                    "Invalid operator: " + operatorSymbol);
        }
    }

    public List<Calculation> getCalculationList() {
        return calculationRepository.findAll();
    }
}
