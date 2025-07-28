package jogodamemoria;

import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws IOException {
        Scanner scanner = new Scanner(System.in);
        boolean runningMainMenu = true;
        boolean runningSubMenu;
        List<String> existingGames = new ArrayList<>();
        List<String> existingCollections = new ArrayList<>();

        while (runningMainMenu) {
            findCollection(existingCollections);
            findGame(existingGames);
            int saveCounter = saveCounter(existingGames);
            System.out.println(existingGames.toString());
            System.out.println(saveCounter);

            Menus.mainMenuDisplay();

            int optionMainMenu = scanner.nextInt();

            switch (optionMainMenu) {
                case 1:
                    Cards card = new Cards();
                    scanner.nextLine();
                    defineCollection(card, scanner);
                    card.createCollectionTxt();
                    break;

                case 2:
                    System.out.println("Indique com qual coleção você deseja jogar.");

                    for (int i = 0; i < existingCollections.size(); i++) {
                        System.out.println((i + 1) + " - " + existingCollections.get(i));
                    }

                    var chosenCollection = scanner.nextInt();
                    String selectedCollection = existingCollections.get(chosenCollection - 1);

                    Game game = Game.createFromCollection(selectedCollection, saveCounter);
                    game.createGameTxt();

                    String gameFileName = game.getGameName();
                    List<String> cards = cardHolder(gameFileName);
                    Collections.shuffle(cards);

                    runningSubMenu = true;
                    while (runningSubMenu) {
                        Menus.subMenuDisplay();
                        int optionSubMenu = scanner.nextInt();

                        switch (optionSubMenu) {
                            case 1:
                                Menus.tabuleiroMenuDisplay(cards, game);

                                System.out.println("Primeira posição: ");
                                int firstPosition = scanner.nextInt() - 1;
                                if (cards.get(firstPosition).equals("x")) {
                                    System.out.println("Esta carta já foi encontrada! Escolha outra posição.");
                                    continue;
                                }
                                System.out.println("Você encontrou: " + cards.get(firstPosition));

                                System.out.println("Segunda posição: ");
                                int secondPosition = scanner.nextInt() - 1;
                                if (secondPosition == firstPosition) {
                                    System.out.println("Posições iguais");
                                    continue;
                                }
                                if (cards.get(secondPosition).equals("x")) {
                                    System.out.println("Esta carta já foi encontrada! Escolha outra posição.");
                                    continue;
                                }
                                System.out.println("Você encontrou: " + cards.get(secondPosition));

                                System.out.println("Carta 1: " + cards.get(firstPosition));
                                System.out.println("Carta 2: " + cards.get(secondPosition));

                                if (cards.get(firstPosition).equals(cards.get(secondPosition))) {
                                    System.out.println("Par Encontrado!");

                                    deleteFoundPar(cards, firstPosition, secondPosition);
                                    saveGameState(game, cards);

                                    boolean allFound = true;
                                    for (String c : cards) {
                                        if (!c.equals("x")) {
                                            allFound = false;
                                            break;
                                        }
                                    }

                                    if (allFound) {
                                        System.out.println("Parabéns! Você encontrou todos os pares!");
                                        System.out.println("Jogo finalizado em " + (game.getRounds() + 1) + " rodadas.");
                                        deleteGameSave(game.getGameName());
                                        runningSubMenu = false;
                                    }
                                } else {
                                    System.out.println("Não são iguais.");
                                }
                                break;

                            case 2:
                                runningSubMenu = false;
                                break;
                        }
                    }
                    break;

                case 3:
                    if (existingGames.isEmpty()) {
                        System.out.println("Nenhum jogo salvo encontrado!");
                        break;
                    }

                    System.out.println("Escolha qual jogo continuar:");
                    for (int i = 0; i < existingGames.size(); i++) {
                        System.out.println((i + 1) + " - " + existingGames.get(i));
                    }

                    int chosenGame = scanner.nextInt();
                    String selectedGame = existingGames.get(chosenGame - 1);

                    Game loadedGame = loadGameFromFile(selectedGame + ".txt");
                    List<String> savedCards = cardHolder(selectedGame + ".txt");

                    System.out.println("Continuando de onde parou");
                    System.out.println("Coleção: " + loadedGame.getUsedCollection());
                    System.out.println("Rounds jogados: " + loadedGame.getRounds());

                    runningSubMenu = true;
                    while (runningSubMenu) {
                        Menus.subMenuDisplay();
                        int optionSubMenu = scanner.nextInt();

                        switch (optionSubMenu) {
                            case 1:
                                Menus.tabuleiroMenuDisplay(savedCards, loadedGame);

                                System.out.println("Primeira posição: ");
                                int firstPosition = scanner.nextInt() - 1;
                                if (savedCards.get(firstPosition).equals("x")) {
                                    System.out.println("Esta carta já foi encontrada! Escolha outra posição.");
                                    continue;
                                }
                                System.out.println("Você encontrou: " + savedCards.get(firstPosition));

                                System.out.println("Segunda posição: ");
                                int secondPosition = scanner.nextInt() - 1;
                                if (secondPosition == firstPosition) {
                                    System.out.println("Posições iguais");
                                    continue;
                                }
                                if (savedCards.get(secondPosition).equals("x")) {
                                    System.out.println("Esta carta já foi encontrada! Escolha outra posição.");
                                    continue;
                                }
                                System.out.println("Você encontrou: " + savedCards.get(secondPosition));

                                System.out.println("Carta 1: " + savedCards.get(firstPosition));
                                System.out.println("Carta 2: " + savedCards.get(secondPosition));

                                if (savedCards.get(firstPosition).equals(savedCards.get(secondPosition))) {
                                    System.out.println("Par Encontrado!");

                                    deleteFoundPar(savedCards, firstPosition, secondPosition);

                                    // Atualizar rounds do jogo carregado
                                    loadedGame.setRounds(loadedGame.getRounds() + 1);
                                    saveGameState(loadedGame, savedCards);

                                    boolean allFound = true;
                                    for (String c : savedCards) {
                                        if (!c.equals("x")) {
                                            allFound = false;
                                            break;
                                        }
                                    }

                                    if (allFound) {
                                        System.out.println("Parabéns! Você encontrou todos os pares!");
                                        System.out.println("Jogo finalizado em " + (loadedGame.getRounds() + 1) + " rodadas.");
                                        deleteGameSave(loadedGame.getGameName());
                                        runningSubMenu = false;
                                    }
                                } else {
                                    System.out.println("Não são iguais.");
                                    // Incrementar rounds mesmo se errar
                                    loadedGame.setRounds(loadedGame.getRounds() + 1);
                                    saveGameState(loadedGame, savedCards);
                                }
                                break;

                            case 2:
                                System.out.println("Jogo pausado e salvo!");
                                runningSubMenu = false;
                                break;
                        }
                    }
                    break;

                case 4:
                    runningMainMenu = false;
                    break;
            }
        }

        scanner.close();
    }

    public static void defineCollection(Cards card, Scanner scanner) {
        System.out.println("Indique o nome da collection: ");
        card.setCollectionName(scanner.nextLine().trim().toLowerCase());

        System.out.println("Indique a quantidade de cartas: ");
        card.setNCards(scanner.nextInt());
        scanner.nextLine();

        System.out.println("Indique as cartas da collection: ");
        for (int i = 0; i < card.getNCards() / 2; i++) {
            String cardName = scanner.nextLine().trim();
            if (!cardName.isEmpty()) {
                card.addCardName(cardName);
            }
        }
    }

    public static void findCollection(List<String> existingCollections) throws IOException {
        existingCollections.clear();

        Path filePathCollection = Path.of("app/src/main/java/jogodamemoria/collections/");

        try (DirectoryStream<Path> stream = Files.newDirectoryStream(filePathCollection, "*.txt")) {
            for (Path file : stream) {
                String fileName = file.getFileName().toString();
                String collectionName = fileName.replace(".txt", "");
                existingCollections.add(collectionName);
            }
        }
    }

    public static void findGame(List<String> existingGames) throws IOException {
        existingGames.clear();

        Path filePathGames = Path.of("app/src/main/java/jogodamemoria/games/");

        try (DirectoryStream<Path> stream = Files.newDirectoryStream(filePathGames, "*.txt")) {
            for (Path file : stream) {
                String fileName = file.getFileName().toString();
                String gameName = fileName.replace(".txt", "");
                existingGames.add(gameName);
            }
        }
    }

    public static int saveCounter(List<String> existingGames) throws IOException {
        int saveCount = 0;

        Path filePathGames = Path.of("app/src/main/java/jogodamemoria/games/");

        try (DirectoryStream<Path> stream = Files.newDirectoryStream(filePathGames, "*.txt")) {
            for (Path file : stream) {
                saveCount++;
            }
        }
        return saveCount;
    }

    public static List<String> cardHolder(String gameFileName) throws IOException {
        Path filePath = Path.of("app/src/main/java/jogodamemoria/games/" + gameFileName);

        List<String> lines = Files.readAllLines(filePath);
        List<String> cardHolder = new ArrayList<>();

        boolean readingCards = false;
        for (String line : lines) {
            if (line.equals("Cards in Game: ")) {
                readingCards = true;
            } else if (readingCards && !line.trim().isEmpty()) {
                cardHolder.add(line.trim());
            }
        }

        return cardHolder;
    }

    public static void deleteFoundPar(List<String> cards, int firstPosition, int secondPosition) {
        cards.set(firstPosition, "x");
        cards.set(secondPosition, "x");
    }

    public static void saveGameState(Game game, List<String> currentCards) throws IOException {
        Path filePath = game.getFilePathGame().resolve(game.getGameName());

        List<String> fileContent = new ArrayList<>();
        fileContent.add("Game Save Number: " + game.getGameSaveNumber());
        fileContent.add("Used Collection: " + game.getUsedCollection());
        fileContent.add("Rounds Passed: " + (game.getRounds() + 1));
        fileContent.add("Cards in Game: ");
        fileContent.addAll(currentCards);

        Files.write(filePath, fileContent);
    }

    public static Game loadGameFromFile(String gameFileName) throws IOException {
        Path filePath = Path.of("app/src/main/java/jogodamemoria/games/" + gameFileName);
        List<String> lines = Files.readAllLines(filePath);

        Game game = new Game();

        for (String line : lines) {
            if (line.startsWith("Game Save Number: ")) {
                game.setGameSaveNumber(Integer.parseInt(line.split(":")[1].trim()));
            } else if (line.startsWith("Used Collection: ")) {
                game.setUsedCollection(line.split(":")[1].trim());
            } else if (line.startsWith("Rounds Passed: ")) {
                game.setRounds(Integer.parseInt(line.split(":")[1].trim()));
            }
        }

        game.setGameName(gameFileName);
        game.setGameState(true);

        return game;
    }

    public static void deleteGameSave(String gameFileName) throws IOException {
        Path filePath = Path.of("app/src/main/java/jogodamemoria/games/" + gameFileName);
        if (Files.exists(filePath)) {
            Files.delete(filePath);
            System.out.println("Jogo salvo deletado com sucesso: " + gameFileName);
        }
    }
}
