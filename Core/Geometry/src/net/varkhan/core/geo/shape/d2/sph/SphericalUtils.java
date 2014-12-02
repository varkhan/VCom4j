package net.varkhan.core.geo.shape.d2.sph;

/**
 * <b></b>.
 * <p/>
 *
 * @author varkhan
 * @date 11/17/14
 * @time 8:21 PM
 */
public class SphericalUtils {

    public static final double DEG2RAD=Math.PI/180;
    public static final double RAD2DEG=180/Math.PI;

    /**
     * Geodetic distance between two points, computed through the exact Haversine
     * formula, with spherical coordinates in radians.
     *
     * @param lat1 latitude of the first point, in radians
     * @param lon1 longitude of the first point, in radians
     * @param lat2 latitude of the second point, in radians
     * @param lon2 longitude of the second point, in radians
     * @return the geodetic distance in radians between the two points
     */
    public static double dHaversine(double lat1, double lon1, double lat2, double lon2) {
        double dlat = lat2-lat1;
        double dlon = lon2-lon1;
        double a = Math.sin(dlat*0.5) * Math.sin(dlat*0.5) + Math.sin(dlon*0.5) * Math.sin(dlon*0.5) * Math.cos(lat1) * Math.cos(lat2);
        return 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));
    }

    /**
     * Square geodetic distance between two points, computed from an equirectangular
     * approximation, with spherical coordinates in radians.
     * <p/>
     * While the equirectangular approximation is much faster to compute than
     * the Haversine formula, it is only a good approximation for smaller
     * distances, and lower latitudes.
     *
     * @param lat1 latitude of the first point, in radians
     * @param lon1 longitude of the first point, in radians
     * @param lat2 latitude of the second point, in radians
     * @param lon2 longitude of the second point, in radians
     * @return the <em>square</em> of the geodetic distance in radians between the two points
     */
    public static double dEquirectangular2(double lat1, double lon1, double lat2, double lon2) {
        double dlon = (lon2-lon1) * Math.cos((lat1+lat2)*0.5);
        double dlat = (lat2-lat1);
        return dlon*dlon + dlat*dlat;
    }

    /**
     * Geodetic distance between two points, computed from an equirectangular
     * approximation, with spherical coordinates in radians.
     *
     * @param lat1 latitude of the first point, in radians
     * @param lon1 longitude of the first point, in radians
     * @param lat2 latitude of the second point, in radians
     * @param lon2 longitude of the second point, in radians
     * @return the <em>square</em> of the geodetic distance in radians between the two points
     */
    public static double dEquirectangular(double lat1, double lon1, double lat2, double lon2) {
        double dlon = (lon2-lon1) * Math.cos((lat1+lat2)*0.5);
        double dlat = (lat2-lat1);
        return Math.sqrt(dlon*dlon + dlat*dlat);
    }


}
