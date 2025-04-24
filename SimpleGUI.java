import javax.swing.*;

public class SimpleGUI {
    public static void main(String[] args) {
        // Create a new window (JFrame)
        JFrame frame = new JFrame("My First GUI");

        // Create a label
        JLabel label = new JLabel("Hello from a GUI!", SwingConstants.CENTER);

        // Set window size and add label
        frame.setSize(300, 200);
        frame.add(label);

        // Close app when window is closed
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Make the window visible
        frame.setVisible(true);
    }
}
