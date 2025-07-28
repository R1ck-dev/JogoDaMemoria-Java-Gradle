package jogodamemoria;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import lombok.Data;

@Data
public class Cards {

    private String collectionName;
    private List<String> cardsName = new ArrayList<>();
    private int nCards;
    public Path filePathCollection = Path.of("app/src/main/java/jogodamemoria/collections/");

    public void createCollectionTxt() throws IOException {
        Path filePath = filePathCollection.resolve(this.collectionName + ".txt");
        
        Files.createDirectories(filePathCollection);

        List<String> fileContent = new ArrayList<>();
        fileContent.add("Name Collection: " + this.collectionName);
        fileContent.add("Number of Cards: " + this.nCards);
        fileContent.add("Cards: ");

        if (this.cardsName != null) {
            fileContent.addAll(cardsName);
        }
        
        Files.write(filePath, fileContent);
    }

    public void addCardName(String name) {
        cardsName.add(name);
        cardsName.add(name);
    }

    public static Cards createFromFile(String collectionName) throws IOException {
        Cards card = new Cards();
        card.setCollectionName(collectionName);

        Path filePath = Path.of("app/src/main/java/jogodamemoria/collections/" + collectionName + ".txt");
        List<String> lines = Files.readAllLines(filePath);

        boolean readingCards = false;
        for (String line : lines) {
            if (line.startsWith("Number of Cards: ")) {
                card.setNCards(Integer.parseInt(line.split(":")[1].trim()));
            } else if (line.equals("Cards: ")) {
                readingCards = true;
            } else if (readingCards && !line.trim().isEmpty()) {
                card.getCardsName().add(line.trim());
            }
        }

        return card;
    }
}
