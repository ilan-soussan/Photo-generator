package geometries;

import java.util.List;
import primitives.*;
import static primitives.Util.*;

/**
 * Polygon class represents two-dimensional polygon in 3D Cartesian coordinate
 * system
 * 
 * @author Dan
 */
public class Polygon extends Geometry {

	/**
	 * List of polygon's vertices
	 */
	protected List<Point3D> vertices;
	/**
	 * Associated plane in which the polygon lays
	 */
	protected Plane plane;
	Box b;

	@Override
	public Box getBox() {
		return null;
	}

	/**
	 * Polygon constructor based on vertices list. The list must be ordered by edge
	 * path. The polygon must be convex.
	 * 
	 * //@param vertices list of vertices according to their order by edge path
	 * @throws IllegalArgumentException in any case of illegal combination of
	 *                                  vertices:
	 *                                  <ul>
	 *                                  <li>Less than 3 vertices</li>
	 *                                  <li>Consequent vertices are in the same
	 *                                  point
	 *                                  <li>The vertices are not in the same
	 *                                  plane</li>
	 *                                  <li>The order of vertices is not according
	 *                                  to edge path</li>
	 *                                  <li>Three consequent vertices lay in the
	 *                                  same line (180&#176; angle between two
	 *                                  consequent edges)
	 *                                  <li>The polygon is concave (not convex)</li>
	 *                                  </ul>
	 */
	/*@Override
	public List<Point3D> findIntsersections(Ray ray) {
		if(!isIntsersectionsExist(ray))
			return null;
		return null; /// צריך לעשות
	}*/

	@Override
	public List<GeoPoint> findGeoIntersection(Ray ray) {
		if(!isIntsersectionsExist(ray))
			return null;
		return null; /// צריך לעשות
	}

	public boolean isIntsersectionsExist(Ray ray)
	{
		///קיים בעיה בלולאה!

		boolean flag = true;
		Vector v1 = (vertices.get(0).subtract(ray.getRayPoint()));
		Vector Vn = (vertices.get(1).subtract(ray.getRayPoint()));
		Vector Vn1 = (vertices.get(2).subtract(ray.getRayPoint()));
		Vector N = Vn1.crossProduct(Vn).normalize();
		Vector N1 = v1.crossProduct(Vn).normalize();
		for (int i =1;i<vertices.size();i++)
		{
			Vn= Vn1;
			Vn1 = (vertices.get(i).subtract(ray.getRayPoint()));
			N = N1;
			N1 = Vn1.crossProduct(Vn).normalize();
			Vector A = ray.getRayDir();
			Double numN1 = N1.dotProduct(ray.getRayDir());
			double numN =N.dotProduct(ray.getRayDir());
			if((numN1>0 && numN<0) || (numN1<0 && numN>0) || Util.isZero(Util.alignZero(numN)))
				flag = false;
		}
		//בדיקה של האחרון והראשון
		Vn = (vertices.get(1).subtract(ray.getRayPoint()));
		N = v1.crossProduct(Vn);
		if((N1.dotProduct(ray.getRayDir())>0 && N.dotProduct(ray.getRayDir())<0) ||
				(N1.dotProduct(ray.getRayDir())<0 && N.dotProduct(ray.getRayDir())>0) ||
				Util.isZero(Util.alignZero(N1.dotProduct(ray.getRayDir()))))
			flag = false;
		return flag;
	}
	public Polygon()
	{
		;
	}
	public Polygon(Point3D... vertices) {
		if (vertices.length < 3)
			throw new IllegalArgumentException("A polygon can't have less than 3 vertices");
		this.vertices = List.of(vertices);
		// Generate the plane according to the first three vertices and associate the
		// polygon with this plane.
		// The plane holds the invariant normal (orthogonal unit) vector to the polygon
		plane = new Plane(vertices[0], vertices[1], vertices[2]);
		if (vertices.length == 3)
			return; // no need for more tests for a Triangle

		Vector n = plane.getNormal();

		// Subtracting any subsequent points will throw an IllegalArgumentException
		// because of Zero Vector if they are in the same point
		Vector edge1 = vertices[vertices.length - 1].subtract(vertices[vertices.length - 2]);
		Vector edge2 = vertices[0].subtract(vertices[vertices.length - 1]);

		// Cross Product of any subsequent edges will throw an IllegalArgumentException
		// because of Zero Vector if they connect three vertices that lay in the same
		// line.
		// Generate the direction of the polygon according to the angle between last and
		// first edge being less than 180 deg. It is hold by the sign of its dot product
		// with
		// the normal. If all the rest consequent edges will generate the same sign -
		// the
		// polygon is convex ("kamur" in Hebrew).
		boolean positive = edge1.crossProduct(edge2).dotProduct(n) > 0;
		for (int i = 1; i < vertices.length; ++i) {
			// Test that the point is in the same plane as calculated originally
			if (!isZero(vertices[i].subtract(vertices[0]).dotProduct(n)))
				throw new IllegalArgumentException("All vertices of a polygon must lay in the same plane");
			// Test the consequent edges have
			edge1 = edge2;
			edge2 = vertices[i].subtract(vertices[i - 1]);
			if (positive != (edge1.crossProduct(edge2).dotProduct(n) > 0))
				throw new IllegalArgumentException("All vertices must be ordered and the polygon must be convex");
		}
	}

	@Override
	public Vector getNormal(Point3D point) {
		return plane.getNormal();
	}

}
