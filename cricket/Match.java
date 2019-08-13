package cricket;

import repo.CricketDbConnector;
import repo.SqlConnectorCricket;

import java.util.concurrent.ThreadLocalRandom;

public class Match {
    private Team team1;
    private Team team2;
    private Player currBowler;
    private int overLimit;
    private Team matchWinner=null;
    private Team tossWinner;
    private Team battingFirst;
    private int targetSet=-1;
    private Team battingSecond;
    private boolean firstInningComplete=false;
    private boolean secondInningComplete=false;
    private CricketDbConnector connection;
    private int matchId;
    private int seriesId;



    public Match(Team team1,Team team2,int overLimit,int seriesId,CricketDbConnector connection){
        this.team1=team1;
        this.team2=team2;
        this.overLimit=overLimit;
        this.seriesId=seriesId;
        this.connection=connection;
    }
    public Team getTossWinner() {
        return tossWinner;
    }

    public void conductToss(){
        int randomNum = ThreadLocalRandom.current().nextInt(0, 1 + 1);
        int randomNum2 = ThreadLocalRandom.current().nextInt(0, 1 + 1);
        if(randomNum==0){
            tossWinner=team1;
        }
        else{
            tossWinner=team2;
        }
        if(randomNum2==0){
            battingFirst=team1;
            battingSecond=team2;
        }
        else{
            battingFirst=team2;
            battingSecond=team1;
        }
        int matchIdGenerated=connection.updateMatchTossWinner(seriesId,tossWinner,battingFirst,team1,team2,overLimit);
        this.matchId=matchIdGenerated;
    }


    public void startOver(){
        if(!firstInningComplete || !secondInningComplete){

            Team currTeamBatting;
            Team currTeamBowling;

            if(!firstInningComplete){
                currTeamBatting=battingFirst;
                currTeamBowling=battingSecond;
            }
            else{
                currTeamBatting=battingSecond;
                currTeamBowling=battingFirst;
            }


            Player currBowler=getBowler(currTeamBowling);
            for (int i = 0; i <6 ; i++) {
                if(currTeamBatting.getWicketsLost()==10){
                    return;
                }
                bowlBall(currBowler,(currTeamBowling.getOversBowled()*6)+currTeamBowling.getBallsInCurrOverBowled()+1,currTeamBatting,currTeamBowling);
                if((currTeamBatting.getWicketsLost()==10 && !firstInningComplete) || (currTeamBowling.getOversBowled()==overLimit && !firstInningComplete)){

                    targetSet=currTeamBatting.getRunsScored()+1;
                    firstInningComplete=true;
                    connection.updateMatchTargetSet(matchId,targetSet);

                    return;
                }

                if(firstInningComplete && targetSet<=currTeamBatting.getRunsScored()){
                    secondInningComplete=true;
                    matchWinner=currTeamBatting;
                }
            }
        }
    }

    private void bowlBall(Player currBowler, int ballNum,Team currTeamBatting,Team currTeamBowling) {
        int currRun;
        boolean isFinallyOut;
        try{
            Thread.sleep(100);
        }
        catch(Exception e){
            e.printStackTrace();
        }
        while(true){
            currRun=getRandomRuns();
            if(currRun==Player.RUN_SPECIFYING_IF_OUT){
                int currBatsmanIndex=currTeamBatting.getBatsmanOnStrike();
                isFinallyOut=currTeamBatting.teamList.get(currBatsmanIndex).getPlayerType().getIfOut();
                if(isFinallyOut){ break; }
            }
            else{ break; }
        }

        int IndexBatsmanOnStrike=currTeamBatting.getBatsmanOnStrike();
        int IndexotherBatsman=currTeamBatting.getBatsmanOnStrike()==(currTeamBatting.getCurrBatsman1())?currTeamBatting.getCurrBatsman2():currTeamBatting.getCurrBatsman1();
        connection.updatePlayerDataBatsman(ballNum,matchId,currRun,Player.RUN_SPECIFYING_IF_OUT,currTeamBatting.teamList.get(currTeamBatting.getBatsmanOnStrike()));
        connection.updatePlayerDataBowler(ballNum,matchId,currRun,Player.RUN_SPECIFYING_IF_OUT,currBowler);
        connection.updateBallData(ballNum,matchId,currTeamBatting.teamList.get(IndexBatsmanOnStrike),currTeamBatting.teamList.get(IndexotherBatsman),currRun,firstInningComplete,currBowler,currTeamBatting.getRunsScored(),currTeamBatting.getWicketsLost(),Player.RUN_SPECIFYING_IF_OUT);
        currTeamBowling.updateRunsGiven(currRun,currBowler);
        currTeamBatting.updateRunsScored(currRun);
    }

    private Player getBowler(Team currTeamBowling) {
        Player bowlerToReturn;
        while(true){
            int randIndex=ThreadLocalRandom.current().nextInt(6,10+1);
            Player selectedBowler=currTeamBowling.teamList.get(randIndex);
            if(selectedBowler.isCanBowlMore() ){
                return currTeamBowling.teamList.get(randIndex);
            }
        }
    }

    public int getRandomRuns(){
        return ThreadLocalRandom.current().nextInt(0,7+1);
    }

    public void startMatch(){
        conductToss();
        for (int i = 0; i < overLimit; i++) {
            if(!firstInningComplete){

                startOver();
            }
        }
        for (int i = 0; i <overLimit ; i++) {
            if(!secondInningComplete){
                startOver();
            }
        }
        if(matchWinner==null){
        matchWinner=battingFirst;
        }
        System.out.println(matchWinner+" is the winner");
        connection.updateMatchWinner(matchId,matchWinner,matchWinner==battingFirst?battingFirst.getRunsScored()-battingSecond.getRunsScored():-1,matchWinner==battingSecond?10-battingSecond.getWicketsLost():-1);
        connection.updateSeriesData(seriesId,matchWinner,team1,team2);
    }
}
