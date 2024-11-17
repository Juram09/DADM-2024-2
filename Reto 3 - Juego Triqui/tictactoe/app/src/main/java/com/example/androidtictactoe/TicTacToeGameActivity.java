package com.example.androidtictactoe;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Button;

import org.w3c.dom.Text;

public class TicTacToeGameActivity extends Activity {
    private TicTacToeGame mGame;
    private ImageView[] mBoardButtons;
    // Various text displayed
    private TextView mInfoTextView;
    private Button mButtonPlayAgain;
    private TextView mInfoDifficultyTextView;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        // Buttons making up the board
        mBoardButtons = new ImageView[TicTacToeGame.BOARD_SIZE];
        mBoardButtons[0] = (android.widget.ImageView) findViewById(R.id.one);
        mBoardButtons[1] = (ImageView) findViewById(R.id.two);
        mBoardButtons[2] = (ImageView) findViewById(R.id.three);
        mBoardButtons[3] = (ImageView) findViewById(R.id.four);
        mBoardButtons[4] = (ImageView) findViewById(R.id.five);
        mBoardButtons[5] = (ImageView) findViewById(R.id.six);
        mBoardButtons[6] = (ImageView) findViewById(R.id.seven);
        mBoardButtons[7] = (ImageView) findViewById(R.id.eight);
        mBoardButtons[8] = (ImageView) findViewById(R.id.nine);
        mInfoTextView = (TextView) findViewById(R.id.information);
        mButtonPlayAgain = (Button) findViewById(R.id.playAgainButton);
        mInfoDifficultyTextView = (TextView) findViewById(R.id.difficulty);
        mGame = new TicTacToeGame();
        startNewGame();
    }

    // Set up the game board.
    private void startNewGame() {
        clearBoard();
        // Human goes first
        mInfoTextView.setText("Player's turn");
        // End of startNewGame
    }

    // Clear the board of all X's and O's by setting all spots to OPEN_SPOT.
    public void clearBoard() {
        // Reset all buttons
        for (int i = 0; i < mBoardButtons.length; i++) {
            mBoardButtons[i].setImageResource(R.drawable.baseline_square_24);
            mBoardButtons[i].setEnabled(true);
            mBoardButtons[i].setOnClickListener(new ButtonClickListener(i));
        }
    }

    private class ButtonClickListener implements View.OnClickListener {
        int location;
        public ButtonClickListener(int location) {
            this.location = location;
        }
        public void onClick(View view) {
            if (mBoardButtons[location].isEnabled()) {
                mGame.setMove(TicTacToeGame.HUMAN_PLAYER, location);
                int winner = mGame.checkForWinner();
                if (winner == 0) {
                    mInfoTextView.setText("Android's turn");
                    int move = mGame.getComputerMove();
                    mGame.setMove(TicTacToeGame.COMPUTER_PLAYER, move);
                    winner = mGame.checkForWinner();
                }
                if (winner == 0)
                    mInfoTextView.setText("Player's turn");
                else if (winner == 1)
                    mInfoTextView.setText("It's a tie!");
                else if (winner == 2)
                    mInfoTextView.setText("You won!");
                else
                    mInfoTextView.setText("Android won!");
            }
        }
    }

    private void setMove(char player, int location) {
        mGame.setMove(player, location);
        mBoardButtons[location].setEnabled(false);
        if (player == TicTacToeGame.HUMAN_PLAYER)
            mBoardButtons[location].setImageResource(R.drawable.x);
        else
            mBoardButtons[location].setImageResource(R.drawable.circle);
    }
}
