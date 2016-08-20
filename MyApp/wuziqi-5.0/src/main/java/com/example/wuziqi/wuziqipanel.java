package com.example.wuziqi;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Created by xiong on 2016/8/17.
 */
public class wuziqipanel extends View
{
    private boolean[][][] wins = new boolean[15][15][572];  //记录全部赢法的排列方式
    private int[][] chessBoard = new int[15][15];           //记录棋盘上的棋子
    private int[] myWin = new int[572];                     //记录玩家各个赢法实现了几个子
    private int[] computerWin = new int[572];               //记录电脑各个赢法实现了几个子
    private int count;                                      //总共有多少种赢法
    private Point point;//坐标取点
    private boolean mIsGameOver = false;                    //表示游戏是否结束

    private int mPanelWidth;                                //棋盘宽度
    private float mLineHeight;                              //棋盘高度
    private int MAX_LINE = 15;                               //棋盘行列数

    private boolean mIsMe = true;                           //轮到谁落子
    private ArrayList<Integer> mPointArray = new ArrayList<>();     //退出activity时储存chessBoard的值
    private ArrayList<Point> mTempArray = new ArrayList<>();


    private int meRemove1, computerRemove1;
    private int meRemove2, computerRemove2;

    private Paint mPaint = new Paint();
    private float ratioPieceOfLineHeight = 3 * 1.0f / 4;           //棋子图片比例
    private Bitmap mWhitePiece;
    private Bitmap mBlackPiece;

    //将二维数组的值保存到集合mPointArray
    public void transformIn(int[][] storeArray)
    {
        for (int i = 0; i < 15; i++)
        {
            for (int j = 0; j < 15; j++)
            {
                Integer typeNum = storeArray[i][j];
                mPointArray.add(typeNum);
            }
        }
    }

    //将集合mPointArray的值还原到二维数组中
    public void transformOut(ArrayList<Integer> storeType)
    {
        for (int i = 0; i < 15; i++)
        {
            for (int j = 0; j < 15; j++)
            {
                chessBoard[i][j] = mPointArray.get(i * 15 + j);
            }
        }
    }

    //    初始化每种赢法需要的步数
    public void intWhoWin()
    {
        for (int i = 0; i < 572; i++)
        {
            myWin[i] = 0;
            computerWin[i] = 0;
        }
    }


    //    列出所有赢法的排列方式
    public void winStatistics()
    {
        count = 0;
        for (int i = 0; i < 15; i++)
        {
            for (int j = 0; j < 11; j++)
            {
                for (int k = 0; k < 5; k++)
                {
                    wins[i][j + k][count] = true;
                }
                count++;
            }
        }
        for (int i = 0; i < 15; i++)
        {
            for (int j = 0; j < 11; j++)
            {
                for (int k = 0; k < 5; k++)
                {
                    wins[j + k][i][count] = true;
                }
                count++;
            }
        }
        for (int i = 0; i < 11; i++)
        {
            for (int j = 0; j < 11; j++)
            {
                for (int k = 0; k < 5; k++)
                {
                    wins[i + k][j + k][count] = true;
                }
                count++;
            }
        }
        for (int i = 0; i < 11; i++)
        {
            for (int j = 14; j > 3; j--)
            {
                for (int k = 0; k < 5; k++)
                {
                    wins[i + k][j - k][count] = true;
                }
                count++;
            }
        }

    }


    public wuziqipanel(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        init();
//        setBackgroundColor(0x44ff0000);
    }

    //    初始化函数
    private void init()
    {
        mPaint.setColor(0x88000000);
        mPaint.setAntiAlias(true);
        mPaint.setDither(true);
        mPaint.setStyle(Paint.Style.STROKE);

        mWhitePiece = BitmapFactory.decodeResource(getResources(), R.drawable.stone_w2);
        mBlackPiece = BitmapFactory.decodeResource(getResources(), R.drawable.stone_b1);

        intChessBoard();
        intWhoWin();
        winStatistics();

    }

    /**
     * 初始化所有棋盘上的点所对应的值
     * 0 表示当前位置无落子
     * 1 表示玩家所落的子
     * 2 表示计算机落的子
     */
    public void intChessBoard()
    {
        for (int i = 0; i < 15; i++)
        {
            for (int j = 0; j < 15; j++)
            {
                chessBoard[i][j] = 0;
            }
        }
    }

    //    计算机落子算法
    public void computerAI()
    {
        int max = 0;
        int u = 0, v = 0;
        int[][] myScore = new int[20][20];
        int[][] computerScore = new int[20][20];
        for (int i = 0; i < 15; i++)
        {
            for (int j = 0; j < 15; j++)
            {
                myScore[i][j] = 0;
                computerScore[i][j] = 0;
            }
        }
        for (int i = 0; i < 15; i++)
        {
            for (int j = 0; j < 15; j++)
            {
                if (chessBoard[i][j] == 0)
                {
                    for (int k = 0; k < 572; k++)
                    {
                        if (wins[i][j][k])
                        {
                            if (myWin[k] == 1)
                            {
                                myScore[i][j] += 200;
                            } else if (myWin[k] == 2)
                            {
                                myScore[i][j] += 400;
                            } else if (myWin[k] == 3)
                            {
                                myScore[i][j] += 2000;
                            } else if (myWin[k] == 4)
                            {
                                myScore[i][j] += 10000;
                            }
                            if (computerWin[k] == 1)
                            {
                                computerScore[i][j] += 220;
                            } else if (computerWin[k] == 2)
                            {
                                computerScore[i][j] += 430;
                            } else if (computerWin[k] == 3)
                            {
                                computerScore[i][j] += 2100;
                            } else if (computerWin[k] == 4)
                            {
                                computerScore[i][j] += 20000;
                            }
                        }
                    }
                    if (myScore[i][j] > max)
                    {
                        max = myScore[i][j];
                        u = i;
                        v = j;
                    } else if (myScore[i][j] == max)
                    {
                        if (computerScore[i][j] > computerScore[u][v])
                        {
                            u = i;
                            v = j;
                        }
                    }
                    if (computerScore[i][j] > max)
                    {
                        max = computerScore[i][j];
                        u = i;
                        v = j;
                    } else if (computerScore[i][j] == max)
                    {
                        if (myScore[i][j] > myScore[u][v])
                        {
                            u = i;
                            v = j;
                        }
                    }

                }
            }
        }
        chessBoard[u][v] = 2;
        point = getPoint(u, v);
        mTempArray.add(point);
        for (int k = 0; k < 572; k++)
        {
            if (wins[u][v][k])
            {
                computerRemove1 = k;
                computerWin[k]++;
                meRemove2 = myWin[k];
                myWin[k] = 6;
                if (computerWin[k] == 5)
                {
                    mIsGameOver = true;
                    Toast.makeText(getContext(), "白棋胜利", Toast.LENGTH_LONG).show();
                }
            }
        }
        if (!mIsGameOver)
        {
            mIsMe = !mIsMe;
        }
    }

    //  悔棋方法
    public void pieceRemove()
    {
        int tempPointSize = mTempArray.size();
        if (tempPointSize > 1)
        {
            Point p1 = mTempArray.get(tempPointSize - 1);
            Point p2 = mTempArray.get(tempPointSize - 2);
            chessBoard[p1.x][p1.y] = 0;
            chessBoard[p2.x][p2.y] = 0;
            myWin[meRemove1]--;
            myWin[computerRemove1] = meRemove2;
            computerWin[computerRemove1]--;
            computerWin[meRemove1] = computerRemove2;
        }
        mIsMe = true;
        mIsGameOver = false;
        invalidate();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event)
    {
        if (mIsGameOver) return false;
        if (!mIsMe) return false;
        int action = event.getAction();
        if (action == MotionEvent.ACTION_UP)
        {
            int x = (int) event.getX();
            int y = (int) event.getY();
            point = getValidPoint(x, y);
            int i = point.x;
            int j = point.y;
            if (i <= 15 && i >= 0 && j <= 15 && j >= 0)
            {
                if (chessBoard[i][j] == 0)
                {
                    chessBoard[i][j] = 1;
                    point = getPoint(i, j);
                    mTempArray.add(point);
                    for (int k = 0; k < 572; k++)
                    {
                        if (wins[i][j][k])
                        {
                            meRemove1 = k;
                            myWin[k]++;
                            computerRemove2 = computerWin[k];
                            computerWin[k] = 6;
                            if (myWin[k] == 5)
                            {
                                mIsGameOver = true;
                                Toast.makeText(getContext(), "黑棋胜利", Toast.LENGTH_LONG).show();
                            }
                        }
                    }
                    if (!mIsGameOver)
                    {
                        mIsMe = !mIsMe;
                        computerAI();
                    }

                } else
                {
                    return false;
                }
            }

            invalidate();

        }
        return true;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec)
    {
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);

        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);

        int width = Math.min(widthSize, heightSize);

        if (widthMode == MeasureSpec.UNSPECIFIED)
        {
            width = heightSize;
        } else if (heightMode == MeasureSpec.UNSPECIFIED)
        {
            width = heightSize;
        }

        setMeasuredDimension(width, width);

    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh)
    {
        super.onSizeChanged(w, h, oldw, oldh);

        mPanelWidth = w;
        mLineHeight = mPanelWidth * 1.0f / MAX_LINE;


        int pieceWidth = (int) (mLineHeight * ratioPieceOfLineHeight);
        mWhitePiece = Bitmap.createScaledBitmap(mWhitePiece, pieceWidth, pieceWidth, false);
        mBlackPiece = Bitmap.createScaledBitmap(mBlackPiece, pieceWidth, pieceWidth, false);
    }

    protected void onDraw(Canvas canvas)
    {
        super.onDraw(canvas);

        drawBoard(canvas);
        drawPieces(canvas);
    }

    //    绘制棋盘
    private void drawBoard(Canvas canvas)
    {
        int w = mPanelWidth;
        float lineHeight = mLineHeight;

        for (int i = 0; i < MAX_LINE; i++)
        {
            int startX = (int) (lineHeight / 2);
            int endX = (int) (w - lineHeight / 2);
            int y = (int) ((0.5 + i) * lineHeight);
            canvas.drawLine(startX, y, endX, y, mPaint);
            canvas.drawLine(y, startX, y, endX, mPaint);
        }
    }

    //获得有效的落子
    private Point getValidPoint(int x, int y)
    {
        return new Point((int) (x / mLineHeight), (int) (y / mLineHeight));
    }

    private Point getPoint(int x, int y)
    {
        return new Point(x, y);
    }


    //    绘制棋子
    private void drawPieces(Canvas canvas)
    {
        for (int i = 0; i < 15; i++)
        {
            for (int j = 0; j < 15; j++)
            {
                if (chessBoard[i][j] == 1)
                {
                    canvas.drawBitmap(mBlackPiece, (i + (1 - ratioPieceOfLineHeight) / 2) * mLineHeight, (j + (1 - ratioPieceOfLineHeight) / 2) * mLineHeight, null);
                } else if (chessBoard[i][j] == 2)
                {
                    canvas.drawBitmap(mWhitePiece, (i + (1 - ratioPieceOfLineHeight) / 2) * mLineHeight, (j + (1 - ratioPieceOfLineHeight) / 2) * mLineHeight, null);
                }
            }
        }
    }

    //    再来一局
    public void start()
    {
        mIsMe = true;
        mIsGameOver = false;
        intChessBoard();
        intWhoWin();
        invalidate();
    }

    //临时保存数据防止切换应用数据丢失
    private static final String INSTANCE = "instance";
    private static final String INSTANCE_GAME_OVER = "instance_game_over";
    private static final String INSTANCE_CHESSBOARD = "instance_chesboard";

    @Override
    protected Parcelable onSaveInstanceState()
    {
        transformIn(chessBoard);
        Bundle bundle = new Bundle();
        bundle.putParcelable(INSTANCE, super.onSaveInstanceState());
        bundle.putBoolean(INSTANCE_GAME_OVER, mIsGameOver);
        bundle.putIntegerArrayList(INSTANCE_CHESSBOARD, mPointArray);
        return bundle;
    }

    @Override
    protected void onRestoreInstanceState(Parcelable state)
    {
        if (state instanceof Bundle)
        {
            Bundle bundle = (Bundle) state;
            mIsGameOver = bundle.getBoolean(INSTANCE_GAME_OVER);
            mPointArray = bundle.getIntegerArrayList(INSTANCE_CHESSBOARD);

            super.onRestoreInstanceState(bundle.getParcelable(INSTANCE));
            transformOut(mPointArray);
            return;

        }
        super.onRestoreInstanceState(state);
    }
}



