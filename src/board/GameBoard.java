package board;
import java.util.ArrayList;
import java.util.List;
import unit.Unit;
import utils.Position;

public class GameBoard {//oyun tahtasıdır; üzerinde karakterleri tutar ve hareket/yer kontrolü sağlar.
private int width;
private int height;
private List<Unit> units;

public GameBoard(int width,int height){
    this.width = width;
    this.height = height;
    this.units = new ArrayList<>();
}

//karakterlerimiz tahta sınırları içerisinde mi?
public boolean isInside(int x, int y){
    return x >= 0 && y >= 0 && x < width && y < height;
}


//gameboard'a birim ekle.
public void addUnit(Unit unit){
    if(unit == null){
        System.out.println("Unit null olamaz!");
        return;
    }
    
    if(unit.getPosition() == null){
        System.out.println("Unit'in pozisyonu null olamaz!");
        return;
    }
    int x = unit.getPosition().getX();
    int y = unit.getPosition().getY();
  
    for(Unit u : units){
        if(u.getPosition().getX() == x && u.getPosition().getY() == y){
            System.out.println("Pozisyon (" + x + "," + y + ") zaten dolu!");
            return;
        }
    }

    if(!isInside(unit.getPosition().getX(), unit.getPosition().getY())){
        System.out.println("Birimin ekleneceği pozisyon, tahta sınırları dışında olduğu için eklenemedi.");
    }else{
        units.add(unit);
        System.out.println(unit.getName() + " (" + x + "," + y + ") pozisyonuna eklendi.");
    }
   
}
//gameboard'dan birimi kaldır.
public void removeUnit(Unit unit){
    if(unit == null) return;
    if(!unit.isAlive()){
        units.remove(unit);
        System.out.println(unit.getName() + " birimi tahtadan kaldırıldı.");
    }else{
        System.out.println("Canlı birim kaldırılamaz.");
    }
    
    
}

//bu metot düşmanın hareketleri ve saldırıları için önemli.
public Unit WhereIsTheEnemyOnTheBoard(int x,int y){
    for(Unit u : units){
        if(u.getPosition().getX() == x && u.getPosition().getY() == y){
            return u;
        }   
    }
    
    return null;
}

public List<Unit> getUnits(){
    return new ArrayList<>(units);
}
//birimler bu bu metot ile yer değiştirebilir.yer değiştirmesi için gereken bütün kontrolleri yapıyoruz.
public void moveUnit(Unit unit, Position newPos){
    if(unit == null || newPos == null)return;
    
    if(!isInside(newPos.getX(), newPos.getY())){
        System.out.println("Hareket sınır dışında: " + newPos);
        return;
    }
    
    if (WhereIsTheEnemyOnTheBoard(newPos.getX(), newPos.getY()) != null){
        System.out.println("Hedef pozisyon dolu: " + newPos);
        return;
    }

    List<Position> hareket = unit.hareketYonu(this);
    if (!hareket.contains(newPos)){
        System.out.println("Bu birim " + newPos + " konumuna hareket edemez! Geçerli hareketler: " + hareket);
        return;
    }
    unit.setPosition(newPos);
}

public List<Unit> getUnitsByTeam(String team){
    List<Unit> teamUnits = new ArrayList<>();
        for(Unit unit : units){
            if(unit.getTeam().equals(team)){
                teamUnits.add(unit);
            }
        }
    return teamUnits;
}

//kazananı belirleyecek metot.yenen takım kazanır veya canı çok olan takım
public void printWinner(String red, String blue){
    List<Unit> redTeam = getUnitsByTeam(red);
    List<Unit> blueTeam = getUnitsByTeam(blue);
    
    int redHealth = 0;
    int blueHealth = 0;
    
    for(Unit r : redTeam){
        if(r.isAlive()){
            redHealth += r.getHealth();
        }
    }
    for(Unit b : blueTeam){
        if(b.isAlive()){
            blueHealth += b.getHealth();
        }
    } 
    
    System.out.println("\n=== SAVAŞ SONUCU ===");

    String krımızı = "\u001B[31m";
    String mavi = "\u001B[34m";
    String yesil = "\u001B[32m";
    String reset = "\u001B[0m";

    if (redHealth <= 0 && blueHealth <= 0) {
        System.out.println(yesil + "DURUM: BERABERE" + reset);
    } 
    else if (redHealth <= 0 || blueHealth > redHealth) {
        System.out.println("Kazanan: " + mavi + blue.toUpperCase() + " TAKIM" + reset);
        System.out.println("Kalan Can: " + blueHealth);
    } 
    else if (blueHealth <= 0 || redHealth > blueHealth) {
        System.out.println("Kazanan: " + krımızı + red.toUpperCase() + " TAKIM" + reset);
        System.out.println("Kalan Can: " + redHealth);
    } 
    else {
        System.out.println(yesil + "BERABERE! (Canlar Eşit)" + reset);
    }
}

//karakterlerimizi tahtaya yazdıracağız.
public void printBoard(){
    String[][] board = new String[height][width];
    
    for(int i = 0; i < height; i++){
        for(int j = 0; j < width; j++) {
            board[i][j] = "\u001B[90m X \u001B[0m";
        }
    }
    
    for(Unit u : units){
    int x = u.getPosition().getX();
    int y = u.getPosition().getY();
    if(isInside(x, y)){
        String symbol;

        symbol = switch (u.getName().toLowerCase()) {
            case "archer" -> " A ";
            case "samurai" -> " S ";
            case "kesis" -> " K ";
            default -> " ? ";
        };

        String color;
        if(u.getTeam().equalsIgnoreCase("red")){
            color = "\u001B[31m"; //kırmızı
        }else if(u.getTeam().equalsIgnoreCase("blue")){
            color = "\u001B[34m"; //mavi
        }else {
            color = "\u001B[37m"; //beyaz 
        }

        board[y][x] = color + symbol + "\u001B[0m";
    }
}


    // üst çerçeve
    System.out.print("    ╔");
    for(int j = 0; j < width; j++) {
        System.out.print("═══");
        if(j < width - 1){
            System.out.print("╦");
        }
    }
    System.out.println("╗");
    
    for(int i = 0; i < height; i++){
        System.out.print(String.format(" %2d ║", i));
        for(int j = 0; j < width; j++){
            System.out.print(board[i][j]);
            if(j < width - 1){
                System.out.print("║");
            }
        }
        System.out.println("║");
        
        // Ara çizgi
        if(i < height - 1){
            System.out.print("    ╠");
            for (int j = 0; j < width; j++) {
                System.out.print("═══");
                if (j < width - 1) System.out.print("╬");
            }
            System.out.println("╣");
        }
    }
    
    // Alt çerçeve
    System.out.print("    ╚");
    for (int j = 0; j < width; j++) {
        System.out.print("═══");
        if (j < width - 1) System.out.print("╩");
    }
    System.out.println("╝");
    
    // Sütun numaraları
    System.out.print("     ");
    for (int j = 0; j < width; j++) {
        System.out.print(String.format(" %d ", j));
        if (j < width - 1) System.out.print(" ");
    }
    System.out.println("\n");
}

}
