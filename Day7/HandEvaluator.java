package days;

import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;


// class used to compare the hands
public class HandEvaluator implements Comparator<String> {

    private final Map<Character, Integer> cardValues;

    private final boolean part2;

    public HandEvaluator(Map<Character, Integer> cardValues, boolean part2) {
        this.cardValues = cardValues;
        this.part2 = part2;
    }

    //function used to compareHands
    public int compareHands(String hand1, String hand2){
        String originalHand1 = hand1;
        String originalHand2 = hand2;
        // if part2, update the hands with the wildcard Joker
        if(part2){
            hand1 = updateHandJoker(hand1);
            hand2 = updateHandJoker(hand2);
        }
        //5ofAKind
        boolean fiveOfAKind1 = this.evaluateFive(hand1);
        boolean fiveOfAKind2 = this.evaluateFive(hand2);
        if(fiveOfAKind1 && !fiveOfAKind2){
            return 1;
        } else if(fiveOfAKind2 && !fiveOfAKind1){
            return -1;
        }
        if(fiveOfAKind1){
            //when we compare EqualHands, aka of sameType, we go with the originalHand, without Joker's wildcard ability
            return compareEqualHands(originalHand1, originalHand2);
        }
        //continue to 4ofAKind
        boolean fourOfAKind1 = this.evaluateFour(hand1);
        boolean fourOfAKind2 = this.evaluateFour(hand2);
        if(fourOfAKind1 && !fourOfAKind2){
            return 1;
        } else if(fourOfAKind2 && !fourOfAKind1){
            return -1;
        }
        if(fourOfAKind1){
            return compareEqualHands(originalHand1, originalHand2);
        }
        //FullHouse
        boolean fullHouse1 = this.evaluateFullHouse(hand1);
        boolean fullHouse2 = this.evaluateFullHouse(hand2);
        if(fullHouse1 && !fullHouse2){
            return 1;
        } else if(fullHouse2 && !fullHouse1){
            return -1;
        }
        if(fullHouse1){
            return compareEqualHands(originalHand1, originalHand2);
        }
        //ThreeOfAKind
        boolean threeOfAKind1 = this.evaluateThree(hand1);
        boolean threeOfAKind2 = this.evaluateThree(hand2);
        if(threeOfAKind1 && !threeOfAKind2){
            return 1;
        } else if(threeOfAKind2 && !threeOfAKind1){
            return -1;
        }
        if(threeOfAKind1){
            return compareEqualHands(originalHand1, originalHand2);
        }
        //TwoPair
        boolean twoPair1 = this.evaluateTwoPair(hand1);
        boolean twoPair2 = this.evaluateTwoPair(hand2);
        if(twoPair1 && !twoPair2){
            return 1;
        } else if(twoPair2 && !twoPair1){
            return -1;
        }
        if(twoPair1){
            return compareEqualHands(originalHand1, originalHand2);
        }
        //OnePair
        boolean onePair1 = this.evaluateOnePair(hand1);
        boolean onePair2 = this.evaluateOnePair(hand2);
        if(onePair1 && !onePair2){
            return 1;
        } else if(onePair2 && !onePair1){
            return -1;
        }
        if(onePair1){
            return compareEqualHands(originalHand1, originalHand2);
        }
        //HighCard
        return compareEqualHands(originalHand1, originalHand2);
    }

    private String updateHandJoker(String hand) {
        Map<Character,Integer> handMap = new HashMap<>();
        char strongestCard = hand.charAt(0);
        char[] handArray = hand.toCharArray();
        //using a map to find the appearances of every card in the hand
        for(char card : handArray){
            handMap.merge(card, 1, Integer::sum);
        }
        //find the strongestCard that is not J
        int countStrongestCard = 0;
        for(Map.Entry<Character,Integer> entry : handMap.entrySet()){
            if(entry.getValue() > countStrongestCard && entry.getKey() != 'J'){
                countStrongestCard = entry.getValue();
                strongestCard = entry.getKey();
            }
        }
        // replace J with strongestCard
        for(int i=0; i < handArray.length; i++){
            if(handArray[i] == 'J' && strongestCard != 'J'){
                handArray[i] = strongestCard;
            }
        }
        return new String(handArray);
    }


    private int compareEqualHands(String hand1, String hand2) {
        for(int i=0; i < hand1.length(); i++){
            int card1Value = cardValues.get(hand1.charAt(i));
            int card2Value = cardValues.get(hand2.charAt(i));
            if(card1Value > card2Value){
                return 1;
            }
            if(card1Value < card2Value){
                return -1;
            }
        }
        System.out.println("Same value?");
        return 0;
    }

    private boolean evaluateFive(String hand) {
        int count = 0;
        char firstCard = hand.charAt(0);
        for(char card : hand.toCharArray()){
            if(card == firstCard){
                count++;
            }
        }

        return count == 5;
    }

    private boolean evaluateFour(String hand) {
        Map<Character, Integer> handMap = new HashMap<>();
        for(char card : hand.toCharArray()){
            handMap.merge(card, 1, Integer::sum);
        }
        for(Map.Entry<Character,Integer> distinctCard : handMap.entrySet()){
            if(distinctCard.getValue() == 4){
                return true;
            }
        }
        return false;
    }

    private boolean evaluateFullHouse(String hand) {
        char[] handCharArray = hand.toCharArray();
        Arrays.sort(handCharArray);
        boolean variant1 = (handCharArray[0] == handCharArray[1]) && (handCharArray[2] == handCharArray[3] && handCharArray[3] == handCharArray[4]);
        boolean variant2 = (handCharArray[0] == handCharArray[1] && handCharArray[1] == handCharArray[2]) && (handCharArray[3] == handCharArray[4]);
        return variant1 || variant2;
    }

    private boolean evaluateThree(String hand) {
        Map<Character, Integer> handMap = new HashMap<>();
        for(char card : hand.toCharArray()){
            handMap.merge(card, 1, Integer::sum);
        }
        for(Map.Entry<Character,Integer> distinctCard : handMap.entrySet()){
            if(distinctCard.getValue() == 3){
                return true;
            }
        }
        return false;
    }

    private boolean evaluateTwoPair(String hand) {
        Map<Character, Integer> handMap = new HashMap<>();
        for(char card : hand.toCharArray()){
            handMap.merge(card, 1, Integer::sum);
        }
        int countPairs = 0;
        for(Map.Entry<Character,Integer> distinctCard : handMap.entrySet()){
            if(distinctCard.getValue() == 2){
                countPairs++;
            }
        }
        return countPairs == 2;
    }

    private boolean evaluateOnePair(String hand) {
        Map<Character, Integer> handMap = new HashMap<>();
        for(char card : hand.toCharArray()){
            handMap.merge(card, 1, Integer::sum);
        }
        for(Map.Entry<Character,Integer> distinctCard : handMap.entrySet()){
            if(distinctCard.getValue() == 2){
                return true;
            }
        }
        return false;
    }

    @Override
    public int compare(String o1, String o2) {
        return compareHands(o1, o2);
    }
}
