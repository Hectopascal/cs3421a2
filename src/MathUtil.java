public class MathUtil {
    // start copy from assn1!
    /**
     * Normalise an angle to the range [-180, 180)
     *
     * @param angle
     * @return
     */
    static public double normaliseAngle(double angle) {
        return ((angle + 180.0) % 360.0 + 360.0) % 360.0 - 180.0;
    }

    /**
     * Clamp a value to the given range
     *
     * @param value
     * @param min
     * @param max
     * @return
     */

    public static double clamp(double value, double min, double max) {
        return Math.max(min, Math.min(max, value));
    }

    /**
     * Multiply two matrices
     *
     * @param p A 3x3 matrix
     * @param q A 3x3 matrix
     * @return
     */
    public static double[][] multiply(double[][] p, double[][] q) {

        double[][] m = new double[3][3];

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                m[i][j] = 0;
                for (int k = 0; k < 3; k++) {
                    m[i][j] += p[i][k] * q[k][j];
                }
            }
        }

        return m;
    }

    /**
     * Multiply two matrices
     *
     * @param p A 4x4 matrix
     * @param q A 4x4 matrix
     * @return
     */
    public static double[][] multiply4(double[][] p, double[][] q) {

        double[][] m = new double[4][4];

        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                m[i][j] = 0;
                for (int k = 0; k < 4; k++) {
                    m[i][j] += p[i][k] * q[k][j];
                }
            }
        }

        return m;
    }

    /**
     * Multiply a vector by a matrix
     *
     * @param m A 4x4 matrix
     * @param v A 3x1 vector
     * @return
     */
    public static double[] multiply(double[][] m, double[] v) {

        double[] u = new double[3];

        for (int i = 0; i < 3; i++) {
            u[i] = 0;
            for (int j = 0; j < 3; j++) {
                u[i] += m[i][j] * v[j];
            }
        }

        return u;
    }



    /**
     * 2D translation
     * @param v
     * @return
     */
    public static double[][] translationMatrix(double[] v) {
        double[][] resultMatrix = new double[3][];
        resultMatrix[0] = new double[] {1,0,v[0]};
        resultMatrix[1] = new double[] {0,1,v[1]};
        resultMatrix[2] = new double[] {0,0,1};
        return resultMatrix;
    }

    /**
     * 3D translation
     * @param v
     * @return
     * suppose our v is type of object have x,y,z
     * if array use v[0],v[1],v[2] instead
     */
    public static double[][] translationMatrix4(Coord v) {
        double[][] resultMatrix = new double[4][4];
        resultMatrix[0] = new double[] {1,0,0,v.x};
        resultMatrix[1] = new double[] {0,1,0,v.y};
        resultMatrix[2] = new double[] {0,0,1,v.z};
        resultMatrix[3] = new double[] {0,0,0,1};
        return resultMatrix;
    }

    /**
     * A 2D rotation matrix for the given angle
     *
     * @param angle in degrees
     * @return
     */
    public static double[][] rotationMatrix3(double angle) {
        double[][] resultMatrix = new double[3][3];
        double sin = Math.sin(angle * Math.PI/180);
        double cos = Math.cos(angle * Math.PI/180);
        resultMatrix[0] = new double[] {cos,-sin,0};
        resultMatrix[1] = new double[] {sin,cos,0};
        resultMatrix[2] = new double[] {0,0,1};
        return resultMatrix;
    }

    /**
     *
     * @param xAngle
     * @return
     * rotate matrix through x-axis
     */
    public static double[][] rotationMatrixX(double xAngle) {
        double[][] resultMatrix = new double[4][4];
        double sin = Math.sin(Math.toRadians(xAngle));
        double cos = Math.cos(Math.toRadians(xAngle));
        resultMatrix[0] = new double[] {1,0,0,0};
        resultMatrix[1] = new double[] {0,cos,-sin,0};
        resultMatrix[2] = new double[] {0,sin,cos,0};
        resultMatrix[3] = new double[] {0,0,0,1};
        return resultMatrix;
    }

    /**
     *
     * @param yAngle
     * @return
     * rotate matrix through x-axis
     */
    public static double[][] rotationMatrixY(double yAngle) {
        double[][] resultMatrix = new double[4][4];
        double cos = Math.cos(Math.toRadians(yAngle));
        double sin = Math.sin(Math.toRadians(yAngle));
        resultMatrix[0] = new double[] {cos,0,sin,0};
        resultMatrix[1] = new double[] {0,1,0,0};
        resultMatrix[2] = new double[] {-sin,0,cos,0};
        resultMatrix[3] = new double[] {0,0,0,1};
        return resultMatrix;
    }

    /**
     *
     * @param zAngle
     * @return
     * rotate matrix through x-axis
     */
    public static double[][] rotationMatrixZ(double zAngle) {
        double[][] resultMatrix = new double[4][4];
        double cos = Math.cos(Math.toRadians(zAngle));
        double sin = Math.sin(Math.toRadians(zAngle));
        resultMatrix[0] = new double[] {cos,-sin,0,0};
        resultMatrix[1] = new double[] {sin,cos,0,0};
        resultMatrix[2] = new double[] {0,0,1,0};
        resultMatrix[3] = new double[] {0,0,0,1};
        return resultMatrix;
    }


    public static double[][] rotationMatrixXYZ(Coord r){
        return MathUtil.multiply4(MathUtil.multiply4(MathUtil.rotationMatrixX(r.x),
                MathUtil.rotationMatrixY(r.y)),MathUtil.rotationMatrixZ(r.z));
    }
    /**
     * A 2D scale matrix that scales both axes by the same factor
     *
     * @param scale
     * @return
     */
    public static double[][] scaleMatrix(double scale) {
        double[][] resultMatrix = new double[3][3];
        resultMatrix[0] = new double[] {scale,0,0};
        resultMatrix[1] = new double[] {0,scale,0};
        resultMatrix[2] = new double[] {0,0,1};
        return resultMatrix;
    }

    /**
     * A 3D scale matrix that scales both axes by the same factor
     *
     * @param scale
     * @return
     */

    public static double[][] scaleMatrix4(double scale) {
        double[][] resultMatrix = new double[4][4];
        resultMatrix[0] = new double[] {scale,0,0,0};
        resultMatrix[1] = new double[] {0,scale,0,0};
        resultMatrix[2] = new double[] {0,0,scale,0};
        resultMatrix[3] = new double[] {0,0,0,1};
        return resultMatrix;
    }

    public static Coord translationVector(double[][] matrix){
        return new Coord(matrix[0][3],matrix[1][3],matrix[2][3]);
    }
}
