import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.awt.Rectangle;
class Ball
{
int x,y,dx,dy;
int top , left , right , bottom ;
public Ball(int a,int b)
{  x=a;
    y=b;
    dx=(int)(5*Math.random()) + 3;
    dy=(int)(5*Math.random()) + 3;
    top = left = 100 ;
    right = bottom = 500 ;
}
public void draw(Graphics g)
{
    Graphics2D  g2D = (Graphics2D)g;
    g2D.setColor(Color.black);
    g2D.drawRect(100,100,400,400);
    g2D.setColor(Color.red);
    g2D.fillOval(x,y,20,20);
}
public void move( )
{
    x = x + dx ;
    y = y + dy ;
    if (x<=left || x+20>=right)
      {  dx = - dx ; }
    if (y<=top || y+20>=bottom)
      {  dy = - dy ; }
 }
 public Rectangle getObject()
{
Rectangle r=new Rectangle (x,y,20,20);
return r; 
}
public void changeYDirection()
{
dy=-dy;
}
public void changeXDirection()
{
dx=-dx;
}
public boolean hitBottom()
{
    if(y+20>=bottom)
      { return true ; }
     return false ; 
}
}
 class Paddle
{
int x,y;
int left,right ;
public Paddle(int a,int b)
{
x=a;
y=b;
left =100; right = 500 ;
}
public  void draw(Graphics g)
{
Graphics2D  g2D = (Graphics2D)g;
g2D.setColor(Color.black);
g2D.fillRect(x,y,70,5);
}
public void moveLeft( )
{
 if(x<=left)
     return;
 x = x-10 ;
}
public void moveRight( )
{
 if(x+70>=right)
   return;
 x = x+10 ;
}
public Rectangle getObject()
{
Rectangle r=new Rectangle (x,y,70,10);
return r; 
}
}

class Brick
{
   int x,y;
   public static int width=40;
   public static int height=15;                                                                                    
   boolean destroyed;
 public Brick(int a,int b)
  { x=a; y=b;
    destroyed = false ;
}
  
 public int getx()
  {  return x; }
 public int gety()
  {  return y;   }
  
 public boolean isDestroyed()
  { return destroyed ; }

 public void draw(Graphics2D g)
  { g.setColor(Color.white); 
    if(!destroyed)
       g.fillRect(x,y,Brick.width,Brick.height);
  }
 public void destroy()
  { destroyed = true ; 
  }
 public Rectangle getObject()
  { Rectangle r=new Rectangle(x,y,width,height);
    return r;
  }
}
class BrickMap
{
    Brick arr[][];
    int rows,columns;
    int hgap,vgap;
    public BrickMap(int a,int b)
    {
        rows=a;columns=b;
        arr=new Brick[rows][columns];
        hgap=4;vgap=5;
        setup();
    }
    public void setup()
    {
        int i,j,x,y;
        for(i=0;i<rows;i++)
        {
            for(j=0;j<columns;j++)
            {
                x=150+(j*(Brick.width+hgap));
                y=150+(i*(Brick.height+vgap));
                arr[i][j]=new Brick(x,y);
            }
        }
    }
    public void draw(Graphics2D g)
    {
        int i,j;
        for(i=0;i<rows;i++)
        {
            for(j=0;j<columns;j++)
            {
                arr[i][j].draw(g);
            }
        }
    }
}

class GamePanel extends JPanel implements  ActionListener , KeyListener
{
    BrickMap map ;
    Ball ball ;
    Timer t ;
    Paddle paddle;
    int score ;
       
    public GamePanel()
    {  
        setSize(600,600);
        setLayout(null);
        setBackground(Color.gray);
        map = new BrickMap(6,7);
       ball = new Ball(170 , 390);
       paddle = new Paddle(260,490);
       t = new Timer(30 , this) ;
       t.start( ) ;
       score = 0 ;
          
       addKeyListener(this) ;
       setFocusable(true);
       requestFocus();
    }
    
  public void ballPaddleCollision(Ball ball , Paddle paddle )
 {
    Rectangle r1=ball.getObject();
    Rectangle r2=paddle.getObject();
     if ( r1.intersects(r2))
      { ball.changeYDirection( ) ; }
 }     

  public void ballBrickCollision(Ball ball, BrickMap map)
  {
      int i,j;
      for(i=0; i<map.rows ;i++)
         for(j=0; j<map.columns ; j++)
          {
              if(map.arr[i][j].isDestroyed())
              continue;
              
              Rectangle r1 = map.arr[i][j].getObject();
              Rectangle r2 = ball.getObject();
              
              if( r1.intersects(r2) ) 
              {
                  map.arr[i][j].destroy();
                  score++;
                  if ( r2.getX()<r1.getX() || r2.getX()>(r1.getX()+r1.getWidth()) )
                  {
                      ball.changeXDirection();
                      continue;
                  }
                  else 
                  {
                      ball.changeYDirection();
                      continue;
                  }
              }
          }
  }
    
    public void actionPerformed(ActionEvent ae)
    {
       
        
        ballPaddleCollision(ball , paddle) ;
        ballBrickCollision(ball, map);
        repaint( );
        
        
    }
   public void paint(Graphics g)
    {
       
        super.paintComponent(g);
        Graphics2D  g2d = (Graphics2D)g;
       
        BasicStroke bs= new BasicStroke(3); 
        g2d.setStroke( bs);
        ball.move( );
        ball.draw(g2d) ;
        paddle.draw(g2d) ;
        map.draw(g2d);
          if(ball.hitBottom())
        {
        g2d.setFont(new Font("arial",Font.BOLD , 24));
        g2d.setColor(Color.blue);
        g2d.drawString("GAME OVER !! Your score = "+score,110 , 530);
        t.stop();
        return;
       }
        g2d.setFont(new Font("arial",Font.BOLD,22));
        g2d.setColor(Color.red);
        g2d.drawString("Score = "+score,220,520);
        
        
    }
    public void keyReleased(KeyEvent e)
{
}
public void keyTyped(KeyEvent e)
{
}
public void keyPressed(KeyEvent e)
{
    int x = e.getKeyCode( );
    if(x==KeyEvent.VK_RIGHT)
        {
            paddle.moveRight();
        }
        else if(x==KeyEvent.VK_LEFT)
        {
            paddle.moveLeft();
        }
}
       
}   

class MyGameWindow extends JFrame
{
    public MyGameWindow()
    {
        getContentPane().add(new GamePanel());
        pack();
    }
}
public class BrickBreaker
{
    public static void main(String args[])
    {
        MyGameWindow obj=new MyGameWindow();
        obj.setSize(600,600);
        obj.setBackground(Color.orange);
        obj.setTitle("Brick Breaker");
        obj.setVisible(true);
        obj.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
}
