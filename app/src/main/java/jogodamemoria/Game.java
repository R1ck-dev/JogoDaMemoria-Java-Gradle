package jogodamemoria;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import lombok.Data;

@Data
public class Game {
    private String gameName;
    private boolean gameState;
    private int gameSaveNumber;
    private int rounds;
    private String usedCollection;
    private List<String> matchedCards = new ArrayList<>();
    private Cards gameCards;
    public Path filePathGame = Path.of("app/src/main/java/jogodamemoria/Games/");

    public void createGameTxt() throws IOException {
        Path filePath = filePathGame.resolve(this.gameName);

        Files.createDirectories(filePathGame);

        List<String> fileContent = new ArrayList<>();
        fileContent.add("Game Save Number: " + this.gameSaveNumber);
        fileContent.add("Used Collection: " + this.usedCollection);
        fileContent.add("Rounds Passed: " + this.rounds);
        fileContent.add("Cards in Game: ");

        if (this.gameCards != null && this.gameCards.getCardsName() != null) {
            fileContent.addAll(this.gameCards.getCardsName());
        }

        Files.write(filePath, fileContent);
    }

    public static Game createFromCollection(String collectionName, int saveCounter) throws IOException {
        Game game = new Game();
        game.setUsedCollection(collectionName);
        game.setGameState(true);
        game.setRounds(0);
        game.setGameSaveNumber(saveCounter+1);
        String gameName = collectionName + "game" + (saveCounter + 1) + ".txt";
        game.setGameName(gameName);
        game.setGameCards(Cards.createFromFile(collectionName));

        return game;
    }
}
