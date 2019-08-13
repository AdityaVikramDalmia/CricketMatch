package cricket;

import java.util.concurrent.ThreadLocalRandom;

public class Player {

    enum PlayerType{
        BATSMAN(4,"BATSMAN"),BOWLER(5,"BOWLER"),ALLROUNDER(4,"ALLROUNDER"),WICKETKEEPER(5,"WICKETKEEPER");
        private int probOfOut;
        private String playerTypeString;
        PlayerType(int probOfOut,String name){
            this.probOfOut=probOfOut;
            this.playerTypeString=name;
        }
        /*
        getIfout gets a random number between 1 to 6 and if it less than the value of probOfOut then it is a wicket or else it is not;

         */
        boolean getIfOut(){
            int randomNum = ThreadLocalRandom.current().nextInt(1, 6 + 1);
            if(randomNum>=probOfOut){
                return false;
            }
            return true;
        }
        @Override public String toString(){
            return this.playerTypeString;
        }
    }

    public static final int RUN_SPECIFYING_IF_OUT=7;
    public static final int MAX_OVERS_PER_BOWLER=5;

    @Override
    public String toString() {
        return playerName;
    }

    private PlayerType playerType;
    private int runScored=0;
    private int ballPlayed=0;

    private int oversBowled=0;
    private int ballsBowledInCurrOver=0;

    private int wicketsTaken=0;
    private String playerName;
    private int countScore0=0;
    private int countScore1=0;
    private int countScore2=0;
    private int countScore3=0;
    private int countScore4=0;
    private int countScore5=0;
    private int countScore6=0;

    private boolean canBowlMore=true;
    private boolean isOut=false;
    private int runsGiven=0;


    public Player(String name, PlayerType playerType){
        this.playerName=name;
        this.playerType=playerType;
    }

    public void runsScoredThisBall(int runs){
        switch (runs){
            case 0:
                score0();
                break;
            case 1:
                score1();
            case 2:
                score2();
            case 3:
                score3();
            case 4:
                score4();
            case 5:
                score5();
            case 6:
                score6();
            case Player.RUN_SPECIFYING_IF_OUT:
                playerIsOut();
        }
    }

    public void runsGivenThisBall(int runs){
        if(runs==RUN_SPECIFYING_IF_OUT){
            this.wicketsTaken+=1;
        }
        else{
            this.runsGiven+=runs;
        }

        if(ballsBowledInCurrOver==5){
            ballsBowledInCurrOver=0;
            oversBowled++;

        }
        else{
            ballsBowledInCurrOver++;
        }
        if(oversBowled==MAX_OVERS_PER_BOWLER){
            canBowlMore=false;
        }
    }

    public Player resetPlayer(){
        return new Player(this.playerName,this.playerType);
    }

    private void score0(){
        this.ballPlayed++;
        this.countScore0++;
    }

    private void score1(){
        this.runScored+=1;
        this.countScore1++;
        this.ballPlayed++;
    }

    private void score2(){
        this.runScored+=2;
        this.ballPlayed++;
        this.countScore2++;
    }

    private void score3(){
        this.ballPlayed++;
        this.runScored+=3;
        this.countScore3++;
    }
    private void score4(){
        this.ballPlayed++;
        this.runScored+=4;
        this.countScore4++;
    }
    private void score5(){
        this.ballPlayed++;
        this.runScored+=5;
        this.countScore5++;
    }
    public void score6(){
        this.ballPlayed++;
        this.runScored+=6;
        this.countScore6++;
    }

    private void playerIsOut(){
        this.ballPlayed++;
        this.isOut=true;
    }
    public PlayerType getPlayerType() {
        return playerType;
    }

    public boolean isCanBowlMore() { return canBowlMore; }

    public int getBallPlayed() {
        return ballPlayed;
    }

    public void setBallPlayed(int ballPlayed) {
        this.ballPlayed = ballPlayed;
    }
}
