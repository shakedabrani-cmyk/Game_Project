package org.example;

public class InstructionsPanel extends BackgroundPanel {

    public InstructionsPanel(int width, int height,BackgroundPanel menuPanel, MainMenu mainMenu) {
        // אנחנו קוראים לבנאי של BackgroundPanel עם התמונה של ההוראות
        super("/background_instructions.png"); // או תמונה אחרת אם יש לך
        this.setBounds(0, 0, width, height);
        RoundedButton backButton =
                RoundedButton.createPanelBackButton(width - 135, 12, mainMenu, menuPanel);

        this.add(backButton);

        RoundedButton exitButton = RoundedButton.createExitButton(width - 80, 12);
        this.add(exitButton);
    }
}