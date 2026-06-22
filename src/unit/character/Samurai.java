package unit.character;
import board.GameBoard;
import java.util.ArrayList;
import java.util.List;
import unit.Unit;
import utils.Position;

public class Samurai extends Unit{
    //yakın saldırı orta hasar
    private int power;

    public Samurai(String team,Position pos){
        super("Samurai", 100,35,pos, team);
        this.power = 30;
    }

    public Samurai(){}

    public int getPower(){
        return power;
    }

    @Override
    public List<Position> hareketYonu(GameBoard board){//samurai sağa sola yukarı aşağı hareket edebilir
        List<Position> moves = new ArrayList<>();
        int x = getPosition().getX();
        int y  = getPosition().getY();

        int[][] directions = {
                                {1,0},//sağa
                                {-1,0},//sola
                                {0,1},//aşağı
                                {0,-1}//yukarı
                                };
        for(int[] d : directions){
            int yeniX = x + d[0];
            int yeniY = y + d[1];
            if(board.isInside(yeniX,yeniY) && board.WhereIsTheEnemyOnTheBoard(yeniX,yeniY) == null){
                moves.add(new Position(yeniX, yeniY));
            }
        }
        return moves;
    }


    @Override
    public List<Unit> targetEnemy(GameBoard board){//samurai sağo sola yukarı aşağı saldırabilir
        List<Unit> targets = new ArrayList<>();//hedef düşmanlarımızı bir listede saklıyoruz
        int x = getPosition().getX();
        int y  = getPosition().getY();
        int[][]  directions = {
                                {1,0},
                                {-1,0},
                                {0,1},
                                {0,-1}
                                };
        for(int[] d : directions){
            int yeniX = x + d[0];
            int yeniY = y + d[1];
            if(board.isInside(yeniX,yeniY)){//düşmanlara saldırabilmemiz için düşmanımızın oyun tahtasının içinde olması gerekir.Onun kontrolünü yapalım.
                Unit enemy = board.WhereIsTheEnemyOnTheBoard(yeniX,yeniY);
                if(enemy != null && !enemy.getTeam().equals(this.getTeam())){
                    targets.add(enemy);
                }
            }
        }                   
        return targets;
    }


    @Override
    public void attack(Unit target,GameBoard board){
        if(target == null){
            return;
        }
        int damage = this.getAttackPower();
        target.takeDamage(damage);
        System.out.println(getTeam() + getName() + "," + target.getTeam() +target.getName() + " ya " + damage + " hasar verdi." + "(" + target.getTeam() +  target.getName() + " health: " + target.getHealth() + ")");

        if(!target.isAlive()){
            System.out.println(target.getTeam() + target.getName() + ", " + getTeam() + getName() + " birimi tarafından öldürüldü.");
            board.removeUnit(target);
        }
    }

    @Override
    public String karakterBilgi() {
        return 
        "Karakter adı: " + getName() + " \n" + 
        "Karakter sağlığı: " + getHealth() + " \n" +
        "Saldırı hasarı: " + getAttackPower()+ " \n" +
        "Karakter pozisyonu: " + getPosition() + " \n" +
        "Karakter takımı: " + getTeam() + " \n";
    }



}
