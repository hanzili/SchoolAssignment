import java.util.Arrays;

public class ClosestAntennaPair {

    private double closestDistance = Double.POSITIVE_INFINITY;
    private long counter = 0;

    public ClosestAntennaPair(Point2D[] aPoints, Point2D[] bPoints) {

        // get the length of the two lists
        if (aPoints == null || bPoints == null) return;
        int nA = aPoints.length;
        int nB = bPoints.length;
        if (nA < 1) return;
        if (nB < 1) return;

        //sort the two list by x order
        Point2D[] aPointsSortedByX = new Point2D[nA];
        for (int i = 0; i < nA; i++)
            aPointsSortedByX[i] = aPoints[i];
        Arrays.sort(aPointsSortedByX, Point2D.Y_ORDER);
        Arrays.sort(aPointsSortedByX, Point2D.X_ORDER);
        Point2D[] bPointsSortedByX = new Point2D[nB];
        for (int u = 0; u < nB; u++)
            bPointsSortedByX[u] = bPoints[u];
        Arrays.sort(bPointsSortedByX, Point2D.Y_ORDER);
        Arrays.sort(bPointsSortedByX, Point2D.X_ORDER);

        // see if there is any shortcut (common point)
        for (int i = 0; i < nA; i++) {
            for (int u = 0; u < nB; u++){
                if (aPointsSortedByX[i].equals(bPointsSortedByX[u])) {
                    closestDistance = 0.0;
                    return;
                }
            }
        }

        Point2D[] aPointsSortedByY = new Point2D[nA];
        Point2D[] bPointsSortedByY = new Point2D[nB];
        for (int i = 0; i < nA; i++)
            aPointsSortedByY[i] = aPointsSortedByX[i];
        for (int u = 0; u < nB; u++)
            bPointsSortedByY[u] = bPointsSortedByX[u];

        // auxiliary array
        Point2D[] auxA = new Point2D[nA];
        Point2D[] auxB = new Point2D[nB];

        closest(aPointsSortedByX, bPointsSortedByX, aPointsSortedByY, bPointsSortedByY, auxA, auxB, 0, 0, nA-1, nB-1);
    }


    public int getIndex(Point2D[] points, double x, int length) {
        if (points[0].x() > x) return -1;
        else if (points[length-1].x() < x) return length-1;
        else {
            for (int i = 0; i < length-1; i++) {
                if (points[i].x() == x) return i;
                else if (points[i].x() < x && points[i+1].x() > x) return i;
            }
        }
        return -1;
    }


    public double closest(Point2D[] aPointsSortedByX, Point2D[] bPointsSortedByX, Point2D[] aPointsSortedByY, Point2D[] bPointsSortedByY, Point2D[] auxA, Point2D[] auxB, int lowA, int lowB, int highA, int highB) {
        // please do not delete/modify the next line!
        counter++;

        // base case
        // return the distance directly if there is one point from each array
        if (lowA == highA && lowB == highB) {
            return aPointsSortedByX[lowA].distanceTo(bPointsSortedByX[lowB]);
        }
        // return infinity if there is zero point or one point from each array
        if (lowA >= highA && lowB >= highB) {return Double.POSITIVE_INFINITY;}
        // return if there is zero point in from one array, but more than one point from the other array
        // need to sort these points from the same array in this case
        if (lowA > highA && lowB < highB) {
            int midB=lowB+(highB-lowB)/2;
            merge(bPointsSortedByY,auxB,lowB,midB,highB);
            return Double.POSITIVE_INFINITY;
        }
        if (lowB > highB && lowA < highA) {
            int midA=lowA+(highA-lowA)/2;
            merge(aPointsSortedByY,auxA,lowA,midA,highA);
            return Double.POSITIVE_INFINITY;
        }
        //find the midindex for arrayA and arrayB
        int midA = (lowA + ((highA - lowA) / 2));
        int midB = (lowB + ((highB - lowB) / 2));
        // find the average of the points with corresponding index
        double midx = (aPointsSortedByX[midA].x() + bPointsSortedByX[midB].x()) / 2;
        int midIndexA = getIndex(aPointsSortedByX, midx, aPointsSortedByX.length);
        int midIndexB = getIndex(bPointsSortedByX, midx, bPointsSortedByX.length);

        // recursive part
        double delta1 = closest(aPointsSortedByX, bPointsSortedByX, aPointsSortedByY, bPointsSortedByY, auxA, auxB, lowA, lowB, midIndexA, midIndexB);
        double delta2 = closest(aPointsSortedByX, bPointsSortedByX, aPointsSortedByY, bPointsSortedByY, auxA, auxB, midIndexA + 1, midIndexB + 1, highA, highB);
        double delta = Math.min(delta1, delta2);

        // sort the function by y
        merge(aPointsSortedByY, auxA, lowA, midIndexA, highA);
        merge(bPointsSortedByY, auxB, lowB, midIndexB, highB);

        // find points that are within delta to the midx
        int m = 0;
        for (int i = lowA; i <= highA; i++) {
            if (Math.abs(aPointsSortedByY[i].x() - midx) < delta)
                auxA[m++] = aPointsSortedByY[i];
        }
        int n = 0;
        for (int u = lowB; u <= highB; u++) {
            if (Math.abs(bPointsSortedByY[u].x() - midx) < delta)
                auxB[n++] = bPointsSortedByY[u];
        }

        // go over each points in aux
        for (int i = 0; i < m; i++) {
            for (int u = 0; u < n; u++) {
                // if the y distance is also within delta, calculate the distance
                if (Math.abs(auxA[i].y() - auxB[u].y()) < delta) {
                    double distance = auxA[i].distanceTo(auxB[u]);
                    if (distance < delta) {
                        // if distance is smaller than closestDistance, replace it
                        delta = distance;
                        if (distance < closestDistance)
                            closestDistance = delta;
                    }
                }
            }
        }
        closestDistance = delta;
        return delta;
    }

    public double distance() {
        return closestDistance;
    }

    public long getCounter() {
        return counter;
    }

    // stably merge a[low .. mid] with a[mid+1 ..high] using aux[low .. high]
    // precondition: a[low .. mid] and a[mid+1 .. high] are sorted subarrays, namely sorted by y coordinate
    // this is the same as in ClosestPair

    private static void merge(Point2D[] a, Point2D[] aux, int low, int mid, int high) {
        // copy to aux[]
        // note this wipes out any values that were previously in aux in the [low,high] range we're currently using

        for (int k = low; k <= high; k++) {
            aux[k] = a[k];
        }

        int i = low, j = mid + 1;
        for (int k = low; k <= high; k++) {
            if (i > mid) a[k] = aux[j++];   // already finished with the low list ?  then dump the rest of high list
            else if (j > high) a[k] = aux[i++];   // already finished with the high list ?  then dump the rest of low list
            else if (aux[i].compareByY(aux[j]) < 0)
                a[k] = aux[i++]; // aux[i] should be in front of aux[j] ? position and increment the pointer
            else a[k] = aux[j++];
        }
    }
}
