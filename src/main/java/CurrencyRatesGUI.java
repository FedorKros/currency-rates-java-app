import java.awt.event.*;
import java.awt.*;
import java.io.IOException;
import javax.swing.*;

class CurrencyRatesGUI extends JFrame implements ItemListener, ActionListener, KeyListener {

    static JFrame f;
    static JLabel l, lRes, lCalculated;
    static JComboBox c1, c2;
    static JButton btnSubmit, btnSwap, btnClear;
    static JTextField amount;
    static double rate = 1;

    public CurrencyRatesGUI() {
        super("Currency Rates");
        setSize(800, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
        setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));
        btnSubmit = new JButton("Submit");
        btnSwap = new JButton("Swap");
        btnClear = new JButton("Clear");

        // icon
        Image icon = Toolkit.getDefaultToolkit().getImage("src/images/icon.png");
        setIconImage(icon);

        // background
        ImageIcon backgroundImage = new ImageIcon("src/images/background.png");
        JPanel backgroundPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.drawImage(backgroundImage.getImage(), 0, 0, getWidth(), getHeight(), this);
            }
        };
        backgroundPanel.setLayout(new BoxLayout(backgroundPanel, BoxLayout.Y_AXIS));
        setContentPane(backgroundPanel);
    }


    public static void main(String[] args) {
        f = new CurrencyRatesGUI();

        CurrencyRatesGUI gui = new CurrencyRatesGUI();

        f.setLayout(new BoxLayout(f.getContentPane(), BoxLayout.Y_AXIS));


        String[] curPool = {"EUR", "BGN", "BRL", "CAD", "CHF", "CNY", "CZK", "DKK", "AUD", "GBP", "HKD",
                "HUF", "IDR", "ILS", "INR", "ISK", "JPY", "KRW", "MXN", "MYR", "NOK", "NZD",
                "PHP", "PLN", "RON", "SEK", "SGD", "THB", "TRY", "USD", "ZAR"};


        amount = new JTextField(10);
        amount.addKeyListener(gui);
        c1 = new JComboBox<>(curPool);
        c2 = new JComboBox<>(curPool);

        // add ItemListener
        c1.addItemListener(gui);
        c2.addItemListener(gui);
        btnSubmit.addActionListener(gui);
        btnSwap.addActionListener(gui);
        btnClear.addActionListener(gui);


        // create labels
        l = new JLabel("Select currencies: ");
        lRes = new JLabel("EUR to EUR rate: 1.0");
        lCalculated = new JLabel("Enter an amount to convert ");


        lRes.setForeground(Color.DARK_GRAY);
        // create panels for displaying rows
        JPanel row1 = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JPanel row2 = new JPanel(new FlowLayout(FlowLayout.CENTER));


        JLabel[] labels = {l, lRes, lCalculated};
        Font newFont = new Font("Segoe UI", Font.PLAIN, 18);
        for (JLabel label : labels) {
            label.setFont(newFont);
        }


        row1.setOpaque(false);
        row2.setOpaque(false);

        row1.add(l);
        row1.add(c1);
        row1.add(btnSwap);
        row1.add(c2);
        row1.add(lRes);
        row2.add(btnSubmit);
        row2.add(amount);
        row2.add(btnClear);
        row2.add(lCalculated);

        f.add(row1);
        f.add(row2);

        f.setVisible(true);


    }


    //    checks whether the entered amount is a valid input
    public void submitAmount() {
        if (!amount.getText().isEmpty() && amount.getText().matches("^[0-9]+(\\.[0-9]+)?$"))
            updateAmount(amount.getText());
        else lCalculated.setText("Enter an amount to convert ");
    }

    //    Swapping currencies
    public void swap(JComboBox<String> c1, JComboBox<String> c2) {
        Object temp = c1.getSelectedItem();
        c1.setSelectedItem(c2.getSelectedItem());
        c2.setSelectedItem(temp);
        updateRate();
        submitAmount();
    }

    //   Rounding to 2 decimal places
    public double round(double num) {
        num *= 100;
        num = Math.round(num);
        return num / 100;
    }

    //    Method to update amount converted to the other currency
    public void updateAmount(String amount) {
        double enteredAmount = Double.parseDouble(amount);
        lCalculated.setText("Your amount converted: " + round(rate * enteredAmount));
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_ENTER) submitAmount();
    }


    @Override
    public void keyReleased(KeyEvent e) {
    }


    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == btnSubmit) {
            submitAmount();
        } else if (e.getSource() == btnSwap) {
            swap(c1, c2);
        } else if (e.getSource() == btnClear) {
            amount.setText("");
            lCalculated.setText("Enter an amount to convert ");
        }
    }

    //     Sending request and receiving the rate
    public void updateRate() {
        String cur1 = (String) c1.getSelectedItem();
        String cur2 = (String) c2.getSelectedItem();

        lRes.setText(cur1 + " " + cur2);
        try {
            rate = CurrencyFetcher.getRate(cur1, cur2);
            lRes.setText(cur1 + " to " + cur2 + " rate: " + rate);
        } catch (IOException | InterruptedException ex) {
            throw new RuntimeException(ex);
        }
        submitAmount();
    }

    //    Changing currencies using dropdown lists
    @Override
    public void itemStateChanged(ItemEvent e) {
        updateRate();
    }
}
