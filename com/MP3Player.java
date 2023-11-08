package com;

import javax.sound.sampled.*;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.io.File;
import java.io.IOException;

public class MP3Player {

    public static void main(String[] args) {
        // Create a file chooser
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Select MP3 File");
        fileChooser.setFileFilter(new FileNameExtensionFilter("MP3 files", "mp3"));

        // Show the file chooser dialog
        int result = fileChooser.showOpenDialog(null);

        if (result == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();

            try {
                AudioInputStream audioStream = AudioSystem.getAudioInputStream(selectedFile);
                Clip clip = AudioSystem.getClip();
                clip.open(audioStream);

                System.out.println("Playing: " + selectedFile.getAbsolutePath());
                clip.start();

                // Wait for the clip to finish playing
                while (clip.isRunning()) {
                    Thread.sleep(100);
                }

                clip.close();
                audioStream.close();

                System.out.println("Playback finished.");
            } catch (UnsupportedAudioFileException | LineUnavailableException | IOException | InterruptedException e) {
                System.err.println("Error: " + e.getMessage());
            }
        } else {
            System.out.println("No file selected.");
        }
    }
}
