package org.example;

public class InstructionsPanel extends BackgroundPanel {

    public InstructionsPanel(int width, int height, BackgroundPanel menuPanel, MainMenu mainMenu) {
        super("/background_instructions.png");
        this.setBounds(0, 0, width, height);
        RoundedButton backButton = RoundedButton.createPanelBackButton(width, mainMenu, menuPanel);
        this.add(backButton);
        RoundedButton exitButton = RoundedButton.createExitButton(width);
        this.add(exitButton);
    }
}