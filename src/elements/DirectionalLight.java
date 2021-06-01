package elements;

import primitives.Color;
import primitives.Point3D;
import primitives.Vector;

public class DirectionalLight extends Light implements LightSource {

    private Vector direction;

    /**
     * constacor for Light
     *
     * @param Intensity
     */
    public DirectionalLight(Color Intensity,Vector Direction ) {
        super(Intensity);
        direction= Direction;
    }


    /**
     * @param p
     * @return
     */
    @Override
    public Color getIntensity(Point3D p) {
        return getIntensity();
    }

    /**
     * @param p
     * @return
     */
    @Override
    public Vector getL(Point3D p) {
        return direction.normalize(); }
}
