
// A brute force method to find the closest distance between points in A and B by pairwise comparison.
public class ClosestAntennaPairBruteForce {
    private double closestDistance = Double.POSITIVE_INFINITY;
    double ax;
    double ay;
    double bx;
    double by;
    double distance;
    Point2D a;
    Point2D b;

    public ClosestAntennaPairBruteForce(Point2D[] aPoints, Point2D[] bPoints) {
        for (Point2D aPoint : aPoints) {
            for (Point2D bPoint : bPoints) {
                double curDistance = aPoint.distanceTo(bPoint);
                if (curDistance < closestDistance) {
                    closestDistance = curDistance;
                    a = aPoint;
                    b = bPoint;
                }
            }
        }
        //System.out.print("bruteforce: ("+ax+", "+ay+")"+" and ("+bx+", "+by+")"+"  distance:"+closestDistance);
    }

    public double getClosestDistance() {
        System.out.print("bruteforce: "+a+" "+b+" distance: "+ closestDistance);
        return closestDistance;
    }
}

/*else if (highA == lowA && highB == lowB) {
            merge(aPointsSortedByY,auxA,lowA,lowA,highA);
            merge(bPointsSortedByY,auxB,lowB,lowB,highB);
            return Point2D.distanceBetween(aPointsSortedByX[highA],bPointsSortedByX[highB]);
        }
        else if (highA < lowA && (highB > lowB)) {
            int midB = (lowB + ((highB - lowB) / 2));
            closest(aPointsSortedByX,bPointsSortedByX,aPointsSortedByY,bPointsSortedByY,auxA,auxB,lowA,lowB,highA,midB);
            closest(aPointsSortedByX,bPointsSortedByX,aPointsSortedByY,bPointsSortedByY,auxA,auxB,lowA,midB+1,highA,highB);
            merge(bPointsSortedByY,auxB,lowB,midB,highB);
            return Double.POSITIVE_INFINITY;
        }
        else if (highB < lowB && (highA > lowA)) {
            int midA = (lowA + ((highA - lowA) / 2));
            closest(aPointsSortedByX,bPointsSortedByX,aPointsSortedByY,bPointsSortedByY,auxA,auxB,lowA,lowB,midA,highB);
            closest(aPointsSortedByX,bPointsSortedByX,aPointsSortedByY,bPointsSortedByY,auxA,auxB,midA+1,lowB,highA,highB);
            merge(aPointsSortedByY,auxA,lowA,midA,highA);
            return Double.POSITIVE_INFINITY;
        }*/
