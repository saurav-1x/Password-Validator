import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Stack;

// Custom Exception
class InvalidPasswordException extends Exception {
    public InvalidPasswordException(String message) {
        super(message);
    }

    @Override
    public String getMessage() {
        return "Error: " + super.getMessage();
    }
}

// Stack Handler
class StackHandler {
    private Stack<Character> stack = new Stack<>();

    public boolean checkBalance(String password) {
        for (char ch : password.toCharArray()) {
            if (ch == '(' || ch == '{' || ch == '[') {
                stack.push(ch);
            } else if (ch == ')' || ch == '}' || ch == ']') {
                if (stack.isEmpty()) return false;

                char top = stack.pop();
                if (!isMatchingPair(top, ch)) {
                    return false;
                }
            }
        }
        return stack.isEmpty();
    }

    private boolean isMatchingPair(char open, char close) {
        return (open == '(' && close == ')') ||
               (open == '{' && close == '}') ||
               (open == '[' && close == ']');
    }
}

// Password Validator
class PasswordValidator {
    private String password;

    public PasswordValidator(String password) {
        this.password = password;
    }

    public void validatePassword() throws InvalidPasswordException {

        if (password == null || password.isEmpty()) {
            throw new InvalidPasswordException("Password cannot be empty");
        }

        if (password.length() < 6) {
            throw new InvalidPasswordException("Password must be at least 6 characters long");
        }

        checkCharacters();
        checkBalancedSymbols();
    }

    private void checkCharacters() throws InvalidPasswordException {
        boolean hasUpper = false;
        boolean hasLower = false;
        boolean hasDigit = false;
        boolean hasSpecial = false;

        for (char ch : password.toCharArray()) {
            if (Character.isUpperCase(ch)) hasUpper = true;
            else if (Character.isLowerCase(ch)) hasLower = true;
            else if (Character.isDigit(ch)) hasDigit = true;
            else hasSpecial = true;
        }

        if (!hasUpper)
            throw new InvalidPasswordException("Must contain uppercase letter");

        if (!hasLower)
            throw new InvalidPasswordException("Must contain lowercase letter");

        if (!hasDigit)
            throw new InvalidPasswordException("Must contain a digit");

        if (!hasSpecial)
            throw new InvalidPasswordException("Must contain a special character");
    }

    private void checkBalancedSymbols() throws InvalidPasswordException {
        StackHandler stackHandler = new StackHandler();

        if (!stackHandler.checkBalance(password)) {
            throw new InvalidPasswordException("Unbalanced brackets");
        }
    }
}

// Main GUI Class
public class MainApp extends JFrame implements ActionListener {

    private JTextField passwordField;
    private JButton validateButton;
    private JLabel resultLabel;

    public MainApp() {
        setTitle("Password Validator");
        setSize(400, 200);
        setLayout(new GridLayout(4, 1, 10, 10));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JLabel label = new JLabel("Enter Password:");
        passwordField = new JTextField(20);
        validateButton = new JButton("Validate");
        resultLabel = new JLabel("");

        validateButton.addActionListener(this);

        add(label);
        add(passwordField);
        add(validateButton);
        add(resultLabel);

        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String password = passwordField.getText();

        PasswordValidator validator = new PasswordValidator(password);

        try {
            validator.validatePassword();
            resultLabel.setText("✅ Password is Valid");
            resultLabel.setForeground(Color.GREEN);
        } catch (InvalidPasswordException ex) {
            resultLabel.setText(ex.getMessage());
            resultLabel.setForeground(Color.RED);
        }
    }

    public static void main(String[] args) {
        new MainApp();
    }
}