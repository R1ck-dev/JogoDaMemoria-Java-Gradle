package jogodamemoria;

import java.util.List;

public class Menus {
    public static void mainMenuDisplay() {
        System.out.println("1 - Criar coleção de cartas");
        System.out.println("2 - Iniciar jogo");
        System.out.println("3 - Continuar jogo");
        System.out.println("4 - Sair");
    }

    public static void subMenuDisplay() {
        System.out.println("1 - Virar 2 cartas");
        System.out.println("2 - Pausar jogo");
    }

    public static void tabuleiroMenuDisplay(List<String> cards, Game game) {
        System.out.println("=== JOGO DA MEMÓRIA ===");
        System.out.println("Coleção: " + game.getUsedCollection());
        System.out.println();
                
        // Printar as cartas em colunas de 3
        for (int i = 0; i < cards.size(); i++) {
            // Mostrar "X" para pares encontrados, "?" para cartas viradas
            if (cards.get(i).equals("x")) {
                System.out.printf("%-15s", "X");
            } else {
                System.out.printf("%-15s", "?");
            }
                    
            // Quebrar linha a cada 3 cartas
            if ((i + 1) % 3 == 0) {
                 System.out.println();
            }
        }
                
        // Se o número total não for múltiplo de 3, quebrar linha
        if (cards.size() % 3 != 0) {
            System.out.println();
        }
                
        System.out.println();
        System.out.println("Escolha duas posições para virar (1-" + cards.size() + "):");
    }
}
