package fr.matelots.polytech.core.game.card.gardener;

import fr.matelots.polytech.core.game.Board;
import fr.matelots.polytech.core.game.goalcards.CardObjectiveGardener;
import fr.matelots.polytech.core.game.parcels.BambooColor;
import fr.matelots.polytech.core.game.parcels.BambooPlantation;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Alexandre Arcil
 */
public class CardObjectiveGardenerTest {

    private Board board;

    @BeforeEach
    public void init() {
        this.board = new Board();
    }

    @Test @DisplayName("seulement l'étang")
    public void onlyPond() {
        CardObjectiveGardener card = new CardObjectiveGardener(this.board, 1, BambooColor.GREEN, 4, 1);
        assertFalse(card.verify());
    }

    @Test @DisplayName("plateau avec des parcelles sans bamboo")
    public void noBamboo() {
        for(int i = 0; i < 4; i++)
            this.board.addParcel(-1 - i, 1, i, new BambooPlantation(BambooColor.GREEN));
        CardObjectiveGardener card = new CardObjectiveGardener(this.board, 1, BambooColor.GREEN, 4, 1);
        assertFalse(card.verify());
    }

    @Test @DisplayName("carte '1 bamboo vert avec 4 de hauteur' avec un bamboo vert de 1 de hauteur sur le plateau")
    public void greenBambooSizeFourCardOne() {
        BambooPlantation parcel = new BambooPlantation(BambooColor.GREEN);
        parcel.growBamboo();
        this.board.addParcel(-1, 1, 0, parcel);
        CardObjectiveGardener card = new CardObjectiveGardener(this.board, 1, BambooColor.GREEN, 4, 1);
        assertFalse(card.verify());
    }

    @Test @DisplayName("carte '1 bamboo vert avec 4 de hauteur' avec un bamboo vert de 2 de hauteur sur le plateau")
    public void greenBambooSizeFourCardTwo() {
        BambooPlantation parcel = new BambooPlantation(BambooColor.GREEN);
        for(int i = 0; i < 2; i++)
            parcel.growBamboo();
        this.board.addParcel(-1, 1, 0, parcel);
        CardObjectiveGardener card = new CardObjectiveGardener(this.board, 1, BambooColor.GREEN, 4, 1);
        assertFalse(card.verify());
    }

    @Test @DisplayName("carte '1 bamboo vert avec 4 de hauteur' avec un bamboo vert de 3 de hauteur sur le plateau")
    public void greenBambooSizeFourCardThree() {
        BambooPlantation parcel = new BambooPlantation(BambooColor.GREEN);
        for(int i = 0; i < 3; i++)
            parcel.growBamboo();
        this.board.addParcel(-1, 1, 0, parcel);
        CardObjectiveGardener card = new CardObjectiveGardener(this.board, 1, BambooColor.GREEN, 4, 1);
        assertFalse(card.verify());
    }

    @Test @DisplayName("carte '1 bamboo vert avec 4 de hauteur' complété")
    public void greenBambooSizeFourCard() {
        BambooPlantation parcel = new BambooPlantation(BambooColor.GREEN);
        for(int i = 0; i < 4; i++)
            parcel.growBamboo();
        this.board.addParcel(-1, 1, 0, parcel);
        CardObjectiveGardener card = new CardObjectiveGardener(this.board, 1, BambooColor.GREEN, 4, 1);
        assertTrue(card.verify());
    }

    @Test @DisplayName("carte '1 bamboo vert avec 4 de hauteur' complété avec 2 bamboos verts de 4 de hauteur")
    public void greenBambooSizeFourCardWithMore() {
        BambooPlantation parcel = new BambooPlantation(BambooColor.GREEN);
        BambooPlantation parcel2 = new BambooPlantation(BambooColor.GREEN);
        for(int i = 0; i < 4; i++) {
            parcel.growBamboo();
            parcel2.growBamboo();
        }
        this.board.addParcel(-1, 1, 0, parcel);
        this.board.addParcel(0, 1, -1, parcel2);
        CardObjectiveGardener card = new CardObjectiveGardener(this.board, 1, BambooColor.GREEN, 4, 1);
        assertTrue(card.verify());
    }

    @Test @DisplayName("carte '1 bamboo jaune avec 4 de hauteur' avec un bamboo jaune de 1 de hauteur sur le plateau")
    public void yellowBambooSizeFourCardOne() {
        BambooPlantation parcel = new BambooPlantation(BambooColor.YELLOW);
        parcel.growBamboo();
        this.board.addParcel(-1, 1, 0, parcel);
        CardObjectiveGardener card = new CardObjectiveGardener(this.board, 1, BambooColor.YELLOW, 4, 1);
        assertFalse(card.verify());
    }

    @Test @DisplayName("carte '1 bamboo jaune avec 4 de hauteur' avec un bamboo jaune de 2 de hauteur sur le plateau")
    public void yellowBambooSizeFourCardTwo() {
        BambooPlantation parcel = new BambooPlantation(BambooColor.YELLOW);
        for(int i = 0; i < 2; i++)
            parcel.growBamboo();
        this.board.addParcel(-1, 1, 0, parcel);
        CardObjectiveGardener card = new CardObjectiveGardener(this.board, 1, BambooColor.YELLOW, 4, 1);
        assertFalse(card.verify());
    }

    @Test @DisplayName("carte '1 bamboo jaune avec 4 de hauteur' avec un bamboo jaune de 3 de hauteur sur le plateau")
    public void yellowBambooSizeFourCardThree() {
        BambooPlantation parcel = new BambooPlantation(BambooColor.YELLOW);
        for(int i = 0; i < 3; i++)
            parcel.growBamboo();
        this.board.addParcel(-1, 1, 0, parcel);
        CardObjectiveGardener card = new CardObjectiveGardener(this.board, 1, BambooColor.YELLOW, 4, 1);
        assertFalse(card.verify());
    }

    @Test @DisplayName("carte '1 bamboo jaune avec 4 de hauteur' complété")
    public void yellowBambooSizeFourCard() {
        BambooPlantation parcel = new BambooPlantation(BambooColor.YELLOW);
        for(int i = 0; i < 4; i++)
            parcel.growBamboo();
        this.board.addParcel(-1, 1, 0, parcel);
        CardObjectiveGardener card = new CardObjectiveGardener(this.board, 1, BambooColor.YELLOW, 4, 1);
        assertTrue(card.verify());
    }

    @Test @DisplayName("carte '1 bamboo jaune avec 4 de hauteur' complété avec 2 bamboos jaune de 4 de hauteur")
    public void yellowBambooSizeFourCardWithMore() {
        BambooPlantation parcel = new BambooPlantation(BambooColor.YELLOW);
        BambooPlantation parcel2 = new BambooPlantation(BambooColor.YELLOW);
        for(int i = 0; i < 4; i++) {
            parcel.growBamboo();
            parcel2.growBamboo();
        }
        this.board.addParcel(-1, 1, 0, parcel);
        this.board.addParcel(0, 1, -1, parcel2);
        CardObjectiveGardener card = new CardObjectiveGardener(this.board, 1, BambooColor.YELLOW, 4, 1);
        assertTrue(card.verify());
    }

    @Test @DisplayName("carte '1 bamboo rose avec 4 de hauteur' avec un bamboo rose de 1 de hauteur sur le plateau")
    public void pinkBambooSizeFourCardOne() {
        BambooPlantation parcel = new BambooPlantation(BambooColor.PINK);
        parcel.growBamboo();
        this.board.addParcel(-1, 1, 0, parcel);
        CardObjectiveGardener card = new CardObjectiveGardener(this.board, 1, BambooColor.PINK, 4, 1);
        assertFalse(card.verify());
    }

    @Test @DisplayName("carte '1 bamboo rose avec 4 de hauteur' avec un bamboo rose de 2 de hauteur sur le plateau")
    public void pinkBambooSizeFourCardTwo() {
        BambooPlantation parcel = new BambooPlantation(BambooColor.PINK);
        for(int i = 0; i < 2; i++)
            parcel.growBamboo();
        this.board.addParcel(-1, 1, 0, parcel);
        CardObjectiveGardener card = new CardObjectiveGardener(this.board, 1, BambooColor.PINK, 4, 1);
        assertFalse(card.verify());
    }

    @Test @DisplayName("carte '1 bamboo rose avec 4 de hauteur' avec un bamboo rose de 3 de hauteur sur le plateau")
    public void pinkBambooSizeFourCardThree() {
        BambooPlantation parcel = new BambooPlantation(BambooColor.PINK);
        for(int i = 0; i < 3; i++)
            parcel.growBamboo();
        this.board.addParcel(-1, 1, 0, parcel);
        CardObjectiveGardener card = new CardObjectiveGardener(this.board, 1, BambooColor.PINK, 4, 1);
        assertFalse(card.verify());
    }

    @Test @DisplayName("carte '1 bamboo rose avec 4 de hauteur' complété")
    public void pinkBambooSizeFourCard() {
        BambooPlantation parcel = new BambooPlantation(BambooColor.PINK);
        for(int i = 0; i < 4; i++)
            parcel.growBamboo();
        this.board.addParcel(-1, 1, 0, parcel);
        CardObjectiveGardener card = new CardObjectiveGardener(this.board, 1, BambooColor.PINK, 4, 1);
        assertTrue(card.verify());
    }

    @Test @DisplayName("carte '1 bamboo rose avec 4 de hauteur' complété avec 2 bamboos rose de 4 de hauteur")
    public void pinkBambooSizeFourCardWithMore() {
        BambooPlantation parcel = new BambooPlantation(BambooColor.PINK);
        BambooPlantation parcel2 = new BambooPlantation(BambooColor.PINK);
        for(int i = 0; i < 4; i++) {
            parcel.growBamboo();
            parcel2.growBamboo();
        }
        this.board.addParcel(-1, 1, 0, parcel);
        this.board.addParcel(0, 1, -1, parcel2);
        CardObjectiveGardener card = new CardObjectiveGardener(this.board, 1, BambooColor.PINK, 4, 1);
        assertTrue(card.verify());
    }

    @Test @DisplayName("carte '2 bamboos rose avec 3 de hauteur' avec un bamboo rose de 3 de hauteur sur le plateau")
    public void twoPinkBambooSizeThreeCardOnlyOne() {
        BambooPlantation parcel = new BambooPlantation(BambooColor.PINK);
        for(int i = 0; i < 3; i++)
            parcel.growBamboo();
        this.board.addParcel(-1, 1, 0, parcel);
        CardObjectiveGardener card = new CardObjectiveGardener(this.board, 1, BambooColor.PINK, 3, 2);
        assertFalse(card.verify());
    }

    @Test @DisplayName("carte '2 bamboos rose avec 3 de hauteur' avec un bamboo rose de 3 de hauteur et un de 2 sur le plateau")
    public void twoPinkBambooSizeThreeCardOnlyTwoTooTiny() {
        BambooPlantation parcel = new BambooPlantation(BambooColor.PINK);
        for(int i = 0; i < 3; i++)
            parcel.growBamboo();
        BambooPlantation parcel2 = new BambooPlantation(BambooColor.PINK);
        parcel2.growBamboo();
        parcel2.growBamboo();
        this.board.addParcel(-1, 1, 0, parcel);
        this.board.addParcel(0, 1, -1, parcel2);
        CardObjectiveGardener card = new CardObjectiveGardener(this.board, 1, BambooColor.PINK, 3, 2);
        assertFalse(card.verify());
    }

    @Test @DisplayName("carte '2 bamboos rose avec 3 de hauteur' avec un bamboo rose de 3 de hauteur et un de 4 sur le plateau")
    public void twoPinkBambooSizeThreeCardOnlyTwoTooBig() {
        BambooPlantation parcel = new BambooPlantation(BambooColor.PINK);
        for(int i = 0; i < 3; i++)
            parcel.growBamboo();
        BambooPlantation parcel2 = new BambooPlantation(BambooColor.PINK);
        for(int i = 0; i < 4; i++)
            parcel2.growBamboo();
        this.board.addParcel(-1, 1, 0, parcel);
        this.board.addParcel(0, 1, -1, parcel2);
        CardObjectiveGardener card = new CardObjectiveGardener(this.board, 1, BambooColor.PINK, 3, 2);
        assertFalse(card.verify());
    }

    @Test @DisplayName("carte '2 bamboos rose avec 3 de hauteur' complété")
    public void twoPinkBambooSizeThreeCard() {
        BambooPlantation parcel = new BambooPlantation(BambooColor.PINK);
        for(int i = 0; i < 3; i++)
            parcel.growBamboo();
        BambooPlantation parcel2 = new BambooPlantation(BambooColor.PINK);
        for(int i = 0; i < 3; i++)
            parcel2.growBamboo();
        this.board.addParcel(-1, 1, 0, parcel);
        this.board.addParcel(0, 1, -1, parcel2);
        CardObjectiveGardener card = new CardObjectiveGardener(this.board, 1, BambooColor.PINK, 3, 2);
        assertTrue(card.verify());
    }

    @Test @DisplayName("carte '2 bamboos rose avec 3 de hauteur' complété avec 3 bamboos rose de 3 de hauteurs")
    public void twoPinkBambooSizeThreeCardWithMore() {
        BambooPlantation parcel = new BambooPlantation(BambooColor.PINK);
        BambooPlantation parcel2 = new BambooPlantation(BambooColor.PINK);
        BambooPlantation parcel3 = new BambooPlantation(BambooColor.PINK);
        for(int i = 0; i < 3; i++) {
            parcel.growBamboo();
            parcel2.growBamboo();
            parcel3.growBamboo();
        }
        this.board.addParcel(-1, 1, 0, parcel);
        this.board.addParcel(0, 1, -1, parcel2);
        this.board.addParcel(1, 0, -1, parcel2);
        CardObjectiveGardener card = new CardObjectiveGardener(this.board, 1, BambooColor.PINK, 3, 2);
        assertTrue(card.verify());
    }

    @Test @DisplayName("carte '3 bamboos jaune avec 3 de hauteur' avec un bamboo jaune de 3 de hauteur sur le plateau")
    public void threeYellowBambooSizeThreeCardOnlyOne() {
        BambooPlantation parcel = new BambooPlantation(BambooColor.YELLOW);
        for(int i = 0; i < 3; i++)
            parcel.growBamboo();
        this.board.addParcel(-1, 1, 0, parcel);
        CardObjectiveGardener card = new CardObjectiveGardener(this.board, 1, BambooColor.YELLOW, 3, 3);
        assertFalse(card.verify());
    }

    @Test @DisplayName("carte '3 bamboos jaune avec 3 de hauteur' avec 2 bamboos jaune de 3 de hauteur sur le plateau")
    public void threeYellowBambooSizeThreeCardOnlyTwo() {
        BambooPlantation parcel = new BambooPlantation(BambooColor.YELLOW);
        BambooPlantation parcel2 = new BambooPlantation(BambooColor.YELLOW);
        for(int i = 0; i < 3; i++) {
            parcel.growBamboo();
            parcel2.growBamboo();
        }
        this.board.addParcel(-1, 1, 0, parcel);
        this.board.addParcel(0, 1, -1, parcel2);
        CardObjectiveGardener card = new CardObjectiveGardener(this.board, 1, BambooColor.YELLOW, 3, 3);
        assertFalse(card.verify());
    }

    @Test @DisplayName("carte '3 bamboos jaune avec 3 de hauteur' avec 2 bamboo jaune de 3 de hauteur et un de 2 sur le plateau")
    public void threeYellowBambooSizeThreeCardOnlyTwoTooTiny() {
        BambooPlantation parcel = new BambooPlantation(BambooColor.YELLOW);
        BambooPlantation parcel2 = new BambooPlantation(BambooColor.YELLOW);
        for(int i = 0; i < 3; i++) {
            parcel.growBamboo();
            parcel2.growBamboo();
        }
        BambooPlantation parcel3 = new BambooPlantation(BambooColor.YELLOW);
        parcel3.growBamboo();
        parcel3.growBamboo();
        this.board.addParcel(-1, 1, 0, parcel);
        this.board.addParcel(0, 1, -1, parcel2);
        this.board.addParcel(1, 0, -1, parcel3);
        CardObjectiveGardener card = new CardObjectiveGardener(this.board, 1, BambooColor.YELLOW, 3, 3);
        assertFalse(card.verify());
    }

    @Test @DisplayName("carte '3 bamboos jaune avec 3 de hauteur' avec un bamboo jaune de 3 de hauteur et un de 4 sur le plateau")
    public void threeYellowBambooSizeThreeCardOnlyTwoTooBig() {
        BambooPlantation parcel = new BambooPlantation(BambooColor.YELLOW);
        BambooPlantation parcel2 = new BambooPlantation(BambooColor.YELLOW);
        for(int i = 0; i < 3; i++) {
            parcel.growBamboo();
            parcel2.growBamboo();
        }
        BambooPlantation parcel3 = new BambooPlantation(BambooColor.YELLOW);
        for(int i = 0; i < 4; i++)
            parcel2.growBamboo();
        this.board.addParcel(-1, 1, 0, parcel);
        this.board.addParcel(0, 1, -1, parcel2);
        this.board.addParcel(1, 0, -1, parcel3);
        CardObjectiveGardener card = new CardObjectiveGardener(this.board, 1, BambooColor.YELLOW, 3, 3);
        assertFalse(card.verify());
    }

    @Test @DisplayName("carte '3 bamboos jaune avec 3 de hauteur' complété")
    public void threeYellowBambooSizeThreeCard() {
        BambooPlantation parcel = new BambooPlantation(BambooColor.YELLOW);
        BambooPlantation parcel2 = new BambooPlantation(BambooColor.YELLOW);
        BambooPlantation parcel3 = new BambooPlantation(BambooColor.YELLOW);
        for(int i = 0; i < 3; i++) {
            parcel.growBamboo();
            parcel2.growBamboo();
            parcel3.growBamboo();
        }
        this.board.addParcel(-1, 1, 0, parcel);
        this.board.addParcel(0, 1, -1, parcel2);
        this.board.addParcel(1, 0, -1, parcel3);
        CardObjectiveGardener card = new CardObjectiveGardener(this.board, 1, BambooColor.YELLOW, 3, 3);
        assertTrue(card.verify());
    }

    @Test @DisplayName("carte '3 bamboos jaune avec 3 de hauteur' complété avec 4 bamboos jaune de 3 de hauteur")
    public void threeYellowBambooSizeThreeCardWithMore() {
        BambooPlantation parcel = new BambooPlantation(BambooColor.YELLOW);
        BambooPlantation parcel2 = new BambooPlantation(BambooColor.YELLOW);
        BambooPlantation parcel3 = new BambooPlantation(BambooColor.YELLOW);
        BambooPlantation parcel4 = new BambooPlantation(BambooColor.YELLOW);
        for(int i = 0; i < 3; i++) {
            parcel.growBamboo();
            parcel2.growBamboo();
            parcel3.growBamboo();
        }
        this.board.addParcel(-1, 1, 0, parcel);
        this.board.addParcel(0, 1, -1, parcel2);
        this.board.addParcel(1, 0, -1, parcel3);
        this.board.addParcel(1, -1, 0, parcel4);
        CardObjectiveGardener card = new CardObjectiveGardener(this.board, 1, BambooColor.YELLOW, 3, 3);
        assertTrue(card.verify());
    }

    @Test @DisplayName("carte '4 bamboos verts avec 3 de hauteur' avec un bamboo vert de 3 de hauteur sur le plateau")
    public void fourGreenBambooSizeThreeCardOnlyOne() {
        BambooPlantation parcel = new BambooPlantation(BambooColor.GREEN);
        for(int i = 0; i < 3; i++)
            parcel.growBamboo();
        this.board.addParcel(-1, 1, 0, parcel);
        CardObjectiveGardener card = new CardObjectiveGardener(this.board, 1, BambooColor.GREEN, 3, 4);
        assertFalse(card.verify());
    }

    @Test @DisplayName("carte '4 bamboos verts avec 3 de hauteur' avec 2 bamboos verts de 3 de hauteur sur le plateau")
    public void fourGreenBambooSizeThreeCardOnlyTwo() {
        BambooPlantation parcel = new BambooPlantation(BambooColor.GREEN);
        BambooPlantation parcel2 = new BambooPlantation(BambooColor.GREEN);
        for(int i = 0; i < 3; i++) {
            parcel.growBamboo();
            parcel2.growBamboo();
        }
        this.board.addParcel(-1, 1, 0, parcel);
        this.board.addParcel(0, 1, -1, parcel2);
        CardObjectiveGardener card = new CardObjectiveGardener(this.board, 1, BambooColor.GREEN, 3, 4);
        assertFalse(card.verify());
    }

    @Test @DisplayName("carte '4 bamboos verts avec 3 de hauteur' avec 3 bamboos verts de 3 de hauteur sur le plateau")
    public void fourGreenBambooSizeThreeCardOnlyThree() {
        BambooPlantation parcel = new BambooPlantation(BambooColor.GREEN);
        BambooPlantation parcel2 = new BambooPlantation(BambooColor.GREEN);
        BambooPlantation parcel3 = new BambooPlantation(BambooColor.GREEN);
        for(int i = 0; i < 3; i++) {
            parcel.growBamboo();
            parcel2.growBamboo();
            parcel3.growBamboo();
        }
        this.board.addParcel(-1, 1, 0, parcel);
        this.board.addParcel(0, 1, -1, parcel2);
        this.board.addParcel(1, 0, -1, parcel3);
        CardObjectiveGardener card = new CardObjectiveGardener(this.board, 1, BambooColor.GREEN, 3, 4);
        assertFalse(card.verify());
    }

    @Test @DisplayName("carte '4 bamboos verts avec 3 de hauteur' avec 3 bamboo verts de 3 de hauteur et un de 2 sur le plateau")
    public void fourGreenBambooSizeThreeCardOnlyTwoTooTiny() {
        BambooPlantation parcel = new BambooPlantation(BambooColor.GREEN);
        BambooPlantation parcel2 = new BambooPlantation(BambooColor.GREEN);
        BambooPlantation parcel3 = new BambooPlantation(BambooColor.GREEN);
        for(int i = 0; i < 3; i++) {
            parcel.growBamboo();
            parcel2.growBamboo();
            parcel3.growBamboo();
        }
        BambooPlantation parcel4 = new BambooPlantation(BambooColor.GREEN);
        parcel3.growBamboo();
        parcel3.growBamboo();
        this.board.addParcel(-1, 1, 0, parcel);
        this.board.addParcel(0, 1, -1, parcel2);
        this.board.addParcel(1, 0, -1, parcel3);
        this.board.addParcel(1, -1, 0, parcel4);
        CardObjectiveGardener card = new CardObjectiveGardener(this.board, 1, BambooColor.GREEN, 3, 4);
        assertFalse(card.verify());
    }

    @Test @DisplayName("carte '4 bamboos verts avec 3 de hauteur' avec un bamboo verts de 3 de hauteur et un de 4 sur le plateau")
    public void fourGreenBambooSizeThreeCardOnlyTwoTooBig() {
        BambooPlantation parcel = new BambooPlantation(BambooColor.GREEN);
        BambooPlantation parcel2 = new BambooPlantation(BambooColor.GREEN);
        BambooPlantation parcel3 = new BambooPlantation(BambooColor.GREEN);
        for(int i = 0; i < 3; i++) {
            parcel.growBamboo();
            parcel2.growBamboo();
            parcel3.growBamboo();
        }
        BambooPlantation parcel4 = new BambooPlantation(BambooColor.GREEN);
        for(int i = 0; i < 4; i++)
            parcel2.growBamboo();
        this.board.addParcel(-1, 1, 0, parcel);
        this.board.addParcel(0, 1, -1, parcel2);
        this.board.addParcel(1, 0, -1, parcel3);
        this.board.addParcel(1, -1, 0, parcel4);
        CardObjectiveGardener card = new CardObjectiveGardener(this.board, 1, BambooColor.GREEN, 3, 4);
        assertFalse(card.verify());
    }

    @Test @DisplayName("carte '4 bamboos verts avec 3 de hauteur' complété")
    public void fourGreenBambooSizeThreeCard() {
        BambooPlantation parcel = new BambooPlantation(BambooColor.GREEN);
        BambooPlantation parcel2 = new BambooPlantation(BambooColor.GREEN);
        BambooPlantation parcel3 = new BambooPlantation(BambooColor.GREEN);
        BambooPlantation parcel4 = new BambooPlantation(BambooColor.GREEN);
        for(int i = 0; i < 3; i++) {
            parcel.growBamboo();
            parcel2.growBamboo();
            parcel3.growBamboo();
            parcel4.growBamboo();
        }
        this.board.addParcel(-1, 1, 0, parcel);
        this.board.addParcel(0, 1, -1, parcel2);
        this.board.addParcel(1, 0, -1, parcel3);
        this.board.addParcel(1, -1, 0, parcel4);
        CardObjectiveGardener card = new CardObjectiveGardener(this.board, 1, BambooColor.GREEN, 3, 4);
        assertTrue(card.verify());
    }

    @Test @DisplayName("carte '4 bamboos verts avec 3 de hauteur' complété avec 5 bamboos vers de 3 de hauteurs")
    public void fourGreenBambooSizeThreeCardWithMore() {
        BambooPlantation parcel = new BambooPlantation(BambooColor.GREEN);
        BambooPlantation parcel2 = new BambooPlantation(BambooColor.GREEN);
        BambooPlantation parcel3 = new BambooPlantation(BambooColor.GREEN);
        BambooPlantation parcel4 = new BambooPlantation(BambooColor.GREEN);
        BambooPlantation parcel5 = new BambooPlantation(BambooColor.GREEN);
        for(int i = 0; i < 3; i++) {
            parcel.growBamboo();
            parcel2.growBamboo();
            parcel3.growBamboo();
            parcel4.growBamboo();
            parcel5.growBamboo();
        }
        this.board.addParcel(-1, 1, 0, parcel);
        this.board.addParcel(0, 1, -1, parcel2);
        this.board.addParcel(1, 0, -1, parcel3);
        this.board.addParcel(1, -1, 0, parcel4);
        this.board.addParcel(1, -1, 0, parcel5);
        CardObjectiveGardener card = new CardObjectiveGardener(this.board, 1, BambooColor.GREEN, 3, 4);
        assertTrue(card.verify());
    }


}
