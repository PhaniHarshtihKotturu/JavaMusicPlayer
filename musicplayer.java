package com;

import java.awt.*;
import java.awt.event.*;
import java.io.*;
import javax.sound.sampled.*;
import javax.swing.*;
import javax.swing.filechooser.*;

public class musicplayer extends JFrame implements ActionListener {

    private JTextField filePathField;
    private JButton playButton;
    private JButton pauseButton;
    private JButton chooseButton;
    private JButton loopButton;
    private boolean isPaused;
    private boolean isLooping = false;
    private JFileChooser fileChooser;
    private Clip clip;

    public musicplayer() {
        super("Music Player");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new FlowLayout());

        filePathField = new JTextField(20);
        playButton = new JButton("Play");
        pauseButton = new JButton("Pause");
        chooseButton = new JButton("Choose File");
        loopButton = new JButton("Loop");
        isPaused = false;
        isLooping = false;

        playButton.addActionListener(this);
        pauseButton.addActionListener(this);
        chooseButton.addActionListener(this);
        loopButton.addActionListener(this);

        add(filePathField);
        add(chooseButton);
        add(playButton);
        add(pauseButton);
        add(loopButton);

        fileChooser = new JFileChooser(".");
        fileChooser.setFileFilter(new FileNameExtensionFilter("WAV Files", "wav"));

        setSize(500, 100);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent event) {
        if (event.getSource() == playButton) {
            playMusic();
        } else if (event.getSource() == pauseButton) {
            pauseMusic();
        } else if (event.getSource() == chooseButton) {
            chooseFile();
        } else if (event.getSource() == loopButton) {
            toggleLoop();
        }
    }

    private void playMusic() {
        if (clip != null && clip.isRunning()) {
            clip.stop();
        }

        try {
            File file = new File(filePathField.getText());
            AudioInputStream audioIn = AudioSystem.getAudioInputStream(file);

            AudioFileFormat fileFormat = AudioSystem.getAudioFileFormat(file);
            System.out.println("Audio file format: " + fileFormat.getType());

            clip = AudioSystem.getClip();
            clip.open(audioIn);

            if (isLooping) {
                clip.loop(Clip.LOOP_CONTINUOUSLY);
            }

            clip.start();
            System.out.println("Started Playing Music");
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    private void pauseMusic() {
        if (clip != null && clip.isRunning()) {
            clip.stop();
            isPaused = true;
            System.out.println("Music Paused");
            pauseButton.setText("Resume");
        } else if (clip != null && isPaused) {
            clip.start();

            if (isLooping) {
                clip.loop(Clip.LOOP_CONTINUOUSLY);
            }

            isPaused = false;
            System.out.println("Music Resumed");
            pauseButton.setText("Pause");
        }
    }

    private void chooseFile() {
        fileChooser.setCurrentDirectory(new File("."));
        int result = fileChooser.showOpenDialog(this);
        if (result == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            filePathField.setText(selectedFile.getAbsolutePath());
        }
    }

    private void toggleLoop() {
        isLooping = !isLooping;
        if (isLooping) {
            System.out.println("Looping ON");
            loopButton.setText("Stop Loop");

            if (clip.isRunning()) {
                clip.loop(Clip.LOOP_CONTINUOUSLY);
            }
        } else {
            System.out.println("Looping off");
            loopButton.setText("Loop");

            if (clip.isRunning()) {
                clip.loop(0);
            }
        }
    }

    public static void main(String[] args) {
        System.out.println("Player Started");
        musicplayer player = new musicplayer();

        // Console instructions
        BufferedReader consoleReader = new BufferedReader(new InputStreamReader(System.in));
        while (true) {
            try {
                System.out.println("Enter a command (play(1)/pause(2)/choose(3)/loop(4)/exit(5)): ");
                String command = consoleReader.readLine();

                if ("1".equalsIgnoreCase(command)) {
                    player.playMusic();
                } else if ("2".equalsIgnoreCase(command)) {
                    player.pauseMusic();
                } else if ("3".equalsIgnoreCase(command)) {
                    player.chooseFile();
                } else if ("4".equalsIgnoreCase(command)) {
                    player.toggleLoop();
                } else if ("5".equalsIgnoreCase(command)) {
                    System.out.println("Exiting the player.");
                    System.exit(0);
                } else {
                    System.out.println("Invalid command. Try again.");
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
