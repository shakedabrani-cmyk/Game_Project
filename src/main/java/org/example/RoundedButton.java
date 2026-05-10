package org.example;

import javax.swing.*;
import java.awt.*;

public class RoundedButton extends JButton {
    private int radius;

    public RoundedButton(String text, int radius) {
        super(text);
        this.radius = radius;
        setContentAreaFilled(false);
        setFocusPainted(false);
        setBorderPainted(false);
        setOpaque(false);
    }
    public static RoundedButton createPanelBackButton(int x, int y, JFrame frame, JPanel targetPanel) {
        RoundedButton backButton = new RoundedButton("↩", 25);
        backButton.setBounds(x, y, 50, 38);
        backButton.setBackground(new Color(91, 137, 166));
        backButton.setForeground(Color.WHITE);
        backButton.setFont(new Font("Segoe UI Symbol", Font.BOLD, 16));
        backButton.setCursor(new Cursor(Cursor.HAND_CURSOR));

        backButton.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent e) {
                backButton.setBackground(new Color(70, 115, 145));
            }

            @Override
            public void mouseExited(java.awt.event.MouseEvent e) {
                backButton.setBackground(new Color(91, 137, 166));
            }
        });

        backButton.addActionListener(e -> {
            frame.setContentPane(targetPanel);
            frame.revalidate();
            frame.repaint();
        });

        return backButton;
    }

    public static RoundedButton createExitButton(int x, int y) {
        RoundedButton exitButton = new RoundedButton("✕", 25);
        exitButton.setBounds(x, y, 50, 38);
        exitButton.setBackground(new Color(255, 95, 110));
        exitButton.setForeground(Color.WHITE);
        exitButton.setFont(new Font("Segoe UI Symbol", Font.BOLD, 14));
        exitButton.setCursor(new Cursor(Cursor.HAND_CURSOR));

        exitButton.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent e) {
                exitButton.setBackground(new Color(230, 65, 80));
            }

            @Override
            public void mouseExited(java.awt.event.MouseEvent e) {
                exitButton.setBackground(new Color(255, 95, 110));
            }
        });

        exitButton.addActionListener(e -> System.exit(0));

        return exitButton;
    }

    public static RoundedButton createBackButton(int x, int y, JPanel panel) {
        RoundedButton backButton = new RoundedButton("↩", 25);
        backButton.setBounds(x, y, 50, 38);
        backButton.setBackground(new Color(91, 137, 166));
        backButton.setForeground(Color.WHITE);
        backButton.setFont(new Font("Segoe UI Symbol", Font.BOLD, 16));
        backButton.setCursor(new Cursor(Cursor.HAND_CURSOR));

        backButton.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent e) {
                backButton.setBackground(new Color(70, 115, 145));
            }

            @Override
            public void mouseExited(java.awt.event.MouseEvent e) {
                backButton.setBackground(new Color(91, 137, 166));
            }
        });

        backButton.addActionListener(e -> {
            Window window = SwingUtilities.getWindowAncestor(panel);
            window.dispose();
            new MainMenu();
        });

        return backButton;
    }

    @Override
    public void paintComponent(Graphics graphics) {
        Graphics2D g2 = (Graphics2D) graphics;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        if (getModel().isArmed()) {
            g2.setColor(getBackground().darker());
        } else {
            g2.setColor(getBackground());
        }

        g2.fillRoundRect(0, 0, getWidth(), getHeight(), this.radius, this.radius);
        super.paintComponent(graphics);
    }
}