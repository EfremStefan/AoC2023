package days;

import java.util.List;
import java.util.Map;

public class Pipe {

    private final Character pipeType;
    private List<Character> possibleDirections;

    public Pipe(Character pipeType) {
        this.pipeType = pipeType;
    }

    public Character getPipeType() {
        return pipeType;
    }

    public List<Character> getPossibleDirections() {
        return possibleDirections;
    }

    public void setPossibleDirections(List<Character> possibleDirections) {
        this.possibleDirections = possibleDirections;
    }

    @Override
    public String toString() {
        return "Pipe = " + pipeType;
    }
}
