public class NBody{
    public static double readRadius(String s){
        In in = new In(s);
        int first = in.readInt();
        double second = in.readDouble();
        return second;
    }
    public static Body[] readBodies(String s){
        // Body[] BB = {};
        In in = new In(s);
        int first = in.readInt();
        double second = in.readDouble();
        Double earth1 = in.readDouble();
        Double earth2 = in.readDouble();
        Double earth3 = in.readDouble();
        Double earth4 = in.readDouble();
        Double earth5 = in.readDouble();
        String earth6 = in.readString();
        Double mars1 = in.readDouble();
        Double mars2 = in.readDouble();
        Double mars3 = in.readDouble();
        Double mars4 = in.readDouble();
        Double mars5 = in.readDouble();
        String mars6 = in.readString();
        Double mercury1 = in.readDouble();
        Double mercury2 = in.readDouble();
        Double mercury3 = in.readDouble();
        Double mercury4 = in.readDouble();
        Double mercury5 = in.readDouble();
        String mercury6 = in.readString();
        Double sun1 = in.readDouble();
        Double sun2 = in.readDouble();
        Double sun3 = in.readDouble();
        Double sun4 = in.readDouble();
        Double sun5 = in.readDouble();
        String sun6 = in.readString();
        Double venus1 = in.readDouble();
        Double venus2 = in.readDouble();
        Double venus3 = in.readDouble();
        Double venus4 = in.readDouble();
        Double venus5 = in.readDouble();
        String venus6 = in.readString();

        Body A = new Body(earth1, earth2, earth3, earth4, earth5, earth6);
        Body B = new Body(mars1, mars2, mars3, mars4, mars5, mars6);
        Body C = new Body(mercury1, mercury2, mercury3, mercury4, mercury5, mercury6);
        Body D = new Body(sun1, sun2, sun3, sun4, sun5, sun6);
        Body E = new Body(venus1, venus2, venus3, venus4, venus5, venus6);
        Body[] BB = {A, B, C, D, E};
        return BB;
    }
    public static void drawBackground(){
        //StdDraw.enableDoubleBuffering();
        StdDraw.picture(0, 0, "images/starfield.jpg");
    }
    public static void main(String[] args) {
        double T = Double.parseDouble(args[0]);
        double dt = Double.parseDouble(args[1]);
        String filename = args[2];
        double radius = readRadius(filename);
        Body[] bodies = readBodies(filename);
        StdDraw.enableDoubleBuffering();
        StdDraw.setScale(-radius, radius);
        double t = 0.0;
        while (t <= T){
            double xForces0 = bodies[0].calcNetForceExertedByX(bodies);
            double xForces1 = bodies[1].calcNetForceExertedByX(bodies);
            double xForces2 = bodies[2].calcNetForceExertedByX(bodies);
            double xForces3 = bodies[3].calcNetForceExertedByX(bodies);
            double xForces4 = bodies[4].calcNetForceExertedByX(bodies);
            double yForces0 = bodies[0].calcNetForceExertedByY(bodies);
            double yForces1 = bodies[1].calcNetForceExertedByY(bodies);
            double yForces2 = bodies[2].calcNetForceExertedByY(bodies);
            double yForces3 = bodies[3].calcNetForceExertedByY(bodies);
            double yForces4 = bodies[4].calcNetForceExertedByY(bodies);
            double[] xForces = {xForces0, xForces1, xForces2, xForces3, xForces4};
            double[] yForces = {yForces0, yForces1, yForces2, yForces3, yForces4};
            drawBackground();
            for (int i = 0; i < bodies.length; i++){
                bodies[i].update(dt, xForces[i], yForces[i]);
                bodies[i].draw();
                //System.out.println(B[0].yyPos);
            }
            StdDraw.show();
            StdDraw.pause(10);
            t = t + dt;
        }
        StdOut.printf("%d\n", bodies.length);
        StdOut.printf("%.2e\n", radius);
        for (int i = 0; i < bodies.length; i++) {
            StdOut.printf("%11.4e %11.4e %11.4e %11.4e %11.4e %12s\n",
                          bodies[i].xxPos, bodies[i].yyPos, bodies[i].xxVel,
                          bodies[i].yyVel, bodies[i].mass, bodies[i].imgFileName);
        }
    }
}
