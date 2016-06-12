# Chess-RMI v1.0

Chess RMI is written in java. 
With Chess RMI you can play online with your friends, and also you can watch someone elses game.
Chess Board can be found here: http://freesourcecode.net/javaprojects/15421/Standard-chess-game-in-java-with-2-player

Steps to Start the Game in Chess RMI v1.0: 

    Step 1: If you start ther server:
              In chess.java change 'IP' to your ip address and 'PORT' to whatever port you want and which is not used.
              
            If you are connecting to a server: 
              In chess.java change 'IP' and 'PORT' to the server you want to be connected.
    
    Step 2: -If you are using Eclipse or any other IDE, you can import the project and compile these Files all at once.
    
            -If you are using terminal(linux) or CMD(win) you can compile all these files, usign this command: 
                'javac Resurset/*.java' - This means that you should be in Chess-RMI directory first.
    
    Step 3: If you start ther server:
               Start the server using command 'java Resurset/startServer.java', if the IP and PORT is correct you should see 
               a Server starting Message.
            If you are connecting to a server: 
               this Step is not needed
    
    Step 4: Start the Client Chat using command 'java Resurset/chess.java'; Now choose what you want to play black or white
            by pressing Start new Game, 0 means you start the game, and 1 means that the other client will start the game,
            or if you want to be a spectator choose a different word or number or something else
  
This version is just for testing java RMI library.
Have fun testing this project, for any problems or anything else please feel free to cantact me.
Author: Mentor Bibaj
