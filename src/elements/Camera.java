package elements;

import primitives.*;

import java.util.LinkedList;
import java.util.List;
import static primitives.Util.*;
import java.math.*;

/**
 *  class that we will use as our camera.
 *  @params point, Vup, Vtowards, Vright, width, height, distance
 *  @author Ilan and didi
 *
 */

public class Camera {
    private Point3D point;
    private Vector Vup;
    private Vector Vtowards;
    private Vector Vright;

    double Width;
    double Height;
    double Distance;

    /**
     * getter for Vtowards
     *
     * @return Vector
     */
    public Vector getVtowards() {
        return Vtowards;
    }

    /**
     * getter for Vright
     *
     * @return Vector
     */
    public Vector getVright() {
        return Vright;
    }

    /**
     * getter for Vup
     *
     * @return Vector
     */

    public Vector getVup() {
        return Vup;
    }

    /**
     * getter for point of the camera
     *
     * @return point
     */

    public Point3D getPoint() {
        return point;
    }


    /**
     * @param cameraPoint
     * @param cameraVtowards
     * @param cameraVup      constructor for our camera check if the Vup and towards are really 90 degrees else it wont work
     *                       next we calculate vright(Vup*Vtoward)
     *                       and put all the prams in the camera params after normalize the voctors
     */
    public Camera(Point3D cameraPoint, Vector cameraVtowards, Vector cameraVup) {
        if (!Util.isZero(cameraVup.dotProduct(cameraVtowards))) {
            throw new IllegalArgumentException("Vectors not vertical to each other"); // check if the vectors are 90 degrees
        }
        Vright = cameraVup.crossProduct(cameraVtowards).normalize(); // calculate Vright
        Vtowards = cameraVtowards.normalize();
        Vup = cameraVup.normalize();
        point = cameraPoint;
    }

    /**
     * setter of the viewPlanesize
     *
     * @param width
     * @param height
     * @return a camera
     */
    public Camera setViewPlaneSize(double width, double height) {
        Width = width;
        Height = height;
        return this;
    }

    /**
     * setter of the distance of the camera
     *
     * @param distance
     * @return a camera
     */
    public Camera setDistance(double distance) {
        Distance = distance;
        return this;
    }
    /*
    public Camera rotateCamera(Point3D newCamPoint,double angleRadXY,double angleRadYZ)
    {
        point =newCamPoint;
        if(!isZero(alignZero(Math.cos(angleRadXY))))
        {

        }
    }*/

    /**
     * Get The ray that pass Through Pixel in j,i
     *
     * @param nX
     * @param nY
     * @param j
     * @param i
     * @return ray
     */
    public Ray constructRayThroughPixel(int nX, int nY, int j, int i) {
        //image center
        Point3D Pc = point.add(Vtowards.scale(Distance));
        //Ratio(pixel width & height)
        double Ry = Height / nY;
        double Rx = Width / nX;
        //Pixel[i,j] center

        double Yi = ((i - (nY - 1) / 2d) * Ry) * -1;
        double Xj = ((j - (nX - 1) / 2d) * Rx) * -1;
        Point3D Pij = Pc;


        if (!isZero(Xj))
            Pij = Pij.add(Vright.scale(Xj));
        if (!isZero(Yi))
            Pij = Pij.add(Vup.scale(Yi));


        Vector Vij = Pij.subtract(point);
        Ray ray = new Ray(Vij.normalize(), point);

        return ray;

    }

    /**
     * return the Distance of thee camera
     *
     * @return Distance
     */
    public double getDistance() {
        return Distance;
    }

    /**
     * Get The ray that pass Through Pixel in j,i with point changes for super sampling
     *
     * @param nX
     * @param nY
     * @param j
     * @param i
     * @param k
     * @param f
     * @param numOfrays
     * @return ray
     */
    public Ray constructRayThroughPixelSuperSampling(int nX, int nY, int j, int i, int k, int f, int numOfrays) {
        //image center
        Point3D Pc = point.add(Vtowards.scale(Distance));
        //Ratio(pixel width & height)
        double Ry = Height / nY;
        double Rx = Width / nX;
        //Pixel[i,j] center

        // changes the point for super sampling
        if (!(k == 0))
            Pc = Pc.add(new Vector(Rx / numOfrays, Rx / numOfrays, Rx / numOfrays).scale(k));
        if (!(f == 0))
            Pc = Pc.add(new Vector(Ry / numOfrays, Ry / numOfrays, Ry / numOfrays).scale(f));

        double Yi = ((i - (nY - 1) / 2d) * Ry) * -1;
        double Xj = ((j - (nX - 1) / 2d) * Rx) * -1;
        Point3D Pij = Pc;


        if (!isZero(Xj))
            Pij = Pij.add(Vright.scale(Xj));
        if (!isZero(Yi))
            Pij = Pij.add(Vup.scale(Yi));


        Vector Vij = Pij.subtract(point);
        Ray ray = new Ray(Vij.normalize(), point);

        return ray;

    }
}


