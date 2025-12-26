package unit;
import board.GameBoard;
import java.util.ArrayList;
import java.util.List;
import skills.Skill;
import utils.Position;


public abstract class Unit{

    private String name;
    private int health;
    private int attackPower;
    private Position pos;
    private String team;//red veya blue
    private List<Skill> skills = new ArrayList<>();

    public Unit(String name,int health,int attackPower,Position pos,String team){
        setName(name);
        setHealth(health);
        setAttackPower(attackPower);
        setPosition(pos);
        setTeam(team);
    }
    public Unit(){
        
    }
    //get ve set metotları
    public String getName(){
        return name;
    }
    public void setName(String name){
        if(name == null || name.trim().isEmpty()){
          throw new IllegalArgumentException("karakter adı boş girilemez.");
        }
        this.name = name;
    }

    public int getHealth(){
        return health;
    }
    public void setHealth(int health){
        if(health < 0){
            this.health = 0;
        }else if(health > 100){
            this.health = 100;
        }else{
             this.health = health;
        }
    }

    public int getAttackPower() {
        return attackPower;
    }

    public void setAttackPower(int attackPower){
        this.attackPower = Math.max(0, Math.min(attackPower, 60));
    }

    public Position getPosition(){
        return pos;
    }

    public void setPosition(Position p){
        if(p == null){
            System.out.println("Board'daki her bir karakterin pozisyonu olmalı..");
            return;
        }
        this.pos = p;
    }

    public String getTeam(){
        return team;
    }
    public void setTeam(String team){
        if(team == null || team.trim().isEmpty()){
            System.out.println("karakter bir takımda olmak zorunda.");
            return;
        }
        if(!team.equalsIgnoreCase("red") && !team.equalsIgnoreCase("blue")) {
            System.out.println("Takımınız 'red' veya 'blue' olmalı");
            return;
        }

        this.team = team;
    }

    public List<Skill> getSkills(){
        return new ArrayList<>(skills);
    }
    
    public void setSkills(List<Skill> skills){
        if(skills == null){
            this.skills = new ArrayList<>();
            System.out.println("karakterin becerisi bulunmuyor.");
        }else{
            this.skills = new ArrayList<>();
        }
    }

    //karakterlerin temel fonksiyonları;
    //birim hayatta mı?
    public boolean isAlive(){
        return health >0;
    }
    //hasar alma metodu
    public int takeDamage(int dmg){
       health = health - dmg;
        if(health < 0){
            health = 0;
        }
        return health;
    }
    //karaktere beceri ekleme.
    public void addSkill(Skill skill){
        if(skill == null){
            System.out.println("skill null olamaz.");
            return;
        }
        if(!this.isAlive()){
            System.out.println(this.name + " öldüğü için beceri eklenemiyor");
            return;
        }

        for(Skill s : skills){
            if(s.getBuff().equalsIgnoreCase(skill.getBuff())){
                System.out.println("bu skill daha önce eklenmiş");
                return;
            }
        }
        skills.add(skill);
    }
    //beceri kullanmak için
    public void useSkill(String skillName){
        if(!this.isAlive()){
            System.out.println(this.name + " ölü olduğundan beceri kullanılamıyor.");
            return;
        }
        if(skillName == null){
            System.out.println("skill adı boş olamaz.");
            return;
        }

        for(Skill buff : skills) {//karakter skilli bulursa uygular
            if(buff.getBuff().equals(skillName)) {
                buff.applySkill(this);
                return;
            }
        }
    System.out.println("skill bulunamadı!");
    }

   //saldırılar
    public void attemptAttack(Unit target, GameBoard board){
        if(!this.isAlive()){
            System.out.println(this.getTeam() + " " + this.getName() + " ölü, birim saldırı yapamaz");
            return;
        }
        if(target == null){
            System.out.println("Hedef bulunamadı!");
            return;
        }
        if(!target.isAlive()){
            System.out.println(target.getTeam() + " " + target.getName() + " zaten ölü!");
            return;
        }  
        if(this.getTeam().equalsIgnoreCase(target.getTeam())){
            System.out.println("Dost ateşi!");
            return;
        }  

        List<Unit> targets = this.targetEnemy(board);
        if(targets.contains(target)){
            this.attack(target, board);
        }else{
          System.out.println("Saldırı yapılamadı.Hedef menzil dışında!");
        }
    
        
    } 


    public abstract List<Unit> targetEnemy(GameBoard board);

    // karakterlerin farklı hareketlerini belirleyecek metot mesela swordsman sadece sağa, sola okçu çapraza gibi...
    public abstract List<Position> hareketYonu(GameBoard board);

    public abstract void attack(Unit target, GameBoard board);

    //karakterlerin bilgileri
    public abstract String karakterBilgi();
    




}
