package basepackage;

class RecursiveSequence {
    private int[]   allowedOperations,
                    allowedNumbers;

    private int     requiredNumber,
                    maxPresses;

    private int     foundDepth;

    RecursiveSequence(int[] nums, int[] ops, int max, int required) {

        // Sort numbers
        allowedNumbers = sort(nums);
        allowedOperations = ops;
        requiredNumber = required;

        maxPresses = max;
        foundDepth = max + 1;

        // Check if sequence can be built
        if (canBeBuilt(requiredNumber)) {
            foundDepth = lengthOfNumber(requiredNumber);
            return;
        }

        // Check if only 0 is available
        if (allowedNumbers.length == 1 && allowedNumbers[0] == 0) {
            return;
        }

        // Find Sequence
        for (int i = 3; i < maxPresses; i++) {
            int operand = nextOperand(0);
            while (operand < 1000) {
                int newDepth = lengthOfNumber(operand);
                sequence(operand, newDepth, i);
                operand = nextOperand(operand);
            }
        }
    }

    int getFoundDepth() {
        return foundDepth;
    }

    private int[] sort(int[] arr) {
        for (int i = 1; i < arr.length; i++) {
            for (int j = 1; j < arr.length - i + 1; j++) {
                if (arr[j] < arr[j - 1]) {
                    int helper = arr[j];
                    arr[j - 1] = arr[j];
                    arr[j] = helper;
                }
            }
        }

        return arr;
    }

    private boolean canBeBuilt(int number) {
        if (number == 0 && allowedNumbers[0] == 0) {
            return true;
        }

        while (number != 0) {
            int digit = number % 10;
            boolean contains = false;
            for (int el : allowedNumbers) {
                if (el == digit) {
                    contains = true;
                }
            }

            if (!contains) {
                return false;
            }

            number = number / 10;
        }
        return true;
    }

    private boolean isAllowed(int number) {
        return number >= 1 && number <= 999;
    }

    private int nextOperand(int operand) {
        int ones = operand % 10;
        int tens = (operand / 10) % 10;
        int huns = (operand / 100) % 10;

        ones = nextDigit(ones);
        if (ones == -1) {
            ones = allowedNumbers[0];
            tens = nextDigit(tens);
            if (tens == -1) {
                tens = allowedNumbers[0];
                huns = nextDigit(huns);
                if (huns == -1) {
                    return 1000;
                }
            }
        }

        return ones + tens * 10 + huns * 100;
    }

    private int nextDigit(int digit) {
        for (int num : allowedNumbers) {
            if (num > digit) {
                return num;
            }
        }

        return -1;
    }

    private int lengthOfNumber(int number) {
        if (number > 99) {
            return 3;
        }
        else {
            if (number > 9) {
                return 2;
            }

            return 1;
        }
    }

    private void sequence(int number, int depth, int maxDepth) {
        if (foundDepth < maxPresses) {
            return;
        }

        if (number == requiredNumber) {

            // +1 because of "="
            if (foundDepth > depth + 1) {
                foundDepth = depth + 1;
            }
            return;
        }

        boolean add = true,
                sub = true,
                mul = true,
                div = true;

        int operand2 = nextOperand(0);
        while (operand2 < 1000) {
            int newDepth = depth + 1 + lengthOfNumber(operand2);

            // Current depth +1 operation + length of operand +1 "=" >= found depth
            if (newDepth + 1 >= maxDepth) {
                return;
            }

            // All operations
            for (int allowedOperation : allowedOperations) {
                switch (allowedOperation) {
                    case 1:
                        // +
                        if (add) {
                            int result = number + operand2;
                            if (isAllowed(result)) {
                                sequence(result, newDepth, maxDepth);
                            }
                            else {
                                add = false;
                            }
                        }
                        break;
                    case 2:
                        // -
                        if (sub) {
                            int result = number - operand2;
                            if (isAllowed(result)) {
                                sequence(result, newDepth, maxDepth);
                            }
                            else {
                                sub = false;
                            }
                        }
                        break;
                    case 3:
                        // *
                        if (mul) {
                            int result = number * operand2;
                            if (isAllowed(result)) {
                                sequence(result, newDepth, maxDepth);
                            }
                            else {
                                mul = false;
                            }
                        }
                        break;
                    case 4:
                        // /
                        if (div) {
                            int result = number / operand2;
                            if (isAllowed(result)) {
                                sequence(result, newDepth, maxDepth);
                            }
                            else {
                                div = false;
                            }
                        }
                        break;
                }
            }

            operand2 = nextOperand(operand2);
        }

    }
}

public class Main {

    public static void main(String[] args) {


        /* Task:
         * the calculator is broken!
         * One has to get the required number
         * using only working keys
         * not exceeding the maximum pressing number
         */


        // Allowed keys of 0123456789+-*/:
        int[] numbers = new int[] {1,2};
        int[] operations = new int[]{1,2,3,4};

        // Maximum press number:
        int presses = 20;

        // Required number
        int required = 344;

        // ------------------------------------------

        //String result = combination(presses, required, keys);
        //System.out.println(result);

        RecursiveSequence rs = new RecursiveSequence(numbers, operations, presses, required);
        System.out.println(rs.getFoundDepth());

        // out: -2*1717
    }
}
