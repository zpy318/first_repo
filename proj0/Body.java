//import java.lang.Math;

public class Body{
    public double xxPos;
    public double yyPos;
    public double xxVel;
    public double yyVel;
    public double mass;
    public String imgFileName;
    public Body(double xP, double yP, double xV,
              double yV, double m, String img){
        xxPos = xP;
        yyPos = yP;
        xxVel = xV;
        yyVel = yV;
        mass = m;
        imgFileName = img;
    }
    public Body(Body b){
        xxPos = b.xxPos;
        yyPos = b.yyPos;
        xxVel = b.xxVel;
        yyVel = b.yyVel;
        mass = b.mass;
        imgFileName = b.imgFileName;
    }
    public double calcDistance(Body c){
        double dist = 0;
        dist = Math.sqrt(Math.pow(xxPos - c.xxPos, 2) +
                Math.pow(yyPos - c.yyPos, 2));
        return dist;
    }
    public double calcForceExertedBy(Body d){
        double G = 6.67e-11;
        double force = 0;
        force = G * mass * d.mass / Math.pow(calcDistance(d), 2);
        return force;
    }
    public double calcForceExertedByX(Body b){
        double distx = b.xxPos - xxPos;
        double forcex = calcForceExertedBy(b) * distx / calcDistance(b);
        return forcex;
    }
    public double calcForceExertedByY(Body b){
        double disty = b.yyPos - yyPos;
        double forcey = calcForceExertedBy(b) * disty / calcDistance(b);
        return forcey;
    }
    public double calcNetForceExertedByX(Body[] allBodys){
        double forcex = 0;
        for(int i = 0; i < allBodys.length; i++){
            if (equals(allBodys[i])){
                continue;
            }else{
                forcex += calcForceExertedByX(allBodys[i]);
            }
        }
        return forcex;
    }
    public double calcNetForceExertedByY(Body[] allBodys){
        double forcey = 0;
        for(int i = 0; i < allBodys.length; i++){
            if (equals(allBodys[i])){
                continue;
            }else{
                forcey += calcForceExertedByY(allBodys[i]);
            }
        }
        return forcey;
    }
    public void update(double dt, double fX, double fY){
        double ax = fX / mass;
        double ay = fY / mass;
        xxVel += ax * dt;
        yyVel += ay * dt;
        xxPos += xxVel * dt;
        yyPos += yyVel * dt;
    }
    public void draw(){
        StdDraw.picture(xxPos, yyPos, "images/" + imgFileName);
        // StdDraw.show();
        // StdDraw.pause(10);
    }
}
