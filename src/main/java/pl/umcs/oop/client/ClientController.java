package pl.umcs.oop.client;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import pl.umcs.oop.server.Server;
import pl.umcs.oop.server.WordBag;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Date;
import java.util.Formatter;
import java.util.Optional;

public class ClientController {
    @FXML
    private TextField filterField;

    @FXML
    private ListView<String> wordList;

    @FXML
    private Label wordCountLabel;

    private ClientThread client;
    private TextArea outputArea;
    private PrintWriter writer;
    private Socket socket;

    // Oryginalna lista do dodawania elementów
    private ObservableList<String> originalList;
    private SortedList<String> sortedList;
    private FilteredList<String> filteredList;

    @FXML
    private void initialize() {
        try {
            socket = new Socket("localhost", 5000);
            writer = new PrintWriter(socket.getOutputStream(), true);

            // Zachowaj oryginalną listę z wordList
            originalList = wordList.getItems();

            // Ustaw sortowanie i filtrowanie na oryginalnej liście
            sortedList = new SortedList<>(originalList);
            filteredList = new FilteredList<>(sortedList);

            // Obsługa filtrowania na podstawie tekstu
            filterField.setOnAction(event -> {
                String text = filterField.getText();
                if (text != null && !text.trim().isEmpty()) {
                    writer.println(text);
                    filterField.clear();
                }
            });

            Thread receiverThread = new Thread(() -> {
                try {
                    BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                    String line;
                    while ((line = reader.readLine()) != null) {
                        String message = line;
                        Platform.runLater(() -> {
                            LocalTime time = LocalTime.now();
                            int seconds = time.getSecond();
                            int minutes = time.getMinute();
                            int hours = time.getHour();
                            String secondsString = String.format("%02d", seconds);
                            String minutesString = String.format("%02d", minutes);
                            String hoursString = String.format("%02d", hours);

                            String timeString = hoursString + ":" + minutesString + ":" + secondsString;
                            String messageWithTimeStamp = message + " " + timeString;

                            // ZAWSZE dodawaj do oryginalnej listy
                            originalList.add(messageWithTimeStamp);

                            // Logika wyświetlania
                            String filterText = filterField.getText();

                            if (filterText == null || filterText.trim().isEmpty()) {
                                // Gdy brak filtra - pokaż posortowaną listę
                                wordList.setItems(sortedList);
                            } else {
                                // Gdy jest filtr - zastosuj filtrowanie na posortowanej liście
                                filteredList.setPredicate(item ->
                                        item.toLowerCase().contains(filterText.toLowerCase()));
                                wordList.setItems(filteredList);
                            }

                            wordCountLabel.setText(String.valueOf(originalList.size()));
                        });
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
            receiverThread.setDaemon(true);
            receiverThread.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void receiveMessage(String message) {
        filterField.clear();
        outputArea.appendText(message+"\n");
    }
}