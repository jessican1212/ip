package canelo;

import java.util.Scanner;
import java.util.ArrayList;
import java.io.FileWriter;
import java.io.IOException;
import java.io.File;
import java.io.FileNotFoundException;

public class Canelo {
    private static final String FILE_PATH = "caneloData.txt";

    private Ui ui;
    private Storage storage;
    private TaskList tasks;
    private Parser parser;

    public Canelo(String filePath) {
        ui = new Ui();
        storage = new Storage(filePath);
        try {
            tasks = new TaskList(storage.load());
        } catch (CaneloException | FileNotFoundException e) {
            ui.showLoadingError();
            tasks = new TaskList();
        }
        parser = new Parser(tasks, ui, storage);
    }

    public void run() {
        ui.printStartMessage();
        String userInput = ui.getUserInput();
        while (parser.isValidUserInput(userInput)) {
            ui.printLine();
            parser.handleCommand(userInput);
            ui.printLine();
            userInput = ui.getUserInput();
        }
        ui.printEndMessage();
    }

    public static void main(String[] args) {
        new Canelo(FILE_PATH).run();
    }
}
