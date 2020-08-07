/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mainProcess;

import clientViews.PlayerView;
import commands.ChangeGhostsSpeedCommand;
import commands.CreateFruitCommand;
import commands.CreateGhostCommand;
import commands.CreatePillCommand;
import connector.ConnectionManager;
import data.ObserverPackage;
import java.util.ArrayList;
import java.util.Arrays;
import sprites.Ghost;
import sprites.Pacman;

/**
 *
 * @author Jon Gs
 */
public class ClientPlayer {
    public String inputBuffer = "";
    public String outputBuffer = "";
    public Boolean isFinished = false;
    private PlayerView view = null;
    private ConnectionManager connector = null;
    
    public ClientPlayer(String serverIP, int serverPort, PlayerView view){
        this.view = view;
        this.connector = new ConnectionManager(serverIP, serverPort, this);
        
        Thread connectorThread = new Thread(this.connector);
        connectorThread.start();
    }
    
    public void updateInputBuffer(){
        ObserverPackage observerPackage = view.getObserverPackage();
        
        inputBuffer += "player,update,";
        
        Pacman pacman = observerPackage.pacman;
        
        inputBuffer += pacman.getPos().x.toString() + ',' +
                pacman.getPos().y.toString() + ',' +
                pacman.getAnimationNumber().toString() + ',' +
                observerPackage.score.toString() + ',';
        
        ArrayList<Ghost> ghosts = observerPackage.ghosts;

        for(Ghost ghost: ghosts){
            Integer isEdible = ( ghost.isIsEdible() ? 1 : 0 );
            inputBuffer += ghost.getPos().x.toString() + ','
                    + ghost.getPos().y.toString() + ',' + isEdible.toString() +
                    ',' + ghost.getColor().toString() + ',';
        }
        
        if(ghosts.size() < 4){
            for(Integer i = (ghosts.size()); i < 4; i++){
                inputBuffer += "-1" + ','
                    + "-1" + ',' + '0' + ',' +
                    '0'+',';
            }
        }
        
        inputBuffer += String.valueOf(observerPackage.pillActive? 1 : 0) + ',';
        
        inputBuffer += observerPackage.level.toString() + ',';
        
        inputBuffer += observerPackage.score.toString() + ',';
        
        inputBuffer += observerPackage.gameState.toString();
        
    }
    
    public void processOutput(){
        
        String[] messageReceived = outputBuffer.split(",");
        
        if(messageReceived[0].equals("events")){
            processCommand(Arrays.copyOfRange(messageReceived, 1, messageReceived.length));
        }
        
        outputBuffer = "";
        
    }
    
    
    private void processCommand(String[] messageReceived){
        
        if(messageReceived.length == 0){
            outputBuffer = "";
            return;
        }
        
        switch(messageReceived[0]){
            case "createGhost":
                Integer ghost = Integer.parseInt(messageReceived[1]);
            
                CreateGhostCommand createGhostCommand = new CreateGhostCommand(view, ghost);

                createGhostCommand.execute();

                processCommand(Arrays.copyOfRange(messageReceived, 2, messageReceived.length));
                
                break;
            case "createFruit":
                Integer fruitScore = Integer.parseInt(messageReceived[1]);
                Integer fruitX = Integer.parseInt(messageReceived[2]);
                Integer fruitY = Integer.parseInt(messageReceived[3]);
                
                CreateFruitCommand createFruitCommand = new CreateFruitCommand(view, fruitScore, fruitX, fruitY);
                
                createFruitCommand.execute();
                
                processCommand(Arrays.copyOfRange(messageReceived, 4, messageReceived.length));
                
                break;
                
            case "createPill":
                
                Integer pillX = Integer.parseInt(messageReceived[1]);
                Integer pillY = Integer.parseInt(messageReceived[2]);
                
                CreatePillCommand createPillCommand = new CreatePillCommand(view, pillX, pillY);
                
                createPillCommand.execute();
                
                processCommand(Arrays.copyOfRange(messageReceived, 3, messageReceived.length));
                
                break;
                
            case "changeSpeed":
                
                Integer speed = Integer.parseInt(messageReceived[1]);
                
                ChangeGhostsSpeedCommand changeGhostsSpeedCommand = new ChangeGhostsSpeedCommand(view, speed);
                
                changeGhostsSpeedCommand.execute();
                
                processCommand(Arrays.copyOfRange(messageReceived, 2, messageReceived.length));
                
                break;
        }
        
    }
    
    
}