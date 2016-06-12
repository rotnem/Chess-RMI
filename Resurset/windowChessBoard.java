package Resurset;
import java.awt.*;
import java.awt.event.*;
import java.rmi.Naming;

//import javax.management.timer.Timer;
import javax.swing.JOptionPane;
import javax.swing.JDialog;

import javax.swing.Timer;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import ServerSide.ChessInterface;

public class windowChessBoard extends objChessBoard implements MouseListener, MouseMotionListener
{
	
	private final int refreshRate = 5; //Amount of pixels moved before screen is refreshed
	
	private Image[][] imgPlayer = new Image[2][6];
	private String[] strPlayerName = {"Player 1", "Player 2"};
	private String strStatusMsg = "";
	private objCellMatrix cellMatrix = new objCellMatrix();
	private int currentPlayer = 1, startRow = 0, startColumn = 0, pieceBeingDragged = 0;
	private int startingX = 0, startingY = 0, currentX = 0, currentY = 0, refreshCounter = 0;
	private boolean firstTime = true, hasWon = false, isDragging = false;
	
	private objPawn pawnObject = new objPawn();
	private objRock rockObject = new objRock();
	private objKnight knightObject = new objKnight();
	private objBishop bishopObject = new objBishop();
	private objQueen queenObject = new objQueen();
	private objKing kingObject = new objKing();
	
	/*Te shtuara - Keto vlera do ti dergoje Klienti 
	 *Serverit per levizje */
	public int tempSX = 0; //vlere e mousePressed(x)
	public int tempSY = 0; //vlere e mousePressed(y)
	public int tempDesColumn = 0; //vlere e mouseReleased(desColumn)
	public int tempDesRow = 0; //vlere e  mouseReleased(desRow)
	public int tempCurrentY = 0; //vlere e mouseRelease(currentY)
	public int tempCurrentX = 0; //vlere e mouseReleased(currentX)
	public String myType = "none";
	public windowChessBoard ()
	{
		
		this.addMouseListener(this);
		this.addMouseMotionListener(this);
		
	}
	
	private String getPlayerMsg ()
	{
		
		if (hasWon)
		{
			return "Congrats " + strPlayerName[currentPlayer - 1] + ", you are the winner!";
		}
		else if (firstTime)
		{
			return "" + strPlayerName[0] + " you are black, " + strPlayerName[1] + " you are white. Press new game to start";
		}
		else
		{
			return "" + strPlayerName[currentPlayer - 1] + " move";
		}
		
	}		 
	
	private void resetBoard ()
	{
		
		hasWon = false;
		currentPlayer = 1;
		strStatusMsg = getPlayerMsg();
		cellMatrix.resetMatrix();
		repaint();
		
	}
	
	public void setupImages (Image[] imgRed, Image[] imgBlue)
	{
		
		imgPlayer[0] = imgRed;
		imgPlayer[1] = imgBlue;
		resetBoard();
		
	}
	
	public void setNames (String strPlayer1Name, String strPlayer2Name)
	{
		
		strPlayerName[0] = strPlayer1Name;
		strPlayerName[1] = strPlayer2Name;
		strStatusMsg = getPlayerMsg();
		repaint();
		
	}
	
	protected void drawExtra (Graphics g)
	{
		
		for (int i = 0; i < vecPaintInstructions.size(); i++)
		{
			
			currentInstruction = (objPaintInstruction)vecPaintInstructions.elementAt(i);
			int paintStartRow = currentInstruction.getStartRow();
			int paintStartColumn = currentInstruction.getStartColumn();
			int rowCells = currentInstruction.getRowCells();
			int columnCells = currentInstruction.getColumnCells();
			
			for (int row = paintStartRow; row < (paintStartRow + rowCells); row++)
			{
				
				for (int column = paintStartColumn; column < (paintStartColumn + columnCells); column++)
				{
					
					int playerCell = cellMatrix.getPlayerCell(row, column);
					int pieceCell = cellMatrix.getPieceCell(row, column);
				
					if (playerCell != 0)
					{
						
						try
						{
							g.drawImage(imgPlayer[playerCell - 1][pieceCell], ((column + 1) * 50), ((row + 1) * 50), this);
						}
						catch (ArrayIndexOutOfBoundsException e)
						{
						}
						
					}
					
				}
				
			}
			
		}
		
		if (isDragging)
		{
			g.drawImage(imgPlayer[currentPlayer - 1][pieceBeingDragged], (currentX - 25), (currentY - 25), this);
		}		

		g.setColor(new Color(0,0,0));
		g.drawString(strStatusMsg, 50, 475);
		
		vecPaintInstructions.clear(); //clear all paint instructions
	}
	
	public void newGame ()
	{
		
		firstTime = false;
		resetBoard();
		
		myType = JOptionPane.showInputDialog("Sheno kush fillon(0 une, 1 shoku)");
		if(!myType.equals("0")){
			startTimer();
		}
		
		if(myType.equals("0") || myType.equals("1")){
			try {
				ChessInterface stubChess= (ChessInterface)Naming.lookup("rmi://"
												+chess.IP+":"+chess.PORT+"/Chess");
		
				stubChess.tellWhoHasToPlay(myType);
			} catch (Exception re) {
				re.printStackTrace();
			}
		}
		
		//startMovingMethodTest();// Kjo metode eshte per Testim
		//moveServerPiecesl();
	}
	
	public int[] vlerat = {0,0,0,0};
	public int[] vleratTemp = vlerat;
	
	Timer timer;
	public void startTimer(){
		ActionListener actionListener = new ActionListener(){
			public void actionPerformed(ActionEvent actionEvent){
				//bej Diqka
				System.out.println("hh");
				try {
					ChessInterface stubChess= (ChessInterface)Naming.lookup("rmi://"
													+chess.IP+":"+chess.PORT+"/Chess");
			
					vlerat = stubChess.getMoveLocation();
					System.out.println("myType:" + myType +" stub:"+ stubChess.getTellWhoHasToPlay());
					if(!myType.equals(stubChess.getTellWhoHasToPlay())){
					  moveServerPieces(vlerat[0], vlerat[1], vlerat[2], vlerat[3]);
					  System.out.println("inhere");
					 if(myType.equals("0") || myType.equals("1")){
						  timer.stop();
					 }
					}
				} catch (Exception re) {
					re.printStackTrace();
				}
			}
		};
		timer = new Timer(500, actionListener);
		timer.start();
		
	}
	
	
	
	public  void moveServerPieces(int tSX, int tSY, int tDesColumn, int tDesRow) {
		//firstTime = false;
		System.out.println("hasWon:" + hasWon + " firstTime:" + firstTime);
		if (!hasWon && !firstTime){	
			int x = tSX;
			int y = tSY;
			
			System.out.println("Move Server Pieces! \nStartX:"+x+" StartY:"+y);
			if ((x > 60 && x < 430) && (y > 60 && y < 430)) //in the correct bounds
			{
			
				startRow = findWhichTileSelected(y);
				startColumn = findWhichTileSelected(x);
				System.out.println("startRow: " + startRow + " startColumn:"+ startColumn);
						
				if (cellMatrix.getPlayerCell(startRow, startColumn) == currentPlayer)
				{
					System.out.println("inside");
					pieceBeingDragged = cellMatrix.getPieceCell(startRow, startColumn);
					cellMatrix.setPieceCell(startRow, startColumn, 6);
					cellMatrix.setPlayerCell(startRow, startColumn, 0);
					isDragging = true;
					
				}
				else
				{
					isDragging = false;
				}
			
			}
		}
		
		if (isDragging) //Vlera e dragging eshte true
		{	
			isDragging = false;
			
			int desRow = tDesRow;
			int desColumn = tDesColumn;
			System.out.println("DestX:"+desRow+" DestY:"+desColumn);
			checkMove(desRow, desColumn);	
			repaint();
			
		}
	}
	
	
	
	private void checkMove (int desRow, int desColumn)
	{
		
		boolean legalMove = false;
		
		if (cellMatrix.getPlayerCell(desRow,desColumn) == currentPlayer)
		{
			strStatusMsg = "Can not move onto a piece that is yours";
		}
		else
		{
			
			switch (pieceBeingDragged)
			{
				
				case 0: legalMove = pawnObject.legalMove(startRow, startColumn, desRow, desColumn, cellMatrix.getPlayerMatrix(), currentPlayer);
						break;
				case 1: legalMove = rockObject.legalMove(startRow, startColumn, desRow, desColumn, cellMatrix.getPlayerMatrix());
						break;
				case 2: legalMove = knightObject.legalMove(startRow, startColumn, desRow, desColumn, cellMatrix.getPlayerMatrix());
						break;
				case 3: legalMove = bishopObject.legalMove(startRow, startColumn, desRow, desColumn, cellMatrix.getPlayerMatrix());
						break;
				case 4: legalMove = queenObject.legalMove(startRow, startColumn, desRow, desColumn, cellMatrix.getPlayerMatrix());
						break;
				case 5: legalMove = kingObject.legalMove(startRow, startColumn, desRow, desColumn, cellMatrix.getPlayerMatrix());
						break;
						
			}
			
		}				
		
		if (legalMove)
		{
			int newDesRow = 0;
			int newDesColumn = 0;
			switch (pieceBeingDragged)
			{
				
				case 0: newDesRow = pawnObject.getDesRow();
						newDesColumn = pawnObject.getDesColumn();
						break;
				case 1: newDesRow = rockObject.getDesRow();
						newDesColumn = rockObject.getDesColumn();
						break;
				case 2: newDesRow = knightObject.getDesRow();
						newDesColumn = knightObject.getDesColumn();
						break;
				case 3: newDesRow = bishopObject.getDesRow();
						newDesColumn = bishopObject.getDesColumn();
						break;
				case 4: newDesRow = queenObject.getDesRow();
						newDesColumn = queenObject.getDesColumn();
						break;
				case 5: newDesRow = kingObject.getDesRow();
						newDesColumn = kingObject.getDesColumn();
						break;
						
			}
			
			cellMatrix.setPlayerCell(newDesRow, newDesColumn, currentPlayer);
			
			if (pieceBeingDragged == 0 && (newDesRow == 0 || newDesRow == 7)) //If pawn has got to the end row
			{
				
				boolean canPass = false;
				int newPiece = 2;
				String strNewPiece = "Rock";
				String[] strPieces = {"Rock","Knight","Bishop","Queen"};
				JOptionPane digBox = new JOptionPane("Choose the piece to change your pawn into", JOptionPane.QUESTION_MESSAGE, JOptionPane.OK_CANCEL_OPTION, null, strPieces, "Rock");
				JDialog dig = digBox.createDialog(null, "pawn at end of board");
				
				do
				{					
					
					dig.setVisible(true);					
					
					try
					{
						
						strNewPiece = digBox.getValue().toString();
						
						for (int i = 0; i < strPieces.length; i++)
						{
						
							if (strNewPiece.equalsIgnoreCase(strPieces[i]))
							{

								canPass = true;
								newPiece = i + 1;
								
							}
							
						}
						
					}
					catch (NullPointerException e)
					{
						canPass = false;							
					}												

				}
				while (canPass == false);

				cellMatrix.setPieceCell(newDesRow, newDesColumn, newPiece);
				
			}
			else
			{
				cellMatrix.setPieceCell(newDesRow, newDesColumn, pieceBeingDragged);
			}			
							
			if (cellMatrix.checkWinner(currentPlayer))
			{
				
				hasWon = true;
				strStatusMsg = getPlayerMsg();					
				
			}
			else
			{					
			
				if (currentPlayer == 1)
				{						
					currentPlayer = 2;						
				}
				else
				{						
					currentPlayer = 1;						
				}
				
				strStatusMsg = getPlayerMsg();
				
			}			
				
		}
		else
		{
			
			switch (pieceBeingDragged)
			{
				
				case 0: strStatusMsg = pawnObject.getErrorMsg();
						break;
				case 1: strStatusMsg = rockObject.getErrorMsg();
						break;
				case 2: strStatusMsg = knightObject.getErrorMsg();
						break;
				case 3: strStatusMsg = bishopObject.getErrorMsg();
						break;
				case 4: strStatusMsg = queenObject.getErrorMsg();
						break;
				case 5: strStatusMsg = kingObject.getErrorMsg();
						break;
						
			}
				
			unsucessfullDrag(desRow, desColumn);
			
		}
			
	}
	
	private void unsucessfullDrag (int desRow, int desColumn)
	{
		cellMatrix.setPieceCell(startRow, startColumn, pieceBeingDragged);
		cellMatrix.setPlayerCell(startRow, startColumn, currentPlayer);
		
	}
	
	private void updatePaintInstructions (int desRow, int desColumn)
	{
		
		currentInstruction = new objPaintInstruction(startRow, startColumn, 1);
		vecPaintInstructions.addElement(currentInstruction);
			
		currentInstruction = new objPaintInstruction(desRow, desColumn);
		vecPaintInstructions.addElement(currentInstruction);
		
	}
	
	public void mouseClicked (MouseEvent e)
	{
	}
	
	public void mouseEntered (MouseEvent e)
	{
	}
	
	public void mouseExited (MouseEvent e)
	{
		mouseReleased(e);
	}
	
	public void mousePressed (MouseEvent e)
	{
		
		if ((!hasWon && !firstTime) &&(myType.equals("0") || myType.equals("1")))
		{			
		
			int x = e.getX();
			int y = e.getY();
			
			//Vlere e shtuar
			tempSX = x;
			tempSY = y;
			
			System.out.println("StartX:"+x+" StartY:"+y);
			if ((x > 60 && x < 430) && (y > 60 && y < 430)) //in the correct bounds
			{
			
				startRow = findWhichTileSelected(y);
				startColumn = findWhichTileSelected(x);
						
				if (cellMatrix.getPlayerCell(startRow, startColumn) == currentPlayer)
				{
					
					pieceBeingDragged = cellMatrix.getPieceCell(startRow, startColumn);
					cellMatrix.setPieceCell(startRow, startColumn, 6);
					cellMatrix.setPlayerCell(startRow, startColumn, 0);
					isDragging = true;
					
				}
				else
				{
					/*Nese nuk e keni rendin dhe pretendoni te levizni*/
					isDragging = false;
				}
			
			}
			
		}
		
	}
	
	public void mouseReleased (MouseEvent e)
	{
		
		if (isDragging) //Vlera e dragging eshte true
		{	
			isDragging = false;
			
			int desRow = findWhichTileSelected(currentY);
			int desColumn = findWhichTileSelected(currentX);
			
			//Vlere e shtuar
			tempDesColumn= desColumn;
			tempDesRow = desRow;
			tempCurrentY = currentY;
			tempCurrentX = currentX;
			if(myType.equals("0") || myType.equals("1")){
				checkMove(desRow, desColumn);
				repaint();
				//System.out.println("mouseReleased: desRow:" +desRow+" desColumn:" +desColumn );
				try {
					ChessInterface stubChess= (ChessInterface)Naming.lookup("rmi://"
													+chess.IP+":"+chess.PORT+"/Chess");
			
					stubChess.sendMoveAndReciveServer(tempSX,tempSY,tempDesColumn, tempDesRow);
					stubChess.tellWhoHasToPlay(myType);
				} catch (Exception re) {
					System.out.println("Dergimi i levizjes nuk mund te behet!");
				}
				startTimer();
			}
		}
		
	}
	
	public void mouseDragged (MouseEvent e)
	{
		
		if (isDragging)
		{
			
			int x = e.getX();
			int y = e.getY();
			
			if ((x > 60 && x < 430) && (y > 60 && y < 430)) //If in the correct bounds
			{
			
				if (refreshCounter >= refreshRate)
				{
								
					currentX = x;
					currentY = y;
					refreshCounter = 0;
					int desRow = findWhichTileSelected(currentY);
					int desColumn = findWhichTileSelected(currentX);
					
					updatePaintInstructions(desRow, desColumn);
					repaint();
					
				}
				else
				{
					refreshCounter++;
				}
			
			}
			
		}
		
	}
	
	public void mouseMoved (MouseEvent e)
	{
	}
	
	public void gotFocus ()
	{
		repaint();		
	}
				
}