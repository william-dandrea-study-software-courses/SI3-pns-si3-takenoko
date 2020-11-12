package fr.matelots.polytech.core.game.GoalCards;

public class AlignedParcelGoal {
    private final int length;

    private boolean isCompleted;

    public AlignedParcelGoal(int length) {
        this.length = length;
    }

    public int getLength() {
         return length;
    }

    public boolean getComplete() {
        return this.isCompleted;
    }
    public void setComplete() {
        this.isCompleted = true;
    }
}
