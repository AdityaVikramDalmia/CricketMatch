package cricket;

import java.util.ArrayList;

public class Team {


    ArrayList<Player> teamList=new ArrayList<>(11);
    private int batsmanOnStrike=0;
    private int currBatsman1=0;
    private int currBatsman2=1;

    private int nextBatsman=2;
    private int runsScored=0;
    private int wicketsLost=0;
    private int overBatted=0;
    private int ballsInCurrOverBatted=0;
    private int playersAdded=0;
    private String teamName;
    private int oversBowled=0;
    private int ballsInCurrOverBowled=0;
    private int wicketsTaken=0;
    private int runsGiven=0;
    public Team(String name){
        this.teamName=name;
    }


    public void resetTeam(){
        for(Player p:teamList){
           p= p.resetPlayer();
        }
        batsmanOnStrike=0;
        currBatsman1=0;
        currBatsman2=1;
        nextBatsman=2;
        runsScored=0;
        wicketsLost=0;
        ballsInCurrOverBatted=0;
        playersAdded=teamList.size();
        overBatted=0;
        oversBowled=0;
        ballsInCurrOverBowled=0;
        wicketsTaken=0;
        runsGiven=0;
    }

    public int getCurrBatsman1() {
        return currBatsman1;
    }


    public int getCurrBatsman2() {
        return currBatsman2;
    }

    public int getBallsInCurrOverBowled() {
        return ballsInCurrOverBowled;
    }


    public boolean addPlayer(Player player){
        if(playersAdded==11){
            return false;
        }
        teamList.add(player);
        playersAdded++;
        return true;
    }

    public void switchStrike(){
        if(batsmanOnStrike==currBatsman1){
            batsmanOnStrike=currBatsman2;
        }
        else{
            batsmanOnStrike=currBatsman1;
        }
    }

    public void updateRunsGiven(int currRun, Player currBowler) {
        currBowler.runsGivenThisBall(currRun);
        if(currRun==Player.RUN_SPECIFYING_IF_OUT){
            wicketsTaken++;
        }
        else{
            runsGiven+=currRun;
        }
        incrementBowlinggBalls();
    }

    private void replaceBatsman() {
        if(batsmanOnStrike==currBatsman1){
            batsmanOnStrike=nextBatsman;
            nextBatsman++;
            currBatsman1=batsmanOnStrike;
            switchStrike();
        }
        else{
            batsmanOnStrike=nextBatsman;
            nextBatsman++;
            currBatsman2=batsmanOnStrike;
            switchStrike();
        }
    }


    public void updateRunsScored(int currRun) {
        Player currBatsman=teamList.get(batsmanOnStrike);
        currBatsman.runsScoredThisBall(currRun);
        if(currRun==Player.RUN_SPECIFYING_IF_OUT){
            replaceBatsman();
            wicketsLost++;

        }
        else{
            runsScored+=currRun;
            if(currRun==1 || currRun==3 || currRun==5){
                switchStrike();
            }
        }
        incrementBattedBalls();
    }

    public void incrementBattedBalls() {
        if(ballsInCurrOverBatted==5){
            ballsInCurrOverBatted=0;
            overBatted++;
        }
        else{ ballsInCurrOverBatted++; }
    }

    public void incrementBowlinggBalls(){
        if(ballsInCurrOverBowled==5){
            ballsInCurrOverBowled=0;
            oversBowled++;
        }
        else{ ballsInCurrOverBowled++; }
    }

    /*
    ALL GETTERS AND SETTERS ARE KEPT AT BOTTOM
     */

    public int getBatsmanOnStrike() {
        return batsmanOnStrike;
    }
    public int getRunsScored() { return runsScored; }

    public int getWicketsLost() { return wicketsLost; }


    public int getOversBowled() { return oversBowled; }

    @Override public String toString(){
        return teamName;
    }



}
